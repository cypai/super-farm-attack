package com.pipai.sfa.battle.controller

import com.pipai.sfa.battle.command.BattleCommand
import com.pipai.sfa.battle.domain.Battle
import com.pipai.sfa.battle.domain.FieldCrop
import com.pipai.sfa.battle.domain.Player
import com.pipai.sfa.battle.domain.PlayerCrop
import com.pipai.sfa.battle.domain.PlayerTeam
import com.pipai.sfa.battle.domain.PlayerUnit
import com.pipai.sfa.battle.domain.PlotObject
import com.pipai.sfa.battle.eventlog.BattleEvent
import com.pipai.sfa.battle.eventlog.BattleEvent.CropTurnsUntilYieldChangeEvent
import com.pipai.sfa.battle.eventlog.BattleEvent.CropYieldChangeEvent
import com.pipai.sfa.battle.eventlog.BattleEvent.FarmDamageEvent
import com.pipai.sfa.battle.eventlog.BattleEvent.FieldCropResolvedEvent
import com.pipai.sfa.battle.eventlog.BattleEvent.PlotObjectDamageEvent
import org.slf4j.LoggerFactory
import java.security.SecureRandom
import java.util.Random
import kotlin.comparisons.compareBy
import com.pipai.sfa.battle.eventlog.BattleEvent.EndTurnEvent
import com.pipai.sfa.battle.eventlog.BattleEvent.BattleOutcomeEvent
import com.pipai.sfa.battle.commandskill.BattleTurnEvaluationException

class BattleTurnEvaluator(private val battle: Battle, private val random: Random = SecureRandom()) {

	companion object {
		private val LOGGER = LoggerFactory.getLogger(BattleTurnEvaluator::class.java)
	}

	private val events: MutableList<BattleEvent> = mutableListOf()

	private var battleOutcome = BattleOutcome.NONE

	fun evaluateTurn(commands: List<BattleCommand>): List<BattleEvent> {
		LOGGER.info("Evaluating turn...")
		events.clear()
		try {
			val commandsInOrder = commands.sortedWith(compareBy({ -it.priority() }, { -it.speed() })).toMutableList()

			while (commandsInOrder.isNotEmpty()) {
				val firstCommand = commandsInOrder.first()
				val prioritySpeedBlock = PrioritySpeed(firstCommand)
				val isInSpeedBlockPredicate: (BattleCommand) -> Boolean = { PrioritySpeed(it) == prioritySpeedBlock }
				val commandsInSpeedBlock = commandsInOrder.filter(isInSpeedBlockPredicate)
				commandsInOrder.removeAll(isInSpeedBlockPredicate)

				LOGGER.info("Evaluate speed block: $prioritySpeedBlock")
				evaluateSpeedBlock(commandsInSpeedBlock)

				if (battleOutcome != BattleOutcome.NONE) {
					break
				}
			}
			LOGGER.info("Finished command evaluations")

			if (battleOutcome == BattleOutcome.NONE) {
				LOGGER.info("Begin post command evaluations procedure")
				decreaseTimersForCrops()
				events.add(EndTurnEvent())
			}

			LOGGER.info("Post-turn evaluation state of the battle: $battle")
		} catch (e: Exception) {
			throw BattleTurnEvaluationException(events, e)
		}
		return events.toList()
	}

	private fun teamLost(team: PlayerTeam): Boolean {
		return team.farm.hp <= 0 || team.crew.all({ unit -> unit.hp <= 0 })
	}

	private fun evaluateSpeedBlock(commands: List<BattleCommand>) {
		val battleTurnData = BattleTurnData(battle)

		val shuffledCommands = commands.map { Pair(it, random.nextFloat()) }.sortedBy { it.second }.map { it.first }

		for (command in shuffledCommands) {
			events.addAll(command.perform(battleTurnData))

			if (battleOutcome != BattleOutcome.NONE) {
				break
			}
		}
		evaluateField(battleTurnData)
	}

	private fun evaluateField(battleTurnData: BattleTurnData) {
		LOGGER.info("Evaluating field:"
				+ " field1: ${battleTurnData.fieldCrops.getField1()} field2: ${battleTurnData.fieldCrops.getField2()}")

		for (column in 0..battleTurnData.battle.plotColumns) {
			val crops1 = battleTurnData.fieldCrops.getField1Column(column).toMutableList()
			val crops2 = battleTurnData.fieldCrops.getField2Column(column).toMutableList()

			while (crops1.isNotEmpty() || crops2.isNotEmpty()) {
				val crop1 = crops1.getOrNull(0)
				if (crop1 != null) {
					crops1.removeAt(0)
				}
				val crop2 = crops2.getOrNull(0)
				if (crop2 != null) {
					crops2.removeAt(0)
				}

				if (crop1 == null && crop2 != null) {
					evaluateDamage(crop2)
				} else if (crop1 != null && crop2 == null) {
					evaluateDamage(crop1)
				} else if (crop1 != null && crop2 != null) {
					evaluateCollision(crop1, crop2)
				} else {
					throw IllegalStateException("Should never happen, context: $crop1 $crop2")
				}
				battleOutcome = checkBattleOutcome()
				if (battleOutcome != BattleOutcome.NONE) {
					events.add(BattleOutcomeEvent(battleOutcome))
					break
				}
			}

			if (battleOutcome != BattleOutcome.NONE) {
				break
			}
		}
	}

	private fun evaluateCollision(fieldCrop1: FieldCrop, fieldCrop2: FieldCrop) {
		LOGGER.info("Evaluating collision of $fieldCrop1 and $fieldCrop2")
		when (winnerOfCollision(fieldCrop1, fieldCrop2)) {
			Player.PLAYER_1 -> {
				LOGGER.info("Player 1 wins collision")
				evaluateDamage(fieldCrop1)
			}
			Player.PLAYER_2 -> {
				LOGGER.info("Player 2 wins collision")
				evaluateDamage(fieldCrop2)
			}
			Player.NONE -> {
				LOGGER.info("Collision cancelled field crops out")
				events.add(FieldCropResolvedEvent(fieldCrop1))
				events.add(FieldCropResolvedEvent(fieldCrop2))
			}
		}
	}

	private fun winnerOfCollision(fieldCrop1: FieldCrop, fieldCrop2: FieldCrop): Player {
		return Player.NONE
	}

	private fun evaluateDamage(fieldCrop: FieldCrop) {
		LOGGER.info("Evaluating damage of $fieldCrop")
		val player = battle.getPlayerForCrop(fieldCrop.crop)

		val plotObjects = when (player) {
			Player.PLAYER_1 -> battle.getObjectsAtLocationField1(fieldCrop.targetPlotLocation)
			Player.PLAYER_2 -> battle.getObjectsAtLocationField2(fieldCrop.targetPlotLocation)
			Player.NONE -> throw IllegalArgumentException("Crop ${fieldCrop.crop} does not belong to either player")
		}

		dealDamageToFarm(fieldCrop, plotObjects.isEmpty())
		for (plotObject in plotObjects) {
			dealDamageToPlotObject(plotObject, fieldCrop)
		}
	}

	private fun dealDamageToFarm(fieldCrop: FieldCrop, halveDamage: Boolean) {
		val player = battle.getPlayerForCrop(fieldCrop.crop)

		// Get the farm of the opposing player of the crop
		val farm = when (player) {
			Player.PLAYER_1 -> battle.player2.farm
			Player.PLAYER_2 -> battle.player1.farm
			Player.NONE -> throw IllegalArgumentException("Player $player cannot be NONE")
		}

		val baseDamage = fieldCrop.crop.patk + fieldCrop.shooter.unit.patk
		val damage = if (halveDamage) baseDamage / 2 else baseDamage

		LOGGER.info("Dealing damage to farm of $player: $damage")
		farm.hp -= damage
		events.add(FarmDamageEvent(player, damage))
	}

	private fun dealDamageToPlotObject(plotObject: PlotObject, fieldCrop: FieldCrop) {
		val damage = calculateDamageToPlotObject(fieldCrop, plotObject)

		LOGGER.info("Dealing damage to plot object ${plotObject.javaClass}: $damage")
		plotObject.hp -= damage
		events.add(PlotObjectDamageEvent(plotObject, damage))
	}

	private fun calculateDamageToPlotObject(fieldCrop: FieldCrop, plotObject: PlotObject): Int {
		val def = when (plotObject) {
			is PlayerUnit -> plotObject.unit.pdef
			is PlayerCrop -> plotObject.crop.pdef
			else -> 0
		}
		return clampToMin(fieldCrop.crop.patk + fieldCrop.shooter.unit.patk - def, 1)
	}

	private fun clampToMin(x: Int, min: Int): Int {
		return if (x < min) min else x
	}

	private fun decreaseTimersForCrops() {
		for (crop in battle.player1.crops) {
			decreaseTurnTimer(crop)
		}
		for (crop in battle.player2.crops) {
			decreaseTurnTimer(crop)
		}
	}

	private fun decreaseTurnTimer(crop: PlayerCrop) {
		crop.turnsUntilYield -= 1
		events.add(CropTurnsUntilYieldChangeEvent(crop, -1))

		if (crop.turnsUntilYield <= 0) {
			when (battle.getPlayerForCrop(crop)) {
				Player.PLAYER_1 -> battle.player1.cropYields.put(crop.crop, battle.player1.cropYields.get(crop.crop)!! + 1)
				Player.PLAYER_2 -> battle.player2.cropYields.put(crop.crop, battle.player2.cropYields.get(crop.crop)!! + 1)
				Player.NONE -> throw IllegalStateException("Crop ${crop} belongs to neither player in battle}")
			}
			events.add(CropYieldChangeEvent(crop, 1))
		}
	}

	private fun checkBattleOutcome(): BattleOutcome {
		val player1Loss = teamLost(battle.player1)
		val player2Loss = teamLost(battle.player2)
		val outcome: BattleOutcome

		if (player1Loss && player2Loss) {
			outcome = BattleOutcome.TIE
		} else if (player1Loss) {
			outcome = BattleOutcome.PLAYER2_VICTORY
		} else if (player2Loss) {
			outcome = BattleOutcome.PLAYER1_VICTORY
		} else {
			outcome = BattleOutcome.NONE
		}

		return outcome
	}

	private data class PrioritySpeed(val priority: Int, val speed: Int) {
		constructor(command: BattleCommand) : this(command.priority(), command.speed())
	}

}

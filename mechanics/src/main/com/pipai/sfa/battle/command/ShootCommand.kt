package com.pipai.sfa.battle.command

import com.pipai.sfa.battle.controller.BattleController
import com.pipai.sfa.battle.domain.Player
import com.pipai.sfa.battle.domain.PlayerCrop
import com.pipai.sfa.battle.domain.PlayerUnit
import com.pipai.sfa.battle.eventlog.BattleEvent
import com.pipai.sfa.battle.eventlog.BattleEvent.CropYieldChangeEvent
import com.pipai.sfa.battle.eventlog.BattleEvent.TextEvent
import com.pipai.sfa.battle.eventlog.BattleEvent.PlayerUnitDamageEvent

class ShootCommand
(val controller: BattleController,
 val performingUnit: PlayerUnit,
 val targetUnit: PlayerUnit,
 val cropToUse: PlayerCrop)

: BattleCommand {

	override fun priority() = 0

	override fun speed() = performingUnit.unit.speed

	override fun perform(): List<BattleEvent> {
		val eventList: MutableList<BattleEvent> = mutableListOf()

		val battle = controller.battle
		val playerEnum = battle.getPlayerForUnit(performingUnit)
		val player = when (playerEnum) {
			Player.PLAYER_1 -> battle.player1
			Player.PLAYER_2 -> battle.player2
			Player.NONE -> throw IllegalStateException("Performing unit is not in either player's crew")
		}

		if (player.cropYields.containsKey(cropToUse.crop)) {

			val amount = player.cropYields.get(cropToUse.crop) ?:
					throw IllegalStateException("Attempted to use a crop where the yield amount was null."
							+ " Player: ${player.name} Context: $this")

			if (amount <= 0) {
				throw IllegalStateException("Attempted to use a crop where the yield amount is currently <= 0."
						+ " Player: ${player.name} Context: $this")
			} else {
				player.cropYields.put(cropToUse.crop, amount - 1)
				eventList.add(CropYieldChangeEvent(cropToUse, -1))

				eventList.add(TextEvent(""
						+ "${player.name}'s ${getDetailedName(performingUnit)}"
						+ " used Shoot ${getDetailedName(cropToUse)}"
						+ " on ${getDetailedName(targetUnit)}"))

				val damageCalc = cropToUse.crop.patk + performingUnit.unit.patk - targetUnit.unit.pdef
				val damage = if (damageCalc <= 0) 1 else damageCalc
				targetUnit.hp -= damage
				eventList.add(PlayerUnitDamageEvent(targetUnit, damage))
			}
		} else {
			throw IllegalStateException("Attempted to use crop which does not exist in player's crop yields."
					+ " Player: ${player.name} Context: $this")
		}

		return eventList.toList()
	}

}

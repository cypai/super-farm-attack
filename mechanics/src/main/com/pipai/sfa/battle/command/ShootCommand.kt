package com.pipai.sfa.battle.command

import com.pipai.sfa.battle.controller.BattleTurnData
import com.pipai.sfa.battle.domain.FieldCrop
import com.pipai.sfa.battle.domain.Player
import com.pipai.sfa.battle.domain.PlayerCrop
import com.pipai.sfa.battle.domain.PlayerUnit
import com.pipai.sfa.battle.domain.PlotLocation
import com.pipai.sfa.battle.eventlog.BattleEvent
import com.pipai.sfa.battle.eventlog.BattleEvent.AddCropToColumnEvent
import com.pipai.sfa.battle.eventlog.BattleEvent.CropYieldChangeEvent
import com.pipai.sfa.battle.eventlog.BattleEvent.TextEvent

data class ShootCommand
(val performingUnit: PlayerUnit,
 val targetPlotLocation: PlotLocation,
 val cropToUse: PlayerCrop)

: BattleCommand {

	override fun priority() = 0

	override fun speed() = performingUnit.unit.speed

	override fun plotLocation() = performingUnit.plotLocation

	override fun perform(battleTurnData: BattleTurnData): List<BattleEvent> {
		val eventList: MutableList<BattleEvent> = mutableListOf()

		val battle = battleTurnData.battle
		val playerEnum = battle.getPlayerForUnit(performingUnit)
		val player = when (playerEnum) {
			Player.PLAYER_1 -> battle.player1
			Player.PLAYER_2 -> battle.player2
			Player.NONE -> throw IllegalStateException("Performing unit is not in either player's crew")
		}

		if (performingUnit.plotLocation.col != targetPlotLocation.col) {
			throw IllegalStateException("Performing unit $performingUnit is not in the same column as the target location $targetPlotLocation")
		}

		if (player.cropYields.containsKey(cropToUse.crop)) {

			val amount = player.cropYields.get(cropToUse.crop) ?:
					throw IllegalStateException("Attempted to use a crop where the yield amount was null."
							+ " Player: ${player.name} Context: $this")

			if (amount <= 0) {
				throw IllegalStateException("Attempted to use a crop where the yield amount is currently <= 0."
						+ " Player: ${player.name} Context: $this")
			}

			player.cropYields.put(cropToUse.crop, amount - 1)
			eventList.add(CropYieldChangeEvent(cropToUse, -1))

			eventList.add(TextEvent(""
					+ "${player.name}'s ${getDetailedName(performingUnit)}"
					+ " used Shoot ${getDetailedName(cropToUse)} !"))

			val fieldCropInformation = FieldCrop(cropToUse.crop, performingUnit, targetPlotLocation)

			when (playerEnum) {
				Player.PLAYER_1 -> battleTurnData.fieldCrops.addCropToField1(fieldCropInformation, performingUnit.plotLocation.col)
				Player.PLAYER_2 -> battleTurnData.fieldCrops.addCropToField2(fieldCropInformation, performingUnit.plotLocation.col)
				Player.NONE -> throw IllegalStateException("Performing unit is not in either player's crew")
			}

			eventList.add(AddCropToColumnEvent(fieldCropInformation, performingUnit.plotLocation.col))
		} else {
			throw IllegalStateException("Attempted to use crop which does not exist in player's crop yields."
					+ " Player: ${player.name} Context: $this")
		}

		return eventList.toList()
	}

}

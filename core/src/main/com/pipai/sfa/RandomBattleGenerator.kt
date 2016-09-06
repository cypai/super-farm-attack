package com.pipai.sfa

import com.pipai.sfa.battle.domain.Battle
import com.pipai.sfa.battle.domain.Crop
import com.pipai.sfa.battle.domain.Farm
import com.pipai.sfa.battle.domain.PlayerTeamFactory
import com.pipai.sfa.battle.domain.PlotLocation
import com.pipai.sfa.battle.domain.Unit
import com.pipai.sfa.index.CropSchemaIndex
import com.pipai.sfa.index.UnitSchemaIndex
import com.pipai.sfa.utils.RNG
import com.pipai.sfa.utils.shuffle

private val UNIT_LOCATION_LIST: List<PlotLocation> = listOf(
		PlotLocation(0, 0), PlotLocation(1, 0), PlotLocation(2, 0))

private val CROP_LOCATION_LIST: List<PlotLocation> = listOf(
		PlotLocation(0, 1), PlotLocation(1, 1), PlotLocation(2, 1),
		PlotLocation(0, 2), PlotLocation(1, 2), PlotLocation(2, 2))

fun generateStandardBattle(
		unitIndex: UnitSchemaIndex,
		cropIndex: CropSchemaIndex,
		playerUnitAmount: Int,
		playerCropAmount: Int,
		opponentUnitAmount: Int,
		opponentCropAmount: Int): Battle {


	val farm1 = Farm("Player 1 Farm", 50)
	val farm2 = Farm("Player 2 Farm", 50)

	val factory = PlayerTeamFactory()
	val playerTeam = factory.generatePlayerTeam(
			"Player Team",
			farm1,
			getRandomUnitSelection(unitIndex, playerUnitAmount),
			getRandomCropSelection(cropIndex, playerCropAmount))
	val opponentTeam = factory.generatePlayerTeam(
			"AI Team",
			farm2,
			getRandomUnitSelection(unitIndex, opponentUnitAmount),
			getRandomCropSelection(cropIndex, opponentCropAmount))

	val battle = Battle(3, 3, playerTeam, opponentTeam)
	return battle
}

private fun getRandomCropSelection(cropIndex: CropSchemaIndex, amount: Int): Map<Crop, PlotLocation> {
	val cropMap: MutableMap<Crop, PlotLocation> = mutableMapOf()
	val randomLocations = shuffle(CROP_LOCATION_LIST)

	for (i in 0..amount - 1) {
		cropMap.put(cropIndex.allCropSchemas.get(RNG.nextInt(cropIndex.size())).generateCrop(), randomLocations.get(i))
	}

	return cropMap
}

private fun getRandomUnitSelection(unitIndex: UnitSchemaIndex, amount: Int): Map<Unit, PlotLocation> {
	val unitMap: MutableMap<Unit, PlotLocation> = mutableMapOf()
	val randomLocations = shuffle(CROP_LOCATION_LIST)

	for (i in 0..amount - 1) {
		unitMap.put(unitIndex.allUnitSchemas.get(RNG.nextInt(unitIndex.size())).generateUnit(), randomLocations.get(i))
	}

	return unitMap
}

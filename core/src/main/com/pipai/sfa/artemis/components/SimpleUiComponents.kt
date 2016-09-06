package com.pipai.sfa.artemis.components

import com.artemis.Component
import com.pipai.sfa.battle.domain.CropSize
import com.pipai.sfa.battle.domain.PlayerCrop
import com.pipai.sfa.battle.domain.PlayerUnit
import com.pipai.sfa.utils.toSimpleLocation

class PlayerUnitUiData(val playerUnit: PlayerUnit) {
	var hp: Int = playerUnit.hp
	val hpMax: Int = playerUnit.unit.hp
	var location: Int = toSimpleLocation(playerUnit.plotLocation)
	val patk: Int = playerUnit.unit.patk
	val pdef: Int = playerUnit.unit.pdef
	val speed: Int = playerUnit.unit.speed
}

class TeamsComponent : Component() {
	val playerTeam: MutableList<PlayerUnitUiData> = mutableListOf()
	val opponentTeam: MutableList<PlayerUnitUiData> = mutableListOf()
}

class PlayerCropUiData(val playerCrop: PlayerCrop, var amount: Int) {
	var hp: Int = playerCrop.hp
	val hpMax: Int = playerCrop.crop.hp
	var location: Int = toSimpleLocation(playerCrop.plotLocation)
	val patk: Int = playerCrop.crop.patk
	val pdef: Int = playerCrop.crop.pdef
	val size: CropSize = playerCrop.crop.size
	var turnsUntilYield: Int = playerCrop.turnsUntilYield
}

class CropsComponent : Component() {
	val playerCrops: MutableList<PlayerCropUiData> = mutableListOf()
	val opponentCrops: MutableList<PlayerCropUiData> = mutableListOf()
}

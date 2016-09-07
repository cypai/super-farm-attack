package com.pipai.sfa.artemis.components

import com.artemis.Component
import com.pipai.sfa.battle.domain.CropSize
import com.pipai.sfa.battle.domain.FieldCrop
import com.pipai.sfa.battle.domain.FieldUnit
import com.pipai.sfa.battle.domain.Player
import com.pipai.sfa.utils.toSimpleLocation

class PlayerComponent : Component() {
	lateinit var player: Player
}

class FieldUnitUiData(val fieldUnit: FieldUnit) {
	val name: String = fieldUnit.unit.schema.name
	val nickname: String = fieldUnit.unit.nickname
	var hp: Int = fieldUnit.hp
	val hpMax: Int = fieldUnit.unit.hp
	var location: Int = toSimpleLocation(fieldUnit.plotLocation)
	val patk: Int = fieldUnit.unit.patk
	val pdef: Int = fieldUnit.unit.pdef
	val speed: Int = fieldUnit.unit.speed
}

class TeamComponent : Component() {
	val teamData: MutableList<FieldUnitUiData> = mutableListOf()
}

class FieldCropUiData(private val fieldCrop: FieldCrop, var amount: Int) {
	val name: String = fieldCrop.crop.schema.name
	val nickname: String = fieldCrop.crop.nickname
	var hp: Int = fieldCrop.hp
	val hpMax: Int = fieldCrop.crop.hp
	var location: Int = toSimpleLocation(fieldCrop.plotLocation)
	val patk: Int = fieldCrop.crop.patk
	val pdef: Int = fieldCrop.crop.pdef
	val size: CropSize = fieldCrop.crop.size
	var turnsUntilYield: Int = fieldCrop.turnsUntilYield
}

class CropsComponent : Component() {
	val cropsData: MutableList<FieldCropUiData> = mutableListOf()
}

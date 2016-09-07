package com.pipai.sfa.battle.command

import com.pipai.sfa.battle.domain.FieldCrop
import com.pipai.sfa.battle.domain.FieldUnit

fun getDetailedName(playerUnit: FieldUnit): String {
	return "${playerUnit.unit.nickname} (${playerUnit.unit.schema.name})"
}

fun getDetailedName(playerCrop: FieldCrop): String {
	return "${playerCrop.crop.nickname} (${playerCrop.crop.schema.name})"
}

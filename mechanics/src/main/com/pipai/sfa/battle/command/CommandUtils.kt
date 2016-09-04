package com.pipai.sfa.battle.command

import com.pipai.sfa.battle.domain.PlayerCrop
import com.pipai.sfa.battle.domain.PlayerUnit

fun getDetailedName(playerUnit: PlayerUnit): String {
	return "${playerUnit.unit.nickname} (${playerUnit.unit.schema.name})"
}

fun getDetailedName(playerCrop: PlayerCrop): String {
	return "${playerCrop.crop.nickname} (${playerCrop.crop.schema.name})"
}

package com.pipai.sfa.battle.command

import com.pipai.sfa.battle.controller.BattleController
import com.pipai.sfa.battle.domain.Crop
import com.pipai.sfa.battle.domain.PlayerUnit

class ShootCommand
(val controller: BattleController,
 val performingUnit: PlayerUnit,
 val targetUnit: PlayerUnit,
 val cropToUse: Crop)

: BattleCommand {

	override fun perform() {
	}

}

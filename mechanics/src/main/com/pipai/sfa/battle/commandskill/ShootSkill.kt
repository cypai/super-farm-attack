package com.pipai.sfa.battle.commandskill

import com.pipai.sfa.battle.command.BattleCommand
import com.pipai.sfa.battle.command.ShootCommand
import com.pipai.sfa.battle.controller.BattleController

class ShootSkill : UnitSkill(), RequiresPerformerMixin, RequiresTargetMixin, RequiresCropYieldMixin {

	override val requiresPerformerImpl = RequiresPerformerImpl()
	override val requiresTargetImpl = RequiresTargetImpl()
	override val requiresCropYieldImpl = RequiresCropYieldImpl()

	override fun getName(): String = "Shoot"

	override fun generateCommandImpl(controller: BattleController): BattleCommand {
		return ShootCommand(controller, getPerformer(), getTarget(), getCropYield())
	}

	override fun clearCommandParameters() {
		clearPerformer()
		clearTarget()
		clearCropYield()
	}

}

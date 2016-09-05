package com.pipai.sfa.battle.commandskill

import com.pipai.sfa.battle.command.BattleCommand
import com.pipai.sfa.battle.command.ShootCommand
import com.pipai.sfa.battle.controller.BattleController

class ShootSkill : UnitSkill(), RequiresPerformerMixin, RequiresTargetPlotLocationMixin, RequiresCropYieldMixin {

	override val requiresPerformerImpl = RequiresPerformerImpl()
	override val requiresTargetPlotLocationImpl = RequiresTargetPlotLocationImpl()
	override val requiresCropYieldImpl = RequiresCropYieldImpl()

	override fun getName(): String = "Shoot"

	override fun generateCommand(controller: BattleController): BattleCommand {
		return ShootCommand(getPerformer(), getTargetPlotLocation(), getCropYield())
	}

	override fun clearCommandParameters() {
		clearPerformer()
		clearTargetPlotLocation()
		clearCropYield()
	}

}

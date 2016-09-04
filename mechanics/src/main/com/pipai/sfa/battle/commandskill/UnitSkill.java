package com.pipai.sfa.battle.commandskill;

import com.pipai.sfa.battle.command.BattleCommand;
import com.pipai.sfa.battle.controller.BattleController;

public abstract class UnitSkill {

	public abstract String getName();

	public final BattleCommand generateCommand(BattleController controller) {
		BattleCommand command = generateCommandImpl(controller);
		clearCommandParameters();
		return command;
	}

	protected abstract BattleCommand generateCommandImpl(BattleController controller);

	protected abstract void clearCommandParameters();

}

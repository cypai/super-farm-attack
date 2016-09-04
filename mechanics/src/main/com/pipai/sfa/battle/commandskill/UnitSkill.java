package com.pipai.sfa.battle.commandskill;

import com.pipai.sfa.battle.command.BattleCommand;
import com.pipai.sfa.battle.controller.BattleController;

public abstract class UnitSkill {

	public abstract String getName();

	public final void sendCommand(BattleController controller) {
		BattleCommand command = generateCommand(controller);
		clearCommandParameters();
		controller.sendBattleCommand(command);
	}

	protected abstract BattleCommand generateCommand(BattleController controller);

	protected abstract void clearCommandParameters();

}

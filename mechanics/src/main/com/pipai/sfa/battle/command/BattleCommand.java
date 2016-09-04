package com.pipai.sfa.battle.command;

public interface BattleCommand {

	int priority();

	int speed();

	void perform();

}

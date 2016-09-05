package com.pipai.sfa.battle.command

import com.pipai.sfa.battle.controller.BattleTurnData
import com.pipai.sfa.battle.domain.PlotLocation
import com.pipai.sfa.battle.eventlog.BattleEvent

interface BattleCommand {
	fun priority(): Int
	fun speed(): Int

	/**
	 * Can be null if the command does not originate from a PlotLocation
	 */
	fun plotLocation(): PlotLocation?

	fun perform(battleTurnData: BattleTurnData): List<BattleEvent>
}

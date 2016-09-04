package com.pipai.sfa.battle.command

import com.pipai.sfa.battle.eventlog.BattleEvent

interface BattleCommand {
	fun priority(): Int
	fun speed(): Int
	fun perform(): List<BattleEvent>
}

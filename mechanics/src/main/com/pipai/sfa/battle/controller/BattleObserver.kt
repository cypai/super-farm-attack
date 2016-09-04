package com.pipai.sfa.battle.controller

import com.pipai.sfa.battle.eventlog.BattleEvent

interface BattleObserver {
	fun handleTurnResults(events: List<BattleEvent>)
}

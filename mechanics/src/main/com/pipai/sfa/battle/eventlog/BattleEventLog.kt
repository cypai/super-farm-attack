package com.pipai.sfa.battle.eventlog

class BattleEventLog {

	private val fullLog: MutableList<BattleEvent> = mutableListOf()
	private val turnLog: MutableList<BattleEvent> = mutableListOf()

	fun addEvent(event: BattleEvent) {
		turnLog.add(event)
	}

	fun addEvents(events: List<BattleEvent>) {
		turnLog.addAll(events)
	}

	fun endTurn(): List<BattleEvent> {
		val theTurnLog = turnLog.toList()
		fullLog.addAll(turnLog)
		turnLog.clear()
		return theTurnLog
	}

}

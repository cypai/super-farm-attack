package com.pipai.sfa.battle.eventlog

class BattleEventLog {

	private val log: MutableList<BattleEvent> = mutableListOf()

	fun addEvent(event: BattleEvent) {
		log.add(event)
	}

	fun addEvents(events: List<BattleEvent>) {
		log.addAll(events)
	}

}

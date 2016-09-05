package com.pipai.sfa.battle.commandskill

import com.pipai.sfa.battle.eventlog.BattleEvent
import org.apache.commons.lang3.builder.ToStringBuilder

class BattleTurnEvaluationException : RuntimeException {

	override val message: String?
	val events: List<BattleEvent>

	constructor(message: String, events: List<BattleEvent>, ex: Exception?) : super(message, ex) {
		this.events = events
		val eventString = if (events.isEmpty()) {
			""
		} else {
			events.map { ToStringBuilder.reflectionToString(it) }.reduce({ a, b -> "$a\n$b" })
		}
		this.message = "$message\nEvents:\n$eventString"
	}

	constructor(message: String, events: List<BattleEvent>) : super(message) {
		this.events = events
		val eventString = if (events.isEmpty()) {
			""
		} else {
			events.map { ToStringBuilder.reflectionToString(it) }.reduce({ a, b -> "$a\n$b" })
		}
		this.message = "$message\nEvents:\n$eventString"
	}

	constructor(events: List<BattleEvent>, ex: Exception) : super(ex) {
		this.events = events
		val eventString = if (events.isEmpty()) {
			""
		} else {
			events.map { ToStringBuilder.reflectionToString(it) }.reduce({ a, b -> "$a\n$b" })
		}
		this.message = "Events:\n$eventString"
	}

}

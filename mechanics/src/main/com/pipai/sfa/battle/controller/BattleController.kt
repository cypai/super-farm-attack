package com.pipai.sfa.battle.controller

import com.pipai.sfa.battle.command.BattleCommand
import com.pipai.sfa.battle.domain.Battle
import com.pipai.sfa.battle.domain.Player
import com.pipai.sfa.battle.domain.FieldCrop
import com.pipai.sfa.battle.domain.PlayerTeam
import com.pipai.sfa.battle.eventlog.BattleEvent
import com.pipai.sfa.battle.eventlog.BattleEvent.CropTurnsUntilYieldChangeEvent
import com.pipai.sfa.battle.eventlog.BattleEventLog
import java.util.concurrent.locks.Lock
import java.util.concurrent.locks.ReentrantLock
import com.pipai.sfa.battle.eventlog.BattleEvent.CropYieldChangeEvent
import com.pipai.sfa.battle.eventlog.BattleEvent.BattleOutcomeEvent

class BattleController(val battle: Battle) {

	private val battleTurnEvaluator: BattleTurnEvaluator = BattleTurnEvaluator(battle)

	private val battleLog: BattleEventLog = BattleEventLog()

	private val observers: MutableList<BattleObserver> = mutableListOf()

	private val commands: MutableList<BattleCommand> = mutableListOf()

	private var playerReadySignalLock: Lock = ReentrantLock()

	private var player1Ready: Boolean = false
	private var player2Ready: Boolean = false

	fun registerObserver(observer: BattleObserver) {
		observers.add(observer)
	}

	fun sendBattleCommand(command: BattleCommand) {
		commands.add(command)
	}

	fun sendPlayer1ReadySignal() {
		playerReadySignalLock.lock()

		try {
			player1Ready = true
			if (player2Ready) {
				executeTurn()
			}
		} finally {
			playerReadySignalLock.unlock()
		}
	}

	fun sendPlayer2ReadySignal() {
		playerReadySignalLock.lock()

		try {
			player2Ready = true
			if (player1Ready) {
				executeTurn()
			}
		} finally {
			playerReadySignalLock.unlock()
		}
	}

	private fun executeTurn() {
		val turnEvents = battleTurnEvaluator.evaluateTurn(commands)

		battleLog.addEvents(turnEvents)

		for (observer in observers) {
			observer.handleTurnResults(turnEvents)
		}

		commands.clear()
		player1Ready = false
		player2Ready = false
	}
}

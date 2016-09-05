package com.pipai.sfa.battle.controller

import com.pipai.sfa.battle.command.BattleCommand
import com.pipai.sfa.battle.domain.Battle
import com.pipai.sfa.battle.domain.Player
import com.pipai.sfa.battle.domain.PlayerCrop
import com.pipai.sfa.battle.domain.PlayerTeam
import com.pipai.sfa.battle.eventlog.BattleEvent
import com.pipai.sfa.battle.eventlog.BattleEvent.CropTurnsUntilYieldChangeEvent
import com.pipai.sfa.battle.eventlog.BattleEventLog
import java.util.concurrent.locks.Lock
import java.util.concurrent.locks.ReentrantLock
import com.pipai.sfa.battle.eventlog.BattleEvent.CropYieldChangeEvent
import com.pipai.sfa.battle.eventlog.BattleEvent.BattleOutcomeEvent

class BattleController(val battle: Battle) {

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
		commands.sortByDescending { command -> command.speed() }
		commands.sortByDescending { command -> command.priority() }

		var battleFinished = false
		for (command in commands) {
			val events = command.perform()
			battleLog.addEvents(events)

			val outcome = checkBattleOutcome()
			if (outcome != BattleOutcome.NONE) {
				battleLog.addEvent(BattleOutcomeEvent(outcome))
				battleFinished = true
				break
			}
		}

		if (!battleFinished) {
			for (crop in battle.player1.crops) {
				battleLog.addEvents(decreaseTurnTimer(crop))
			}
			for (crop in battle.player2.crops) {
				battleLog.addEvents(decreaseTurnTimer(crop))
			}
		}

		val turnEvents = battleLog.endTurn()
		for (observer in observers) {
			observer.handleTurnResults(turnEvents)
		}

		player1Ready = false
		player2Ready = false
	}

	private fun decreaseTurnTimer(crop: PlayerCrop): List<BattleEvent> {
		val events: MutableList<BattleEvent> = mutableListOf()

		crop.turnsUntilYield -= 1
		events.add(CropTurnsUntilYieldChangeEvent(crop, -1))

		if (crop.turnsUntilYield <= 0) {
			when (battle.getPlayerForCrop(crop)) {
				Player.PLAYER_1 -> battle.player1.cropYields.put(crop.crop, battle.player1.cropYields.get(crop.crop)!! + 1)
				Player.PLAYER_2 -> battle.player2.cropYields.put(crop.crop, battle.player2.cropYields.get(crop.crop)!! + 1)
				Player.NONE -> throw IllegalStateException("Crop ${crop} belongs to neither player in battle}")
			}
			events.add(CropYieldChangeEvent(crop, 1))
		}

		return events.toList()
	}

	fun checkBattleOutcome(): BattleOutcome {
		val player1Loss = teamLost(battle.player1)
		val player2Loss = teamLost(battle.player2)
		val outcome: BattleOutcome

		if (player1Loss && player2Loss) {
			outcome = BattleOutcome.TIE
		} else if (player1Loss) {
			outcome = BattleOutcome.PLAYER2_VICTORY
		} else if (player2Loss) {
			outcome = BattleOutcome.PLAYER1_VICTORY
		} else {
			outcome = BattleOutcome.NONE
		}

		return outcome
	}

	private fun teamLost(team: PlayerTeam): Boolean {
		return team.farm.hp <= 0 || team.crew.all({ unit -> unit.hp <= 0 })
	}
}

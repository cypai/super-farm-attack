package com.pipai.sfa.battle.controller

import com.pipai.sfa.battle.command.BattleCommand
import com.pipai.sfa.battle.domain.Battle
import com.pipai.sfa.battle.domain.PlayerTeam
import java.util.concurrent.locks.Lock
import java.util.concurrent.locks.ReentrantLock

class BattleController(val battle: Battle) {

	private val commands: MutableList<BattleCommand> = mutableListOf()

	private var playerReadySignalLock: Lock = ReentrantLock()

	private var player1Ready: Boolean = false
	private var player2Ready: Boolean = false

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

		for (command in commands) {
			command.perform()
		}

		player1Ready = false
		player2Ready = false
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

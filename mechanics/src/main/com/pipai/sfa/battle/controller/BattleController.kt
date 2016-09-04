package com.pipai.sfa.battle.controller

import com.pipai.sfa.battle.domain.Battle
import com.pipai.sfa.battle.domain.PlayerTeam

class BattleController(val battle: Battle) {

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

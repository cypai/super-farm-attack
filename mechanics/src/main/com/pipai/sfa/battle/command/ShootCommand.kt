package com.pipai.sfa.battle.command

import com.pipai.sfa.battle.controller.BattleController
import com.pipai.sfa.battle.domain.Crop
import com.pipai.sfa.battle.domain.PlayerUnit

class ShootCommand
(val controller: BattleController,
 val performingUnit: PlayerUnit,
 val targetUnit: PlayerUnit,
 val cropToUse: Crop)

: BattleCommand {

	override fun priority() = 0

	override fun speed() = performingUnit.unit.speed

	override fun perform() {
		val battle = controller.battle
		val player = if (battle.player1.crew.contains(performingUnit)) battle.player1 else battle.player2

		if (player.cropYields.containsKey(cropToUse)) {

			val amount = player.cropYields.get(cropToUse) ?:
					throw IllegalStateException("Attempted to use a crop where the yield amount was null." +
							" Context: Player " + (if (player == battle.player1) 1 else 2) + " " + this)

			if (amount <= 0) {
				throw IllegalStateException("Attempted to use a crop where the yield amount is currently <= 0." +
						" Context: Player " + (if (player == battle.player1) 1 else 2) + " " + this)
			} else {
				player.cropYields.put(cropToUse, amount - 1)

				val damage = cropToUse.patk + performingUnit.unit.patk - targetUnit.unit.pdef
				targetUnit.hp -= if (damage <= 0) 1 else damage
			}
		} else {
			throw IllegalStateException("Attempted to use crop which does not exist in player's crop yields." +
					" Context: Player " + (if (player == battle.player1) 1 else 2) + " " + this)
		}
	}

}

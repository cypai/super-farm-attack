package com.pipai.sfa.battle.eventlog

import com.pipai.sfa.battle.domain.Player
import com.pipai.sfa.battle.domain.PlayerCrop
import com.pipai.sfa.battle.domain.PlayerUnit

sealed class BattleEvent {

	class TextEvent(val text: String) : BattleEvent()

	class CropYieldChangeEvent(val player: Player, val crop: PlayerCrop, val amount: Int) : BattleEvent()

	class PlayerUnitDamageEvent(val playerUnit: PlayerUnit, val amount: Int) : BattleEvent()

}

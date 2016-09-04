package com.pipai.sfa.battle.eventlog

import com.pipai.sfa.battle.controller.BattleOutcome
import com.pipai.sfa.battle.domain.PlayerCrop
import com.pipai.sfa.battle.domain.PlayerUnit

sealed class BattleEvent {

	class BattleOutcomeEvent(val outcome: BattleOutcome) : BattleEvent()

	class TextEvent(val text: String) : BattleEvent()

	class CropTurnsUntilYieldChangeEvent(val crop: PlayerCrop, val amount: Int) : BattleEvent()

	class CropYieldChangeEvent(val crop: PlayerCrop, val amount: Int) : BattleEvent()

	class PlayerUnitDamageEvent(val playerUnit: PlayerUnit, val amount: Int) : BattleEvent()

}

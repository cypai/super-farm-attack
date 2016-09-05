package com.pipai.sfa.battle.eventlog

import com.pipai.sfa.battle.controller.BattleOutcome
import com.pipai.sfa.battle.domain.FieldCrop
import com.pipai.sfa.battle.domain.Player
import com.pipai.sfa.battle.domain.PlayerCrop
import com.pipai.sfa.battle.domain.PlotObject

sealed class BattleEvent {

	class EndTurnEvent() : BattleEvent()

	class BattleOutcomeEvent(val outcome: BattleOutcome) : BattleEvent()

	class TextEvent(val text: String) : BattleEvent()

	class AddCropToColumnEvent(val fieldCrop: FieldCrop, val column: Int) : BattleEvent()

	class CropTurnsUntilYieldChangeEvent(val crop: PlayerCrop, val amount: Int) : BattleEvent()

	class CropYieldChangeEvent(val crop: PlayerCrop, val amount: Int) : BattleEvent()

	class PlotObjectDamageEvent(val plotObject: PlotObject, val amount: Int) : BattleEvent()

	class FarmDamageEvent(val player: Player, val amount: Int) : BattleEvent()

	class FieldCropResolvedEvent(val fieldCrop: FieldCrop) : BattleEvent()

}

package com.pipai.sfa.battle

import java.util.ArrayList
import org.apache.commons.lang3.builder.ToStringBuilder
import org.junit.Assert
import org.junit.Test
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import com.google.common.collect.ImmutableList
import com.google.common.collect.ImmutableMap
import com.pipai.sfa.battle.commandskill.ShootSkill
import com.pipai.sfa.battle.controller.BattleController
import com.pipai.sfa.battle.controller.BattleObserver
import com.pipai.sfa.battle.controller.BattleOutcome
import com.pipai.sfa.battle.domain.Battle
import com.pipai.sfa.battle.domain.Crop
import com.pipai.sfa.battle.domain.CropSchema
import com.pipai.sfa.battle.domain.Farm
import com.pipai.sfa.battle.domain.PlayerTeam
import com.pipai.sfa.battle.domain.PlayerTeamFactory
import com.pipai.sfa.battle.domain.PlotLocation
import com.pipai.sfa.battle.domain.Unit
import com.pipai.sfa.battle.domain.UnitSchema
import com.pipai.sfa.battle.eventlog.BattleEvent
import com.pipai.sfa.battle.eventlog.BattleEvent.BattleOutcomeEvent

class FullBattleTest {

	companion object {
		private val LOGGER = LoggerFactory.getLogger(FullBattleTest::class.java)
	}

	@Test
	fun shootUnitsTest() {
		val cropOneSchema = CropSchema("Crop One", 1, 1, 1, 1, 1)
		val crop1 = cropOneSchema.generateCrop("Crop 1")
		val crop2 = cropOneSchema.generateCrop("Crop 2")

		val speedOneSchema = UnitSchema("Speed One", 2, 1, 1, 1)
		val speedTwoSchema = UnitSchema("Speed Two", 2, 1, 1, 2)

		val unit11 = speedOneSchema.generateUnit("Unit 1 1")
		val unit12 = speedTwoSchema.generateUnit("Unit 1 2")
		val unit22 = speedTwoSchema.generateUnit("Unit 2 2")

		val farm1 = Farm("Player 1 Farm", 3)
		val farm2 = Farm("Player 2 Farm", 3)

		val factory = PlayerTeamFactory()

		val team1 = factory.generatePlayerTeam(
				"Team 1",
				farm1,
				ImmutableMap.of(unit11, PlotLocation(0, 0), unit12, PlotLocation(0, 1)),
				ImmutableMap.of(crop1, PlotLocation(1, 0)))
		val team2 = factory.generatePlayerTeam(
				"Team 2",
				farm2,
				ImmutableMap.of(unit22, PlotLocation(0, 0)),
				ImmutableMap.of(crop2, PlotLocation(1, 0)))

		val battle = Battle(3, 3, team1, team2)

		val controller = BattleController(battle)

		val observer = LogbackBattleObserver()
		controller.registerObserver(observer)

		val shootSkill = ShootSkill()
		shootSkill.setCropYield(battle.player1.crops.get(0))
		shootSkill.setPerformer(battle.player1.crew.get(0))
		shootSkill.setTargetPlotLocation(PlotLocation(0, 0))
		shootSkill.sendCommand(controller)

		controller.sendPlayer1ReadySignal()

		shootSkill.setCropYield(battle.player2.crops.get(0))
		shootSkill.setPerformer(battle.player2.crew.get(0))
		shootSkill.setTargetPlotLocation(PlotLocation(0, 0))
		shootSkill.sendCommand(controller)

		controller.sendPlayer2ReadySignal()

		Assert.assertTrue(observer.getObservedEvents().size > 0)

		shootSkill.setCropYield(battle.player1.crops.get(0))
		shootSkill.setPerformer(battle.player1.crew.get(1))
		shootSkill.setTargetPlotLocation(PlotLocation(0, 0))
		shootSkill.sendCommand(controller)

		controller.sendPlayer1ReadySignal()

		shootSkill.setCropYield(battle.player2.crops.get(0))
		shootSkill.setPerformer(battle.player2.crew.get(0))
		shootSkill.setTargetPlotLocation(PlotLocation(0, 0))
		shootSkill.sendCommand(controller)

		controller.sendPlayer2ReadySignal()

		shootSkill.setCropYield(battle.player1.crops.get(0))
		shootSkill.setPerformer(battle.player1.crew.get(0))
		shootSkill.setTargetPlotLocation(PlotLocation(0, 0))
		shootSkill.sendCommand(controller)

		controller.sendPlayer1ReadySignal()

		controller.sendPlayer2ReadySignal()

		val events = observer.getObservedEvents()
		val finalEvent = events.last()
		Assert.assertTrue(finalEvent is BattleOutcomeEvent)
		Assert.assertEquals(BattleOutcome.PLAYER1_VICTORY, (finalEvent as BattleOutcomeEvent).outcome)
	}

	private class LogbackBattleObserver : BattleObserver {

		private val observedEvents: MutableList<BattleEvent> = mutableListOf()

		override fun handleTurnResults(events: List<BattleEvent>) {
			for (event in events) {
				LOGGER.debug(ToStringBuilder.reflectionToString(event))
				observedEvents.add(event)
			}
		}

		fun getObservedEvents(): List<BattleEvent> {
			return observedEvents.toList()
		}
	}
}

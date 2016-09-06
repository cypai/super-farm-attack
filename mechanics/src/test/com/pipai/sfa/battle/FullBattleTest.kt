package com.pipai.sfa.battle

import com.google.common.collect.ImmutableMap
import com.pipai.sfa.battle.commandskill.ShootSkill
import com.pipai.sfa.battle.controller.BattleController
import com.pipai.sfa.battle.controller.BattleObserver
import com.pipai.sfa.battle.controller.BattleOutcome
import com.pipai.sfa.battle.domain.Battle
import com.pipai.sfa.battle.domain.CropSchema
import com.pipai.sfa.battle.domain.CropSize
import com.pipai.sfa.battle.domain.Farm
import com.pipai.sfa.battle.domain.PlayerTeamFactory
import com.pipai.sfa.battle.domain.PlotLocation
import com.pipai.sfa.battle.domain.UnitSchema
import com.pipai.sfa.battle.eventlog.BattleEvent
import com.pipai.sfa.battle.eventlog.BattleEvent.BattleOutcomeEvent
import org.apache.commons.lang3.builder.ToStringBuilder
import org.junit.Assert
import org.junit.Test
import org.slf4j.LoggerFactory

class FullBattleTest {

	companion object {
		private val LOGGER = LoggerFactory.getLogger(FullBattleTest::class.java)
	}

	@Test
	fun shootUnitsTest() {
		val cropOneSchema = CropSchema("Crop One", 1, 1, 1, 1, CropSize.MEDIUM)
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
		shootSkill.setCropYield(battle.getPlayer1Crop(PlotLocation(1, 0))!!)
		shootSkill.setPerformer(battle.getPlayer1Unit(PlotLocation(0, 0))!!)
		shootSkill.setTargetPlotLocation(PlotLocation(0, 0))
		shootSkill.sendCommand(controller)

		controller.sendPlayer1ReadySignal()

		shootSkill.setCropYield(battle.getPlayer2Crop(PlotLocation(1, 0))!!)
		shootSkill.setPerformer(battle.getPlayer2Unit(PlotLocation(0, 0))!!)
		shootSkill.setTargetPlotLocation(PlotLocation(0, 0))
		shootSkill.sendCommand(controller)

		controller.sendPlayer2ReadySignal()

		Assert.assertTrue(observer.getObservedEvents().size > 0)

		shootSkill.setCropYield(battle.getPlayer1Crop(PlotLocation(1, 0))!!)
		shootSkill.setPerformer(battle.getPlayer1Unit(PlotLocation(0, 1))!!)
		shootSkill.setTargetPlotLocation(PlotLocation(0, 0))
		shootSkill.sendCommand(controller)

		controller.sendPlayer1ReadySignal()

		shootSkill.setCropYield(battle.getPlayer2Crop(PlotLocation(1, 0))!!)
		shootSkill.setPerformer(battle.getPlayer2Unit(PlotLocation(0, 0))!!)
		shootSkill.setTargetPlotLocation(PlotLocation(0, 0))
		shootSkill.sendCommand(controller)

		controller.sendPlayer2ReadySignal()

		shootSkill.setCropYield(battle.getPlayer1Crop(PlotLocation(1, 0))!!)
		shootSkill.setPerformer(battle.getPlayer1Unit(PlotLocation(0, 0))!!)
		shootSkill.setTargetPlotLocation(PlotLocation(0, 0))
		shootSkill.sendCommand(controller)

		controller.sendPlayer1ReadySignal()

		controller.sendPlayer2ReadySignal()

		val events = observer.getObservedEvents()
		val finalEvent = events.last()
		Assert.assertTrue(finalEvent is BattleOutcomeEvent)
		Assert.assertEquals(BattleOutcome.PLAYER1_VICTORY, (finalEvent as BattleOutcomeEvent).outcome)
	}

	@Test
	fun testCollision() {
		val cropSmallSchema = CropSchema("Crop Small", 1, 1, 1, 1, CropSize.SMALL)
		val cropMediumSchema = CropSchema("Crop Medium", 1, 1, 1, 1, CropSize.MEDIUM)
		val cropLargeSchema = CropSchema("Crop Large", 1, 1, 1, 1, CropSize.LARGE)

		val cropSmall1 = cropSmallSchema.generateCrop("Crop Small 1")
		val cropMedium1 = cropMediumSchema.generateCrop("Crop Medium 1")
		val cropLarge1 = cropLargeSchema.generateCrop("Crop Large 1")

		val cropSmall2 = cropSmallSchema.generateCrop("Crop Small 2")
		val cropMedium2 = cropMediumSchema.generateCrop("Crop Medium 2")
		val cropLarge2 = cropLargeSchema.generateCrop("Crop Large 2")

		val unitSchema = UnitSchema("Speed One", 2, 1, 1, 1)

		val unit1 = unitSchema.generateUnit("Unit 1")
		val unit2 = unitSchema.generateUnit("Unit 2")

		val farm1 = Farm("Player 1 Farm", 3)
		val farm2 = Farm("Player 2 Farm", 3)

		val factory = PlayerTeamFactory()

		val team1 = factory.generatePlayerTeam(
				"Team 1",
				farm1,
				ImmutableMap.of(unit1, PlotLocation(0, 0)),
				ImmutableMap.of(cropSmall1, PlotLocation(1, 0), cropMedium1, PlotLocation(1, 1), cropLarge1, PlotLocation(1, 2)))
		val team2 = factory.generatePlayerTeam(
				"Team 2",
				farm2,
				ImmutableMap.of(unit2, PlotLocation(0, 0)),
				ImmutableMap.of(cropSmall2, PlotLocation(1, 0), cropMedium2, PlotLocation(1, 1), cropLarge2, PlotLocation(1, 2)))

		val battle = Battle(3, 3, team1, team2)

		val controller = BattleController(battle)

		val observer = LogbackBattleObserver()
		controller.registerObserver(observer)

		val shootSkill = ShootSkill()
		shootSkill.setCropYield(battle.getPlayer1Crop(PlotLocation(1, 0))!!)
		shootSkill.setPerformer(battle.getPlayer1Unit(PlotLocation(0, 0))!!)
		shootSkill.setTargetPlotLocation(PlotLocation(0, 0))
		shootSkill.sendCommand(controller)

		controller.sendPlayer1ReadySignal()

		shootSkill.setCropYield(battle.getPlayer2Crop(PlotLocation(1, 0))!!)
		shootSkill.setPerformer(battle.getPlayer2Unit(PlotLocation(0, 0))!!)
		shootSkill.setTargetPlotLocation(PlotLocation(0, 0))
		shootSkill.sendCommand(controller)

		controller.sendPlayer2ReadySignal()

		Assert.assertEquals(3, battle.player1.farm.hp)
		Assert.assertEquals(3, battle.player2.farm.hp)

		shootSkill.setCropYield(battle.getPlayer1Crop(PlotLocation(1, 0))!!)
		shootSkill.setPerformer(battle.getPlayer1Unit(PlotLocation(0, 0))!!)
		shootSkill.setTargetPlotLocation(PlotLocation(0, 0))
		shootSkill.sendCommand(controller)

		controller.sendPlayer1ReadySignal()

		shootSkill.setCropYield(battle.getPlayer2Crop(PlotLocation(1, 1))!!)
		shootSkill.setPerformer(battle.getPlayer2Unit(PlotLocation(0, 0))!!)
		shootSkill.setTargetPlotLocation(PlotLocation(0, 0))
		shootSkill.sendCommand(controller)

		controller.sendPlayer2ReadySignal()

		Assert.assertEquals(3, battle.player1.farm.hp)
		Assert.assertEquals(3, battle.player2.farm.hp)

		shootSkill.setCropYield(battle.getPlayer1Crop(PlotLocation(1, 0))!!)
		shootSkill.setPerformer(battle.getPlayer1Unit(PlotLocation(0, 0))!!)
		shootSkill.setTargetPlotLocation(PlotLocation(0, 0))
		shootSkill.sendCommand(controller)

		controller.sendPlayer1ReadySignal()

		shootSkill.setCropYield(battle.getPlayer2Crop(PlotLocation(1, 2))!!)
		shootSkill.setPerformer(battle.getPlayer2Unit(PlotLocation(0, 0))!!)
		shootSkill.setTargetPlotLocation(PlotLocation(0, 0))
		shootSkill.sendCommand(controller)

		controller.sendPlayer2ReadySignal()

		Assert.assertEquals(1, battle.player1.farm.hp)
		Assert.assertEquals(3, battle.player2.farm.hp)

		shootSkill.setCropYield(battle.getPlayer1Crop(PlotLocation(1, 1))!!)
		shootSkill.setPerformer(battle.getPlayer1Unit(PlotLocation(0, 0))!!)
		shootSkill.setTargetPlotLocation(PlotLocation(0, 0))
		shootSkill.sendCommand(controller)

		controller.sendPlayer1ReadySignal()

		shootSkill.setCropYield(battle.getPlayer2Crop(PlotLocation(1, 1))!!)
		shootSkill.setPerformer(battle.getPlayer2Unit(PlotLocation(0, 0))!!)
		shootSkill.setTargetPlotLocation(PlotLocation(0, 0))
		shootSkill.sendCommand(controller)

		controller.sendPlayer2ReadySignal()

		Assert.assertEquals(1, battle.player1.farm.hp)
		Assert.assertEquals(3, battle.player2.farm.hp)

		shootSkill.setCropYield(battle.getPlayer1Crop(PlotLocation(1, 1))!!)
		shootSkill.setPerformer(battle.getPlayer1Unit(PlotLocation(0, 0))!!)
		shootSkill.setTargetPlotLocation(PlotLocation(0, 0))
		shootSkill.sendCommand(controller)

		controller.sendPlayer1ReadySignal()

		shootSkill.setCropYield(battle.getPlayer2Crop(PlotLocation(1, 2))!!)
		shootSkill.setPerformer(battle.getPlayer2Unit(PlotLocation(0, 0))!!)
		shootSkill.setTargetPlotLocation(PlotLocation(0, 0))
		shootSkill.sendCommand(controller)

		controller.sendPlayer2ReadySignal()

		Assert.assertEquals(1, battle.player1.farm.hp)
		Assert.assertEquals(3, battle.player2.farm.hp)

		shootSkill.setCropYield(battle.getPlayer1Crop(PlotLocation(1, 2))!!)
		shootSkill.setPerformer(battle.getPlayer1Unit(PlotLocation(0, 0))!!)
		shootSkill.setTargetPlotLocation(PlotLocation(0, 0))
		shootSkill.sendCommand(controller)

		controller.sendPlayer1ReadySignal()

		shootSkill.setCropYield(battle.getPlayer2Crop(PlotLocation(1, 2))!!)
		shootSkill.setPerformer(battle.getPlayer2Unit(PlotLocation(0, 0))!!)
		shootSkill.setTargetPlotLocation(PlotLocation(0, 0))
		shootSkill.sendCommand(controller)

		controller.sendPlayer2ReadySignal()

		Assert.assertEquals(1, battle.player1.farm.hp)
		Assert.assertEquals(3, battle.player2.farm.hp)
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

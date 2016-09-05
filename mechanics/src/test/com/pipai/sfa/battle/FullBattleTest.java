package com.pipai.sfa.battle;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.pipai.sfa.battle.commandskill.ShootSkill;
import com.pipai.sfa.battle.controller.BattleController;
import com.pipai.sfa.battle.controller.BattleObserver;
import com.pipai.sfa.battle.controller.BattleOutcome;
import com.pipai.sfa.battle.domain.Battle;
import com.pipai.sfa.battle.domain.Crop;
import com.pipai.sfa.battle.domain.CropSchema;
import com.pipai.sfa.battle.domain.Farm;
import com.pipai.sfa.battle.domain.PlayerTeam;
import com.pipai.sfa.battle.domain.PlayerTeamFactory;
import com.pipai.sfa.battle.domain.PlotLocation;
import com.pipai.sfa.battle.domain.Unit;
import com.pipai.sfa.battle.domain.UnitSchema;
import com.pipai.sfa.battle.eventlog.BattleEvent;
import com.pipai.sfa.battle.eventlog.BattleEvent.BattleOutcomeEvent;

public class FullBattleTest {

	private static final Logger LOGGER = LoggerFactory.getLogger(FullBattleTest.class);

	@Test
	public void shootUnitsTest() {
		CropSchema cropOneSchema = new CropSchema("Crop One", 1, 1, 1, 1, 1);
		Crop crop1 = cropOneSchema.generateCrop("Crop 1");
		Crop crop2 = cropOneSchema.generateCrop("Crop 2");

		UnitSchema speedOneSchema = new UnitSchema("Speed One", 2, 1, 1, 1);
		UnitSchema speedTwoSchema = new UnitSchema("Speed Two", 2, 1, 1, 2);
		Unit unit11 = speedOneSchema.generateUnit("Unit 1 1");
		Unit unit12 = speedTwoSchema.generateUnit("Unit 1 2");
		Unit unit22 = speedTwoSchema.generateUnit("Unit 2 2");

		Farm farm1 = new Farm("Player 1 Farm", 3);
		Farm farm2 = new Farm("Player 2 Farm", 3);

		PlayerTeamFactory factory = new PlayerTeamFactory();
		PlayerTeam team1 = factory.generatePlayerTeam(
				"Team 1",
				farm1,
				ImmutableMap.of(unit11, new PlotLocation(0, 0), unit12, new PlotLocation(0, 1)),
				ImmutableMap.of(crop1, new PlotLocation(1, 0)));

		PlayerTeam team2 = factory.generatePlayerTeam(
				"Team 2",
				farm2,
				ImmutableMap.of(unit22, new PlotLocation(0, 0)),
				ImmutableMap.of(crop2, new PlotLocation(1, 0)));

		Battle battle = new Battle(3, 3, team1, team2);

		BattleController controller = new BattleController(battle);

		LogbackBattleObserver observer = new LogbackBattleObserver();
		controller.registerObserver(observer);

		ShootSkill shootSkill = new ShootSkill();
		shootSkill.setCropYield(battle.getPlayer1().getCrops().get(0));
		shootSkill.setPerformer(battle.getPlayer1().getCrew().get(0));
		shootSkill.setTargetPlotLocation(new PlotLocation(0, 0));
		shootSkill.sendCommand(controller);

		controller.sendPlayer1ReadySignal();

		shootSkill.setCropYield(battle.getPlayer2().getCrops().get(0));
		shootSkill.setPerformer(battle.getPlayer2().getCrew().get(0));
		shootSkill.setTargetPlotLocation(new PlotLocation(0, 0));
		shootSkill.sendCommand(controller);

		controller.sendPlayer2ReadySignal();

		Assert.assertTrue(observer.getObservedEvents().size() > 0);

		shootSkill.setCropYield(battle.getPlayer1().getCrops().get(0));
		shootSkill.setPerformer(battle.getPlayer1().getCrew().get(1));
		shootSkill.setTargetPlotLocation(new PlotLocation(0, 0));
		shootSkill.sendCommand(controller);

		controller.sendPlayer1ReadySignal();

		shootSkill.setCropYield(battle.getPlayer2().getCrops().get(0));
		shootSkill.setPerformer(battle.getPlayer2().getCrew().get(0));
		shootSkill.setTargetPlotLocation(new PlotLocation(0, 0));
		shootSkill.sendCommand(controller);

		controller.sendPlayer2ReadySignal();

		shootSkill.setCropYield(battle.getPlayer1().getCrops().get(0));
		shootSkill.setPerformer(battle.getPlayer1().getCrew().get(0));
		shootSkill.setTargetPlotLocation(new PlotLocation(0, 0));
		shootSkill.sendCommand(controller);

		controller.sendPlayer1ReadySignal();

		controller.sendPlayer2ReadySignal();

		List<BattleEvent> events = observer.getObservedEvents();
		BattleEvent finalEvent = events.get(events.size() - 1);
		Assert.assertTrue(finalEvent instanceof BattleOutcomeEvent);
		Assert.assertEquals(BattleOutcome.PLAYER1_VICTORY, ((BattleOutcomeEvent) finalEvent).getOutcome());
	}

	private static class LogbackBattleObserver implements BattleObserver {

		private List<BattleEvent> observedEvents;

		LogbackBattleObserver() {
			observedEvents = new ArrayList<>();
		}

		@Override
		public void handleTurnResults(List<? extends BattleEvent> events) {
			for (BattleEvent event : events) {
				LOGGER.debug(ToStringBuilder.reflectionToString(event));
				observedEvents.add(event);
			}
		}

		public ImmutableList<BattleEvent> getObservedEvents() {
			return ImmutableList.copyOf(observedEvents);
		}

	}

}

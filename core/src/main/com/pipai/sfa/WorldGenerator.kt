package com.pipai.sfa

import com.artemis.ComponentMapper
import com.artemis.World
import com.artemis.annotations.Wire
import com.badlogic.gdx.Gdx
import com.pipai.sfa.artemis.components.CropsComponent
import com.pipai.sfa.artemis.components.FarmComponent
import com.pipai.sfa.artemis.components.FieldComponent
import com.pipai.sfa.artemis.components.FieldCropUiData
import com.pipai.sfa.artemis.components.FieldUnitUiData
import com.pipai.sfa.artemis.components.PlayerComponent
import com.pipai.sfa.artemis.components.TeamComponent
import com.pipai.sfa.artemis.components.XYComponent
import com.pipai.sfa.battle.domain.Battle
import com.pipai.sfa.battle.domain.Player

@Wire
class WorldGenerator(private val world: World, private val battle: Battle) {

	private lateinit var mXy: ComponentMapper<XYComponent>
	private lateinit var mPlayer: ComponentMapper<PlayerComponent>
	private lateinit var mTeams: ComponentMapper<TeamComponent>
	private lateinit var mCrops: ComponentMapper<CropsComponent>
	private lateinit var mFarm: ComponentMapper<FarmComponent>
	private lateinit var mField: ComponentMapper<FieldComponent>

	private val screenWidth = Gdx.graphics.width.toFloat()
	private val screenHeight = Gdx.graphics.height.toFloat()

	init {
		world.inject(this)
	}

	fun generate() {
		generateFieldUnitEntities()
		generateFieldCropEntities()
		generateFarmEntities()
		generateFieldEntities()
	}

	fun generateFieldUnitEntities() {
		val playerTeamId = world.create()
		val cPlayerTeam = mTeams.create(playerTeamId)
		cPlayerTeam.teamData.addAll(battle.player1.crew.map { FieldUnitUiData(it) })
		val cPlayerTeamPosition = mXy.create(playerTeamId)
		cPlayerTeamPosition.x = screenWidth * 2 / 3
		cPlayerTeamPosition.y = screenHeight / 2

		val opponentTeamId = world.create()
		val cOpponentTeam = mTeams.create(opponentTeamId)
		cOpponentTeam.teamData.addAll(battle.player2.crew.map { FieldUnitUiData(it) })
		val cOpponentTeamPosition = mXy.create(opponentTeamId)
		cOpponentTeamPosition.x = screenWidth * 2 / 3
		cOpponentTeamPosition.y = screenHeight * 19 / 20
	}

	fun generateFieldCropEntities() {
		val playerCropsId = world.create()
		val cPlayerCrops = mCrops.create(playerCropsId)
		cPlayerCrops.cropsData.addAll(battle.player1.crops.map { FieldCropUiData(it, battle.player1.cropYields.get(it.crop)!!) })
		val cPlayerCropsPosition = mXy.create(playerCropsId)
		cPlayerCropsPosition.x = screenWidth / 3
		cPlayerCropsPosition.y = screenHeight / 2

		val opponentCropsId = world.create()
		val cOpponentCrops = mCrops.create(opponentCropsId)
		cOpponentCrops.cropsData.addAll(battle.player2.crops.map { FieldCropUiData(it, battle.player2.cropYields.get(it.crop)!!) })
		val cOpponentCropsPosition = mXy.create(opponentCropsId)
		cOpponentCropsPosition.x = screenWidth / 3
		cOpponentCropsPosition.y = screenHeight * 19 / 20
	}

	fun generateFarmEntities() {
		val playerFarmId = world.create()
		mFarm.create(playerFarmId).initByFarm(battle.player1.farm)
		mXy.create(playerFarmId).setXy(screenWidth / 20, screenHeight * 5 / 20)

		val opponentFarmId = world.create()
		mFarm.create(opponentFarmId).initByFarm(battle.player2.farm)
		mXy.create(opponentFarmId).setXy(screenWidth / 20, screenHeight * 19 / 20)
	}

	fun generateFieldEntities() {
		val playerFieldId = world.create()
		mField.create(playerFieldId).initByPlayerTeam(battle.player1)
		mPlayer.create(playerFieldId).player = Player.PLAYER_1
		mXy.create(playerFieldId).setXy(screenWidth / 20, screenHeight / 2)

		val opponentFieldId = world.create()
		mField.create(opponentFieldId).initByPlayerTeam(battle.player2)
		mPlayer.create(opponentFieldId).player = Player.PLAYER_2
		mXy.create(opponentFieldId).setXy(screenWidth / 20, screenHeight * 17 / 20)
	}

}

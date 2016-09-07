package com.pipai.sfa

import com.artemis.ComponentMapper
import com.artemis.World
import com.artemis.annotations.Wire
import com.badlogic.gdx.Gdx
import com.pipai.sfa.artemis.components.CropsComponent
import com.pipai.sfa.artemis.components.FieldCropUiData
import com.pipai.sfa.artemis.components.FieldUnitUiData
import com.pipai.sfa.artemis.components.TeamComponent
import com.pipai.sfa.artemis.components.XYComponent
import com.pipai.sfa.battle.domain.Battle

@Wire
class WorldGenerator(private val world: World, private val battle: Battle) {

	private lateinit var mXy: ComponentMapper<XYComponent>
	private lateinit var mTeams: ComponentMapper<TeamComponent>
	private lateinit var mCrops: ComponentMapper<CropsComponent>

	init {
		world.inject(this)
	}

	fun generate() {
		generateFieldUnitEntities()
		generateFieldCropEntities()
	}

	fun generateFieldUnitEntities() {
		val playerTeamId = world.create()
		val cPlayerTeam = mTeams.create(playerTeamId)
		cPlayerTeam.teamData.addAll(battle.player1.crew.map { FieldUnitUiData(it) })
		val cPlayerTeamPosition = mXy.create(playerTeamId)
		cPlayerTeamPosition.x = Gdx.graphics.width.toFloat() / 2
		cPlayerTeamPosition.y = Gdx.graphics.height.toFloat() / 2

		val opponentTeamId = world.create()
		val cOpponentTeam = mTeams.create(opponentTeamId)
		cOpponentTeam.teamData.addAll(battle.player2.crew.map { FieldUnitUiData(it) })
		val cOpponentTeamPosition = mXy.create(opponentTeamId)
		cOpponentTeamPosition.x = Gdx.graphics.width.toFloat() / 2
		cOpponentTeamPosition.y = Gdx.graphics.height.toFloat()
	}

	fun generateFieldCropEntities() {
		val playerCropsId = world.create()
		val cPlayerCrops = mCrops.create(playerCropsId)
		cPlayerCrops.cropsData.addAll(battle.player1.crops.map { FieldCropUiData(it, battle.player1.cropYields.get(it.crop)!!) })
		val cPlayerCropsPosition = mXy.create(playerCropsId)
		cPlayerCropsPosition.x = 0f
		cPlayerCropsPosition.y = Gdx.graphics.height.toFloat() / 2

		val opponentCropsId = world.create()
		val cOpponentCrops = mCrops.create(opponentCropsId)
		cOpponentCrops.cropsData.addAll(battle.player2.crops.map { FieldCropUiData(it, battle.player2.cropYields.get(it.crop)!!) })
		val cOpponentCropsPosition = mXy.create(opponentCropsId)
		cOpponentCropsPosition.x = 0f
		cOpponentCropsPosition.y = Gdx.graphics.height.toFloat()
	}

}

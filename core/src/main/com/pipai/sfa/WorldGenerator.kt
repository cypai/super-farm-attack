package com.pipai.sfa

import com.artemis.ComponentMapper
import com.artemis.World
import com.artemis.annotations.Wire
import com.pipai.sfa.artemis.components.CropsComponent
import com.pipai.sfa.artemis.components.PlayerCropUiData
import com.pipai.sfa.artemis.components.PlayerUnitUiData
import com.pipai.sfa.artemis.components.TeamsComponent
import com.pipai.sfa.battle.domain.Battle

@Wire
class WorldGenerator(private val world: World, private val battle: Battle) {

	private lateinit var mTeams: ComponentMapper<TeamsComponent>
	private lateinit var mCrops: ComponentMapper<CropsComponent>

	init {
		world.inject(this)
	}

	fun generate() {
		val teamsId = world.create()
		val cTeams = mTeams.create(teamsId)
		cTeams.playerTeam.addAll(battle.player1.crew.map { PlayerUnitUiData(it) })
		cTeams.opponentTeam.addAll(battle.player2.crew.map { PlayerUnitUiData(it) })

		val cropsId = world.create()
		val cCrops = mCrops.create(cropsId)
		cCrops.playerCrops.addAll(battle.player1.crops.map { PlayerCropUiData(it, battle.player1.cropYields.get(it.crop)!!) })
		cCrops.opponentCrops.addAll(battle.player2.crops.map { PlayerCropUiData(it, battle.player2.cropYields.get(it.crop)!!) })
	}

}

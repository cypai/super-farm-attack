package com.pipai.sfa.screen

import com.artemis.World
import com.artemis.WorldConfiguration
import com.artemis.WorldConfigurationBuilder
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.GL20
import com.pipai.sfa.SfaGame
import com.pipai.sfa.WorldGenerator
import com.pipai.sfa.artemis.system.SimpleCropsDrawingSystem
import com.pipai.sfa.artemis.system.SimpleTeamDrawingSystem
import com.pipai.sfa.generateStandardBattle
import com.pipai.sfa.gui.BatchHelper
import org.slf4j.LoggerFactory

class BattleScreen(game: SfaGame) : SwitchableScreen(game) {

	companion object {
		private val LOGGER = LoggerFactory.getLogger(BattleScreen::class.java)
	}

	private val world: World
	private val batch: BatchHelper = game.batchHelper

	init {
		val battle = generateStandardBattle(game.unitSchemaIndex, game.cropSchemaIndex, 6, 6, 6, 6)

		val config: WorldConfiguration = WorldConfigurationBuilder()
				.with(
						SimpleTeamDrawingSystem(batch),
						SimpleCropsDrawingSystem(batch)
				).build()

		world = World(config)

		val generator = WorldGenerator(world, battle)
		generator.generate()
	}

	override fun render(delta: Float) {
		Gdx.gl.glClearColor(0f, 0f, 0f, 1f)
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT or GL20.GL_DEPTH_BUFFER_BIT)
		world.process()
	}

	override fun resize(width: Int, height: Int) {
	}

	override fun pause() {
	}

	override fun resume() {
	}

	override fun show() {
	}

	override fun hide() {
	}

	override fun dispose() {
	}
}

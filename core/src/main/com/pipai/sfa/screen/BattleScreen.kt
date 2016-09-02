package com.pipai.sfa.screen

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.GL20
import com.pipai.sfa.SfaGame
import com.pipai.sfa.battle.components.Crop
import com.pipai.sfa.battle.components.CropSchema
import com.pipai.sfa.battle.components.Unit
import com.pipai.sfa.battle.components.UnitSchema
import org.slf4j.LoggerFactory

class BattleScreen(game: SfaGame) : SwitchableScreen(game) {

	companion object {
		private val LOGGER = LoggerFactory.getLogger(BattleScreen::class.java)
	}

	init {
		val cropSchema = CropSchema("Potato", 0, 0, 0)
		val crop = Crop(cropSchema, "Potato Actual", 0, 0, 0)
		LOGGER.info("Hello world: " + crop)

		val unitSchema = UnitSchema("Red Riding Hood", 0, 0, 0, 0)
		val unit = Unit(unitSchema, "Ruby", 0, 0, 0, 0)
		LOGGER.info("Hello world: " + unit)
	}

	override fun render(delta: Float) {
		Gdx.gl.glClearColor(0f, 0f, 0f, 1f)
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT or GL20.GL_DEPTH_BUFFER_BIT)
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

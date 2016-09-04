package com.pipai.sfa.screen

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.GL20
import com.pipai.sfa.SfaGame
import com.pipai.sfa.index.CropSchemaIndex
import com.pipai.sfa.index.UnitSchemaIndex
import org.slf4j.LoggerFactory

class BattleScreen(game: SfaGame) : SwitchableScreen(game) {

	companion object {
		private val LOGGER = LoggerFactory.getLogger(BattleScreen::class.java)
	}

	init {
		val cropSchemaIndex = CropSchemaIndex(Gdx.files.internal("data/crops.csv"))
		LOGGER.info("Crop Schema Index: " + cropSchemaIndex.allCropSchemas)

		val unitSchemaIndex = UnitSchemaIndex(Gdx.files.internal("data/crewUnits.csv"))
		LOGGER.info("Unit Schema Index: " + unitSchemaIndex.allUnitSchemas)
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

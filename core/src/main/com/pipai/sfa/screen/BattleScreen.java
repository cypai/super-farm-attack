package com.pipai.sfa.screen;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.pipai.sfa.SfaGame;
import com.pipai.sfa.battle.components.Crop;
import com.pipai.sfa.battle.components.CropSchema;
import com.pipai.sfa.gui.BatchHelper;

public class BattleScreen extends SwitchableScreen {

	private static final Logger LOGGER = LoggerFactory.getLogger(BattleScreen.class);

	private BatchHelper batch;

	public BattleScreen(SfaGame game) {
		super(game);

		batch = new BatchHelper(game.getSpriteBatch(), game.getShapeRenderer(), game.getModelBatch(), game.getFont());

		CropSchema cropSchema = new CropSchema("Potato", 0, 0, 0);
		Crop crop = new Crop(cropSchema, "Potato Actual", 0, 0, 0);

		LOGGER.info("Hello world: " + crop);

	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
	}

	@Override
	public void resize(int width, int height) {
	}

	@Override
	public void pause() {

	}

	@Override
	public void resume() {

	}

	@Override
	public void show() {

	}

	@Override
	public void hide() {

	}

	@Override
	public void dispose() {
	}

}

package com.pipai.sfa;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.pipai.sfa.gui.BatchHelper;
import com.pipai.sfa.index.CropSchemaIndex;
import com.pipai.sfa.index.UnitSchemaIndex;
import com.pipai.sfa.screen.BattleScreen;

public class SfaGame extends Game {

	private static final Logger LOGGER = LoggerFactory.getLogger(SfaGame.class);

	private BatchHelper batchHelper;

	private SpriteBatch spriteBatch;
	private ShapeRenderer shapeRenderer;
	private BitmapFont font;

	private UnitSchemaIndex unitSchemaIndex;
	private CropSchemaIndex cropSchemaIndex;

	@Override
	public void create() {
		ToStringBuilder.setDefaultStyle(ToStringStyle.SHORT_PREFIX_STYLE);

		cropSchemaIndex = new CropSchemaIndex(Gdx.files.internal("data/crops.csv"));
		LOGGER.info("Crop Schema Index: " + cropSchemaIndex.getAllCropSchemas());

		unitSchemaIndex = new UnitSchemaIndex(Gdx.files.internal("data/crewUnits.csv"));
		LOGGER.info("Unit Schema Index: " + unitSchemaIndex.getAllUnitSchemas());

		spriteBatch = new SpriteBatch();
		shapeRenderer = new ShapeRenderer();
		font = new BitmapFont();

		batchHelper = new BatchHelper(spriteBatch, shapeRenderer, font);
		setScreen(new BattleScreen(this));
	}

	@Override
	public void render() {
		super.render();
	}

	@Override
	public void dispose() {
		spriteBatch.dispose();
		shapeRenderer.dispose();
		font.dispose();
	}

	public BatchHelper getBatchHelper() {
		return batchHelper;
	}

	public SpriteBatch getSpriteBatch() {
		return spriteBatch;
	}

	public ShapeRenderer getShapeRenderer() {
		return shapeRenderer;
	}

	public BitmapFont getFont() {
		return font;
	}

	public UnitSchemaIndex getUnitSchemaIndex() {
		return unitSchemaIndex;
	}

	public CropSchemaIndex getCropSchemaIndex() {
		return cropSchemaIndex;
	}
}

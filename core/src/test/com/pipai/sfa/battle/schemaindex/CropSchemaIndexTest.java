package com.pipai.sfa.battle.schemaindex;

import org.junit.Assert;
import org.junit.Test;

import com.badlogic.gdx.Gdx;
import com.google.common.collect.ImmutableList;
import com.pipai.libgdx.test.GdxMockedTest;
import com.pipai.sfa.battle.components.CropSchema;

public class CropSchemaIndexTest extends GdxMockedTest {

	@Test
	public void testReadActualFile() {
		CropSchemaIndex cropSchemaIndex = new CropSchemaIndex(Gdx.files.internal("data/crops.csv"));
		ImmutableList<CropSchema> cropSchemas = cropSchemaIndex.getAllCropSchemas();
		Assert.assertTrue(cropSchemas.size() > 0);
	}

}

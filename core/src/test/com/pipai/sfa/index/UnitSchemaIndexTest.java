package com.pipai.sfa.index;

import org.junit.Assert;
import org.junit.Test;

import com.badlogic.gdx.Gdx;
import com.google.common.collect.ImmutableList;
import com.pipai.libgdx.test.GdxMockedTest;
import com.pipai.sfa.battle.domain.UnitSchema;

public class UnitSchemaIndexTest extends GdxMockedTest {

	@Test
	public void testReadActualFile() {
		UnitSchemaIndex unitSchemaIndex = new UnitSchemaIndex(Gdx.files.internal("data/crewUnits.csv"));
		ImmutableList<UnitSchema> unitSchemas = unitSchemaIndex.getAllUnitSchemas();
		Assert.assertTrue(unitSchemas.size() > 0);
	}

}

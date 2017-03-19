package com.pipai.sfa.artemis.system

import com.artemis.systems.IteratingSystem
import com.badlogic.gdx.graphics.Color
import com.pipai.sfa.artemis.components.FarmComponent
import com.pipai.sfa.artemis.components.FieldUnitUiData
import com.pipai.sfa.artemis.components.XYComponent
import com.pipai.sfa.gui.BatchHelper
import com.pipai.sfa.utils.allOf
import com.pipai.sfa.utils.require

class SimpleFarmDrawingSystem(val batch: BatchHelper) : IteratingSystem(allOf()) {

	private val mFarm by require<FarmComponent>()
	private val mXy by require<XYComponent>()

	override fun process(entityId: Int) {
		val cFarm = mFarm.get(entityId)
		val farmString = "${cFarm.name}: HP ${cFarm.hp}/${cFarm.hpMax}"
		val position = mXy.get(entityId)

		batch.spr.begin()
		batch.font.setColor(Color.WHITE)
		batch.font.draw(batch.spr, farmString, position.x, position.y)
		batch.spr.end()
	}

}

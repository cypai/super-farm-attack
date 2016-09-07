package com.pipai.sfa.artemis.system

import com.artemis.systems.IteratingSystem
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.g2d.GlyphLayout
import com.pipai.sfa.artemis.components.CropsComponent
import com.pipai.sfa.artemis.components.FieldCropUiData
import com.pipai.sfa.gui.BatchHelper
import com.pipai.sfa.utils.allOf
import com.pipai.sfa.utils.require
import com.pipai.sfa.artemis.components.XYComponent

class SimpleCropsDrawingSystem(val batch: BatchHelper) : IteratingSystem(allOf()) {

	private val mCrops by require<CropsComponent>()
	private val mXy by require<XYComponent>()

	override fun process(entityId: Int) {
		val cropsData = mCrops.get(entityId).cropsData

		val cropString = cropsData.map { generateCropString(it) }.reduce({ x, y -> "$x\n$y" })

		val position = mXy.get(entityId)

		batch.spr.begin()
		batch.font.setColor(Color.WHITE)
		batch.font.draw(batch.spr, cropString, position.x, position.y)
		batch.spr.end()
	}

}

private fun generateCropString(cropData: FieldCropUiData): String {
	return """
			${cropData.amount} ${cropData.nickname} ${cropData.hp}/${cropData.hpMax} Yield Time: ${cropData.turnsUntilYield}
			  ${cropData.size} PAtk: ${cropData.patk} PDef: ${cropData.pdef}
			""".trimIndent()
}

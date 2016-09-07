package com.pipai.sfa.artemis.system

import com.artemis.systems.IteratingSystem
import com.badlogic.gdx.graphics.Color
import com.pipai.sfa.artemis.components.FieldUnitUiData
import com.pipai.sfa.artemis.components.TeamComponent
import com.pipai.sfa.artemis.components.XYComponent
import com.pipai.sfa.gui.BatchHelper
import com.pipai.sfa.utils.allOf
import com.pipai.sfa.utils.require

class SimpleTeamDrawingSystem(val batch: BatchHelper) : IteratingSystem(allOf()) {

	private val mTeam by require<TeamComponent>()
	private val mXy by require<XYComponent>()

	override fun process(entityId: Int) {
		val teamData = mTeam.get(entityId).teamData

		val teamString = teamData.map { generateUnitString(it) }.reduce({ x, y -> "$x\n$y" })

		val position = mXy.get(entityId)

		batch.spr.begin()
		batch.font.setColor(Color.WHITE)
		batch.font.draw(batch.spr, teamString, position.x, position.y)
		batch.spr.end()
	}

}

private fun generateUnitString(unitData: FieldUnitUiData): String {
	return """
			${unitData.nickname} ${unitData.hp}/${unitData.hpMax} Location: ${unitData.location}
			  Speed: ${unitData.speed} PAtk: ${unitData.patk} PDef: ${unitData.pdef}
			""".trimIndent()
}

package com.pipai.sfa.artemis.system

import com.artemis.systems.IteratingSystem
import com.badlogic.gdx.graphics.Color
import com.pipai.sfa.artemis.components.FieldComponent
import com.pipai.sfa.artemis.components.PlayerComponent
import com.pipai.sfa.artemis.components.XYComponent
import com.pipai.sfa.battle.domain.Player
import com.pipai.sfa.gui.BatchHelper
import com.pipai.sfa.utils.allOf
import com.pipai.sfa.utils.require

class SimpleFieldDrawingSystem(val batch: BatchHelper) : IteratingSystem(allOf()) {

	private val mField by require<FieldComponent>()
	private val mPlayer by require<PlayerComponent>()
	private val mXy by require<XYComponent>()

	override fun process(entityId: Int) {
		val cField = mField.get(entityId)

		val text = generateFieldString(cField, mPlayer.get(entityId).player == Player.PLAYER_2)

		val position = mXy.get(entityId)

		batch.spr.begin()
		batch.font.setColor(Color.WHITE)
		batch.font.draw(batch.spr, text, position.x, position.y)
		batch.spr.end()
	}

}

private fun generateFieldString(field: FieldComponent, reverse: Boolean): String {
	val row1 = " 1    2    3\n${locStr(field, 1)} ${locStr(field, 2)} ${locStr(field, 3)}"
	val row2 = " 4    5    6\n${locStr(field, 4)} ${locStr(field, 5)} ${locStr(field, 6)}"
	val row3 = " 7    8    9\n${locStr(field, 7)} ${locStr(field, 8)} ${locStr(field, 9)}"

	if (reverse) {
		return "$row3\n\n$row2\n\n$row1"
	} else {
		return "$row1\n\n$row2\n\n$row3"
	}
}

private fun locStr(field: FieldComponent, location: Int): String {
	return (if (field.fieldCropLocations.get(location)!!) "C" else "-") + " " + (if (field.fieldUnitLocations.get(location)!!) "U" else "-")
}

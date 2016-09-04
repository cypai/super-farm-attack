package com.pipai.sfa.battle.commandskill

import com.pipai.sfa.battle.domain.PlayerUnit

interface RequiresTarget {

	fun setTarget(performer: PlayerUnit)
	fun getTarget(): PlayerUnit
	fun clearTarget()
}

interface RequiresTargetMixin : RequiresTarget {
	val requiresTargetImpl: RequiresTargetImpl

	override fun setTarget(performer: PlayerUnit) {
		requiresTargetImpl.setTarget(performer)
	}

	override fun getTarget(): PlayerUnit {
		return requiresTargetImpl.getTarget()
	}

	override fun clearTarget() {
		requiresTargetImpl.clearTarget()
	}
}

class RequiresTargetImpl : RequiresTarget {

	private var target: PlayerUnit? = null

	override fun setTarget(performer: PlayerUnit) {
		this.target = performer
	}

	override fun getTarget(): PlayerUnit {
		return target ?: throw CommandGenerationException("No crop yield was selected")
	}

	override fun clearTarget() {
		target = null
	}

}

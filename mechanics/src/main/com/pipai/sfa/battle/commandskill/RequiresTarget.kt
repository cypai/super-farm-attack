package com.pipai.sfa.battle.commandskill

import com.pipai.sfa.battle.domain.PlayerUnit

interface RequiresTarget {

	fun setTarget(target: PlayerUnit)
	fun getTarget(): PlayerUnit
	fun clearTarget()
}

interface RequiresTargetMixin : RequiresTarget {
	val requiresTargetImpl: RequiresTargetImpl

	override fun setTarget(target: PlayerUnit) {
		requiresTargetImpl.setTarget(target)
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

	override fun setTarget(target: PlayerUnit) {
		this.target = target
	}

	override fun getTarget(): PlayerUnit {
		return target ?: throw CommandGenerationException("No target PlayerUnit was selected")
	}

	override fun clearTarget() {
		target = null
	}

}

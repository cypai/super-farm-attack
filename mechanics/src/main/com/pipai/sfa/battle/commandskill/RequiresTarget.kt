package com.pipai.sfa.battle.commandskill

import com.pipai.sfa.battle.domain.FieldUnit

interface RequiresTarget {

	fun setTarget(target: FieldUnit)
	fun getTarget(): FieldUnit
	fun clearTarget()
}

interface RequiresTargetMixin : RequiresTarget {
	val requiresTargetImpl: RequiresTargetImpl

	override fun setTarget(target: FieldUnit) {
		requiresTargetImpl.setTarget(target)
	}

	override fun getTarget(): FieldUnit {
		return requiresTargetImpl.getTarget()
	}

	override fun clearTarget() {
		requiresTargetImpl.clearTarget()
	}
}

class RequiresTargetImpl : RequiresTarget {

	private var target: FieldUnit? = null

	override fun setTarget(target: FieldUnit) {
		this.target = target
	}

	override fun getTarget(): FieldUnit {
		return target ?: throw CommandGenerationException("No target PlayerUnit was selected")
	}

	override fun clearTarget() {
		target = null
	}

}

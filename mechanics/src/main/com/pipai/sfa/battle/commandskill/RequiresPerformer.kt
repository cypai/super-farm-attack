package com.pipai.sfa.battle.commandskill

import com.pipai.sfa.battle.domain.FieldUnit

interface RequiresPerformer {

	fun setPerformer(performer: FieldUnit)
	fun getPerformer(): FieldUnit
	fun clearPerformer()
}

interface RequiresPerformerMixin : RequiresPerformer {
	val requiresPerformerImpl: RequiresPerformerImpl

	override fun setPerformer(performer: FieldUnit) {
		requiresPerformerImpl.setPerformer(performer)
	}

	override fun getPerformer(): FieldUnit {
		return requiresPerformerImpl.getPerformer()
	}

	override fun clearPerformer() {
		requiresPerformerImpl.clearPerformer()
	}
}

class RequiresPerformerImpl : RequiresPerformer {

	private var performer: FieldUnit? = null

	override fun setPerformer(performer: FieldUnit) {
		this.performer = performer
	}

	override fun getPerformer(): FieldUnit {
		return performer ?: throw CommandGenerationException("No performer PlayerUnit was selected")
	}

	override fun clearPerformer() {
		performer = null
	}

}


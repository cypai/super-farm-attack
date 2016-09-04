package com.pipai.sfa.battle.commandskill

import com.pipai.sfa.battle.domain.PlayerUnit

interface RequiresPerformer {

	fun setPerformer(performer: PlayerUnit)
	fun getPerformer(): PlayerUnit
	fun clearPerformer()
}

interface RequiresPerformerMixin : RequiresPerformer {
	val requiresPerformerImpl: RequiresPerformerImpl

	override fun setPerformer(performer: PlayerUnit) {
		requiresPerformerImpl.setPerformer(performer)
	}

	override fun getPerformer(): PlayerUnit {
		return requiresPerformerImpl.getPerformer()
	}

	override fun clearPerformer() {
		requiresPerformerImpl.clearPerformer()
	}
}

class RequiresPerformerImpl : RequiresPerformer {

	private var performer: PlayerUnit? = null

	override fun setPerformer(performer: PlayerUnit) {
		this.performer = performer
	}

	override fun getPerformer(): PlayerUnit {
		return performer ?: throw CommandGenerationException("No crop yield was selected")
	}

	override fun clearPerformer() {
		performer = null
	}

}


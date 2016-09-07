package com.pipai.sfa.battle.commandskill

import com.pipai.sfa.battle.domain.PlayerCrop
import com.pipai.sfa.battle.domain.FieldCrop

interface RequiresCropYield {

	fun setCropYield(cropYield: FieldCrop)
	fun getCropYield(): FieldCrop
	fun clearCropYield()
}

interface RequiresCropYieldMixin : RequiresCropYield {
	val requiresCropYieldImpl: RequiresCropYieldImpl

	override fun setCropYield(cropYield: FieldCrop) {
		requiresCropYieldImpl.setCropYield(cropYield)
	}

	override fun getCropYield(): FieldCrop {
		return requiresCropYieldImpl.getCropYield()
	}

	override fun clearCropYield() {
		requiresCropYieldImpl.clearCropYield()
	}
}

class RequiresCropYieldImpl : RequiresCropYield {

	private var cropYield: FieldCrop? = null

	override fun setCropYield(cropYield: FieldCrop) {
		this.cropYield = cropYield
	}

	override fun getCropYield(): FieldCrop {
		return cropYield ?: throw CommandGenerationException("No crop yield was selected")
	}

	override fun clearCropYield() {
		cropYield = null
	}

}

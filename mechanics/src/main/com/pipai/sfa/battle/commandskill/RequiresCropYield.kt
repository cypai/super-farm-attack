package com.pipai.sfa.battle.commandskill

import com.pipai.sfa.battle.domain.Crop

interface RequiresCropYield {

	fun setCropYield(cropYield: Crop)
	fun getCropYield(): Crop
	fun clearCropYield()
}

interface RequiresCropYieldMixin : RequiresCropYield {
	val requiresCropYieldImpl: RequiresCropYieldImpl

	override fun setCropYield(cropYield: Crop) {
		requiresCropYieldImpl.setCropYield(cropYield)
	}

	override fun getCropYield(): Crop {
		return requiresCropYieldImpl.getCropYield()
	}

	override fun clearCropYield() {
		requiresCropYieldImpl.clearCropYield()
	}
}

class RequiresCropYieldImpl : RequiresCropYield {

	private var cropYield: Crop? = null

	override fun setCropYield(cropYield: Crop) {
		this.cropYield = cropYield
	}

	override fun getCropYield(): Crop {
		return cropYield ?: throw CommandGenerationException("No crop yield was selected")
	}

	override fun clearCropYield() {
		cropYield = null
	}

}

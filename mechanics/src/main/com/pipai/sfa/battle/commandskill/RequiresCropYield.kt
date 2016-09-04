package com.pipai.sfa.battle.commandskill

import com.pipai.sfa.battle.domain.Crop
import com.pipai.sfa.battle.domain.PlayerCrop

interface RequiresCropYield {

	fun setCropYield(cropYield: PlayerCrop)
	fun getCropYield(): PlayerCrop
	fun clearCropYield()
}

interface RequiresCropYieldMixin : RequiresCropYield {
	val requiresCropYieldImpl: RequiresCropYieldImpl

	override fun setCropYield(cropYield: PlayerCrop) {
		requiresCropYieldImpl.setCropYield(cropYield)
	}

	override fun getCropYield(): PlayerCrop {
		return requiresCropYieldImpl.getCropYield()
	}

	override fun clearCropYield() {
		requiresCropYieldImpl.clearCropYield()
	}
}

class RequiresCropYieldImpl : RequiresCropYield {

	private var cropYield: PlayerCrop? = null

	override fun setCropYield(cropYield: PlayerCrop) {
		this.cropYield = cropYield
	}

	override fun getCropYield(): PlayerCrop {
		return cropYield ?: throw CommandGenerationException("No crop yield was selected")
	}

	override fun clearCropYield() {
		cropYield = null
	}

}

package com.pipai.sfa.artemis.system

import com.artemis.systems.IteratingSystem
import com.pipai.sfa.artemis.components.CropsComponent
import com.pipai.sfa.utils.allOf
import com.pipai.sfa.utils.require

class SimpleCropsDrawingSystem : IteratingSystem(allOf()) {

	private val mCrops by require<CropsComponent>()

	override fun process(entityId: Int) {
	}

}

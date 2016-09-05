package com.pipai.sfa.battle.commandskill

import com.pipai.sfa.battle.domain.PlotLocation

interface RequiresTargetPlotLocation {

	fun setTargetPlotLocation(plotLocation: PlotLocation)
	fun getTargetPlotLocation(): PlotLocation
	fun clearTargetPlotLocation()
}

interface RequiresTargetPlotLocationMixin : RequiresTargetPlotLocation {
	val requiresTargetPlotLocationImpl: RequiresTargetPlotLocationImpl

	override fun setTargetPlotLocation(plotLocation: PlotLocation) {
		requiresTargetPlotLocationImpl.setTargetPlotLocation(plotLocation)
	}

	override fun getTargetPlotLocation(): PlotLocation {
		return requiresTargetPlotLocationImpl.getTargetPlotLocation()
	}

	override fun clearTargetPlotLocation() {
		requiresTargetPlotLocationImpl.clearTargetPlotLocation()
	}
}

class RequiresTargetPlotLocationImpl : RequiresTargetPlotLocation {

	private var target: PlotLocation? = null

	override fun setTargetPlotLocation(plotLocation: PlotLocation) {
		this.target = plotLocation
	}

	override fun getTargetPlotLocation(): PlotLocation {
		return target ?: throw CommandGenerationException("No plot location was selected")
	}

	override fun clearTargetPlotLocation() {
		target = null
	}

}

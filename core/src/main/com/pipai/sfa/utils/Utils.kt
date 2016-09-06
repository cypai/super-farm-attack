package com.pipai.sfa.utils

import com.pipai.sfa.battle.domain.PlotLocation

fun toPlotLocation(location: Int): PlotLocation {
	return when (location) {
		1 -> PlotLocation(0, 0)
		2 -> PlotLocation(1, 0)
		3 -> PlotLocation(2, 0)
		4 -> PlotLocation(0, 1)
		5 -> PlotLocation(1, 1)
		6 -> PlotLocation(2, 1)
		7 -> PlotLocation(0, 2)
		8 -> PlotLocation(1, 2)
		9 -> PlotLocation(2, 2)
		else -> throw IllegalArgumentException("$location is not within 1-9")
	}
}

fun toSimpleLocation(plotLocation: PlotLocation): Int {
	return when (plotLocation) {
		PlotLocation(0, 0) -> 1
		PlotLocation(1, 0) -> 2
		PlotLocation(2, 0) -> 3
		PlotLocation(0, 1) -> 4
		PlotLocation(1, 1) -> 5
		PlotLocation(2, 1) -> 6
		PlotLocation(0, 2) -> 7
		PlotLocation(1, 2) -> 8
		PlotLocation(2, 2) -> 9
		else -> throw IllegalArgumentException("$plotLocation is not within (0, 0) and (2, 2)")
	}
}

fun <T> shuffle(list: List<T>) : List<T>{
	return list.map{ Pair(it, RNG.nextFloat()) }.sortedBy { it.second }.map { it.first }
}

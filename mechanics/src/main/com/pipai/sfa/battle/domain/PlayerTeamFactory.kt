package com.pipai.sfa.battle.domain

class PlayerTeamFactory {
	fun generatePlayerTeam(
			name: String,
			farm: Farm,
			crew: Map<PlayerUnit, PlotLocation>,
			crops: Map<PlayerCrop, PlotLocation>)
			: PlayerTeam {

		val cropYields: MutableMap<PlayerCrop, Int> = mutableMapOf()

		for (cropEntry in crops) {
			cropYields.put(cropEntry.key, 1)
		}

		return PlayerTeam(name,
				farm.generatePlayerFarm(),
				crew.map { x -> x.key.generatePlayerUnit(x.value) },
				crops.map { x -> x.key.generatePlayerCrop(x.value) },
				cropYields)
	}
}

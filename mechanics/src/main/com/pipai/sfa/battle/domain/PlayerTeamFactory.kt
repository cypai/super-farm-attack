package com.pipai.sfa.battle.domain

class PlayerTeamFactory {
	fun generatePlayerTeam(name: String, farm: Farm, crew: List<Unit>, crops: List<Crop>): PlayerTeam {
		val cropYields: MutableMap<Crop, Int> = mutableMapOf()

		for (crop in crops) {
			cropYields.put(crop, 1)
		}

		return PlayerTeam(name,
				farm.generatePlayerFarm(),
				crew.map { x -> x.generatePlayerUnit() },
				crops.map { x -> x.generatePlayerCrop() },
				cropYields)
	}
}

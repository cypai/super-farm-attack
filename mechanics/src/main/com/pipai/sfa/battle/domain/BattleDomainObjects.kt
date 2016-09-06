package com.pipai.sfa.battle.domain

data class PlotLocation(val col: Int, val row: Int)

interface PlotObject {
	var hp: Int
	var plotLocation: PlotLocation
}

enum class CropSize(val sizeNumber: Int) {
	SMALL(1), MEDIUM(2), LARGE(3)
}

// Base version
data class CropSchema(
		val name: String,
		val hp: Int,
		val patk: Int,
		val pdef: Int,
		val yieldTime: Int,
		val size: CropSize) {

	companion object {
		private var nextId = 0
	}

	fun generateCrop(nickname: String): Crop {
		nextId += 1
		return Crop(this, if (nickname == "") name else nickname, nextId, hp, patk, pdef, yieldTime, size)
	}
}

// The crop that the player owns
data class Crop(
		val schema: CropSchema,
		val nickname: String,
		val id: Int,
		val hp: Int,
		val patk: Int,
		val pdef: Int,
		val yieldTime: Int,
		val size: CropSize) {

	fun generatePlayerCrop(plotLocation: PlotLocation): PlayerCrop {
		return PlayerCrop(this, hp, yieldTime, plotLocation)
	}
}

// The actual crop in battle
data class PlayerCrop(
		val crop: Crop,
		override var hp: Int,
		var turnsUntilYield: Int,
		override var plotLocation: PlotLocation) : PlotObject

data class FieldCrop(val crop: Crop, val shooter: PlayerUnit, val targetPlotLocation: PlotLocation)

data class UnitSchema(
		val name: String,
		val hp: Int,
		val patk: Int,
		val pdef: Int,
		val speed: Int) {

	companion object {
		private var nextId = 0
	}

	fun generateUnit(nickname: String): Unit {
		nextId += 1
		return Unit(this, if (nickname == "") name else nickname, nextId, 1, hp, patk, pdef, speed)
	}
}

data class Unit(
		val schema: UnitSchema,
		val nickname: String,
		val id: Int,
		val level: Int,
		val hp: Int,
		val patk: Int,
		val pdef: Int,
		val speed: Int) {

	fun generatePlayerUnit(plotLocation: PlotLocation): PlayerUnit {
		return PlayerUnit(this, hp, plotLocation)
	}
}

data class PlayerUnit(
		val unit: Unit,
		override var hp: Int,
		override var plotLocation: PlotLocation) : PlotObject

data class Farm(
		val name: String,
		val hp: Int) {

	fun generatePlayerFarm(): PlayerFarm {
		return PlayerFarm(this, hp)
	}
}

data class PlayerFarm(
		val farm: Farm,
		var hp: Int)

data class PlayerTeam(
		val name: String,
		val farm: PlayerFarm,
		val crew: List<PlayerUnit>,
		val crops: List<PlayerCrop>,
		val cropYields: MutableMap<Crop, Int>
)

data class Battle(
		val plotRows: Int,
		val plotColumns: Int,
		val player1: PlayerTeam,
		val player2: PlayerTeam) {

	fun getPlayer1Unit(location: PlotLocation): PlayerUnit? {
		return player1.crew.find { it.plotLocation == location }
	}

	fun getPlayer2Unit(location: PlotLocation): PlayerUnit? {
		return player2.crew.find { it.plotLocation == location }
	}

	fun getPlayer1Crop(location: PlotLocation): PlayerCrop? {
		return player1.crops.find { it.plotLocation == location }
	}

	fun getPlayer2Crop(location: PlotLocation): PlayerCrop? {
		return player2.crops.find { it.plotLocation == location }
	}

	fun getObjectsAtLocationField1(location: PlotLocation): List<PlotObject> {
		val plotLocationObjects: MutableList<PlotObject> = mutableListOf()

		plotLocationObjects.addAll(player1.crew.filter { it.plotLocation == location })
		plotLocationObjects.addAll(player1.crops.filter { it.plotLocation == location })

		return plotLocationObjects.toList()
	}

	fun getObjectsAtLocationField2(location: PlotLocation): List<PlotObject> {
		val plotLocationObjects: MutableList<PlotObject> = mutableListOf()

		plotLocationObjects.addAll(player2.crew.filter { it.plotLocation == location })
		plotLocationObjects.addAll(player2.crops.filter { it.plotLocation == location })

		return plotLocationObjects.toList()
	}

	fun getPlayerForUnit(unit: PlayerUnit): Player {
		if (player1.crew.contains(unit)) {
			return Player.PLAYER_1
		} else if (player2.crew.contains(unit)) {
			return Player.PLAYER_2
		} else {
			return Player.NONE
		}
	}

	fun getPlayerForCrop(crop: PlayerCrop): Player {
		if (player1.crops.contains(crop)) {
			return Player.PLAYER_1
		} else if (player2.crops.contains(crop)) {
			return Player.PLAYER_2
		} else {
			return Player.NONE
		}
	}

	fun getPlayerForCrop(crop: Crop): Player {
		if (player1.crops.map { it.crop }.contains(crop)) {
			return Player.PLAYER_1
		} else if (player2.crops.map { it.crop }.contains(crop)) {
			return Player.PLAYER_2
		} else {
			return Player.NONE
		}
	}

}

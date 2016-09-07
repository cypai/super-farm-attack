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

	fun generateCrop(nickname: String = ""): PlayerCrop {
		nextId += 1
		return PlayerCrop(this, if (nickname == "") name else nickname, nextId, hp, patk, pdef, yieldTime, size)
	}
}

// The crop that the player owns
data class PlayerCrop(
		val schema: CropSchema,
		val nickname: String,
		val id: Int,
		val hp: Int,
		val patk: Int,
		val pdef: Int,
		val yieldTime: Int,
		val size: CropSize) {

	fun generatePlayerCrop(plotLocation: PlotLocation): FieldCrop {
		return FieldCrop(this, hp, yieldTime, plotLocation)
	}
}

// The actual crop in battle
data class FieldCrop(
		val crop: PlayerCrop,
		override var hp: Int,
		var turnsUntilYield: Int,
		override var plotLocation: PlotLocation) : PlotObject

data class AmmoCrop(val crop: PlayerCrop, val shooter: FieldUnit, val targetPlotLocation: PlotLocation)

data class UnitSchema(
		val name: String,
		val hp: Int,
		val patk: Int,
		val pdef: Int,
		val speed: Int) {

	companion object {
		private var nextId = 0
	}

	fun generateUnit(nickname: String = ""): PlayerUnit {
		nextId += 1
		return PlayerUnit(this, if (nickname == "") name else nickname, nextId, 1, hp, patk, pdef, speed)
	}
}

data class PlayerUnit(
		val schema: UnitSchema,
		val nickname: String,
		val id: Int,
		val level: Int,
		val hp: Int,
		val patk: Int,
		val pdef: Int,
		val speed: Int) {

	fun generatePlayerUnit(plotLocation: PlotLocation): FieldUnit {
		return FieldUnit(this, hp, plotLocation)
	}
}

data class FieldUnit(
		val unit: PlayerUnit,
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
		val crew: List<FieldUnit>,
		val crops: List<FieldCrop>,
		val cropYields: MutableMap<PlayerCrop, Int>
)

data class Battle(
		val plotRows: Int,
		val plotColumns: Int,
		val player1: PlayerTeam,
		val player2: PlayerTeam) {

	fun getPlayer1Unit(location: PlotLocation): FieldUnit? {
		return player1.crew.find { it.plotLocation == location }
	}

	fun getPlayer2Unit(location: PlotLocation): FieldUnit? {
		return player2.crew.find { it.plotLocation == location }
	}

	fun getPlayer1Crop(location: PlotLocation): FieldCrop? {
		return player1.crops.find { it.plotLocation == location }
	}

	fun getPlayer2Crop(location: PlotLocation): FieldCrop? {
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

	fun getPlayerForUnit(unit: FieldUnit): Player {
		if (player1.crew.contains(unit)) {
			return Player.PLAYER_1
		} else if (player2.crew.contains(unit)) {
			return Player.PLAYER_2
		} else {
			return Player.NONE
		}
	}

	fun getPlayerForCrop(crop: FieldCrop): Player {
		if (player1.crops.contains(crop)) {
			return Player.PLAYER_1
		} else if (player2.crops.contains(crop)) {
			return Player.PLAYER_2
		} else {
			return Player.NONE
		}
	}

	fun getPlayerForCrop(crop: PlayerCrop): Player {
		if (player1.crops.map { it.crop }.contains(crop)) {
			return Player.PLAYER_1
		} else if (player2.crops.map { it.crop }.contains(crop)) {
			return Player.PLAYER_2
		} else {
			return Player.NONE
		}
	}

}

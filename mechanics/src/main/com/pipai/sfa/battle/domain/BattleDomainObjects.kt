package com.pipai.sfa.battle.domain

// Base version
data class CropSchema(
		val name: String,
		val hp: Int,
		val patk: Int,
		val pdef: Int,
		val yieldTime: Int,
		val mass: Int) {

	companion object {
		private var nextId = 0
	}

	fun generateCrop(nickname: String): Crop {
		nextId += 1
		return Crop(this, if (nickname == "") name else nickname, nextId, hp, patk, pdef, yieldTime, mass)
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
		val mass: Int) {

	fun generatePlayerCrop(): PlayerCrop {
		return PlayerCrop(this, hp, yieldTime)
	}
}

// The actual crop in battle
data class PlayerCrop(
		val crop: Crop,
		var hp: Int,
		var turnsUntilYield: Int)

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

	fun generatePlayerUnit(): PlayerUnit {
		return PlayerUnit(this, hp)
	}
}

data class PlayerUnit(
		val unit: Unit,
		var hp: Int)

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
		val player1: PlayerTeam,
		val player2: PlayerTeam) {

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

}

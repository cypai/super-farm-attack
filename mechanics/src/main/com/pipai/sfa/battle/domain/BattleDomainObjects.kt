package com.pipai.sfa.battle.domain

// Base version
data class CropSchema(
		val name: String,
		val hp: Int,
		val patk: Int,
		val pdef: Int,
		val yieldTime: Int,
		val mass: Int)

// The crop that the player owns
data class Crop(
		val schema: CropSchema,
		val nickname: String,
		val hp: Int,
		val patk: Int,
		val pdef: Int,
		val yieldTime: Int,
		val mass: Int)

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
		val speed: Int)

data class Unit(
		val schema: UnitSchema,
		val nickname: String,
		val level: Int,
		val hp: Int,
		val patk: Int,
		val pdef: Int,
		val speed: Int)

data class PlayerUnit(
		val unit: Unit,
		var hp: Int)

data class Farm(
		val name: String,
		val hp: Int)

data class PlayerFarm(
		val farm: Farm,
		var hp: Int)

data class PlayerTeam(
		val farm: PlayerFarm,
		val crew: List<PlayerUnit>,
		val crops: List<PlayerCrop>,
		val cropYields: MutableMap<Crop, Int>
)

data class Battle(
		val player1: PlayerTeam,
		val player2: PlayerTeam)
package com.pipai.sfa.battle.components

data class CropSchema(
		val name: String,
		val patk: Int,
		val growth: Int,
		val mass: Int)

data class Crop(
		val schema: CropSchema,
		val name: String,
		val patk: Int,
		val growth: Int,
		val mass: Int)

data class UnitSchema(
		val name: String,
		val hp: Int,
		val patk: Int,
		val pdef: Int,
		val speed: Int)

data class Unit(
		val schema: UnitSchema,
		val name: String,
		val hp: Int,
		val patk: Int,
		val pdef: Int,
		val speed: Int)

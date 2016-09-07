package com.pipai.sfa.battle.controller

import com.pipai.sfa.battle.domain.AmmoCrop
import com.pipai.sfa.battle.domain.Battle
import com.pipai.sfa.battle.domain.Player

data class AmmoCropColumns(private val columns: Int) {

	private val ammoCrops1: List<MutableList<AmmoCrop>>
	private val ammoCrops2: List<MutableList<AmmoCrop>>

	init {
		ammoCrops1 = generateFieldList(columns)
		ammoCrops2 = generateFieldList(columns)
	}

	private fun generateFieldList(columns: Int): List<MutableList<AmmoCrop>> {
		val tempCropsOnField: MutableList<MutableList<AmmoCrop>> = mutableListOf()
		for (i in 0..columns) {
			tempCropsOnField.add(mutableListOf())
		}
		return tempCropsOnField.toList()
	}

	fun getAmmoForPlayer1(): List<List<AmmoCrop>> {
		return ammoCrops1.map { it.toList() }
	}

	fun getAmmoForPlayer2(): List<List<AmmoCrop>> {
		return ammoCrops2.map { it.toList() }
	}

	fun getAmmoColumnForPlayer1(column: Int): List<AmmoCrop> {
		return ammoCrops1.get(column).toList()
	}

	fun getAmmoColumnForPlayer2(column: Int): List<AmmoCrop> {
		return ammoCrops2.get(column).toList()
	}

	fun addAmmoForPlayer1(ammoCrop: AmmoCrop, column: Int) {
		addCropToField(ammoCrop, column, Player.PLAYER_1)
	}

	fun addAmmoForPlayer2(ammoCrop: AmmoCrop, column: Int) {
		addCropToField(ammoCrop, column, Player.PLAYER_2)
	}

	private fun addCropToField(ammoCrop: AmmoCrop, column: Int, player: Player) {
		val sideToAddTo = when (player) {
			Player.PLAYER_1-> ammoCrops1
			Player.PLAYER_2 -> ammoCrops2
			Player.NONE -> throw IllegalArgumentException("$player is not a valid argument")
		}
		if (column < 0) {
			throw IllegalArgumentException("Column $column cannot be < 0")
		}
		if (column >= ammoCrops1.size) {
			throw IllegalArgumentException("Column $column specified a column not within dimensions ${sideToAddTo.size}")
		}
		sideToAddTo.get(column).add(ammoCrop)
	}

}

data class BattleTurnData(val battle: Battle, val ammoColumns: AmmoCropColumns) {
	constructor(battle: Battle) : this(battle, AmmoCropColumns(battle.plotColumns))
}

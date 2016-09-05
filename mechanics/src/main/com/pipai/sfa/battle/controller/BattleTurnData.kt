package com.pipai.sfa.battle.controller

import com.pipai.sfa.battle.domain.Battle
import com.pipai.sfa.battle.domain.FieldCrop
import com.pipai.sfa.battle.domain.PlayerUnit

data class FieldCrops(private val columns: Int) {

	private val cropsOnField1: List<MutableList<FieldCrop>>
	private val cropsOnField2: List<MutableList<FieldCrop>>

	init {
		cropsOnField1 = generateFieldList(columns)
		cropsOnField2 = generateFieldList(columns)
	}

	private fun generateFieldList(columns: Int): List<MutableList<FieldCrop>> {
		val tempCropsOnField: MutableList<MutableList<FieldCrop>> = mutableListOf()
		for (i in 0..columns) {
			tempCropsOnField.add(mutableListOf())
		}
		return tempCropsOnField.toList()
	}

	fun getField1(): List<List<FieldCrop>> {
		return cropsOnField1.map { it.toList() }
	}

	fun getField2(): List<List<FieldCrop>> {
		return cropsOnField2.map { it.toList() }
	}

	fun getField1Column(column: Int): List<FieldCrop> {
		return cropsOnField1.get(column).toList()
	}

	fun getField2Column(column: Int): List<FieldCrop> {
		return cropsOnField2.get(column).toList()
	}

	fun addCropToField1(fieldCropInformation: FieldCrop, column: Int) {
		addCropToField(fieldCropInformation, column, Field.FIELD_1)
	}

	fun addCropToField2(fieldCropInformation: FieldCrop, column: Int) {
		addCropToField(fieldCropInformation, column, Field.FIELD_2)
	}

	private fun addCropToField(fieldCropInformation: FieldCrop, column: Int, field: Field) {
		val fieldToAddTo = when (field) {
			Field.FIELD_1 -> cropsOnField1
			Field.FIELD_2 -> cropsOnField2
		}
		if (column < 0) {
			throw IllegalArgumentException("Column $column cannot be < 0")
		}
		if (column >= cropsOnField1.size) {
			throw IllegalArgumentException("Column $column specified a column not within dimensions ${fieldToAddTo.size}")
		}
		fieldToAddTo.get(column).add(fieldCropInformation)
	}

	private enum class Field { FIELD_1, FIELD_2 }

}

data class BattleTurnData(val battle: Battle, val fieldCrops: FieldCrops) {
	constructor(battle: Battle) : this(battle, FieldCrops(battle.plotColumns))
}

package com.pipai.sfa.gui

import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.g3d.ModelBatch
import com.badlogic.gdx.graphics.g3d.decals.DecalBatch
import com.badlogic.gdx.graphics.glutils.ShapeRenderer

data class BatchHelper(val spr: SpriteBatch, val shape: ShapeRenderer, val model: ModelBatch, val font: BitmapFont)

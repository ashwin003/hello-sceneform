package com.thyagash.hellosceneform

import android.view.View
import com.google.ar.core.Anchor
import com.google.ar.sceneform.rendering.ModelRenderable

open class Model(val name: String, val view: View, val model: Int, var renderable: ModelRenderable? = null, var isSelected: Boolean = false) {
    override fun toString(): String {
        return "Failed to load model for $name"
    }
}
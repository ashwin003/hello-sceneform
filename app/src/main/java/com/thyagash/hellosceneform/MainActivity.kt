package com.thyagash.hellosceneform

import android.graphics.Color
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.google.ar.sceneform.AnchorNode
import com.google.ar.sceneform.rendering.ModelRenderable
import com.google.ar.sceneform.ux.ArFragment
import com.google.ar.sceneform.ux.TransformableNode
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), View.OnClickListener {

    // List of models to render
    lateinit var modelsToRender: List<Model>

    // Currently selected model
    lateinit var selected: Model

    lateinit var arFragment: ArFragment

    override fun onClick(v: View?) {
        selected = modelsToRender.first{ it.view == v }
        setSelectedBackground()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        modelsToRender = prepareModelsToRender()
        selected = modelsToRender[0]

        setupClickListeners()

        setupRenderables()

        arFragment = supportFragmentManager.findFragmentById(R.id.sceneform_fragment) as ArFragment
        
        arFragment.setOnTapArPlaneListener { hitResult, plane, motionEvent ->
            val anchor = hitResult.createAnchor()
            val anchorNode = AnchorNode(anchor)
            anchorNode.setParent(arFragment.arSceneView.scene)

            renderModel(anchorNode, selected)
        }
    }

    private fun setSelectedBackground() {
        for(i in modelsToRender.indices) {
            val color = if (modelsToRender[i] == selected)
                Color.parseColor("#80333639") else Color.TRANSPARENT
            modelsToRender[i].view.setBackgroundColor(color)
        }
    }

    private fun renderModel(anchorNode: AnchorNode, selected: Model) {
        val modelToRender = TransformableNode(arFragment.transformationSystem)
        modelToRender.setParent(anchorNode)
        modelToRender.renderable = selected.renderable
        modelToRender.select()
    }

    private fun setupRenderables() {
        for(i in modelsToRender.indices) {
            ModelRenderable.builder()
                .setSource(this, modelsToRender[i].model)
                .build()
                .thenAccept { renderable -> modelsToRender[i].renderable = renderable }
                .exceptionally { throwable ->
                    Toast.makeText(this@MainActivity, modelsToRender[i].toString() , Toast.LENGTH_SHORT).show()
                    null
                }
        }
    }

    private fun setupClickListeners() {
        for(i in modelsToRender.indices) {
            modelsToRender[i].view.setOnClickListener(this)
        }
    }

    private fun prepareModelsToRender(): List<Model> {
        return listOf(
            Model("Andy",andy, R.raw.andy),
            Model("Bear",bear, R.raw.bear),
            Model("Cat", cat, R.raw.cat),
            Model("Cow", cow, R.raw.cow),
            Model("Dog", dog, R.raw.dog),
            Model("Elephant", elephant, R.raw.elephant),
            Model("Ferret", ferret, R.raw.ferret),
            Model("Hippopotamus", hippopotamus, R.raw.hippopotamus),
            Model("Horse", horse, R.raw.horse),
            Model("Koala Bear", koala_bear, R.raw.koala_bear),
            Model("Lion", lion, R.raw.lion),
            Model("Reindeer", reindeer, R.raw.reindeer),
            Model("Wolverine", wolverine, R.raw.wolverine)
        )
    }
}

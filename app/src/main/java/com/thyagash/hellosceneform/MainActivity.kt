package com.thyagash.hellosceneform

import android.content.Context
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import android.widget.ListView
import android.widget.Toast
import com.google.ar.sceneform.AnchorNode
import com.google.ar.sceneform.rendering.ModelRenderable
import com.google.ar.sceneform.ux.ArFragment
import com.google.ar.sceneform.ux.TransformableNode
import com.afollestad.materialdialogs.LayoutMode
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.bottomsheets.BottomSheet
import com.afollestad.materialdialogs.customview.customView

class MainActivity : AppCompatActivity() {

    // List of models to render
    lateinit var modelsToRender: List<Model>
    lateinit var listView: ListView
    lateinit var context: Context
    private lateinit var adapter: ModelAdapter
    // Currently selected model
    var selected: Model? = null

    private lateinit var arFragment: ArFragment

    private lateinit var materialDialog: MaterialDialog

    private lateinit var pullUpButton: ImageButton


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        context = this
        setContentView(R.layout.activity_main)

        modelsToRender = prepareModelsToRender()
        selected = null


        modelsToRender = prepareModelsToRender()
        selected = modelsToRender[0]

        setupRenderables()

        arFragment = supportFragmentManager.findFragmentById(R.id.sceneform_fragment) as ArFragment

        arFragment.setOnTapArPlaneListener { hitResult, plane, motionEvent ->
            val anchor = hitResult.createAnchor()
            val anchorNode = AnchorNode(anchor)
            anchorNode.setParent(arFragment.arSceneView.scene)

            renderModel(anchorNode, selected)
        }


        materialDialog = MaterialDialog(this, BottomSheet(LayoutMode.WRAP_CONTENT))
        materialDialog.customView(R.layout.bottom_sheet)

        pullUpButton = findViewById<ImageButton>(R.id.btn_bottom_sheet)

        pullUpButton.setOnClickListener{
            materialDialog.show {
                listView = findViewById(R.id.modelList)
                adapter = ModelAdapter(context, ArrayList(modelsToRender))
                listView.adapter = adapter

                listView.setOnItemClickListener { parent, view, position, id ->
                    selected = modelsToRender.get(position)
                    selected!!.view = view
                    setSelectedBackground()
                }
            }
        }
    }

    private fun prepareModelsToRender(): List<Model> {
        return listOf(
            Model("Andy",R.raw.andy, R.drawable.andy),
            Model("Bear",R.raw.bear, R.drawable.bear),
            Model("Cat", R.raw.cat, R.drawable.cat),
            Model("Cow", R.raw.cow, R.drawable.cow),
            Model("Dog", R.raw.dog, R.drawable.dog),
            Model("Dragon", R.raw.dragon, R.drawable.dragon),
            Model("Elephant", R.raw.elephant, R.drawable.elephant),
            Model("Ferret", R.raw.ferret, R.drawable.ferret),
            Model("Hippopotamus", R.raw.hippopotamus, R.drawable.hippopotamus),
            Model("Horse", R.raw.horse, R.drawable.horse),
            Model("Koala Bear", R.raw.koala_bear, R.drawable.koala_bear),
            Model("Lion", R.raw.lion, R.drawable.lion),
            Model("Reindeer", R.raw.reindeer, R.drawable.reindeer),
            Model("Wolverine", R.raw.wolverine, R.drawable.wolverine)
        )
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

    private fun renderModel(anchorNode: AnchorNode, selected: Model?) {
        val modelToRender = TransformableNode(arFragment.transformationSystem)
        modelToRender.setParent(anchorNode)
        modelToRender.renderable = selected?.renderable
        modelToRender.select()
    }

    private fun setSelectedBackground() {
        for(i in modelsToRender.indices) {
            val color = if (modelsToRender[i] == selected)
                Color.parseColor("#80333639") else Color.TRANSPARENT
            if(modelsToRender[i].view != null)
                modelsToRender[i].view!!.setBackgroundColor(color)
        }
    }
}

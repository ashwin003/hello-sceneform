package com.thyagash.hellosceneform

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.*
import com.google.ar.sceneform.AnchorNode
import com.google.ar.sceneform.rendering.ModelRenderable
import com.google.ar.sceneform.ux.ArFragment
import com.google.ar.sceneform.ux.TransformableNode
import com.afollestad.materialdialogs.LayoutMode
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.bottomsheets.BottomSheet
import com.afollestad.materialdialogs.customview.customView
import java.lang.Exception

class MainActivity : AppCompatActivity() {

    private lateinit var modelsToRender: List<Model>
    private lateinit var listView: GridView
    private lateinit var adapter: ModelAdapter
    private lateinit var selected: Model

    private lateinit var arFragment: ArFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val context: Context = this
        setContentView(R.layout.activity_main)

        modelsToRender = prepareModelsToRender()

        modelsToRender = prepareModelsToRender()
        selected = modelsToRender[0]

        setupRenderables()

        arFragment = supportFragmentManager.findFragmentById(R.id.sceneform_fragment) as ArFragment

        arFragment.setOnTapArPlaneListener { hitResult, plane, motionEvent ->
            val anchor = hitResult.createAnchor()
            val anchorNode = AnchorNode(anchor)
            anchorNode.setParent(arFragment.arSceneView.scene)

            renderSelectedModel(anchorNode)
        }

        val selectedIconImage = findViewById<ImageView>(R.id.selected_model_image)

        val materialDialog = MaterialDialog(this, BottomSheet(LayoutMode.WRAP_CONTENT))
        materialDialog.customView(R.layout.bottom_sheet)

        val pullUpButton = findViewById<ImageButton>(R.id.btn_bottom_sheet)

        pullUpButton.setOnClickListener{
            materialDialog.show {
                listView = findViewById(R.id.modelList)
                adapter = ModelAdapter(context, ArrayList(modelsToRender))
                listView.adapter = adapter
                if(selected != null) {
                    adapter.setSelectedModel(selected)
                }

                listView.setOnItemClickListener { _, view, position, _ ->
                    selected = modelsToRender[position]
                    selected.view = view
                    selectedIconImage.setImageResource(selected.icon)
                }
            }
        }
    }

    private fun prepareModelsToRender(): List<Model> {
        return listOf(
            Model("Acnologia",R.raw.acnologia, R.drawable.acnologia),
            Model("Andy",R.raw.andy, R.drawable.andy),
            Model("Bear",R.raw.bear, R.drawable.bear),
            Model("Cat", R.raw.cat, R.drawable.cat),
            Model("Cow", R.raw.cow, R.drawable.cow),
            Model("Dog", R.raw.dog, R.drawable.dog),
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
                .exceptionally { e ->
                    Toast.makeText(this@MainActivity, modelsToRender[i].toString() , Toast.LENGTH_SHORT).show()
                    null
                }
        }
    }

    private fun renderSelectedModel(anchorNode: AnchorNode) {
       try {
           val modelToRender = TransformableNode(arFragment.transformationSystem)
           modelToRender.setParent(anchorNode)
           modelToRender.renderable = selected.renderable
           modelToRender.select()
       }
       catch (_:Exception) {
           Toast.makeText(this@MainActivity, selected.toString() , Toast.LENGTH_SHORT).show()
       }
    }
}

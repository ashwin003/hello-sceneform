package com.thyagash.hellosceneform

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
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

    private lateinit var categories: List<Category>
    private lateinit var selectedCategory: Category
    private lateinit var categoryAdapter: CategoryAdapter
    private lateinit var spinner: Spinner

    private lateinit var arFragment: ArFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val context: Context = this
        setContentView(R.layout.activity_main)

        modelsToRender = prepareModelsToRender()

        modelsToRender = prepareModelsToRender()
        selected = modelsToRender[0]

        categories = prepareCategories()
        selectedCategory = categories[0]

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

                spinner = findViewById(R.id.spinner)
                categoryAdapter = CategoryAdapter(context, ArrayList(categories))
                spinner.adapter = categoryAdapter

                spinner.onItemSelectedListener = object: AdapterView.OnItemSelectedListener {
                    override fun onNothingSelected(parent: AdapterView<*>?) {

                    }

                    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                        selectedCategory = categories[position]

                        val modelsList = getFilteredModels()
                        adapter = ModelAdapter(context, ArrayList(modelsList))
                        listView.adapter = adapter
                    }
                }

                listView = findViewById(R.id.modelList)

                val modelsList = getFilteredModels()
                adapter = ModelAdapter(context, ArrayList(modelsList))
                listView.adapter = adapter
                if(selected != null) {
                    adapter.setSelectedModel(selected)
                }

                listView.setOnItemClickListener { _, view, position, _ ->
                    selected = adapter.getItem(position)!!
                    selected.view = view
                    selectedIconImage.setImageResource(selected.icon)
                }
            }
        }
    }

    private fun prepareModelsToRender(): List<Model> {
        return listOf(
            Model("Acnologia",R.raw.acnologia, R.drawable.acnologia, 4),
            Model("Andy",R.raw.andy, R.drawable.andy, 4),
            Model("Bear",R.raw.bear, R.drawable.bear, 2),
            Model("Cat", R.raw.cat, R.drawable.cat, 1),
            Model("Cow", R.raw.cow, R.drawable.cow, 1),
            Model("Dog", R.raw.dog, R.drawable.dog, 1),
            Model("Elephant", R.raw.elephant, R.drawable.elephant, 1),
            Model("Ferret", R.raw.ferret, R.drawable.ferret, 1),
            Model("Flareon", R.raw.flareon, R.drawable.flareon, 2),
            Model("Freddy Fazbear", R.raw.freddy_fazbear, R.drawable.freddy_fazbear, 3),
            Model("Golden Freddy", R.raw.golden_freddy, R.drawable.golden_freddy, 3),
            Model("Hippopotamus", R.raw.hippopotamus, R.drawable.hippopotamus, 1),
            Model("Horse", R.raw.horse, R.drawable.horse, 1),
            Model("Koala Bear", R.raw.koala_bear, R.drawable.koala_bear, 1),
            Model("Lion", R.raw.lion, R.drawable.lion, 1),
            Model("Reindeer", R.raw.reindeer, R.drawable.reindeer, 1),
            Model("Squirtle", R.raw.squirtle, R.drawable.squirtle, 2),
            Model("Wolverine", R.raw.wolverine, R.drawable.wolverine, 1),
            Model("Zubat", R.raw.zubat, R.drawable.zubat, 2)
        )
    }

    private fun getFilteredModels(): List<Model> {
        return if(selectedCategory.id == 0) {
            modelsToRender
        } else {
            modelsToRender.filter { it.category == selectedCategory.id }
        }
    }

    private fun prepareCategories(): List<Category> {
        return listOf(
            Category(0, "All"),
            Category(1, "Animals"),
            Category(2, "Pokemon"),
            Category(3, "Fazbear"),
            Category(4, "Misc")
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

package com.example.cookingapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.animation.AnimationUtils
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.HorizontalScrollView
import android.widget.ImageButton
import android.widget.ListView
import android.widget.TextClock
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.blogspot.atifsoftwares.animatoolib.Animatoo
import com.example.cookingapp.Adapters.AppliancesAdapter
import com.example.cookingapp.Listeners.RecipeDetailsListener
import com.example.cookingapp.Models.RecipeDetailsResponse
import org.w3c.dom.Text

class OverviewActivity : AppCompatActivity() {
    var id: Int = 0
    lateinit var recipeTitle : TextView
    lateinit var author : TextView
    lateinit var tvTime : TextView
    lateinit var tvServings : TextView
    lateinit var tvIngredientCount : TextView
    lateinit var recycler_recipe_ingredients : RecyclerView
   // lateinit var recycler_recipe_appliances: RecyclerView
    lateinit var manager : RequestManager
    //lateinit var appliancesAdapter: AppliancesAdapter

    private lateinit var ingredientListView : ListView
    val ingredients: MutableList<String> = ArrayList()


    private var arrayAdapter: ArrayAdapter<String> ?=null
    private lateinit var btnOverview : ImageButton
    private lateinit var btnOverviewBack : ImageButton
    private lateinit var btnHeart : ImageButton
    private lateinit var btnIngredientsPlus : ImageButton
    private lateinit var btnIngredientsMinus : ImageButton
    private lateinit var servings : TextView
    private lateinit var time : TextView
    private lateinit var numIng : TextView
    private var servingsCounter : Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.Light)
        setContentView(R.layout.activity_overview)
        findViews()

        id = getIntent().getStringExtra("id")?.toIntOrNull() ?: 0
        manager = RequestManager(this)
        manager.getRecipeDetails(recipeDetailsListener, id)

        btnOverview = findViewById(R.id.btnOverview)
        btnOverviewBack = findViewById(R.id.btnOverviewBack)
        btnHeart = findViewById(R.id.btnHeart)
        btnIngredientsPlus = findViewById(R.id.btnIngredientsPlus)
        btnIngredientsMinus = findViewById(R.id.btnIngredientsMinus)

        //if api response contains appliances, edit text like this
        /*appliance1.text = "Oven"
        appliance2.text = "Rice cooker"*/


        btnOverview.setOnClickListener{
            val intent = Intent(this,PrepActivity::class.java)
            //this is the current activity, and PrepActivity is the one we want to navigate to
            startActivity(intent)
            Animatoo.animateSlideLeft(this)
        }

        btnOverviewBack.setOnClickListener{
            finish()
            Animatoo.animateSlideRight(this)
        }

        btnHeart.setOnClickListener{
            btnHeart.startAnimation(AnimationUtils.loadAnimation(this, R.anim.anim_heart));
            Toast.makeText(this, "Recipe saved to Favorites!", Toast.LENGTH_LONG).show()
            //do something when recipe is favorited
        }

        //increment servings
        btnIngredientsPlus.setOnClickListener{
            servingsCounter += 1
            servings.text= servingsCounter.toString()
        }
        btnIngredientsMinus.setOnClickListener{
            if (servingsCounter > 0) {
                servingsCounter -= 1
                servings.text = servingsCounter.toString()
            }
        }
    }

    private fun findViews() {
        recipeTitle = findViewById(R.id.recipeTitle)
        author = findViewById(R.id.author)
        tvTime = findViewById(R.id.tvTime)
        tvServings = findViewById(R.id.tvServings)
        tvIngredientCount = findViewById(R.id.tvIngredientCount)
        ingredientListView = findViewById(R.id.lvingredients)
        //recycler_recipe_ingredients = findViewById(R.id.recycler_recipe_ingredients)
        //recycler_recipe_appliances = findViewById(R.id.recycler_recipe_appliances)
    }

    private val recipeDetailsListener = object : RecipeDetailsListener {
        override fun didFetch(response: RecipeDetailsResponse?, message: String?) {
            if (response != null) {
                recipeTitle.text = response.title
                author.text = response.sourceName
                tvTime.text = response.readyInMinutes.toString()
                tvServings.text = response.servings.toString()
                tvIngredientCount.text = response.extendedIngredients.size.toString()

                ingredients.clear()
                for (i in 0 until response.extendedIngredients.size) {
                    ingredients.add(response.extendedIngredients[i].name)
                }

                arrayAdapter = ArrayAdapter(this@OverviewActivity, R.layout.ingredient_item,R.id.tvIngredient ,ingredients)
                ingredientListView.adapter = arrayAdapter
                //recycler_recipe_appliances.setHasFixedSize(true)
                //recycler_recipe_appliances.layoutManager = LinearLayoutManager(this@OverviewActivity, LinearLayoutManager.HORIZONTAL, false)
                //appliancesAdapter = AppliancesAdapter(this@OverviewActivity, response.extendedIngredients)
                //recycler_recipe_appliances.adapter = appliancesAdapter

            }
            else {
                recipeTitle.text = ""
                author.text = ""
                tvTime.text = ""
                tvServings.text = ""
                tvIngredientCount.text = ""
            }
        }

        override fun didError(message: String?) {
            Toast.makeText(this@OverviewActivity, message, Toast.LENGTH_SHORT).show()
        }
    }

}
package com.example.foodapp.fragments


import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.foodapp.activity.MealAktivity
import com.example.foodapp.adapters.CategoriesAdapter
import com.example.foodapp.adapters.MostPopularAdapter
import com.example.foodapp.databinding.FragmentHomeBinding
import com.example.foodapp.pojo.MealsByCategory
import com.example.foodapp.pojo.Meal
import com.example.foodapp.viewModel.HomeVM

class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding
    private lateinit var homeMvvm: HomeVM
    private lateinit var randomMeal:Meal
    private lateinit var popularItemsAdapter:MostPopularAdapter
    private lateinit var categoriesAdapter: CategoriesAdapter

    companion object {

     const val MEAL_ID = "com.example.foodapp.fragments.idMeal"
     const val MEAL_NAME = "com.example.foodapp.fragments.nameMeal"
     const val MEAL_THUMB = "com.example.foodapp.fragments.thumbMeal"
 }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        homeMvvm = ViewModelProvider(this).get(HomeVM::class.java)

        popularItemsAdapter =  MostPopularAdapter()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        preparePopularItemsRCV()

        homeMvvm.getRandomMeal()
        observeRandomMeal()
        onRandomMealClick()

        homeMvvm.getPopularItems()
        observePopularItemsLiveData()
        onPopularItemClick()


        prepareCategoriesRCV()
        homeMvvm.getCategories()
        observeCategoriesLiveData()




    }

    private fun prepareCategoriesRCV() {
        categoriesAdapter = CategoriesAdapter()
        binding.rcCategories.apply {
            layoutManager =  GridLayoutManager(context,3,GridLayoutManager.VERTICAL,false)
            adapter = categoriesAdapter
        }
    }


    private fun observeCategoriesLiveData() {
        homeMvvm.observeCategoriesLiveData().observe(viewLifecycleOwner, Observer { categories->

            categories.forEach {  category ->
                categoriesAdapter.setCategoryList(categories)


            }
        })
    }

    private fun onPopularItemClick() {
        popularItemsAdapter.onItemClick = {
            meal ->
            val intent = Intent(activity,MealAktivity::class.java)
            intent.putExtra(MEAL_ID,meal.idMeal)
            intent.putExtra(MEAL_NAME,meal.strMeal)
            intent.putExtra(MEAL_THUMB,meal.strMealThumb)
            startActivity(intent)

        }
    }

    private fun preparePopularItemsRCV() {
        binding.rcViewPopular.apply {
            layoutManager = LinearLayoutManager(activity,LinearLayoutManager.HORIZONTAL,false)
adapter = popularItemsAdapter
        }
    }

    private fun observePopularItemsLiveData() {
        homeMvvm.observePopularItemsLiveData().observe(viewLifecycleOwner,
         { mealList ->
popularItemsAdapter.setMeals(mealsList = mealList as ArrayList<MealsByCategory>)

        })
    }

    private fun  onRandomMealClick(){
        binding.randomMealcardview.setOnClickListener {
            val intent = Intent(activity,MealAktivity::class.java)
            intent.putExtra(MEAL_ID,randomMeal.idMeal)
            intent.putExtra(MEAL_NAME,randomMeal.strMeal)
            intent.putExtra(MEAL_THUMB,randomMeal.strMealThumb)
            startActivity(intent)
        }
    }

    private fun observeRandomMeal() {
        homeMvvm.observeRandomMealLiveData().observe(viewLifecycleOwner)
        { meal ->

                Glide.with(this@HomeFragment)
                    .load(meal.strMealThumb)
                    .into(binding.imageRandomMeal)
                this.randomMeal = meal
            }
        }
    }




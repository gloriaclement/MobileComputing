package com.gclem19.smartpantry.network

//import androidx.room.Query
import retrofit2.http.GET
import retrofit2.http.Query

data class MealResponse(
    val meals: List<Meal>?
)

data class Meal(
    val idMeal: String,
    val strMeal: String,
    val strInstructions: String,
    val strMealThumb: String
)

interface TheMealDBApi {
    @GET("filter.php")
    suspend fun getMealsByIngredient(
        @Query("i") ingredient: String
    ): MealResponse
}

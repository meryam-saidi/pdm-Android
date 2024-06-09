package tn.esprit.pdm.network

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import tn.esprit.pdm.network.model.LoginRequest
import tn.esprit.pdm.network.model.UserResponse
import tn.esprit.pdm.network.model.Post
import tn.esprit.pdm.network.model.Collection

interface UserApiService {
    @POST("auth")
    fun loginUser(@Body loginRequest: LoginRequest): Call<UserResponse>

    @GET("surplusFood")
    fun getPosts(): Call<List<Post>>

    @POST("collection")
    fun createCollection(@Body collection: Collection): Call<Collection>

    @GET("collection")
    fun getCollections(): Call<List<Collection>>

    @PUT("collection/{id}")
    fun updateCollection(@Path("id") id: String, @Body collection: Collection): Call<Collection>


}
package tn.esprit.pdm.network

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import tn.esprit.pdm.network.model.LoginRequest
import tn.esprit.pdm.network.model.UserResponse
import tn.esprit.pdm.viewModel.Post
import tn.esprit.pdm.viewModel.Collection

interface UserApiService {
    @POST("auth")
    fun loginUser(@Body loginRequest: LoginRequest): Call<UserResponse>

    @GET("surplusFood")
    fun getPosts(): Call<List<Post>>

    @POST("collection")
    fun createCollection(@Body collection: Collection): Call<Collection>

}
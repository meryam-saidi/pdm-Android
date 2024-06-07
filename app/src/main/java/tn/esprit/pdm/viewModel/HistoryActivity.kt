package tn.esprit.pdm.viewModel

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import retrofit2.Call
import retrofit2.Response
import tn.esprit.pdm.R
import tn.esprit.pdm.network.RetrofitClient
import tn.esprit.pdm.network.UserApiService

class HistoryActivity : AppCompatActivity() {
    private lateinit var apiService: UserApiService
    private lateinit var recyclerViewCollection: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_history)

        apiService = RetrofitClient.getClient("http://10.0.2.2:7002/").create(UserApiService::class.java)
        recyclerViewCollection = findViewById(R.id.recyclerHistory)
        fetchCollections()

    }

    private fun fetchCollections() {
        apiService.getCollections().enqueue(object : retrofit2.Callback<List<Collection>> {
            override fun onResponse(call: Call<List<Collection>>, response: Response<List<Collection>>) {
                if (response.isSuccessful) {
                    response.body()?.let {
                        updateRecyclerView(it)
                    }
                } else {
                    Toast.makeText(this@HistoryActivity, "Error: ${response.message()}", Toast.LENGTH_LONG).show()
                }
            }

            override fun onFailure(call: Call<List<Collection>>, t: Throwable) {
                Toast.makeText(this@HistoryActivity, "Failure: ${t.message}", Toast.LENGTH_LONG).show()
            }
        })
    }

    private fun updateRecyclerView(collections: List<Collection>) {
        recyclerViewCollection.apply {
            layoutManager = LinearLayoutManager(this@HistoryActivity)
            adapter = CollectionAdapter(collections)
        }
    }
}

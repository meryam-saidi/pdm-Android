package tn.esprit.pdm.viewModel

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import tn.esprit.pdm.R
import tn.esprit.pdm.adapter.CollectionAdapter
import tn.esprit.pdm.network.RetrofitClient
import tn.esprit.pdm.network.UserApiService
import tn.esprit.pdm.network.model.Collection

class HistoryActivity : AppCompatActivity(), CollectionAdapter.ItemInteractionListener {
    private lateinit var apiService: UserApiService
    private lateinit var recyclerViewCollection: RecyclerView
    private lateinit var adapterCollection: CollectionAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_history)

        apiService = RetrofitClient.getClient("http://10.0.2.2:7002/").create(UserApiService::class.java)
        recyclerViewCollection = findViewById(R.id.recyclerHistory)

        fetchCollectionsByRequestorId()
        recyclerViewCollection.layoutManager = LinearLayoutManager(this)
    }

    private fun fetchCollectionsByRequestorId() {
        val requestorId = "6660c1783c9663264f09e91a"  // This ID could also be dynamically obtained from user data or session
        apiService.getCollectionsByRequestorId(requestorId).enqueue(object : retrofit2.Callback<List<Collection>> {
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
            adapter = CollectionAdapter(collections, this@HistoryActivity)  // Pass 'this' as listener
        }
    }

    override fun onUpdate(collection: Collection) {
        val intent = Intent(this, UpdateCollectionActivity::class.java)
        intent.putExtra("collectionId", collection._id)
        startActivity(intent)
    }

    override fun onDelete(collectionId: String) {
        deleteCollection(collectionId)
    }

    private fun deleteCollection(collectionId: String) {
        apiService.deleteCollection(collectionId).enqueue(object : Callback<Unit> {
            override fun onResponse(call: Call<Unit>, response: Response<Unit>) {
                if (response.isSuccessful) {
                    Toast.makeText(this@HistoryActivity, "Collection deleted successfully", Toast.LENGTH_SHORT).show()
                    fetchCollectionsByRequestorId()  // Refresh the list
                } else {
                    Toast.makeText(this@HistoryActivity, "Failed to delete collection", Toast.LENGTH_LONG).show()
                }
            }

            override fun onFailure(call: Call<Unit>, t: Throwable) {
                Toast.makeText(this@HistoryActivity, "Error deleting collection: ${t.message}", Toast.LENGTH_LONG).show()
            }
        })
    }
}

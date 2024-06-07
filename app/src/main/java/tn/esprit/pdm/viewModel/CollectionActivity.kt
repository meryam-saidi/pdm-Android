package tn.esprit.pdm.viewModel

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.DatePicker
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import tn.esprit.pdm.R
import tn.esprit.pdm.network.RetrofitClient
import tn.esprit.pdm.network.UserApiService

class CollectionActivity : AppCompatActivity() {
    private lateinit var apiService: UserApiService
    private lateinit var datePickerStart: DatePicker
    private lateinit var datePickerEnd: DatePicker
    private lateinit var btnSchedule: Button
    private lateinit var btnCancel: Button
    private lateinit var spinnerPostTitles: Spinner
    private lateinit var commentView: EditText
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_collection)

        apiService = RetrofitClient.getClient("http://10.0.2.2:7002/").create(UserApiService::class.java)

        datePickerStart = findViewById(R.id.datePickerStart)
        datePickerEnd = findViewById(R.id.datePickerEnd)
        btnSchedule = findViewById(R.id.btnSchedule)
        btnCancel = findViewById(R.id.btnCancel)
        spinnerPostTitles = findViewById(R.id.spinnerPostTitles)
        commentView = findViewById(R.id.etComment)
        postTitles()

        btnSchedule.setOnClickListener {
            val selectedSurplusFoodItem = spinnerPostTitles.selectedItem as? SurplusFoodItem
            if (selectedSurplusFoodItem != null) {
                //val surplusFoodId = selectedSurplusFoodItem.id
                scheduleCollection()
            } else {
                Toast.makeText(this, "No item selected", Toast.LENGTH_SHORT).show()
            }
        }

        btnCancel.setOnClickListener {
            val intentToHomeActivity = Intent(this@CollectionActivity, HomeActivity::class.java)
            startActivity(intentToHomeActivity)
        }
    }

    private fun postTitles() {
        apiService.getPosts().enqueue(object : retrofit2.Callback<List<Post>> {
            override fun onResponse(call: Call<List<Post>>, response: Response<List<Post>>) {
                if (response.isSuccessful) {
                    response.body()?.let { posts ->
                        updateSpinnerWithPostTitles(posts)
                    }
                } else {
                    Toast.makeText(this@CollectionActivity, "Error: ${response.message()}", Toast.LENGTH_LONG).show()
                }
            }

            override fun onFailure(call: Call<List<Post>>, t: Throwable) {
                Toast.makeText(this@CollectionActivity, "Failure: ${t.message}", Toast.LENGTH_LONG).show()
            }
        })
    }

    private fun updateSpinnerWithPostTitles(posts: List<Post>) {
        val surplusFoodItems = posts.map { SurplusFoodItem(it.id.toString(), it.foodType) }
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, surplusFoodItems)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerPostTitles.adapter = adapter
    }

    private fun scheduleCollection() {
        val selectedSurplusFoodItem = spinnerPostTitles.selectedItem as? SurplusFoodItem
        if (selectedSurplusFoodItem != null) {
            val foodType = selectedSurplusFoodItem.title
            //val foodType = selectedSurplusFoodItem.foodType  // Assuming foodType is also needed

            val startDate = "${datePickerStart.year}-${datePickerStart.month + 1}-${datePickerStart.dayOfMonth}"
            val endDate = "${datePickerEnd.year}-${datePickerEnd.month + 1}-${datePickerEnd.dayOfMonth}"
            val comment = commentView.text.toString()

            // Assuming requestorId is obtained securely from user session or similar
            val requestorId = "6660c1783c9663264f09e91a"

            val newCollection = Collection(
                foodType = foodType,
                requestorId = requestorId,
                startDate = startDate,
                endDate = endDate,
                comment = comment
            )

            createCollection(newCollection)
        } else {
            Toast.makeText(this, "No item selected", Toast.LENGTH_SHORT).show()
        }
    }



    private fun createCollection(collection: Collection) {
        apiService.createCollection(collection).enqueue(object : Callback<Collection> {
            override fun onResponse(call: Call<Collection>, response: Response<Collection>) {
                if (response.isSuccessful) {
                    Toast.makeText(this@CollectionActivity, "Collection scheduled successfully", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this@CollectionActivity, "Failed to schedule collection: ${response.errorBody()?.string()}", Toast.LENGTH_LONG).show()
                }
            }

            override fun onFailure(call: Call<Collection>, t: Throwable) {
                Toast.makeText(this@CollectionActivity, "Error: ${t.localizedMessage}", Toast.LENGTH_LONG).show()
            }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.collection_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.historyItem -> {
                val intentToCollectionActivity = Intent(this@CollectionActivity, HistoryActivity::class.java)
                startActivity(intentToCollectionActivity)
            }
        }
        return super.onOptionsItemSelected(item)
    }

}
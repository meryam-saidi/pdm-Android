package tn.esprit.pdm.viewModel

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.DatePicker
import android.widget.EditText
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import tn.esprit.pdm.R
import tn.esprit.pdm.network.RetrofitClient
import tn.esprit.pdm.network.UserApiService
import tn.esprit.pdm.network.model.Collection
import tn.esprit.pdm.network.model.Post
import tn.esprit.pdm.network.model.SurplusFoodItem

class UpdateCollectionActivity : AppCompatActivity() {
    private lateinit var apiService: UserApiService
    private lateinit var spinnerTitles: Spinner
    private lateinit var datePickerStart: DatePicker
    private lateinit var datePickerEnd: DatePicker
    private lateinit var tvUpdateComment: EditText
    private lateinit var btnUpdateConfirm: Button
    private lateinit var textViewCollectionId: TextView

    private var collectionId: String? = null
    private var collection: Collection? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_update_collection)

        apiService = RetrofitClient.getClient("http://10.0.2.2:7002/").create(UserApiService::class.java)
        spinnerTitles = findViewById(R.id.spinnerTitles)
        datePickerStart = findViewById(R.id.updateDatePickerStart)
        datePickerEnd = findViewById(R.id.updateDatePickerEnd)
        tvUpdateComment = findViewById(R.id.tvUpdateComment)
        btnUpdateConfirm = findViewById(R.id.btnUpdateConfirm)
        textViewCollectionId = findViewById(R.id.tvCollectionId)

        collectionId = intent.getStringExtra("collectionId")

        textViewCollectionId.text = "Collection ID: $collectionId"

        fetchCollectionDetails()
        fetchPosts()

        btnUpdateConfirm.setOnClickListener {
            updateCollection()
        }
    }

    private fun fetchCollectionDetails() {
        collectionId?.let { id ->
            apiService.getCollectionById(id).enqueue(object : Callback<Collection> {
                override fun onResponse(call: Call<Collection>, response: Response<Collection>) {
                    if (response.isSuccessful) {
                        response.body()?.let { collection ->
                            this@UpdateCollectionActivity.collection = collection
                            populateFields(collection)
                        }
                    } else {
                        Toast.makeText(this@UpdateCollectionActivity, "Failed to load collection details", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<Collection>, t: Throwable) {
                    Toast.makeText(this@UpdateCollectionActivity, "Error: ${t.localizedMessage}", Toast.LENGTH_SHORT).show()
                }
            })
        }
    }

    private fun fetchPosts() {
        apiService.getPosts().enqueue(object : Callback<List<Post>> {
            override fun onResponse(call: Call<List<Post>>, response: Response<List<Post>>) {
                if (response.isSuccessful) {
                    response.body()?.let { posts ->
                        updateSpinnerWithPostTitles(posts)
                        collection?.let { collection ->
                            setSpinnerSelection(collection.foodType)
                        }
                    }
                } else {
                    Toast.makeText(this@UpdateCollectionActivity, "Failed to load posts", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<List<Post>>, t: Throwable) {
                Toast.makeText(this@UpdateCollectionActivity, "Error: ${t.localizedMessage}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun updateSpinnerWithPostTitles(posts: List<Post>) {
        val surplusFoodItems = posts.map { SurplusFoodItem(it.id.toString(), it.foodType) }
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, surplusFoodItems)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerTitles.adapter = adapter
    }

    private fun setSpinnerSelection(foodType: String) {
        val adapter = spinnerTitles.adapter as ArrayAdapter<SurplusFoodItem>
        val position = adapter.getPosition(SurplusFoodItem("", foodType))
        spinnerTitles.setSelection(position)
    }

    private fun populateFields(collection: Collection) {
        val startDateParts = collection.startDate.split("-")
        datePickerStart.updateDate(startDateParts[0].toInt(), startDateParts[1].toInt() - 1, startDateParts[2].toInt())

        val endDateParts = collection.endDate.split("-")
        datePickerEnd.updateDate(endDateParts[0].toInt(), endDateParts[1].toInt() - 1, endDateParts[2].toInt())

        tvUpdateComment.setText(collection.comment)
    }

    private fun updateCollection() {
        val selectedSurplusFoodItem = spinnerTitles.selectedItem as? SurplusFoodItem
        val foodType = selectedSurplusFoodItem?.title ?: return
        val startDate = "${datePickerStart.year}-${datePickerStart.month + 1}-${datePickerStart.dayOfMonth}"
        val endDate = "${datePickerEnd.year}-${datePickerEnd.month + 1}-${datePickerEnd.dayOfMonth}"
        val comment = tvUpdateComment.text.toString()

        val updatedCollection = Collection(
            _id = collectionId ?: "",
            foodType = foodType,
            requestorId = "6660c1783c9663264f09e91a",
            startDate = startDate,
            endDate = endDate,
            comment = comment
        )

        apiService.updateCollection(collectionId ?: "", updatedCollection).enqueue(object : Callback<Collection> {
            override fun onResponse(call: Call<Collection>, response: Response<Collection>) {
                if (response.isSuccessful) {
                    Toast.makeText(this@UpdateCollectionActivity, "Collection updated successfully", Toast.LENGTH_SHORT).show()
                    finish()
                } else {
                    Toast.makeText(this@UpdateCollectionActivity, "Failed to update collection", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<Collection>, t: Throwable) {
                Toast.makeText(this@UpdateCollectionActivity, "Error: ${t.localizedMessage}", Toast.LENGTH_SHORT).show()
            }
        })
    }
}

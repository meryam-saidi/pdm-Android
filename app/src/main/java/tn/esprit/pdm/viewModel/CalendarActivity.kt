package tn.esprit.pdm.viewModel

import android.content.Intent
import android.icu.text.SimpleDateFormat
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.CalendarView
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import tn.esprit.pdm.R
import tn.esprit.pdm.network.RetrofitClient
import tn.esprit.pdm.network.UserApiService
import java.util.Calendar
import java.util.Locale

class CalendarActivity : AppCompatActivity() {
    private lateinit var apiService: UserApiService
    private lateinit var calendarView: CalendarView
    private lateinit var btnSchedule: Button
    private lateinit var btnCancel: Button
    private lateinit var spinnerPostTitles: Spinner
    private lateinit var commentView: EditText
    private var pickedDate: Calendar? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_calendar)

        apiService = RetrofitClient.getClient("http://10.0.2.2:7002/").create(UserApiService::class.java)

        calendarView = findViewById(R.id.calendarView)
        btnSchedule = findViewById(R.id.btnSchedule)
        btnCancel = findViewById(R.id.btnCancel)
        commentView = findViewById(R.id.etComment)
        spinnerPostTitles = findViewById(R.id.spinnerPostTitles)
        postTitles()

        calendarView.setOnDateChangeListener { view, year, month, dayOfMonth ->
            pickedDate = Calendar.getInstance().apply {
                set(year, month, dayOfMonth)
            }
        }

        btnSchedule.setOnClickListener {
            scheduleCollection()
        }


        btnCancel.setOnClickListener {
            val intentToHomeActivity = Intent(this@CalendarActivity, HomeActivity::class.java)
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
                    Toast.makeText(this@CalendarActivity, "Error: ${response.message()}", Toast.LENGTH_LONG).show()
                }
            }

            override fun onFailure(call: Call<List<Post>>, t: Throwable) {
                Toast.makeText(this@CalendarActivity, "Failure: ${t.message}", Toast.LENGTH_LONG).show()
            }
        })
    }

    private fun updateSpinnerWithPostTitles(posts: List<Post>) {
        // Extraire les titres des posts
        val titles = posts.map { it.foodType } // foodType est le titre du post
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, titles)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        // Trouver le Spinner et mettre à jour avec les nouveaux titres
        findViewById<Spinner>(R.id.spinnerPostTitles).adapter = adapter
    }

    private fun scheduleCollection() {
        val selectedPost = spinnerPostTitles.selectedItem as Post
        val surplusFoodId = selectedPost.id.toString()
        val requestorId = "6660c1783c9663264f09e91a"  // À remplacer par l'ID de l'utilisateur connecté

        val collectionDate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(pickedDate?.time ?: Calendar.getInstance().time)
        //val collectionTime = SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(pickedDate?.time ?: Calendar.getInstance().time)

        val comment = commentView.text.toString()

        val newCollection = Collection(surplusFoodId, requestorId, collectionDate, comment)
        createCollection(newCollection)
    }

    private fun createCollection(collection: Collection) {
        apiService.createCollection(collection).enqueue(object : Callback<Collection> {
            override fun onResponse(call: Call<Collection>, response: Response<Collection>) {
                if (response.isSuccessful) {
                    Toast.makeText(this@CalendarActivity, "Collection created successfully", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this@CalendarActivity, "Failed to create collection: ${response.errorBody()?.string()}", Toast.LENGTH_LONG).show()
                }
            }

            override fun onFailure(call: Call<Collection>, t: Throwable) {
                Toast.makeText(this@CalendarActivity, "Error: ${t.localizedMessage}", Toast.LENGTH_LONG).show()
            }
        })
    }

}

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
import tn.esprit.pdm.adapter.PostsAdapter
import tn.esprit.pdm.network.RetrofitClient
import tn.esprit.pdm.network.UserApiService
import tn.esprit.pdm.network.model.Post

class HomeActivity : AppCompatActivity() {
    private lateinit var apiService: UserApiService
    lateinit var recyclerViewPosts: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        apiService = RetrofitClient.getClient("http://10.0.2.2:7002/").create(UserApiService::class.java)
        recyclerViewPosts = findViewById(R.id.recyclerViewPosts)
        fetchPosts()

        /*
        recyclerViewPosts = findViewById(R.id.recyclerViewPosts)

        val items = listOf<Post>(
            Post(R.drawable.food, "Cheese", "Ahmed", 122, "yum mosh normal"),
            Post(R.drawable.food, "Cheese", "Ahmed", 122, "yum mosh normal"),
            Post(R.drawable.food, "Cheese", "Ahmed", 122, "yum mosh normal"),
            Post(R.drawable.food, "Cheese", "Ahmed", 122, "yum mosh normal"),
            Post(R.drawable.food, "Cheese", "Ahmed", 122, "yum mosh normal"),
            Post(R.drawable.food, "Cheese", "Ahmed", 122, "yum mosh normal"),
            Post(R.drawable.food, "Cheese", "Ahmed", 122, "yum mosh normal"),
        )

        recyclerViewPosts.apply {
            layoutManager = LinearLayoutManager(this@HomeActivity)
            adapter = PostsAdapter(items)
        }
        */
    }

    private fun fetchPosts() {
        apiService.getPosts().enqueue(object : retrofit2.Callback<List<Post>> {
            override fun onResponse(call: Call<List<Post>>, response: Response<List<Post>>) {
                if (response.isSuccessful) {
                    response.body()?.let {
                        updateRecyclerView(it)
                    }
                } else {
                    Toast.makeText(this@HomeActivity, "Error: ${response.message()}", Toast.LENGTH_LONG).show()
                }
            }

            override fun onFailure(call: Call<List<Post>>, t: Throwable) {
                Toast.makeText(this@HomeActivity, "Failure: ${t.message}", Toast.LENGTH_LONG).show()
            }
        })
    }


    private fun updateRecyclerView(posts: List<Post>) {
        recyclerViewPosts.apply {
            layoutManager = LinearLayoutManager(this@HomeActivity)
            adapter = PostsAdapter(posts)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.home_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.calendar -> {
                val intentToScheduleCollectionActivity = Intent(this@HomeActivity, ScheduleCollectionActivity::class.java)
                startActivity(intentToScheduleCollectionActivity)
            }
            R.id.itemProfile -> {

            }
            R.id.itemSettings -> {

            }
        }
        return super.onOptionsItemSelected(item)
    }

}
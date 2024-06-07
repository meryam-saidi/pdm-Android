package tn.esprit.pdm.viewModel

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import tn.esprit.pdm.R

class HistoryActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var collectionAdapter: CollectionAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_collection) // Make sure this layout has a RecyclerView

        val collections = listOf(
            Collection("1", "Food Donation", "Vegetables", "2021-06-01", "2021-06-02", "No specific notes."),
            // Add more sample data here
        )

        recyclerView = findViewById(R.id.recyclerHistory)
        collectionAdapter = CollectionAdapter(collections)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = collectionAdapter
    }
}

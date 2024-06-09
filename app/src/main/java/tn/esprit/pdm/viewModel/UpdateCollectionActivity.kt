package tn.esprit.pdm.viewModel

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import tn.esprit.pdm.R

class UpdateCollectionActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_update_collection)

        val collectionId = intent.getStringExtra("collectionId") ?: return
        val textViewCollectionId = findViewById<TextView>(R.id.tvCollectionId)

        // Set the text of TextView to the collectionId
        textViewCollectionId.text = "Collection ID: $collectionId"

        // Optionally, fetch more details about the collection using the collectionId
        // and populate other fields
    }
}
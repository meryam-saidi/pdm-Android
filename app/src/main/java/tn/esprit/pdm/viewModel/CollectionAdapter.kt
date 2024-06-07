package tn.esprit.pdm.viewModel

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import tn.esprit.pdm.R
class CollectionAdapter(private val collectionEvents: List<Collection>) :
    RecyclerView.Adapter<CollectionAdapter.CollectionViewHolder>() {

    class CollectionViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvCollectionTitle: TextView = view.findViewById(R.id.tvCollectionTitle)
        val tvFoodTypeAnswer: TextView = view.findViewById(R.id.tvFoodTypeAnswer)
        val tvStartDateAnswer: TextView = view.findViewById(R.id.tvStartDateAnswer)
        val tvEndDateAnswer: TextView = view.findViewById(R.id.tvEndDateAnswer)
        val tvCommentAnswer: TextView = view.findViewById(R.id.tvCommentAnswer)
        val btnUpdate: Button = view.findViewById(R.id.btnUpdate)
        val btnCancel: Button = view.findViewById(R.id.btnCancel)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CollectionViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_collection, parent, false)
        return CollectionViewHolder(view)
    }

    override fun onBindViewHolder(holder: CollectionViewHolder, position: Int) {
        val item = collectionEvents[position]
        holder.tvCollectionTitle.text = item.title
        holder.tvFoodTypeAnswer.text = item.foodType
        holder.tvStartDateAnswer.text = item.startDate
        holder.tvEndDateAnswer.text = item.endDate
        holder.tvCommentAnswer.text = item.comment
    }

    override fun getItemCount(): Int = collectionEvents.size
}


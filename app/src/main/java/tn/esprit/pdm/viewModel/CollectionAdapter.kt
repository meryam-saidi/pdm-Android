package tn.esprit.pdm.viewModel

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.PopupMenu
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import tn.esprit.pdm.R

class CollectionAdapter(var items: List<Collection>) : RecyclerView.Adapter<CollectionAdapter.CollectionsViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CollectionsViewHolder {
        //parent= recyclerView
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_collection, parent, false)
        return CollectionsViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: CollectionAdapter.CollectionsViewHolder, position: Int) {
        val collection = items[position]
        holder.bindData(collection)    }

    override fun getItemCount() = items.size

    class CollectionsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var tvFoodType: TextView
        var tvStartDate: TextView
        var tvEndDate: TextView
        var tvComment: TextView

        init {
            tvFoodType = itemView.findViewById(R.id.tvFoodTypeAnswer)
            tvStartDate = itemView.findViewById(R.id.tvStartDateAnswer)
            tvEndDate = itemView.findViewById(R.id.tvEndDateAnswer)
            tvComment = itemView.findViewById(R.id.tvCommentAnswer)
        }

        fun bindData(collection: Collection) {
            tvFoodType.text = collection.foodType
            tvStartDate.text = collection.startDate
            tvEndDate.text = collection.endDate
            tvComment.text = collection.comment
        }
    }
}
package tn.esprit.pdm.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import tn.esprit.pdm.R
import tn.esprit.pdm.network.model.Collection

class CollectionAdapter(private val items: List<Collection>, private val listener: ItemInteractionListener) : RecyclerView.Adapter<CollectionAdapter.CollectionViewHolder>() {

    interface ItemInteractionListener {
        fun onUpdate(collection: Collection)
        fun onDelete(collectionId: String)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CollectionViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_collection, parent, false)
        return CollectionViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: CollectionViewHolder, position: Int) {
        holder.bindData(items[position], listener)
    }

    override fun getItemCount() = items.size

    class CollectionViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvFoodType: TextView = itemView.findViewById(R.id.tvFoodTypeAnswer)
        private val tvStartDate: TextView = itemView.findViewById(R.id.tvStartDateAnswer)
        private val tvEndDate: TextView = itemView.findViewById(R.id.tvEndDateAnswer)
        private val tvComment: TextView = itemView.findViewById(R.id.tvCommentAnswer)
        private val btnUpdate: Button = itemView.findViewById(R.id.btnUpdate)
        private val btnDelete: Button = itemView.findViewById(R.id.btnDelete)
        private val tvId: TextView = itemView.findViewById(R.id.tvId)

        fun bindData(collection: Collection, listener: ItemInteractionListener) {
            tvId.text = collection._id
            tvFoodType.text = collection.foodType
            tvStartDate.text = collection.startDate
            tvEndDate.text = collection.endDate
            tvComment.text = collection.comment

            btnUpdate.setOnClickListener {
                listener.onUpdate(collection)
            }

            btnDelete.setOnClickListener {
                listener.onDelete(collection._id)
            }
        }
    }
}

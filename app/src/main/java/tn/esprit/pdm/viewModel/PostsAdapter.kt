package tn.esprit.pdm.viewModel

import android.content.ClipDescription
import android.content.Context
import android.icu.text.CaseMap.Title
import android.icu.text.SimpleDateFormat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.PopupMenu
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import tn.esprit.pdm.R
import java.util.Locale

class PostsAdapter(var items: List<Post>) : RecyclerView.Adapter<PostsAdapter.PostsViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostsViewHolder {
                                    //parent= recyclerView
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_post, parent, false)
        return PostsViewHolder(itemView)
    }

    override fun getItemCount() = items.size

    override fun onBindViewHolder(holder: PostsViewHolder, position: Int) {
        val post = items[position]
        holder.bindData(post)
    }

    class PostsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var tvTitle: TextView
        //var tvDonor: TextView
        var tvDescription: TextView
        //var imageFood: ImageView
        var tvQuantity: TextView
        //var tvExpirationdate: TextView
        var popupMenuItem: ImageView

        init {
            tvTitle = itemView.findViewById(R.id.tvTitle)
            //tvDonor = itemView.findViewById(R.id.tvDonorName)
            tvDescription = itemView.findViewById(R.id.tvDescriptionAnswer)
            //imageFood = itemView.findViewById(R.id.imageFood)
            tvQuantity = itemView.findViewById(R.id.tvQuantityAnswer)
            //tvExpirationdate = itemView.findViewById(R.id.tvExpirationDate)
            popupMenuItem = itemView.findViewById(R.id.menuItem)
        }

        fun bindData(post: Post) {
            tvTitle.text = post.foodType
            //tvDonor.text = post.donor
            tvDescription.text = post.description
            tvQuantity.text = post.quantity.toString()
            //imageFood.setImageResource(post.photo)
            /*
                // Format the date
                val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                holder.tvExpirationdate.text = dateFormat.format(post.expirationDate)
            */

            popupMenuItem.setOnClickListener{
                val popupMenu = PopupMenu(itemView.context, it)
                popupMenu.menuInflater.inflate(R.menu.item_menu, popupMenu.menu)
                popupMenu.show()
            }
        }
    }
}

package org.abanoubmilad.nut.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import org.abanoubmilad.nut.R
import org.abanoubmilad.nut.adapters.BookSearchResultsAdapter.BookSearchResultHolder
import org.abanoubmilad.nut.models.Volume
import java.util.*

class BookSearchResultsAdapter : RecyclerView.Adapter<BookSearchResultHolder>() {
    private var results: List<Volume> = ArrayList()
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): BookSearchResultHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.book_item, parent, false)
        return BookSearchResultHolder(itemView)
    }

    override fun onBindViewHolder(
        holder: BookSearchResultHolder,
        position: Int
    ) {
        val volume = results[position]
        holder.titleTextView.text = volume.volumeInfo?.title
        holder.publishedDateTextView.text = volume.volumeInfo?.publishedDate
        if (volume.volumeInfo?.imageLinks != null) {
            val imageUrl = volume.volumeInfo?.imageLinks?.smallThumbnail
                ?.replace("http://", "https://")
            Glide.with(holder.itemView)
                .load(imageUrl)
                .into(holder.smallThumbnailImageView)
        }
        holder.authorsTextView.text = volume.volumeInfo?.authors?.joinToString()

    }

    override fun getItemCount(): Int {
        return results.size
    }

    fun setResults(results: List<Volume>) {
        this.results = results
        notifyDataSetChanged()
    }

    inner class BookSearchResultHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {
        val titleTextView: TextView
        val authorsTextView: TextView
        val publishedDateTextView: TextView
        val smallThumbnailImageView: ImageView

        init {
            titleTextView = itemView.findViewById(R.id.book_item_title)
            authorsTextView = itemView.findViewById(R.id.book_item_authors)
            publishedDateTextView = itemView.findViewById(R.id.book_item_publishedDate)
            smallThumbnailImageView =
                itemView.findViewById(R.id.book_item_smallThumbnail)
        }
    }
}
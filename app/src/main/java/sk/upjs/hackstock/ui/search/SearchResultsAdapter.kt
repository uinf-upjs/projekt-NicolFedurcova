
package sk.upjs.hackstock.ui.search

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.graphics.ColorMatrix
import androidx.recyclerview.widget.RecyclerView
import sk.upjs.hackstock.R
import com.bumptech.glide.Glide
import sk.upjs.hackstock.entities.Share
import sk.upjs.hackstock.ui.home.OnItemClickListener

class SearchResultsAdapter(private var results: List<SearchResult>, private val listener: OnItemClickListener) : RecyclerView.Adapter<SearchResultsAdapter.ViewHolder>() {

    class ViewHolder(view: View, private val listener: OnItemClickListener) : RecyclerView.ViewHolder(view) , View.OnClickListener {

        init {
            view.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            val position = adapterPosition
            if (position != RecyclerView.NO_POSITION) {
                listener.onItemClick(position)
            }
        }

        val logoImageView: ImageView = view.findViewById(R.id.logoImageView)
        val nameTextView: TextView = view.findViewById(R.id.nameTextView)
        val symbolTextView: TextView = view.findViewById(R.id.symbolTextView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.search_result_item, parent, false)
        return ViewHolder(view, listener)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val result = results[position]
        holder.nameTextView.text = result.name
        holder.symbolTextView.text = result.symbol


        val logoUrl = "https://financialmodelingprep.com/image-stock/${result.symbol}.png"


        Glide.with(holder.logoImageView.context)
            .load(logoUrl)
            .into(holder.logoImageView)

        holder.logoImageView.colorFilter = android.graphics.ColorMatrixColorFilter(
            android.graphics.ColorMatrix().apply {
                setSaturation(0f) // Remove color saturation
                setScale(-1f, 1f, 1f, 1f) // Invert the colors // Shift the brightness to make white become black
            }
        )
    }

    fun getCurrentShare(position: Int): SearchResult {
        return results[position]
    }

    override fun getItemCount() = results.size

    fun updateResults(newResults: List<SearchResult>) {
        results = newResults
        notifyDataSetChanged()
    }
}

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

class SearchResultsAdapter(private var results: List<SearchResult>) : RecyclerView.Adapter<SearchResultsAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val logoImageView: ImageView = view.findViewById(R.id.logoImageView)
        val nameTextView: TextView = view.findViewById(R.id.nameTextView)
        val symbolTextView: TextView = view.findViewById(R.id.symbolTextView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.search_result_item, parent, false)
        return ViewHolder(view)
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

    override fun getItemCount() = results.size

    fun updateResults(newResults: List<SearchResult>) {
        results = newResults
        notifyDataSetChanged()
    }
}
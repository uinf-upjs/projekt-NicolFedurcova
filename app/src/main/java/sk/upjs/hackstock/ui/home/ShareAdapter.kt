package sk.upjs.hackstock.ui.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import sk.upjs.hackstock.R
import sk.upjs.hackstock.entities.Share
import sk.upjs.hackstock.ui.search.SearchResult

class ShareAdapter(private var shares: List<Share>) : RecyclerView.Adapter<ShareAdapter.ShareViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShareViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_layout, parent, false)
        return ShareViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ShareViewHolder, position: Int) {
        val currentShare = shares[position]
        holder.bind(currentShare)


    }

    override fun getItemCount(): Int {
        return shares.size
    }

    inner class ShareViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textViewShareName: TextView = itemView.findViewById(R.id.textViewShareName)
        val textViewSharePrice: TextView = itemView.findViewById(R.id.textViewSharePrice)

        fun bind(currentShare: Share) {

            textViewShareName.text = currentShare.company
            textViewSharePrice.text = "Profit " + currentShare.price.toString()

            when {
                currentShare.price > 0 -> textViewSharePrice.setTextColor(itemView.context.getColor(R.color.price_increase))
                currentShare.price < 0 -> textViewSharePrice.setTextColor(itemView.context.getColor(R.color.price_decrease))
            }
        }
    }

    fun updateResults(newResults: List<Share>) {
        shares = newResults
        notifyDataSetChanged()
    }

}



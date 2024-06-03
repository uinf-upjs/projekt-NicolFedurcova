package sk.upjs.hackstock.ui.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import sk.upjs.hackstock.R
import sk.upjs.hackstock.entities.Share
import sk.upjs.hackstock.ui.search.SearchResult

class ShareAdapter(private var shares: List<Share>, private val listener: OnItemClickListener) : RecyclerView.Adapter<ShareAdapter.ShareViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShareViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_layout, parent, false)
        return ShareViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ShareViewHolder, position: Int) {
        val currentShare = shares[position]
        holder.bind(currentShare)


    }

    fun getCurrentShare(position: Int): Share{
        return shares[position]
    }

    override fun getItemCount(): Int {
        return shares.size
    }

    inner class ShareViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
        val textViewShareName: TextView = itemView.findViewById(R.id.textViewShareName)
        val textViewSharePrice: TextView = itemView.findViewById(R.id.textViewSharePrice)
        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            val position = adapterPosition
            if (position != RecyclerView.NO_POSITION) {
                listener.onItemClick(position)
            }
        }
        fun bind(currentShare: Share) {

            textViewShareName.text = currentShare.company
            textViewSharePrice.text = "Profit " + formatNumberToThreeDecimalPlaces(currentShare.price)


            when {
                currentShare.price > 0 -> textViewSharePrice.setTextColor(itemView.context.getColor(R.color.price_increase))
                currentShare.price < 0 -> textViewSharePrice.setTextColor(itemView.context.getColor(R.color.price_decrease))
            }
        }
    }

    fun formatNumberToThreeDecimalPlaces(number: Double): String {
        return String.format("%.2f", number)
    }

    fun updateResults(newResults: List<Share>) {
        shares = newResults
        notifyDataSetChanged()
    }

}

interface OnItemClickListener {
    fun onItemClick(position: Int)
}



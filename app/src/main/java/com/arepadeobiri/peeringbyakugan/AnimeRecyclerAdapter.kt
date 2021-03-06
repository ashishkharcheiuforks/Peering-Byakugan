package com.arepadeobiri.peeringbyakugan

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.arepadeobiri.peeringbyakugan.network.searchDataModels.SearchOnlyResultsItem
import com.arepadeobiri.peeringbyakugan.databinding.ItemLayoutBinding

class AnimeRecyclerAdapter(private val clickListener: AnimeClickListener) :
    ListAdapter<SearchOnlyResultsItem, AnimeRecyclerAdapter.ViewHolder>(AnimeInstanceDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
//        val item = getItem(position)
//        holder.bind(item)
        holder.bind(getItem(position), clickListener)

    }


    class ViewHolder private constructor(private val binding: ItemLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {


        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding: ItemLayoutBinding =
                    DataBindingUtil.inflate(layoutInflater, R.layout.item_layout, parent, false)
                return ViewHolder(binding)
            }
        }

        fun bind(
            item: SearchOnlyResultsItem,
            clickListener: AnimeClickListener

        ) {
//            Picasso.get().load(item.imageUrl).into(binding.animeImageView)
//            binding.animeTitle.text = item.title

            binding.clickListener = clickListener
            binding.searchAnime = item
            binding.executePendingBindings()
        }
    }

}


class AnimeInstanceDiffCallback : DiffUtil.ItemCallback<SearchOnlyResultsItem>() {
    override fun areItemsTheSame(oldItem: SearchOnlyResultsItem, newItem: SearchOnlyResultsItem): Boolean {
        return oldItem.malId == newItem.malId
    }

    override fun areContentsTheSame(oldItem: SearchOnlyResultsItem, newItem: SearchOnlyResultsItem): Boolean {
        return oldItem == newItem
    }


}


class AnimeClickListener(val clickListener: (animeId: Int, animeTitle: String) -> Unit) {
    fun onClick(anime: SearchOnlyResultsItem) = clickListener(anime.malId!!, anime.title!!)
}


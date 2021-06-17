package com.example.iconify.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.iconify.R
import com.example.iconify.model.publicIconSets.Iconset
import com.example.iconify.model.searchIcons.Icon
import kotlinx.android.synthetic.main.recycler_view_layout_search_icon.view.*

class SearchIconsAdapter : RecyclerView.Adapter<SearchIconsAdapter.MyViewHolder>() {

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    private val diffUtilCallback = object : DiffUtil.ItemCallback<Icon>() {
        override fun areItemsTheSame(oldItem: Icon, newItem: Icon): Boolean {
            return oldItem.categories == newItem.categories &&
                    oldItem.containers == newItem.categories &&
                    oldItem.icon_id == newItem.icon_id &&
                    oldItem.is_icon_glyph == newItem.is_icon_glyph &&
                    oldItem.is_premium == newItem.is_premium &&
                    oldItem.prices == newItem.prices &&
                    oldItem.published_at == newItem.published_at &&
                    oldItem.raster_sizes == newItem.raster_sizes &&
                    oldItem.styles == newItem.styles &&
                    oldItem.tags == newItem.tags &&
                    oldItem.type == newItem.type &&
                    oldItem.vector_sizes == newItem.vector_sizes
        }

        override fun areContentsTheSame(oldItem: Icon, newItem: Icon): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, diffUtilCallback)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.recycler_view_layout_search_icon, parent, false)
        )
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        val searchIcons = differ.currentList[position]

        holder.itemView.apply {
            tv_name.text = searchIcons.tags[0]
            tv_type.text = "Type: ${searchIcons.type}"

            if (searchIcons.prices == null) {
                tv_price.text = "$0"
            } else {
                tv_price.text = "$${
                    searchIcons.prices[0].price
                }"
            }

            val count = searchIcons.raster_sizes.size

            Glide.with(this).load(searchIcons.raster_sizes[count - 1].formats[0].preview_url)
                .placeholder(R.drawable.placeholder)
                .into(iv_icon)


            setOnClickListener {
                onItemClickListener?.let {
                    it(searchIcons)
                }
            }
        }


    }

    override fun getItemCount(): Int {
        return differ.currentList.count()
    }

    private var onItemClickListener: ((Icon) -> Unit)? = null

    fun setOnItemClickListener(listener: (Icon) -> Unit) {
        onItemClickListener = listener
    }

}
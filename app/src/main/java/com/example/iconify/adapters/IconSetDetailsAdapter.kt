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
import kotlinx.android.synthetic.main.fragment_icon_set_details.view.*
import kotlinx.android.synthetic.main.fragment_icon_set_details.view.tv_name
import kotlinx.android.synthetic.main.fragment_icon_set_details.view.tv_price
import kotlinx.android.synthetic.main.fragment_icon_set_details.view.tv_type
import kotlinx.android.synthetic.main.recycler_view_layout_search_icon.view.*

class IconSetDetailsAdapter : RecyclerView.Adapter<IconSetDetailsAdapter.MyViewHolder>() {

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    private val diffUtilCallback =
        object : DiffUtil.ItemCallback<com.example.iconify.model.allIconsInIconSet.Icon>() {
            override fun areItemsTheSame(
                oldItem: com.example.iconify.model.allIconsInIconSet.Icon,
                newItem: com.example.iconify.model.allIconsInIconSet.Icon
            ): Boolean {
                return oldItem.categories == newItem.categories &&
                        oldItem.containers == newItem.categories &&
                        oldItem.icon_id == newItem.icon_id &&
                        oldItem.is_icon_glyph == newItem.is_icon_glyph &&
                        oldItem.is_premium == newItem.is_premium &&
                        oldItem.published_at == newItem.published_at &&
                        oldItem.raster_sizes == newItem.raster_sizes &&
                        oldItem.styles == newItem.styles &&
                        oldItem.tags == newItem.tags &&
                        oldItem.type == newItem.type &&
                        oldItem.vector_sizes == newItem.vector_sizes
            }

            override fun areContentsTheSame(
                oldItem: com.example.iconify.model.allIconsInIconSet.Icon,
                newItem: com.example.iconify.model.allIconsInIconSet.Icon
            ): Boolean {
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

        val icon = differ.currentList[position]

        holder.itemView.apply {
            tv_name.text = icon.tags[0]
            tv_type.text = "Type: ${icon.type}"

            if (icon.prices == null) {
                tv_price.text = "$0"
            } else {
                tv_price.text = "$${
                    icon.prices[0].price
                }"
            }

            val count = icon.raster_sizes.size

            Glide.with(this).load(icon.raster_sizes[count - 1].formats[0].preview_url)
                .placeholder(R.drawable.placeholder)
                .into(iv_icon)

            setOnClickListener {
                onItemClickListener?.let {
                    it(icon)
                }
            }
        }


    }

    override fun getItemCount(): Int {
        return differ.currentList.count()
    }

    private var onItemClickListener: ((com.example.iconify.model.allIconsInIconSet.Icon) -> Unit)? = null

    fun setOnItemClickListener(listener: (com.example.iconify.model.allIconsInIconSet.Icon) -> Unit) {
        onItemClickListener = listener
    }


}
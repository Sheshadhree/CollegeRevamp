package com.octovalley.collegerevamp.viewHolder

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.octovalley.collegerevamp.R
import com.octovalley.collegerevamp.`interface`.ItemClickListener
import kotlinx.android.synthetic.main.image_query_cardview.view.*

open class ImageViewHolder (itemView: View) :RecyclerView.ViewHolder(itemView) , View.OnClickListener {

    open var name:TextView? = null
    open var image :ImageView? = null
    open var listener:ItemClickListener? = null

    override fun onClick(v: View?) {

        listener?.onClick(v,adapterPosition,false)

    }

    init {
        name = itemView.findViewById(R.id.text_cardview)
        image = itemView.findViewById(R.id.image_cardview)
    }


}
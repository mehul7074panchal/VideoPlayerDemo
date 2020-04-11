package com.mehul.videoapplication

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.amulyakhare.textdrawable.TextDrawable
import com.amulyakhare.textdrawable.util.ColorGenerator
import java.util.*

class VideoAdapter(
    private val mValues: List<Video>,
    private val mediaPlayer: MediaPlayer,
    private val ctx: Context
) : RecyclerView.Adapter<VideoAdapter.ViewHolder>() {
    private val arraylist: ArrayList<Video> = ArrayList()
    var selectedPosition = 0

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.layout_playlist, parent, false)
        return ViewHolder(view)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(
        holder: ViewHolder,
        position: Int
    ) {
        holder.mItem = mValues[position]
        if (holder.mItem != null) {
            holder.tvP_Name.text = holder.mItem!!.title
            val generator = ColorGenerator.MATERIAL
            var ch = 'V'
            holder.mItem!!.title
            if (holder.mItem!!.title.isNotEmpty()) {
                ch = holder.mItem!!.title[0]
            }
            val colorRandam = generator.getColor(Character.toUpperCase(ch))
            val drawable = TextDrawable.builder()
                .buildRound(
                    Character.toUpperCase(holder.mItem!!.title[0]).toString() + "",
                    colorRandam
                )
            holder.ivP.setImageDrawable(drawable)

        }
        if(selectedPosition==position)
            holder.itemView.setBackgroundColor(Color.parseColor("#000000"));
        else
            holder.itemView.setBackgroundColor(Color.parseColor("#ffffff"));
        holder.mView.setOnClickListener {
            if (holder.mItem != null)

            mediaPlayer.preparePlayer(holder.mItem!!.url, 0)

            selectedPosition = position
            notifyDataSetChanged()
        }
    }

    override fun getItemCount(): Int {
        return mValues.size
    }

    inner class ViewHolder(val mView: View) : RecyclerView.ViewHolder(mView) {
        val tvP_Name: TextView = mView.findViewById(R.id.tvName)
        var mItem: Video? = null
        val ivP: ImageView = mView.findViewById(R.id.ivProfile)
        override fun toString(): String {
            return ""
        }


    }

    init {
        arraylist.addAll(mValues)
    }
}
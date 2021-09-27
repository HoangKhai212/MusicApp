package gst.trainingcourse.musicapp.controller

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import gst.trainingcourse.musicapp.R
import gst.trainingcourse.musicapp.fragment.MusicListFragment
import gst.trainingcourse.musicapp.model.Music

class MusicAdapter(private val listMusic: MutableList<Music>,  private val itemClickListener: OnItemClickListener):RecyclerView.Adapter<MusicAdapter.MusicViewHolder>() {
    inner class MusicViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
        var tvName = itemView.findViewById(R.id.tvName) as TextView
        var tvSinger = itemView.findViewById(R.id.tvSinger) as TextView
        var imgMusic = itemView.findViewById(R.id.imgMusic) as ImageView
        var tvAlbum = itemView.findViewById(R.id.tvAlbum) as TextView

        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            val position = adapterPosition
            itemClickListener.onItemClickListener(position)
        }
    }

    interface OnItemClickListener {
        fun onItemClickListener(position: Int)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MusicViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.music_item, parent, false)
        return MusicViewHolder(v)
    }

    override fun onBindViewHolder(holder: MusicViewHolder, position: Int) {
        val music = listMusic[position]
        holder.apply {
            tvName.text = music.name
            tvSinger.text = music.singer
            tvAlbum.text = music.album
            imgMusic.setImageResource(music.image)
        }
    }

    override fun getItemCount(): Int {
        return listMusic.size
    }
}
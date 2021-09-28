package gst.trainingcourse.musicapp.controller

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import gst.trainingcourse.musicapp.R
import gst.trainingcourse.musicapp.databinding.MusicItemBinding
import gst.trainingcourse.musicapp.fragment.MusicListFragment
import gst.trainingcourse.musicapp.model.Music

class MusicAdapter(private val listMusic: MutableList<Music>,  private val itemClickListener: OnItemClickListener):RecyclerView.Adapter<MusicAdapter.MusicViewHolder>() {
    private lateinit var binding: MusicItemBinding

    inner class MusicViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

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
            binding.tvName.text = music.name
            binding.tvSinger.text = music.singer
            binding.tvAlbum.text = music.album
            binding.imgMusic.setImageResource(music.image)
        }
        holder.itemView.setOnClickListener(){
            itemClickListener.onItemClickListener(position)
        }
    }

    override fun getItemCount(): Int {
        return listMusic.size
    }
}
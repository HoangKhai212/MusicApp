package gst.trainingcourse.musicapp.fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import gst.trainingcourse.musicapp.MyCommon
import gst.trainingcourse.musicapp.R
import gst.trainingcourse.musicapp.controller.MusicAdapter
import gst.trainingcourse.musicapp.service.MusicService
import kotlinx.android.synthetic.main.fragment_music_list.*


class MusicListFragment : Fragment(), MusicAdapter.OnItemClickListener {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val v = inflater.inflate(R.layout.fragment_music_list, container, false)
        val rvMusicList = v.findViewById<RecyclerView>(R.id.rvMusicList)
        var musicAdapter = MusicAdapter(MyCommon.listMusic, this)

        rvMusicList.layoutManager = LinearLayoutManager(v.context)
        rvMusicList.adapter = musicAdapter

        return v
    }

    companion object {

        @JvmStatic
        fun newInstance() =
            MusicListFragment().apply {
                arguments = Bundle().apply {
                }
            }
    }
    override fun onItemClickListener(position: Int) {
        val intent = Intent(context, MusicService::class.java)
        val bundle = Bundle()

        bundle.putInt("position", position)
        bundle.putString("action", MyCommon.MUSIC_START)

        val fragmentManager = activity?.supportFragmentManager
        val fragmentTransaction = fragmentManager?.beginTransaction()
        fragmentTransaction?.add(R.id.fragmentMusicControl, MusicControlFragment.newInstance(bundle))?.commit()

        intent.putExtras(bundle)
        context?.startService(intent)
    }

}
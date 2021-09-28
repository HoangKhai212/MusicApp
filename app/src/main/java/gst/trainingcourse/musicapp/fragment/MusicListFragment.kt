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
import gst.trainingcourse.musicapp.data.SQLiteHelper
import gst.trainingcourse.musicapp.databinding.FragmentMusicControlBinding
import gst.trainingcourse.musicapp.databinding.FragmentMusicListBinding
import gst.trainingcourse.musicapp.model.Music
import gst.trainingcourse.musicapp.service.MusicService
import kotlinx.android.synthetic.main.fragment_music_list.*


class MusicListFragment : Fragment(), MusicAdapter.OnItemClickListener {
    private lateinit var binding: FragmentMusicListBinding
    private var sqlHelper: SQLiteHelper? = null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMusicListBinding.inflate(inflater, container, false)

        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        sqlHelper = SQLiteHelper(requireContext())
        MyCommon.listMusic = sqlHelper?.getAll!!

        var musicAdapter = MusicAdapter(MyCommon.listMusic, this)
        binding.rvMusicList.adapter = musicAdapter
        super.onViewCreated(view, savedInstanceState)
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

        val fragmentManager = childFragmentManager
        val fragmentTransaction = fragmentManager?.beginTransaction()
        fragmentTransaction?.replace(R.id.fragmentMusicControl, MusicControlFragment.newInstance(bundle))?.commit()

        intent.putExtras(bundle)
        context?.startService(intent)
    }

}
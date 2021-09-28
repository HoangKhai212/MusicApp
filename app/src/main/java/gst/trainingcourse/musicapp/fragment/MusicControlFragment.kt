package gst.trainingcourse.musicapp.fragment

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import android.widget.Toast
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import gst.trainingcourse.musicapp.MyCommon
import gst.trainingcourse.musicapp.R
import gst.trainingcourse.musicapp.databinding.FragmentMusicControlBinding
import gst.trainingcourse.musicapp.model.Music
import gst.trainingcourse.musicapp.service.MusicService
import kotlinx.android.synthetic.main.fragment_music_control.*
import java.text.SimpleDateFormat


class MusicControlFragment : Fragment(), View.OnClickListener {
    private var bundle : Bundle? = null
    private var isPlay : Boolean = false
    private var music : Music? = null
    private var duration : Int? = 0
    private lateinit var actionReceiver : BroadcastReceiver
    private lateinit var  timeReceiver: BroadcastReceiver
    lateinit var v: View
    private lateinit var binding: FragmentMusicControlBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            bundle = it.getBundle("bundle")!!
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMusicControlBinding.inflate(inflater, container, false)
        broadcastReceiver()

        sbTime.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                TODO("Not yet implemented")
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
                TODO("Not yet implemented")
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                val intent  = Intent (v.context, MusicService::class.java)
                val bundle = Bundle()
                bundle.putInt("time", sbTime.progress)
                intent.putExtras(bundle)
                context?.startService(intent)
            }

        })
        binding.imgPlayMusic.setOnClickListener(this)
        binding.imgForward.setOnClickListener(this)
        binding.imgRewind.setOnClickListener(this)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (bundle != null) {
            music = bundle!!.getSerializable("music") as Music?
        }
    }


    private fun broadcastReceiver() {
        actionReceiver = object : BroadcastReceiver(){
            override fun onReceive(context: Context?, intent: Intent?) {
                val bundle = intent?.extras
                if(bundle != null){
                    isPlay = bundle?.getBoolean("play")
                    duration = bundle?.getInt("duration")
                    music = bundle?.getSerializable("music") as Music?
                    val action = bundle.getString("action") as String?
                    if(action == MyCommon.MUSIC_START){
                        val timeFormat = SimpleDateFormat("mm:ss")
                        tvEnd.text = timeFormat.format(duration)
                        sbTime.max = duration as Int
                    }
                }

            }

        }
        LocalBroadcastManager.getInstance(v.context).registerReceiver(actionReceiver, IntentFilter("doAction"))
        timeReceiver = object : BroadcastReceiver(){
            override fun onReceive(context: Context?, intent: Intent?) {
                val bundle = intent?.extras
                val timeBundle = bundle!!.getInt("time")
                val timeFormat = SimpleDateFormat("mm:ss")
                tvStart.text = timeFormat.format(timeBundle)
                sbTime.progress = timeBundle
            }
        }
        LocalBroadcastManager.getInstance(v.context).registerReceiver(timeReceiver, IntentFilter("getTime"))
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: Bundle) =
            MusicControlFragment().apply {
                arguments = Bundle().apply {
                    putBundle("bundle", bundle)
                }
            }
    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.imgPlayMusic ->{
                val intent = Intent(context, MusicService::class.java)
                if(isPlay){
                    imgPlayMusic.setImageResource(R.drawable.ic_baseline_play_arrow_24)
                    val bundle = Bundle()
                    bundle.putString("action", MyCommon.MUSIC_PAUSE)
                    intent.putExtras(bundle)
                    Toast.makeText(context, "Pause", Toast.LENGTH_SHORT).show()
                }
                else{
                    imgPlayMusic.setImageResource(R.drawable.ic_baseline_pause_circle_24)
                    val bundle = Bundle()
                    bundle.putString("action", MyCommon.MUSIC_RESUME)
                    intent.putExtras(bundle)
                    Toast.makeText(context,"Continue", Toast.LENGTH_SHORT).show()
                }
                context?.startService(intent)
            }
            R.id.imgForward -> {
                imgPlayMusic.setImageResource(R.drawable.ic_baseline_pause_circle_24)
                val intent = Intent(context, MusicService::class.java)
                intent.putExtra("action", MyCommon.MUSIC_NEXT)
                Toast.makeText(context, "Next", Toast.LENGTH_SHORT).show()
                context?.startService(intent)
            }
            R.id.imgRewind -> {
                imgPlayMusic.setImageResource(R.drawable.ic_baseline_pause_circle_24)
                val intent = Intent(context, MusicService::class.java)
                intent.putExtra("action", MyCommon.MUSIC_PREVIOUS)
                Toast.makeText(context, "Previous", Toast.LENGTH_SHORT).show()
                context?.startService(intent)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        LocalBroadcastManager.getInstance(v.context).unregisterReceiver(actionReceiver)
        LocalBroadcastManager.getInstance(v.context).unregisterReceiver(timeReceiver)
    }

}
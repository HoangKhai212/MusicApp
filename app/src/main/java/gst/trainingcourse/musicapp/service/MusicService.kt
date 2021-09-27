package gst.trainingcourse.musicapp.service

import android.app.Service
import android.content.Intent
import android.media.MediaPlayer
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.IBinder
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import gst.trainingcourse.musicapp.MyCommon
import gst.trainingcourse.musicapp.model.Music

class MusicService : Service() {
    private lateinit var mediaPlayer : MediaPlayer
    private var isPlay : Boolean = false
    private var mMusic : Music? = null
    private var position : Int = 0

    override fun onBind(intent: Intent): IBinder? {
        return  null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val bundle = intent?.extras
        val action = bundle?.get("action") as String?
        val time = bundle?.get("time")

        if(action == MyCommon.MUSIC_START){
            position = bundle?.get("position") as Int
            val music : Music = MyCommon.listMusic[position]
            startMusic(music)
            mMusic = music
        }
        else{
            if(action != null)
                checkAction(action)
        }
        if(time != null)
            mediaPlayer.seekTo(time as Int)
        if(mediaPlayer.currentPosition == mediaPlayer.duration) {
            nextMusic()
        }
        return START_NOT_STICKY
    }


    private fun checkAction(action: String) {
        when(action){
            MyCommon.MUSIC_PAUSE ->{
                mediaPlayer.pause()
                isPlay = false
            }
            MyCommon.MUSIC_RESUME ->{
                if(!isPlay){
                    mediaPlayer.start()
                    isPlay = true
                }
            }
            MyCommon.MUSIC_NEXT ->{
                nextMusic()
            }
            MyCommon.MUSIC_PREVIOUS ->{
                previousMusic()
            }
        }
        doAction(action)
    }

    private fun startMusic(music: Music) {
        if (isPlay)
            mediaPlayer.stop()

        mediaPlayer = MediaPlayer.create(applicationContext, Uri.parse(music.musicUrl))
        mediaPlayer.start()

        val handler = Handler()
        handler.postDelayed(object : Runnable{
            override fun run() {
                currentTime(mediaPlayer.currentPosition)
                mediaPlayer.setOnCompletionListener {
                    nextMusic()
                }
                handler.postDelayed(this, 100)
            }

        },100)

        isPlay = true

        doAction(MyCommon.MUSIC_START)
    }

    private fun doAction(action: String) {
        val intent = Intent("doAction")
        val bundle = Bundle()

        bundle.apply {
            putSerializable("music", mMusic)
            putString("action", action)
            putBoolean("play", isPlay)
            putInt("duration", mediaPlayer.duration)
        }

        intent.putExtras(bundle)
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent)
    }

    private fun nextMusic() {
        position = (position + 1) % MyCommon.listMusic.size
        startMusic(MyCommon.listMusic[position])
    }

    private fun previousMusic() {
        position -= 1
        if(position < 0)
            position = MyCommon.listMusic.size - 1
        startMusic(MyCommon.listMusic[position])
    }

    private fun currentTime(time: Int) {
        val intent = Intent("getTime")
        val bundle = Bundle()

        bundle.putInt("time", time)

        intent.putExtras(bundle)
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent)
    }
    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer.release()
    }


}
package gst.trainingcourse.musicapp

import gst.trainingcourse.musicapp.model.Music

object MyCommon {
    const val MUSIC_START = "start"
    const val MUSIC_RESUME = "resume"
    const val MUSIC_PAUSE = "pause"
    const val MUSIC_NEXT = "next"
    const val MUSIC_PREVIOUS = "previous"
    val listMusic: MutableList<Music> = mutableListOf()

}
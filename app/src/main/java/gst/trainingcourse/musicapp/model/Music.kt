package gst.trainingcourse.musicapp.model

import java.io.Serializable

class Music(var name : String, var singer : String, var album : String, var image : Int, var musicUrl : String) : Serializable {
}
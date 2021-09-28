package gst.trainingcourse.musicapp.data

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import gst.trainingcourse.musicapp.model.Music
import java.util.ArrayList

class SQLiteHelper(context : Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
    companion object {
        private const val DATABASE_NAME = "music.db"
        private const val DATABASE_VERSION = 1
    }

    override fun onCreate(db: SQLiteDatabase?) {
        val sql = "CREATE TABLE music (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "name TEXT," +
                "singer TEXT," +
                "album TEXT," +
                "musicUrl TEXT," +
                "image INTERGER)"
        db?.execSQL(sql)
    }
    fun addMusic(music : Music): Long {
        val v = ContentValues()
        v.put("name", music.name)
        v.put("singer", music.singer)
        v.put("album", music.album)
        v.put("musicUrl", music.musicUrl)
        v.put("image", music.image)
        val sld = writableDatabase
        return sld.insert("music", null, v)
    }

    val getAll: MutableList<Music>
        get() {
            val list: MutableList<Music> = ArrayList()
            val statement = readableDatabase
            val c = statement.query("music", null, null, null, null, null, null)
            while (c != null && c.moveToNext()) {
                val name = c.getString(1)
                val singer = c.getString(2)
                val album = c.getString(3)
                val musicUrl = c.getString(4)
                val image = c.getString(5) as Int
                list.add(Music(name, singer, album, image, musicUrl ))
            }
            return list
        }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
    }
}
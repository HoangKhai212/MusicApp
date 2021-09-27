package gst.trainingcourse.musicapp


import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.provider.MediaStore
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import gst.trainingcourse.musicapp.fragment.MusicListFragment
import gst.trainingcourse.musicapp.model.Music


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.add(R.id.fragmentMusicList, MusicListFragment.newInstance()).commit()

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                requestPermissions(
                    arrayOf(
                        Manifest.permission.READ_EXTERNAL_STORAGE
                    ), 57
                )
            }
            return
        }


        inputSong()
    }

    private fun inputSong() {
        val cursor = contentResolver.query(
            MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
            arrayOf(
                MediaStore.Audio.Media.TITLE,
                MediaStore.Audio.Media.ARTIST,
                MediaStore.Audio.Media.ALBUM,
                MediaStore.Audio.Media.DATA),
            MediaStore.Audio.Media.IS_MUSIC + " != 0",
            null,
            null
        )

        if (cursor != null) {
            while (cursor.moveToNext()) {
                if (cursor.getString(3).endsWith(".mp3")) {
                    MyCommon.listMusic.add(
                        Music(
                            cursor.getString(0),
                            cursor.getString(1),
                            cursor.getString(2),
                            R.drawable.icon_music,
                            cursor.getString(3)
                        )
                    )
                }
            }
        }

    }
}
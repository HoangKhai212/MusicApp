package gst.trainingcourse.musicapp


import android.Manifest
import android.app.DownloadManager
import android.content.ContentValues.TAG
import android.content.Context
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import gst.trainingcourse.musicapp.data.SQLiteHelper
import gst.trainingcourse.musicapp.databinding.ActivityMainBinding
import gst.trainingcourse.musicapp.fragment.MusicListFragment
import gst.trainingcourse.musicapp.model.Music


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private var sqlHelper: SQLiteHelper? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.add(R.id.fragmentMusicList, MusicListFragment.newInstance()).commit()
        binding.btnDownload.setOnClickListener {
            if(binding.edUrl.text.toString() == ""){
                Toast.makeText(this, "Enter Url", Toast.LENGTH_SHORT).show()
            }
            else{
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                    if(checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)==PackageManager.PERMISSION_DENIED){
                        requestPermissions(arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), 1000)
                    }
                    else{
                        startDownloading()
                    }
                }
                else{
                    startDownloading()
                }
            }

        }

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),111)
        }else{
            inputSong()
        }

        setContentView(binding.root)
    }

    private fun startDownloading() {
        val url = binding.edUrl.text.toString()

        val request = DownloadManager.Request(Uri.parse(url))
        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI or DownloadManager.Request.NETWORK_MOBILE)
        request.setTitle("Download")
        request.setDescription("The file is downloading...")

        request.allowScanningByMediaScanner()
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, "${System.currentTimeMillis()}")

        val manager = getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
        manager.enqueue(request)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String?>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            111 -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_DENIED) {
                    inputSong()
                } else {
                    Toast.makeText(this, "Permissions denied!", Toast.LENGTH_SHORT).show()
                }
            }
            1000 -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_DENIED) {
                    startDownloading()
                } else {
                    Toast.makeText(this, "Permissions denied!", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }


    private fun inputSong() {
        sqlHelper = SQLiteHelper(this)
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
                    sqlHelper!!.addMusic(
                        Music(
                            cursor.getString(0),
                            cursor.getString(1),
                            cursor.getString(2),
                            R.drawable.ic_music,
                            cursor.getString(3)
                        )
                    )
                Log.e(TAG, "Can add music");
            }
        }

    }
}
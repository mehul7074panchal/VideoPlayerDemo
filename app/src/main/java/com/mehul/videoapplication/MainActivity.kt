package com.mehul.videoapplication


import android.annotation.SuppressLint
import android.content.pm.ActivityInfo
import android.content.res.Configuration
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.view.View.SYSTEM_UI_FLAG_FULLSCREEN
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.RelativeLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.google.android.exoplayer2.ui.AspectRatioFrameLayout
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.exo_playback_control_view.*



class MainActivity : AppCompatActivity() {
    var adapter:VideoAdapter? = null
    private enum class PlayMode { SINGLE_VIDEO, PLAY_LIST }

    var fullscreen : Boolean = false





    @SuppressLint("SourceLockedOrientationActivity")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        player_view.setResizeMode(AspectRatioFrameLayout.RESIZE_MODE_FIT)

        exo_fullscreen_icon.setOnClickListener {

                toggleScreen(fullscreen)




        }
        startDemoOfAndroidPlayer()
        toggleScreen(!fullscreen)


    }

    @SuppressLint("SourceLockedOrientationActivity")
    override fun onStart() {
        super.onStart()


    }

    private var playMode = PlayMode.PLAY_LIST

    private fun startDemoOfAndroidPlayer(){

        var lstVideo :MutableList<Video> = ArrayList()

        lstVideo.add(Video("Big Buck Bunny","https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4"))
        lstVideo.add(Video("Elephant Dream","https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ElephantsDream.mp4"))
        lstVideo.add(Video("For Bigger Blazes","https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ForBiggerBlazes.mp4"))
        lstVideo.add(Video("For Bigger Escape","https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ForBiggerEscapes.mp4"))
        lstVideo.add(Video("For Bigger Fun","https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ForBiggerFun.mp4"))
        lstVideo.add(Video("For Bigger Joyrides","https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ForBiggerJoyrides.mp4"))
        lstVideo.add(Video("For Bigger Meltdowns","https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ForBiggerJoyrides.mp4"))
        lstVideo.add(Video("Sintel","https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/Sintel.mp4"))
        lstVideo.add(Video("Subaru Outback On Street And Dirt","https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/SubaruOutbackOnStreetAndDirt.mp4"))
        adapter = VideoAdapter(lstVideo.toList())
        recVideo.adapter = adapter
        val videoUrl = "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ElephantsDream.mp4"
        val uri = Uri.parse(videoUrl)
        var lstUrl : MutableList<Uri> =  ArrayList()
        lstVideo.forEach{
            lstUrl.add(Uri.parse(it.url))
        }
        val uriList =lstUrl.toTypedArray()

        when(playMode){
            PlayMode.SINGLE_VIDEO -> playVideo(uri)
            PlayMode.PLAY_LIST -> playVideoList(uriList)
        }


    }

    private fun playVideo(uri: Uri){
        //Here the videoView is
        MediaPlayer(this, player_view).play(uri)
    }

    private fun playVideoList(uriList: Array<Uri>){
        MediaPlayer(this, player_view).play(uriList)
    }



    @SuppressLint("SourceLockedOrientationActivity")
    fun toggleScreen(flg : Boolean){
        if(flg){
        exo_fullscreen_icon.setImageDrawable(
            ContextCompat.getDrawable(
                this@MainActivity,
                R.drawable.ic_fullscreen_expand
            )
        )
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_VISIBLE
        if (supportActionBar != null) {
            supportActionBar!!.show()
        }
        this.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        val params =
            player_view.layoutParams as FrameLayout.LayoutParams
        params.width = ViewGroup.LayoutParams.MATCH_PARENT
        params.height =
            (200 * applicationContext.resources.displayMetrics.density).toInt()
        player_view.layoutParams = params
            recVideo.visibility = View.VISIBLE
        }else{
            exo_fullscreen_icon.setImageDrawable(
                ContextCompat.getDrawable(
                    this@MainActivity,
                    R.drawable.ic_fullscreen_skrink
                )
            )
            window.decorView.systemUiVisibility = (SYSTEM_UI_FLAG_FULLSCREEN
                    or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                    or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION)
            if (supportActionBar != null) {
                supportActionBar!!.hide()
            }
            requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
            val params =
                player_view.layoutParams as FrameLayout.LayoutParams
            params.width = ViewGroup.LayoutParams.MATCH_PARENT
            params.height = ViewGroup.LayoutParams.MATCH_PARENT
            player_view.layoutParams = params
            recVideo.visibility = View.GONE

        }
        fullscreen = !flg
    }



}

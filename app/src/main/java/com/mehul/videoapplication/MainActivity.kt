package com.mehul.videoapplication


import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.ActivityInfo
import android.content.res.Configuration
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import android.view.View.SYSTEM_UI_FLAG_FULLSCREEN
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.amazonaws.auth.CognitoCachingCredentialsProvider
import com.amazonaws.mobile.client.AWSMobileClient
import com.amazonaws.mobileconnectors.s3.transferutility.*
import com.amazonaws.regions.Region
import com.amazonaws.regions.Regions
import com.amazonaws.services.s3.AmazonS3Client
import com.amazonaws.services.s3.internal.Constants
import com.amazonaws.services.s3.model.ListObjectsRequest
import com.amazonaws.services.s3.model.ObjectListing
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.exo_playback_control_view.*
import java.io.File
import java.lang.Thread.sleep
import java.util.*
import kotlin.collections.ArrayList


class MainActivity : AppCompatActivity() {
    var adapter: VideoAdapter? = null
    var url = ""
    private enum class PlayMode { SINGLE_VIDEO, PLAY_LIST }

    var fullscreen: Boolean = false
    lateinit var mediaPlayer: MediaPlayer
    private var IsAuto: Boolean = false
    var TAG = "S3"

    @SuppressLint("SourceLockedOrientationActivity")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        //  player_view.resizeMode = AspectRatioFrameLayout.RESIZE_MODE_FIT
        applicationContext.startService(
            Intent(
                applicationContext,
                TransferService::class.java
            )
        )

        var r = Runnable {


            val credentialsProvider =
                CognitoCachingCredentialsProvider(
                    applicationContext,
                    "ap-south-1:846c13bc-fc00-4027-a9c1-27c2257bc433",  // Identity pool ID
                    Regions.AP_SOUTH_1 // Region
                )

           // credentialsProvider.
            var au = AWSMobileClient.getInstance().identityId
            val s3 = AmazonS3Client(AWSMobileClient.getInstance().awsCredentials, Region.getRegion("ap-south-1"))
            val objectListing: ObjectListing = s3.listObjects(
                ListObjectsRequest("video-app154857-dev", "", null, null, null)
                    .withEncodingType(Constants.URL_ENCODING)
            )
            //      url =  s3.getResourceUrl("video-app154857-dev","BigBuckBunny.mp4")
            var dt = Date()
            val c = Calendar.getInstance()
            c.time = dt
            c.add(Calendar.DATE, 1)
            dt = c.time
            var urlS = s3.getSignerByURI(s3.getUrl("video-app154857-dev","BigBuckBunny.mp4").toURI())
            var uu = s3.getUrl("video-app154857-dev","BigBuckBunny.mp4").toURI()
        //    var uhk = AmazonS3Client(AWSMobileClient.getInstance().awsCredentials, Region.getRegion("ap-south-1")).getSignerByURI()

            var u =  s3.getUrl("video-app154857-dev","BigBuckBunny.mp4")//,dt)
            var u2 =  s3.getUrl("video-app154857-dev","BigBuckBunny.mp4").authority//,dt)
            var u3 =  s3.getUrl("video-app154857-dev","BigBuckBunny.mp4").userInfo//,dt)
            url=  urlS.toString()  //u.toString()


            var obj =s3.getObject("video-app154857-dev","BigBuckBunny.mp4")
            var lst = objectListing.bucketName
        }

        Thread(r).start()


        exo_fullscreen_icon.setOnClickListener {
            IsAuto = false
            toggleScreen(fullscreen, false)

        }

        sleep(2000)
        startDemoOfAndroidPlayer()
        IsAuto = true
        toggleScreen(fullscreen, IsAuto)



    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        if (!IsAuto) {
            IsAuto = true
            Handler().postDelayed({
                requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED
            }, 1000 * 15)
            return
        }
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {

            toggleScreen(true, isAuto = true)

            Toast.makeText(this, "ORIENTATION LANDSCAPE", Toast.LENGTH_LONG).show()
        } else {
            toggleScreen(false, isAuto = true)
            Toast.makeText(this, "ORIENTATION PORTRAIT", Toast.LENGTH_LONG).show()


        }
        IsAuto = true

        //requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED

    }

    @SuppressLint("SourceLockedOrientationActivity")
    override fun onStart() {
        super.onStart()


    }

    private var playMode = PlayMode.PLAY_LIST

    private fun startDemoOfAndroidPlayer() {


        var lstVideo: MutableList<Video> = ArrayList()

        lstVideo.add(
            Video(
                "Big Buck Bunny",
                url
            )
        )
        lstVideo.add(
            Video(
                "Elephant Dream",
                "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ElephantsDream.mp4"
            )
        )
        lstVideo.add(
            Video(
                "For Bigger Blazes",
                "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ForBiggerBlazes.mp4"
            )
        )
        lstVideo.add(
            Video(
                "For Bigger Escape",
                "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ForBiggerEscapes.mp4"
            )
        )
        lstVideo.add(
            Video(
                "For Bigger Fun",
                "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ForBiggerFun.mp4"
            )
        )
        lstVideo.add(
            Video(
                "For Bigger Joyrides",
                "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ForBiggerJoyrides.mp4"
            )
        )
        lstVideo.add(
            Video(
                "For Bigger Meltdowns",
                "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ForBiggerJoyrides.mp4"
            )
        )
        lstVideo.add(
            Video(
                "Sintel",
                "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/Sintel.mp4"
            )
        )
        lstVideo.add(
            Video(
                "Subaru Outback On Street And Dirt",
                "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/SubaruOutbackOnStreetAndDirt.mp4"
            )
        )

        val videoUrl = url
        val uri = Uri.parse(videoUrl)
        val lstUrl: MutableList<Uri> = ArrayList()
        lstVideo.forEach {
            lstUrl.add(Uri.parse(it.url))
        }
        val uriList = lstUrl.toTypedArray()

        when (playMode) {
            PlayMode.SINGLE_VIDEO -> playVideo(uri)
            PlayMode.PLAY_LIST -> playVideoList(uriList)
        }
        adapter = VideoAdapter(lstVideo.toList(), mediaPlayer,this)
        recVideo.adapter = adapter

    }

    private fun playVideo(uri: Uri) {
        //Here the videoView is
        MediaPlayer(this, player_view).play(uri)
    }

    private fun playVideoList(uriList: Array<Uri>) {

        mediaPlayer = MediaPlayer(this, player_view)
        mediaPlayer.play(uriList)

    }


    @SuppressLint("SourceLockedOrientationActivity")
    fun toggleScreen(flg: Boolean, isAuto: Boolean) {
        if (!flg) {
            exo_fullscreen_icon.setImageDrawable(
                ContextCompat.getDrawable(
                    this@MainActivity,
                    R.drawable.baseline_fullscreen_white_36
                )
            )
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_VISIBLE
            if (supportActionBar != null) {
                supportActionBar!!.show()
            }
            if (!isAuto)
                requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
            requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT


            val params =
                player_view.layoutParams as FrameLayout.LayoutParams
            params.width = ViewGroup.LayoutParams.MATCH_PARENT
            params.height =
                (200 * applicationContext.resources.displayMetrics.density).toInt()
            player_view.layoutParams = params
            recVideo.visibility = View.VISIBLE
        } else {
            exo_fullscreen_icon.setImageDrawable(
                ContextCompat.getDrawable(
                    this@MainActivity,
                    R.drawable.baseline_fullscreen_exit_white_36
                )
            )
            window.decorView.systemUiVisibility = (SYSTEM_UI_FLAG_FULLSCREEN
                    or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                    or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION)
            if (supportActionBar != null) {
                supportActionBar!!.hide()
            }
            if (!isAuto)
                requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE


            val params =
                player_view.layoutParams as FrameLayout.LayoutParams
            params.width = ViewGroup.LayoutParams.MATCH_PARENT
            params.height = ViewGroup.LayoutParams.MATCH_PARENT
            player_view.layoutParams = params
            recVideo.visibility = View.GONE

        }
        if (isAuto)
            requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED




        fullscreen = !flg
    }

    private fun downloadWithTransferUtility() {
        val transferUtility: TransferUtility = TransferUtility.builder()
            .context(applicationContext)
            .awsConfiguration(AWSMobileClient.getInstance().configuration)
            .s3Client(AmazonS3Client(AWSMobileClient.getInstance()))
            .build()
        val downloadObserver: TransferObserver = transferUtility.download(
            "BigBuckBunny.mp4",
            File(applicationContext.filesDir, "download.mp4")
        )

        // Attach a listener to the observer to get state update and progress notifications
        downloadObserver.setTransferListener(object : TransferListener {
           override fun onStateChanged(id: Int, state: TransferState) {
                if (TransferState.COMPLETED === state) {
                    // Handle a completed upload.
                }
            }

            override fun onProgressChanged(
                id: Int,
                bytesCurrent: Long,
                bytesTotal: Long
            ) {
                val percentDonef =
                    bytesCurrent.toFloat() / bytesTotal.toFloat() * 100
                val percentDone = percentDonef.toInt()
                Log.d(
                    "Your Activity",
                    "   ID:$id   bytesCurrent: $bytesCurrent   bytesTotal: $bytesTotal $percentDone%"
                )
            }

           override fun onError(id: Int, ex: java.lang.Exception?) {
                // Handle errors
            }
        })

        // If you prefer to poll for the data, instead of attaching a
        // listener, check for the state and progress in the observer.
        if (TransferState.COMPLETED === downloadObserver.state) {
            // Handle a completed upload.
        }
        Log.d(
            "Your Activity",
            "Bytes Transferred: " + downloadObserver.bytesTransferred
        )
        Log.d("Your Activity", "Bytes Total: " + downloadObserver.bytesTotal)
    }
}

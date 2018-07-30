package com.junlong0716.musicplayerdemo

import android.annotation.SuppressLint
import android.content.ComponentName
import android.content.Intent
import android.content.ServiceConnection
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.IBinder
import android.util.Log
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), PlayMusicCallback {
    private var musicService: MusicService? = null
    private var mPlayerManager: PlayerManager? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val intent = Intent(this, MusicService::class.java)
        bindService(intent, conn, BIND_AUTO_CREATE)

        bt_play.setOnClickListener {
            mPlayerManager!!.playMusic("http://www.ytmp3.cn/down/50029.mp3")
        }

        bt_go.setOnClickListener {
            startActivity(Intent(this, MusicDetailActivity::class.java))
        }

        bt_kill.setOnClickListener {
            //musicService!!.stopSelf()
        }

    }

    private var conn = object : ServiceConnection {
        override fun onServiceDisconnected(name: ComponentName?) {

        }

        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            val myBinder = service as (MusicService.MyBinder)
            musicService = myBinder.getService()
            mPlayerManager = musicService!!.getPlayerManager()
            mPlayerManager!!.addListenerCallback(this@MainActivity)
        }
    }

    override fun onPlayMusicComplete() {

    }


    override fun onPlayerMediaPrepared() {
        Toast.makeText(this, "缓冲完成", Toast.LENGTH_SHORT).show()
    }

    @SuppressLint("LongLogTag")
    override fun onPlayerCurrentPosition(currentPosition: Int) {
        Log.i("currentPosition--MainActivity", currentPosition.toString())
    }

    override fun onDestroy() {
        super.onDestroy()
        if (mPlayerManager != null) {
            mPlayerManager!!.removeListenerCallback(this)
            mPlayerManager = null
        }
        unbindService(conn)
    }
}

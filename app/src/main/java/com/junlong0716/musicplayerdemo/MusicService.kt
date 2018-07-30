package com.junlong0716.musicplayerdemo

import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.IBinder

/**
 *@author: EdsionLi
 *@description:
 *@date: Created in 2018/7/30 下午4:55
 *@modified by:
 */
class MusicService : Service() {
    private var myBinder: MyBinder = MyBinder()
    private var playerManager: PlayerManager? = null

    inner class MyBinder : Binder() {
        fun getService(): MusicService {
            return this@MusicService
        }
    }

    override fun onBind(intent: Intent?): IBinder {
        return myBinder
    }

    override fun onCreate() {
        super.onCreate()
        if (playerManager == null){
            playerManager = PlayerManager.getInstance(this)!!
        }
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        return START_NOT_STICKY
    }

    fun getPlayerManager(): PlayerManager {
        return playerManager!!
    }

    override fun onDestroy() {
        super.onDestroy()
        playerManager!!.onReleaseEverything()
    }
}
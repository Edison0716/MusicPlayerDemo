package com.junlong0716.musicplayerdemo

import android.media.MediaPlayer
import android.util.Log
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import java.util.concurrent.TimeUnit

/**
 *@author: EdsionLi
 *@description:
 *@date: Created in 2018/7/30 下午5:05
 *@modified by:
 */
class PlayerManager private constructor(musicService: MusicService) {
    private var mediaPlayer: MediaPlayer? = null
    private var mMusicService = musicService
    private var mPlayMusicCallbackList: ArrayList<PlayMusicCallback> = ArrayList()
    private var disposable: Disposable? = null

    companion object {
        @Volatile
        var instance: PlayerManager? = null

        fun getInstance(musicService: MusicService): PlayerManager? {
            if (instance == null) {
                synchronized(PlayerManager::class) {
                    if (instance == null) {
                        instance = PlayerManager(musicService)
                    }
                }
            }
            return instance
        }
    }

    init {

        if (mediaPlayer == null) {
            mediaPlayer = MediaPlayer()
        }

        //是否循环播放
        mediaPlayer!!.isLooping = false

        //音乐播放监听
        mediaPlayer!!.setOnCompletionListener {

            for (i in 0 until mPlayMusicCallbackList.size){
                mPlayMusicCallbackList[i].onPlayMusicComplete()
            }

        }

        //音乐缓冲完成
        mediaPlayer!!.setOnPreparedListener {
            mediaPlayer!!.start()
            for (i in 0 until mPlayMusicCallbackList.size){
                mPlayMusicCallbackList[i].onPlayerMediaPrepared()
            }
        }

        if (disposable != null && !disposable!!.isDisposed) {
            disposable!!.dispose()
        }

        //获取当前播放位置
        disposable = Observable.interval(1, TimeUnit.SECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    for (i in 0 until mPlayMusicCallbackList.size){
                        mPlayMusicCallbackList[i].onPlayerCurrentPosition(mediaPlayer!!.currentPosition)
                    }
                }


    }

    //添加监听回调
    fun addListenerCallback(playMusicCallback: PlayMusicCallback) {
        mPlayMusicCallbackList.add(playMusicCallback)
    }

    //移除监听回调
    fun removeListenerCallback(playMusicCallback: PlayMusicCallback){
        mPlayMusicCallbackList.remove(playMusicCallback)
    }

    //继续播放音乐
    fun playMusic() {
        mediaPlayer!!.start()
    }

    //播放一条音乐
    fun playMusic(musicRes: String) {
        mediaPlayer!!.reset()
        mediaPlayer!!.setDataSource(musicRes)
        mediaPlayer!!.prepareAsync()
    }

    //播放一组音乐
    fun playMusic(musicRes: ArrayList<String>) {

    }

    //停止播放
    fun stopMusic() {
        mediaPlayer!!.stop()
    }

    //暂停播放
    fun pauseMusic() {
        mediaPlayer!!.pause()
    }

    //获取音频总长度
    fun getMusicDuration(): Int {
        return mediaPlayer!!.duration
    }

    //是否播放
    fun getCurrentMediaplayerState(): Boolean {
        return mediaPlayer!!.isPlaying
    }

    //释放资源
    fun onReleaseEverything() {
        if (!disposable!!.isDisposed) {
            disposable!!.dispose()
        }

        stopMusic()

        if (mediaPlayer != null) {
            mediaPlayer = null
        }
    }
}
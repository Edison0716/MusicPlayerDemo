package com.junlong0716.musicplayerdemo

/**
 *@author: EdsionLi
 *@description:
 *@date: Created in 2018/7/30 下午5:15
 *@modified by:
 */
interface PlayMusicCallback {

    //音乐播放完成
    fun onPlayMusicComplete()

    //音乐播放缓冲完成
    fun onPlayerMediaPrepared()

    //获取当前播放位置
    fun onPlayerCurrentPosition(currentPosition:Int)
}
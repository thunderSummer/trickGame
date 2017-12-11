package com.example.trickgame.listener

import android.app.Activity

/**
 * Created by thunder on 17-12-3.
 */
interface GameControllerListener {
    fun soundEffectPlay()
    fun success()
    fun lose()
    fun start()
    fun showScore(score:Int)
}
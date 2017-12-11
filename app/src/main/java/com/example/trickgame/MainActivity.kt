package com.example.trickgame

import android.media.AudioManager
import android.media.SoundPool
import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.util.Log
import android.view.View
import android.widget.*
import com.example.trickgame.listener.GameControllerListener
import com.example.trickgame.view.GameView

class MainActivity : AppCompatActivity(),View.OnClickListener {
    override fun onClick(v: View?) {
        if (v==null) return
        when(v.id){
            R.id.start_stop_btn -> {
                if (!isFinish){
                    gameView!!.startAndStopGame()
                    isStart=gameView!!.startOrNot
                    if (isStart){
                        soundPool.play(map[2]!!,1.0f,1.0f,1,0,1f)
                        startStopBtn!!.setImageResource(R.drawable.pause)
                    }else{
                        startStopBtn!!.setImageResource(R.drawable.start)
                    }
                }else{
                    startStopBtn!!.setImageResource(R.drawable.pause)
                    scoreTV!!.text="0"
                    soundPool.play(map[2]!!,1.0f,1.0f,1,0,1f)
                    gameView!!.restart()
                }
                }


        }
    }
    private val soundPool: SoundPool = SoundPool(2, AudioManager.STREAM_MUSIC,0)
    private val map = HashMap<Int,Int>()

    private var gameView:GameView? = null
    private var startStopBtn: ImageView?=null
    private var scoreTV:TextView?=null
    private var isStart=true
    private var isFinish=false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
//        map.put(1,soundPool.load(this,R.raw.bgm,1))
        map.put(2,soundPool.load(this,R.raw.readygo,3))
        map.put(3,soundPool.load(this,R.raw.score,2))
        gameView=findViewById(R.id.game_view)
        gameView!!.init(GameListener())
        startStopBtn= findViewById(R.id.start_stop_btn)
        startStopBtn!!.setOnClickListener(this)
        scoreTV=findViewById(R.id.score)
        scoreTV!!.text="0"
        soundPool.setOnLoadCompleteListener({soundPool, sampleId, status ->
           // soundPool.play(map[1]!!,1.0f,1.0f,1,-1,1f)
            soundPool.play(map[2]!!,1.0f,1.0f,1,0,1f)
        })
    }
    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        if (hasFocus && Build.VERSION.SDK_INT >= 19) {
            val decorView = window.decorView
            decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    or View.SYSTEM_UI_FLAG_FULLSCREEN
                    or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY)
        }

    }
    override fun onBackPressed() {
        if (isStart) {
            gameView!!.startAndStopGame()
            isStart = gameView!!.startOrNot
            startStopBtn!!.setImageResource(R.drawable.start)

            val dialog = AlertDialog.Builder(this).setTitle("提示")
                    .setNegativeButton("取消") { dialog, _ ->
                        dialog.cancel()
                    }
                    .setPositiveButton("确定") { dialog, _ ->
                        dialog.cancel()
                        finish()
                    }
                    .setMessage("确认退出游戏？").create()
            dialog.show()
        }
        else{
            finish()
        }
    }
    fun showDia(string:String){
        val dialog = AlertDialog.Builder(this!!).setTitle("提示")
                .setNegativeButton("取消"){
                    dialog, _ ->
                    dialog.cancel()
                }
                .setPositiveButton("确定"){
                    dialog, _ ->
                    dialog.cancel()
                    startStopBtn!!.setImageResource(R.drawable.pause)
                    isFinish=false
                    scoreTV!!.text="0"
                    gameView!!.restart()

                }
                .setMessage(string).create()
        dialog.show()
    }
    override fun onDestroy() {
        super.onDestroy()
        soundPool.stop(map[2]!!)
        soundPool.stop(map[3]!!)

    }
    inner class GameListener:GameControllerListener{
        override fun showScore(score: Int) {
            scoreTV!!.text=score.toString()
        }

        override fun success() {
            startStopBtn!!.setImageResource(R.drawable.restart)
            isFinish=true
            showDia("恭喜,闯关成功,是否继续?")
        }

        override fun lose() {
            startStopBtn!!.setImageResource(R.drawable.restart)
            isFinish=true
            showDia("抱歉,游戏结束,是否继续?")
        }

        override fun start() {
        }

        override fun soundEffectPlay() {
            soundPool.play(map[3]!!,1.0f,1.0f,1,0,1f)
        }

    }
}



package com.example.trickgame

import android.graphics.Color
import android.media.AudioManager
import android.media.SoundPool
import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.annotation.RequiresApi
import android.view.View
import android.widget.FrameLayout
import com.example.trickgame.util.startActivity
import android.content.DialogInterface
import android.support.v7.app.AlertDialog
import android.widget.Toast


class HelloActivity : AppCompatActivity(),View.OnClickListener{
    private val soundPool:SoundPool= SoundPool(2,AudioManager.STREAM_MUSIC,0)
    private val map = HashMap<Int,Int>()

    override fun onClick(v: View?) {
        when(v!!.id){
            R.id.top_hello->{
            }
            R.id.start_hello->startActivity<MainActivity>()
            R.id.about_hello->{}
        }
    }
    override fun onBackPressed() {
        val dialog = AlertDialog.Builder(this).setTitle("提示")
                .setNegativeButton("取消"){
                    dialog, _ ->
                    dialog.cancel()
                }
                .setPositiveButton("确定"){
                    dialog, _ ->
                    dialog.cancel()
                    finish()
                }
                .setMessage("确认退出游戏？").create()
        dialog.show()
    }
    private lateinit var startGame:FrameLayout
    private lateinit var aboutGame:FrameLayout
    private lateinit var leaderBoards:FrameLayout
    @RequiresApi(Build.VERSION_CODES.JELLY_BEAN)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_hello)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.statusBarColor = Color.TRANSPARENT
        }
        map.put(2,soundPool.load(this,R.raw.open,2))
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
        startGame=findViewById(R.id.start_hello)
        aboutGame=findViewById(R.id.about_hello)
        leaderBoards=findViewById(R.id.top_hello)
        startGame.setOnClickListener(this)
        aboutGame.setOnClickListener(this)
        leaderBoards.setOnClickListener(this)
        soundPool.setOnLoadCompleteListener { soundPool, sampleId, status ->
            soundPool.play(map[2]!!,1.0f,1.0f,1,0,1f)
        }

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
}

package com.example.trickgame.view

import android.content.Context
import android.graphics.*
import android.os.Handler
import android.os.Message
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.widget.Toast
import com.example.trickgame.MyApplication
import com.example.trickgame.listener.GameControllerListener
import com.example.trickgame.modle.Block
import com.example.trickgame.modle.BlockSet
import com.example.trickgame.modle.Grid
import com.example.trickgame.util.*
import android.R.attr.bitmap
import android.graphics.BitmapFactory
import android.graphics.Bitmap
import android.support.v7.app.AlertDialog
import com.example.trickgame.R


/**
 * Created by thunder on 17-12-2.
 *
 */
class GameView : View{
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int): super(context, attrs, defStyleAttr)
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    private var blockSet:BlockSet? =null
    private var xLength:Float= 0f
    private var yLength:Float= 0f
    private var bottomPaint = Paint()
    private var fontPaint = Paint()
    private var touchX=0f
    private var touchY=0f
    var startOrNot=true
    private var speedUp=100L
    private var touchT=0L
    var win:Boolean =false
    private var gameWidth=0f
    private val grid = Grid
    private var handle:GameHandle = GameHandle()
    private var gameListener:GameControllerListener?=null
    var speed:Speed=Speed.NORMAL
    lateinit var bitmap0:Bitmap
    lateinit var bitmap1:Bitmap
    lateinit var bitmap2:Bitmap
    lateinit var bitmap3:Bitmap
    var timeInt=speed.value.toLong()
    enum class Speed(var value: Int){
        HIGH(200),NORMAL(500),SLOW(1000)
    }
    companion object {
        val START_DOWN:Int =0
        val STOP_DOWN=1
    }
    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        if (canvas==null||win){
            return
        }
        yLength=(height/ yNum).toFloat()
        bottomPaint.style=Paint.Style.FILL
        fontPaint.style=Paint.Style.STROKE
        fontPaint.color=Color.BLACK
        fontPaint.textSize= spToPx(16f)
        blockSet?.blockList?.forEach {
            drawBrick(canvas,it)
        }
        for (i in 0 until xNum){
            (0 until yNum)
                    .filter { grid.gridMap[i][it]!=null }
                    .forEach { drawBrick(canvas,grid.gridMap[i][it]!!) }
        }
    }
    fun init(listener: GameControllerListener){
        xLength= (getScreenWidth()/xNum).toFloat()

        this.gameListener=listener
         bitmap0 = BitmapFactory.decodeResource(MyApplication.context!!.resources,R.drawable.brick0 )
         bitmap1 = BitmapFactory.decodeResource(MyApplication.context!!.resources,R.drawable.brick1 )
         bitmap2 = BitmapFactory.decodeResource(MyApplication.context!!.resources,R.drawable.brick2 )
         bitmap3 = BitmapFactory.decodeResource(MyApplication.context!!.resources,R.drawable.brick3 )
        randomBlockSet()
        val message =Message()
        message.what= START_DOWN
        handle.sendMessage(message)
    }
    private fun randomBlockSet(){
        val randomInt=(Math.random()*7).toInt()
        when(randomInt){
            0 -> blockSet=BlockSet(BlockSet.BlockSetType.LINE, createValues(), createType())
            1 -> blockSet=BlockSet(BlockSet.BlockSetType.CORNER1, createValues(), createType())
            2 -> blockSet=BlockSet(BlockSet.BlockSetType.CORNER2, createValues(), createType())
            3 -> blockSet=BlockSet(BlockSet.BlockSetType.STONE, createValues(), createType())
            4 -> blockSet=BlockSet(BlockSet.BlockSetType.DISLOCATION1, createValues(), createType())
            5 -> blockSet=BlockSet(BlockSet.BlockSetType.DISLOCATION2, createValues(), createType())
            6 -> blockSet=BlockSet(BlockSet.BlockSetType.OTHER, createValues(), createType())
        }

    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        when(event!!.action){
            MotionEvent.ACTION_UP ->{
                if (System.currentTimeMillis()-touchT<1000){
                    when {
                        touchX-event.x>150 -> {
                            blockSet?.leftMove()
                            postInvalidate()
                        }
                        event.x-touchX>150 -> {
                            blockSet?.rightMove()
                            postInvalidate()
                        }
                        touchY-event.y>150 -> {
                            if (timeInt==speedUp){
                                timeInt=speed.value.toLong()
                            }else{
                                blockSet?.rotate()
                                blockSet?.rotateAfter()
                                postInvalidate()
                            }   

                        }
                        event.y-touchY>150 ->timeInt=speedUp
                    }

                    return true
                }
            }
            MotionEvent.ACTION_DOWN -> {
                performClick()
                touchX=event.x
                touchY=event.y
                touchT=System.currentTimeMillis()
            }


        }
        return true
    }

    override fun performClick(): Boolean {
        super.performClick()
        return false
    }
    inner class GameHandle :Handler(){
        override fun handleMessage(msg: Message?) {
            super.handleMessage(msg)
            when(msg!!.what){
                START_DOWN ->{
                    var canNext=false
                    postInvalidate()
                    val message = Message()
                    message.what= START_DOWN
                    if(blockSet==null){
                        randomBlockSet()
                    }
                    if (!blockSet!!.downMove()){
                        canNext=true
                        val array = ArrayList<Block>()
                            blockSet!!.blockList.forEach{
                               if ( grid.check(it)){
                                   gameListener!!.showScore(grid.score)
                                   array.add(it)
                                   canNext=false
                               }
                            }
                        array.forEach{
                            blockSet!!.blockList.remove(it)
                        }

                    }
                    if (canNext||blockSet!!.blockList.size==0){


                        blockSet!!.blockList.forEach{
                            try {
                                grid.addBlock(it)
                            }catch (e:Exception){
                                gameListener!!.lose()
                                handle.removeMessages(START_DOWN)
                                return
                            }

                        }
                       while(!grid.checkAll()){
                       }
                        if (grid.win()){
                            handle.removeMessages(START_DOWN)
                            gameListener!!.success()
                            return
                        }
                        blockSet=null
                        randomBlockSet()
                        timeInt=speed.value.toLong()
                        gameListener!!.showScore(grid.score)
                        gameListener!!.soundEffectPlay()
                    }
                    handle.sendMessageDelayed(message,timeInt)
                }
            }

        }
    }

    fun startAndStopGame(){
        if (!startOrNot){
            val message = Message()
            message.what= START_DOWN
            handle.sendMessage(message)
        }else
            handle.removeMessages(START_DOWN)
        startOrNot=!startOrNot
    }
    fun drawBrick(canvas: Canvas,block: Block){
        val rectF = RectF(block.x*xLength,block.y*yLength,(block.x+1)*xLength,(block.y+1)*yLength)
        canvas.drawRoundRect(rectF,10f,10f,bottomPaint)
        when(block.type){
            0 -> canvas.drawBitmap(bitmap0,Rect(0,0,bitmap0.width,bitmap0.height),rectF,bottomPaint)
            1 -> canvas.drawBitmap(bitmap1,Rect(0,0,bitmap1.width,bitmap1.height),rectF,bottomPaint)
            2 -> canvas.drawBitmap(bitmap2,Rect(0,0,bitmap2.width,bitmap2.height),rectF,bottomPaint)
            3 -> canvas.drawBitmap(bitmap3,Rect(0,0,bitmap3.width,bitmap3.height),rectF,bottomPaint)
        }
        val rect = Rect()
        fontPaint.getTextBounds(block.value.toString(),0,block.value.toString().length,rect)
        canvas.drawText(block.value.toString(),(block.x*2+1)*xLength/2-rect.width()/2,(block.y*2+1)*yLength/2+rect.height()/2,fontPaint)

    }
    fun restart(){
        grid.clear()
        randomBlockSet()
        val message =Message()
        message.what= START_DOWN
        handle.sendMessage(message)
    }
}
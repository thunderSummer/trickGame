package com.example.trickgame.view

import android.content.Context
import android.graphics.*
import android.os.Handler
import android.os.Message
import android.view.MotionEvent
import android.view.View
import android.widget.Toast
import com.example.trickgame.MyApplication
import com.example.trickgame.modle.Block
import com.example.trickgame.modle.BlockSet
import com.example.trickgame.modle.Grid
import com.example.trickgame.util.*


/**
 * Created by thunder on 17-12-2.
 *
 */
class GameView(context: Context?) : View(context) {
    private var blockSet:BlockSet? =null
    private var xLength:Float= 0f
    private var yLength:Float= 0f
    private var bottomPaint = Paint()
    private var fontPaint = Paint()
    private var touchX=0f
    private var touchY=0f
    private var touchT=0L
    private var gameWidth=0f
    private val grid = Grid
    private var handle:GameHandle = GameHandle()
    var speed:Speed=Speed.NORMAL
    var timeInt=speed.value.toLong()
    enum class Speed(var value: Int){
        HIGH(200),NORMAL(500),SLOW(1000)
    }
    companion object {
        val START_DOWN:Int =0
        val SPEED_DOWN:Int =1
    }


    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        if (canvas==null){
            return
        }
        bottomPaint.style=Paint.Style.FILL
        bottomPaint.color=Color.RED
        fontPaint.style=Paint.Style.STROKE
        fontPaint.color=Color.BLACK
        fontPaint.textSize= spToPx(16f)
        blockSet?.blockList?.forEach {
            val rectF = RectF(it.x*xLength,it.y*yLength,(it.x+1)*xLength,(it.y+1)*yLength)
            canvas.drawRoundRect(rectF,10f,10f,bottomPaint)
            val rect = Rect()
            fontPaint.getTextBounds(it.value.toString(),0,it.value.toString().length,rect)
            canvas.drawText(it.value.toString(),(it.x*2+1)*xLength/2-rect.width()/2,(it.y*2+1)*yLength/2+rect.height()/2,fontPaint)
        }
        for (i in 0 until xNum){
            for (j in 0 until yNum){
                if( grid.gridMap[i][j]!=null){

                    val tempX = grid.gridMap[i][j]!!.x
                    val tempY = grid.gridMap[i][j]!!.y
                    val tempV = grid.gridMap[i][j]!!.value
                    val rectF = RectF(tempX*xLength,(tempY)*yLength,(tempX+1)*xLength,(tempY+1)*yLength)
                    canvas.drawRoundRect(rectF,10f,10f,bottomPaint)
                    val rect = Rect()
                    fontPaint.getTextBounds(tempV.toString(),0,tempV.toString().length,rect)
                    canvas.drawText(tempV.toString(),(tempX*2+1)*xLength/2-rect.width()/2,(tempY*2+1)*yLength/2+rect.height()/2,fontPaint)
                }

            }
        }
    }
    fun init(){
        xLength= (getScreenWidth()/xNum).toFloat()
        yLength = (getScreenHeight()/ yNum).toFloat()
        randomBlockSet()
        val message =Message()
        message.what= START_DOWN
        handle.sendMessage(message)

    }
    private fun randomBlockSet(){
        var randomInt=(Math.random()*7).toInt()
        when(randomInt){
            0 -> blockSet=BlockSet(BlockSet.BlockSetType.LINE, createValues())
            1 -> blockSet=BlockSet(BlockSet.BlockSetType.CORNER1, createValues())
            2 -> blockSet=BlockSet(BlockSet.BlockSetType.CORNER2, createValues())
            3 -> blockSet=BlockSet(BlockSet.BlockSetType.STONE, createValues())
            4 -> blockSet=BlockSet(BlockSet.BlockSetType.DISLOCATION1, createValues())
            5 -> blockSet=BlockSet(BlockSet.BlockSetType.DISLOCATION2, createValues())
            6 -> blockSet=BlockSet(BlockSet.BlockSetType.OTHER, createValues())
        }
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        when(event!!.action){
            MotionEvent.ACTION_UP ->{
                if (System.currentTimeMillis()-touchT<1000){
                    when {
                        touchX-event.x>200 -> {
                            blockSet?.leftMove()
                            postInvalidate()
                        }
                        event.x-touchX>200 -> {
                            blockSet?.rightMove()
                            postInvalidate()
                        }
                        touchY-event.y>200 -> {
                            blockSet?.rotate()
                            blockSet?.rotateAfter()
                            postInvalidate()
                        }
                        event.y-touchY>200 ->timeInt=100L
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
                    }
                    if (!blockSet!!.downMove()){
                        canNext=true
                        val array = ArrayList<Block>()
                            blockSet!!.blockList.forEach{
                               if ( grid.check(it)){
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
                                Toast.makeText(MyApplication.context,"lose",Toast.LENGTH_LONG).show()
                                handle.removeMessages(START_DOWN)
                                return
                            }

                        }
                       while(!grid.checkAll()){
                       }
                        blockSet=null
                        randomBlockSet()
                        timeInt=speed.value.toLong()
                    }
                    handle.sendMessageDelayed(message,timeInt)
                }
            }

        }
    }
}
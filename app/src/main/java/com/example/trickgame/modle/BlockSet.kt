package com.example.trickgame.modle

import android.os.Handler
import android.os.Message
import android.util.Log
import com.example.trickgame.util.xNum
import com.example.trickgame.util.yNum

/**
 * Created by thunder on 17-12-1.
 *
 */
 class BlockSet private constructor(val type: BlockSetType){
    enum class BlockSetType{
        /**
        *  **** **  *     *    *       *    *
        *       **  **   **    ***   ***   ***
        *            *   *
        *    LINE,STONE,DISLOCATION1,DISLOCATION2,CORNER1,CORNER2,OTHER
         */
        LINE,STONE,DISLOCATION1,DISLOCATION2,CORNER1,CORNER2,OTHER
    }
    val blockList = arrayListOf<Block>()
    constructor(type: BlockSetType,  values:IntArray,valueType: IntArray) : this(type) {
        when(type){
            BlockSetType.LINE ->line(values,valueType)
            BlockSetType.DISLOCATION1 ->dislocation1(values,valueType)
            BlockSetType.DISLOCATION2 ->dislocation2(values,valueType)
            BlockSetType.STONE ->stone(values,valueType)
            BlockSetType.CORNER1 ->cover1(values,valueType)
            BlockSetType.CORNER2 ->cover2(values,valueType)
            BlockSetType.OTHER ->other(values,valueType)
        }
    }
    private fun line(values: IntArray,type:IntArray){
        blockList.apply {
            add(Block(xNum / 2 - 2, 0, values[0], type [0], false))
            add(Block(xNum / 2 - 1, 0, values[1], type[1], true))
            add(Block(xNum / 2, 0, values[2], type[2], false))
            add(Block(xNum / 2 + 1, 0, values[3],type[3], false))
        }
    }
    private fun stone(values: IntArray,type:IntArray){
        blockList.add(Block(xNum / 2 - 1, 0, values[0],type[0] , false))
        blockList.add(Block(xNum / 2 , 0, values[1],type[1], false))
        blockList.add(Block(xNum / 2 - 1, 1, values[2], type[2], false))
        blockList.add(Block(xNum / 2 , 1, values[3], type[3], false))
    }
    private fun dislocation1(values: IntArray,type:IntArray){
        blockList.add(Block(xNum / 2 - 1, 0, values[0], type[0], false))
        blockList.add(Block(xNum / 2 - 1, 1, values[1], type[1], true))
        blockList.add(Block(xNum / 2 , 1, values[2],type[2], false))
        blockList.add(Block(xNum / 2 , 2, values[3], type[3], false))
    }
    private fun dislocation2(values: IntArray,type:IntArray){
        blockList.add(Block(xNum / 2 , 0, values[0], type[0], false))
        blockList.add(Block(xNum / 2 , 1, values[1], type[1], true))
        blockList.add(Block(xNum / 2 - 1, 1, values[2], type[2], false))
        blockList.add(Block(xNum / 2 - 1, 2, values[3], type[3], false))
    }
    private fun cover1(values: IntArray,type:IntArray){
        blockList.add(Block(xNum / 2 - 1, 0, values[0], type[0], false))
        blockList.add(Block(xNum / 2 - 1, 1, values[1], type[1], false))
        blockList.add(Block(xNum / 2, 1, values[2],type[2], true))
        blockList.add(Block(xNum / 2 + 1, 1, values[3], type[3], false))
    }
    private fun cover2(values: IntArray,type:IntArray){
        blockList.add(Block(xNum / 2 , 0, values[0], type[0], false))
        blockList.add(Block(xNum / 2 - 2, 1, values[1], type[1], false))
        blockList.add(Block(xNum / 2 - 1, 1, values[2], type[2], true))
        blockList.add(Block(xNum / 2, 1, values[3], type[3], false))
    }
    private fun other(values: IntArray,type:IntArray){
        blockList.add(Block(xNum / 2 - 2, 0, values[1], type[0], false))
        blockList.add(Block(xNum / 2 - 1, 0, values[2], type[1], true))
        blockList.add(Block(xNum / 2 , 0, values[3], type[2], false))
        blockList.add(Block(xNum / 2 - 1, 1, values[0],type[3], false))

    }
    fun rotate():Boolean{
        if (blockList.size<4){
            return false
        }
        when(type){
            BlockSetType.STONE ->{
                Log.d("ss","rotate")
                val temp=blockList[1].value
                blockList[1].value=blockList[0].value
                blockList[0].value=blockList[2].value
                blockList[2].value=blockList[3].value
                blockList[3].value=temp
            }
            else ->{
                var centerX:Int=0
                var centerY:Int=0
                blockList.filter {
                    it.isCenter
                }.forEach {
                    centerX=it.x
                    centerY=it.y
                }
                blockList.filter{ !it.isCenter }
                        .forEach {
                            val tempX= centerX-(it.y-centerY)
                            val tempY = centerY+it.x-centerX
                            it.x= tempX
                            it.y=tempY
                }
            }

        }
        return true
    }
    fun rotateAfter(){
        for (block: Block in blockList){
            while (block.x<0){
                blockList.forEach {
                    it.x=it.x+1
                }
            }
            while (block.x>= xNum){
                blockList.forEach {
                    it.x=it.x-1
                }
            }
            while (block.y<0){
                blockList.forEach {
                    it.y=it.y+1
                }
            }
            while (block.y>= yNum){
                blockList.forEach {
                    it.y-=1
                }
            }
        }
    }
    fun downMove():Boolean{
        var canDown=true
        if (blockList.size==0){
            return false
        }
        blockList.forEach {
            if(!Grid.canDown(it))
                canDown=false
        }
        if (canDown){
            blockList.forEach {
                it.y+=1
            }
        }
        return canDown

    }
    fun rightMove():Boolean{
        if (blockList.size<4){
            return false
        }
        var canRight=true
        blockList.forEach {
            if(!Grid.canRight(it))
                canRight=false
        }
        if(canRight){
            blockList.forEach {
                it.x+=1
            }
        }
        return canRight
    }
    fun leftMove():Boolean{
        if (blockList.size<4){
            return false
        }
        var canLeft=true
        blockList.forEach {
            if(!Grid.canLeft(it))
                canLeft=false
        }
        if(canLeft){
            blockList.forEach {
                it.x-=1
            }
        }
        return canLeft
    }



}
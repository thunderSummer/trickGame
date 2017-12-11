package com.example.trickgame.modle

import android.util.Log
import com.example.trickgame.util.xNum
import com.example.trickgame.util.yNum

/**
 * Created by thunder on 17-12-1.
 *
 */
object Grid {
    var score=0
    val gridMap = Array(xNum){(Array<Block?>(yNum){ null})}
    fun addBlock(block: Block){
        block.active=false
        if (gridMap[block.x][block.y]!=null){
            throw Exception("lose")
        }else{
            gridMap[block.x][block.y]=block
        }

    }
    fun removeBlock(block: Block){
        gridMap[block.x][block.y]=null
    }
    fun check(block: Block):Boolean{
        var blockC: Block?
        if(block.y-1>=0){
            blockC = gridMap[block.x][block.y-1]
            if(blockC!=null&&blockC.value==block.value){
                scoreINC(blockC.value)
                blockC.value*=2
                blockC.active=true
                return true
            }
        }
        if(block.y+1<= yNum -1){
            blockC = gridMap[block.x][block.y+1]
            if(blockC!=null&&blockC.value==block.value){
                scoreINC(blockC.value)
                blockC.value*=2
                blockC.active=true
                return true
            }
        }
        if (block.x-1>=0){
            blockC = gridMap[block.x-1][block.y]
            if(blockC!=null&&blockC.value==block.value){
                scoreINC(blockC.value)
                blockC.value*=2
                blockC.active=true
                return true
            }
        }
        if (block.x+1<= xNum -1){
            blockC = gridMap[block.x+1][block.y]
            if(blockC!=null&&blockC.value==block.value){
                scoreINC(blockC.value)
                blockC.value*=2
                blockC.active=true
                return true
            }
        }
        return false
    }
    fun checkAll():Boolean{
        var isFinish =true
        while(true){
            var canBreak=true
            for (i in 0 until xNum){
                for (j in 0 until yNum){
                    val block = gridMap[i][j]
                    if (block!=null&& block.active){
                        if (!check(block)){
                            block.active=false
                        }else{
                            gridMap[i][j]=null
                            canBreak=false
                        }
                    }
                }
            }
            if (canBreak)
                break
        }
        for (j in yNum-2 downTo  0){


            var i=0
            while(i < xNum){
                var k =0
                val block = gridMap[i][j]
                if(block!=null){
                    for (temp in 1 until xNum-i){
                        if (gridMap[i+temp][j]==null){
                            k=temp
                            break
                        }
                    }
                    var canNextMove = false
                    if (k!=0){
                        canNextMove= (i until i+k).none { gridMap[it][j+1]!=null }
                    }
                    if (canNextMove){
                        var min =1
                        var m=2

                            for (l in i until i+k){
                                while(j+m< yNum){
                                    if(gridMap[l][j+m]!=null){
                                        break
                                    }
                                    m++
                                }
                                if (min==1){
                                    min=m
                                    m=2
                                }else{
                                    min= minOf(min,m)
                                    m=2
                                }
                            }

                        for (l in i until i+k){
                            gridMap[l][j+min-1]= gridMap[l][j]
                            gridMap[l][j+min-1]!!.x= l
                            gridMap[l][j+min-1]!!.y=j+min-1
                            gridMap[l][j+min-1]!!.active=true
                            isFinish=false
                            gridMap[l][j]=null
                        }
                    }
                    i+=k
                }
                i++
            }
        }
        return isFinish

    }
    fun canRight(block: Block)= (block.x< xNum -1 && gridMap[block.x+1][block.y]==null)
    fun canLeft(block: Block) =(block.x>0&& gridMap[block.x-1][block.y]==null)
    fun canDown(block: Block)=(block.y< yNum -1&& gridMap[block.x][block.y+1]==null)
    fun win() :Boolean{
        for (i in 0 until xNum){
            for (j in 0 until yNum){
                val temp = gridMap[i][j]?:continue
                    if (temp.value>=1024){
                        return true
                    }
            }
        }
        return false
    }
    private fun scoreINC( value: Int) {
        var temp = value
        score++
        while (temp != 1) {
            temp /= 2
            score ++

        }
    }
    fun clear(){
        score=0
        for (i in 0 until xNum)
            (0 until yNum).forEach {
                gridMap[i][it]=null
            }
    }
}
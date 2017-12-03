package com.example.trickgame.util

import java.util.*
import kotlin.collections.ArrayList

/**
 * Created by thunder on 17-12-1.
 *
 */
var xNum:Int=14
var yNum:Int=35
fun createValues():IntArray{
    val list = listOf(1,2,4,8)
    val arrayList = ArrayList<Int>()
    while (arrayList.size<4){
        val randomInt = (Math.random()*4).toInt()
        if (!arrayList.contains(randomInt)){
            arrayList.add(randomInt)
        }
    }
    return intArrayOf(list[arrayList[0]],list[arrayList[1]],list[arrayList[2]],list[arrayList[3]])
}
package com.example.trickgame.modle

/**
 * Created by thunder on 17-12-1.
 */
class Block(var x:Int, var y:Int, var value:Int, val type: BlockType, val isCenter:Boolean, var active:Boolean = true){
    enum class BlockType{
        Red
    }
    companion object {
        var blockSize:Float=50f
    }

}

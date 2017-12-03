package com.example.trickgame.util

import com.example.trickgame.MyApplication

/**
 * Created by thunder on 17-12-1.
 */
fun getScreenWidth() = MyApplication.context!!.resources.displayMetrics.widthPixels
fun getScreenHeight() = MyApplication.context!!.resources.displayMetrics.heightPixels
fun dpToPx(dp: Float): Float = MyApplication.context!!.resources.displayMetrics.density * dp
fun spToPx(sp: Float): Float = sp * MyApplication.context!!.resources.displayMetrics.scaledDensity


package com.adityaaugusta.githubuser.utils

import android.view.View

fun showView(show: Boolean, viewSuccess: View,viewFailed:View){
    if (show){
        viewSuccess.visibility = View.VISIBLE
        viewFailed.visibility = View.GONE
    }else{
        viewSuccess.visibility = View.GONE
        viewFailed.visibility = View.VISIBLE
    }
}

fun Int.divideNumberOneThousand():String{
    return if(this>1000){
        val result = this/1000
        "$result K"
    }else{
        this.toString()
    }
}
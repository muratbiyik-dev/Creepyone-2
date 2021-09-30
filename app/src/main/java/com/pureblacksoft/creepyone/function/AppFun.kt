package com.pureblacksoft.creepyone.function

import android.content.Context
import android.view.Gravity
import android.widget.Toast

class AppFun
{
    companion object {
        fun showToast(context: Context, string: Int, length: Int) {
            val backToast = Toast.makeText(context, string, length)
            backToast.setGravity(Gravity.BOTTOM, 0, 160)
            backToast.show()
        }
    }
}

//PureBlack Software / Murat BIYIK
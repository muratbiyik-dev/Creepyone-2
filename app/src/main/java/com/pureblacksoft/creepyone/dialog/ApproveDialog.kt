package com.pureblacksoft.creepyone.dialog

import android.app.AlertDialog
import android.content.Context
import android.view.ContextThemeWrapper
import com.pureblacksoft.creepyone.R

class ApproveDialog
{
    companion object {
        fun alertBuilder(context: Context): AlertDialog.Builder {
            return AlertDialog.Builder(ContextThemeWrapper(context, R.style.AlertDialogTheme))
        }
    }
}

//PureBlack Software / Murat BIYIK
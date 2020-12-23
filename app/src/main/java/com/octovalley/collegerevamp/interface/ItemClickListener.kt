package com.octovalley.collegerevamp.`interface`

import android.view.View
import java.text.FieldPosition

open interface ItemClickListener {
    fun onClick(view : View? , position: Int , isLongClick : Boolean )
}
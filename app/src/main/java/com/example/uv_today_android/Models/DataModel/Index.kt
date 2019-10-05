package com.example.uv_today_android.Models.DataModel

import android.content.Context
import com.example.uv_today_android.R


typealias Index = Int

fun Index.getAssociatedDescriptionFromContext(context: Context): String {
    return when (this) {
        0, 1, 2 -> context.getString(R.string.low_uv_desc)
        3, 4, 5 -> context.getString(R.string.middle_uv_desc)
        6, 7 -> context.getString(R.string.high_uv_desc)
        8, 9, 10 -> context.getString(R.string.very_high_uv_desc)
        11, 12, 13, 14 -> context.getString(R.string.extreme_uv_desc)
        else -> context.getString(R.string.error_desc)
    }.toString()
}
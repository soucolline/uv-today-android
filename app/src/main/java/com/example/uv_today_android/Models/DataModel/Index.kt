package com.example.uv_today_android.Models.DataModel

import android.content.Context
import androidx.core.content.ContextCompat
import com.example.uv_today_android.R


typealias Index = Int

fun Index.getAssociatedColorFromContext(context: Context): Int {
    return when (this) {
        0 -> ContextCompat.getColor(context, R.color.app_blue)
        1, 2 -> ContextCompat.getColor(context, R.color.app_green)
        3, 4, 5 -> ContextCompat.getColor(context, R.color.app_yellow)
        6, 7 -> ContextCompat.getColor(context, R.color.app_light_red)
        8, 9, 10 -> ContextCompat.getColor(context, R.color.app_dark_red)
        11, 12, 13, 14 -> ContextCompat.getColor(context, R.color.app_purple)
        else -> ContextCompat.getColor(context, R.color.app_black)
    }
}

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
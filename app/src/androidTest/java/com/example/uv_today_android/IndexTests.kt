package com.example.uv_today_android

import androidx.test.platform.app.InstrumentationRegistry
import com.example.uv_today_android.Models.DataModel.getAssociatedColorFromContext
import com.example.uv_today_android.Models.DataModel.getAssociatedDescriptionFromContext
import junit.framework.TestCase.assertEquals
import org.junit.Test

class IndexTests {
    @Test
    fun testGetColorFromIndex() {
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext

        for(index in 0..15) {
            when(index) {
                0 -> assertEquals(index.getAssociatedColorFromContext(appContext), appContext.getColor(R.color.app_blue))
                1, 2 -> assertEquals(index.getAssociatedColorFromContext(appContext), appContext.getColor(R.color.app_green))
                3, 4, 5 -> assertEquals(index.getAssociatedColorFromContext(appContext), appContext.getColor(R.color.app_yellow))
                6, 7 -> assertEquals(index.getAssociatedColorFromContext(appContext), appContext.getColor(R.color.app_light_red))
                8, 9, 10 -> assertEquals(index.getAssociatedColorFromContext(appContext), appContext.getColor(R.color.app_dark_red))
                11, 12, 13, 14 -> assertEquals(index.getAssociatedColorFromContext(appContext), appContext.getColor(R.color.app_purple))
                else -> assertEquals(index.getAssociatedColorFromContext(appContext), appContext.getColor(R.color.app_black))
            }

        }
    }

    @Test
    fun testGetDescriptionFromIndex() {
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext

        for(index in 0..15) {
            when(index) {
                0, 1, 2 -> assertEquals(index.getAssociatedDescriptionFromContext(appContext), appContext.getString(R.string.low_uv_desc))
                3, 4, 5 -> assertEquals(index.getAssociatedDescriptionFromContext(appContext), appContext.getString(R.string.middle_uv_desc))
                6, 7 -> assertEquals(index.getAssociatedDescriptionFromContext(appContext), appContext.getString(R.string.high_uv_desc))
                8, 9, 10 -> assertEquals(index.getAssociatedDescriptionFromContext(appContext), appContext.getString(R.string.very_high_uv_desc))
                11, 12, 13, 14 -> assertEquals(index.getAssociatedDescriptionFromContext(appContext), appContext.getString(R.string.extreme_uv_desc))
                else -> assertEquals(index.getAssociatedDescriptionFromContext(appContext), appContext.getString(R.string.error_desc))
            }

        }
    }

}
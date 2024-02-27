package com.n0stalgiaultra.utils

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

fun addWatermark(bitmap: Bitmap): Bitmap{
    val canvas = Canvas(bitmap)
    val paint = Paint(Paint.ANTI_ALIAS_FLAG)

    paint.color = Color.YELLOW
    paint.textSize = 100f

    val currentDateTime = SimpleDateFormat("yyyy/MM/dd HH:mm:ss", Locale.getDefault())
        .format(Date(System.currentTimeMillis()))

    val x = bitmap.width - 900f
    val y = bitmap.height - 20f

    canvas.drawText(
        currentDateTime,
        x, y,
        paint
    )

    return bitmap
}
package com.n0stalgiaultra.utils

import android.graphics.Bitmap
import android.graphics.Matrix

fun adjustBitmap(bitmap: Bitmap) : Bitmap{
    val matrix = Matrix()
    matrix.postRotate(90F)
    val scaledBitmap = Bitmap.createScaledBitmap(bitmap, bitmap.width, bitmap.height, true)
    val rotatedBitmap = Bitmap.createBitmap(
        scaledBitmap,
        0, 0,
        scaledBitmap.width,
        scaledBitmap.height,
        matrix, true)

    bitmap.recycle()
    scaledBitmap.recycle()
    return rotatedBitmap
}
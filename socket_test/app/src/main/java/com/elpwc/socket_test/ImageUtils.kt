package com.elpwc.socket_test

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import java.io.ByteArrayOutputStream


object ImageUtils {
    /**
     * 压缩图片方法1
     */
    fun  bitmapCom1(data: ByteArray) : ByteArray {
        var bitmap: Bitmap
        bitmap = bytes2Bitmap(data)!!
        var baos = ByteArrayOutputStream(1024*1024)
        try {
            var quality = 10
            bitmap.compress(Bitmap.CompressFormat.JPEG, quality, baos)
        }catch (e: Exception){
            e.printStackTrace()
        }
        return baos.toByteArray()
    }

    /**
     * bitmap -> bytes
     *
     */
    fun bitmap2Bytes(bitmap: Bitmap?): ByteArray? {
        if (null == bitmap) {
            return null
        }
        val byteArrayOutputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream)
        return byteArrayOutputStream.toByteArray()
    }

    /**
     * bytes -> bitmap
     *
     */
    fun bytes2Bitmap(bytes: ByteArray?): Bitmap? {
        return if (null == bytes || bytes.size == 0) {
            null
        } else BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
    }
}
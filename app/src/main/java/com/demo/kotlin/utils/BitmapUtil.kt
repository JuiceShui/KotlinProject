package com.demo.kotlin.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.text.TextUtils
import android.util.Base64
import androidx.annotation.DrawableRes
import java.io.ByteArrayOutputStream

class BitmapUtil {
    companion object {
        /**
         * 由图片的base64编码获取bitmap
         *
         * @param base64
         * @return
         */
        fun getBitmap(base64: String): Bitmap? {
            var bitmap: Bitmap? = null
            val arr = base64.split(",").toTypedArray()
            if (arr.size > 1 && !TextUtils.isEmpty(arr[1])) {
                val decodedString = Base64.decode(arr[1], Base64.DEFAULT)
                bitmap = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.size)
            }
            return bitmap
        }


        /**
         * 对bitmap进行base64编码
         *
         * @param bitmap
         * @return
         */
        fun base64Encode(bitmap: Bitmap): String? {
            //convert to byte array
            val out = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, out)
            val bytes = out.toByteArray()
            //base64 encode
            val encode = Base64.encode(bytes, Base64.DEFAULT)
            return String(encode)
        }


        /**
         * 由本地资源生成base64编码
         *
         * @param context
         * @param resId
         * @return
         */
        fun base64Encode(context: Context, @DrawableRes resId: Int): String? {
            val bitmap = BitmapFactory.decodeResource(context.resources, resId)
            return base64Encode(bitmap)
        }
    }

}
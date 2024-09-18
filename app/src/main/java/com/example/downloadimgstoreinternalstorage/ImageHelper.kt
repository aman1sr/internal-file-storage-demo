package com.example.downloadimgstoreinternalstorage
import android.content.Context
import android.content.SharedPreferences
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.net.HttpURLConnection
import java.net.URL

class ImageHelper(private val context: Context) {
    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences("ImagePrefs", Context.MODE_PRIVATE)

    suspend fun downloadAndSaveImage(imageUrl: String): Boolean{
        return withContext(Dispatchers.IO){
            try {
                val url = URL(imageUrl)
                val connection = url.openConnection() as HttpURLConnection
                connection.requestMethod = "GET"
                connection.connect()

                val inputStream = connection.inputStream
                val bitmap = BitmapFactory.decodeStream(inputStream)
                inputStream.close()

                //let ImageUrl = https://i.redd.it/t6mv0a8sochb1.jpg
                val filepath = imageUrl.substringAfterLast("/").substringBeforeLast(".")    // filePath = t6mv0a8sochb1
                val file = File(context.filesDir, "${filepath}.png")
                FileOutputStream(file).use { outputStream ->
                    bitmap.compress(Bitmap.CompressFormat.PNG,100,outputStream)
                }

                sharedPreferences.edit().putString("image_path",file.absolutePath).apply()
                Log.d(TAG, "downloadAndSaveImage: path: ${file.absolutePath}")
                true
            }catch (e: IOException){
                Log.e(TAG, "Error: $e" )
                false
            }
        }
    }

    // Function to get image from internal storage
    fun getImageFromStorage(): Bitmap? {
        val filePath = sharedPreferences.getString("image_path", null) ?: return null
        val file = File(filePath)
        return if (file.exists()) {
            BitmapFactory.decodeFile(filePath)
        } else {
            null
        }
    }

}
package com.test

import UnzipUtils
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.api.APIService
import com.barservicegam.app.R
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import java.io.*


class DownloadActivity : AppCompatActivity() {
    lateinit var btnCheckFile: Button
    lateinit var btnLoadImage: Button
    lateinit var imgCalendar: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_download)

        btnCheckFile = findViewById(R.id.btnCheckFile)
        btnLoadImage = findViewById(R.id.btnLoadImage)
        imgCalendar = findViewById(R.id.imgCalendar)

        btnCheckFile.setOnClickListener{
            val file =
                    File(getExternalFilesDir(null).toString() + File.separator + "data_20210213_1.zip")
            val strDest = getExternalFilesDir(null).toString()
            if(file.exists()) {
                Log.d("vietnb", "ton tai")
                UnzipUtils.unzip(file, strDest)
                file.delete()
            } else {
                Log.d("vietnb", "khong ton tai")
            }
        }

        btnLoadImage.setOnClickListener{
            val file =
                    File(getExternalFilesDir(null).toString() + File.separator + "1_1.png")

            if (file.exists()) {
                val myBitmap = BitmapFactory.decodeFile(file.absolutePath)
                imgCalendar.setImageBitmap(myBitmap)
            }
        }

        //downloadFile()
    }

    fun downloadFile(){
        val retrofit = Retrofit.Builder()
            .baseUrl("https://media.tinmoi24.vn")
            .build()

        // Create Service
        val service = retrofit.create(APIService::class.java)
        val call = service.downloadFileWithDynamicUrlSync()
        call.enqueue(object : Callback<ResponseBody?> {
            override fun onResponse(call: Call<ResponseBody?>?, response: Response<ResponseBody?>) {
                if (response.isSuccessful) {
                    Log.d("vietnb", "server contacted and has file")
                    val writtenToDisk: Boolean = writeResponseBodyToDisk(response.body()!!)
                    Log.d("vietnb", "file download was a success? " + writtenToDisk);
                } else {
                    Log.d("vietnb", "server contact failed: " + response.code())
                }
            }

            override fun onFailure(call: Call<ResponseBody?>?, t: Throwable?) {
                Log.e("vietnb", "error")
            }
        })
    }

    private fun writeResponseBodyToDisk(body: ResponseBody): Boolean {
        return try {
            val path =
                File(getExternalFilesDir(null).toString() + File.separator + "data_20210213_1.zip")
            Log.d("vietnb", "file: " + path)

            var inputStream: InputStream? = null
            var outputStream: OutputStream? = null
            try {
                val fileReader = ByteArray(6000)
                val fileSize = body.contentLength()
                var fileSizeDownloaded: Long = 0
                inputStream = body.byteStream()
                outputStream = FileOutputStream(path)
                while (true) {
                    val read: Int = inputStream.read(fileReader)
                    if (read == -1) {
                        break
                    }
                    outputStream.write(fileReader, 0, read)
                    fileSizeDownloaded += read.toLong()
                    Log.d("vietnb", "file download: $fileSizeDownloaded of $fileSize")
                }
                outputStream.flush()
                true
            } catch (e: IOException) {
                false
            } finally {
                if (inputStream != null) {
                    inputStream.close()
                }
                if (outputStream != null) {
                    outputStream.close()
                }
            }
        } catch (e: IOException) {
            false
        }
    }

    fun calulateProgress(totalSize: Double, downloadSize: Double):Double{
        return ((downloadSize/totalSize)*100)
    }
}
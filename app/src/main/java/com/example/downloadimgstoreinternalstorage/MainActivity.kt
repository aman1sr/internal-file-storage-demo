package com.example.downloadimgstoreinternalstorage

import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.example.downloadimgstoreinternalstorage.databinding.ActivityMainBinding
import kotlinx.coroutines.launch

/*
* https://chatgpt.com/share/66ea5a52-b468-8007-b6c5-b540883d9127
* */
const val TAG = "ImageHelper_d"
class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    private lateinit var imageHelper: ImageHelper


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        imageHelper = ImageHelper(this)


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.btnDownload.setOnClickListener { 
            val imageUrl = binding.etDownloadUrl.text.toString()
            if(!imageUrl.isNullOrEmpty()){  // check appropriate format type
                lifecycleScope.launch { 
                    val success = imageHelper.downloadAndSaveImage(imageUrl)
                    if(success) Toast.makeText(this@MainActivity, "Download Compeleted Now can see", Toast.LENGTH_SHORT).show()
                    else
                        Toast.makeText(this@MainActivity, "Oops!!! something went wrong", Toast.LENGTH_SHORT).show()
                }
            }else{
                Toast.makeText(this, "please Enter valid Url", Toast.LENGTH_SHORT).show()
            }
        }

        binding.btnShowimage.setOnClickListener { 
            val bitmap = imageHelper.getImageFromStorage()
            if(bitmap != null){
                binding.imageView.setImageBitmap(bitmap)
            }else{
                Toast.makeText(this, "Oops No bitmap found", Toast.LENGTH_SHORT).show()
            }
        }


    }
}
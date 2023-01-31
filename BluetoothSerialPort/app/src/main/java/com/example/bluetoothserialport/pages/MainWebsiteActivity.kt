package com.example.bluetoothserialport.pages

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.webkit.WebViewClient
import com.example.bluetoothserialport.R
import com.example.bluetoothserialport.databinding.ActivityMainBinding
import com.example.bluetoothserialport.databinding.ActivityMainWebsiteBinding

class MainWebsiteActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainWebsiteBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_website)

        binding=ActivityMainWebsiteBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.mainWebView.settings.javaScriptEnabled=true
        binding.mainWebView.webViewClient = WebViewClient()
        binding.mainWebView.loadUrl("https://github.com/iqdxa/SG90-open-the-door")
    }

}
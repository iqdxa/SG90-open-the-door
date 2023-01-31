package com.example.bluetoothserialport.pages

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.webkit.WebViewClient
import com.example.bluetoothserialport.R
import com.example.bluetoothserialport.databinding.ActivityGithubWebsiteBinding
import com.example.bluetoothserialport.databinding.ActivityMainWebsiteBinding

class GithubWebsiteActivity : AppCompatActivity() {

    private lateinit var binding: ActivityGithubWebsiteBinding

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_github_website)

        binding=ActivityGithubWebsiteBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.githubWebView.settings.javaScriptEnabled=true
        binding.githubWebView.webViewClient = WebViewClient()
        binding.githubWebView.loadUrl("https://github.com/iqdxa")
    }
}
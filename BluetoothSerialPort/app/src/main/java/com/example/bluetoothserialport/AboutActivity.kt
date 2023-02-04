package com.example.bluetoothserialport

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.util.Log
import android.view.Gravity
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.azhon.appupdate.manager.DownloadManager
import com.azhon.appupdate.util.ApkUtil
import com.azhon.appupdate.util.LogUtil
import com.azhon.appupdate.view.UpdateDialogActivity
import com.example.bluetoothserialport.pages.GithubWebsiteActivity
import com.example.bluetoothserialport.pages.MainWebsiteActivity
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import mehdi.sakout.aboutpage.AboutPage
import mehdi.sakout.aboutpage.Element
import okhttp3.OkHttpClient
import okhttp3.Request
import kotlin.concurrent.thread

class AboutActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_about)

        val authorElement = Element()
        authorElement.gravity = Gravity.CENTER
        authorElement.title = "作者: TFC"

        val versionElement = Element()
        versionElement.gravity = Gravity.CENTER
        versionElement.title = "版本号: " + packageManager.getPackageInfo(packageName, 0).versionName

        val mainWebsiteElement = Element()
        mainWebsiteElement.gravity = Gravity.START
        mainWebsiteElement.title = "访问主页"
        mainWebsiteElement.setOnClickListener {
            startActivity(Intent(this, MainWebsiteActivity::class.java))
        }

        val githubElement = Element()
        githubElement.gravity = Gravity.START
        githubElement.title = "访问GitHub"
        githubElement.setOnClickListener {
            startActivity(Intent(this, GithubWebsiteActivity::class.java))
        }

        val aboutPage: View = AboutPage(this)
            .isRTL(false)
            .setDescription("蓝牙开门手机端")
//            .setCustomFont(String) // or Typeface
            .setImage(R.mipmap.ic_launcher_logo)
//            .addItem(Element().setTitle(packageManager.getPackageInfo(packageName, 0).versionName))
//            .addItem(adsElement)
            .addGroup("Connect with us")
            .addEmail("1429316040@qq.com")
//            .addWebsite("https://shanyaliux.cn/serialport/")
            .addGitHub("iqdxa\\SG90-open-the-door")
            .addItem(mainWebsiteElement)
            .addItem(githubElement)
            .addItem(authorElement)
            .addItem(versionElement)
            .create()
        setContentView(aboutPage)
    }

    private fun checkUpdate() {

    }

    private fun parseJSONWithGSON(jsonData: String) {
//        val packageInfo = context.packageManager.getPackageInfo(context.packageName, 0)
//        val localVersionCode=packageInfo.longVersionCode

    }
}

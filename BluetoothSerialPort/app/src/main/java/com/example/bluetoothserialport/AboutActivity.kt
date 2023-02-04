package com.example.bluetoothserialport

import android.content.Intent
import android.os.Bundle
import android.view.Gravity
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.bluetoothserialport.pages.GithubWebsiteActivity
import com.example.bluetoothserialport.pages.MainWebsiteActivity
import mehdi.sakout.aboutpage.AboutPage
import mehdi.sakout.aboutpage.Element

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

        //TODO:删除内置浏览器
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
            .addItem(Element().setTitle(packageManager.getPackageInfo(packageName, 0).versionName))
//            .addItem(adsElement)
            .addGroup("Connect with us")
            .addEmail("1429316040@qq.com")
            .addWebsite("https://iqdxa.github.io/")
            .addGitHub("iqdxa\\SG90-open-the-door")
            .addItem(mainWebsiteElement)
            .addItem(githubElement)
            .addItem(authorElement)
            .addItem(versionElement)
            .create()
        setContentView(aboutPage)
    }
}

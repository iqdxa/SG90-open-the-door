package com.example.bluetoothserialport

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Gravity
import android.view.View
import androidx.appcompat.app.AlertDialog
import com.example.bluetoothserialport.pages.GithubWebsiteActivity
import com.example.bluetoothserialport.pages.MainWebsiteActivity
import mehdi.sakout.aboutpage.AboutPage
import mehdi.sakout.aboutpage.Element
//import world.shanya.serialportsample.utils.CheckUpdate

private const val jsonUrl = "https://gitee.com/Shanya/serialportappupdate/raw/master/update.json"

class AboutActivity : AppCompatActivity() {

    //private val checkUpdate = CheckUpdate(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_about)

        val authorElement = Element()
        authorElement.gravity = Gravity.CENTER
        authorElement.title = "作者: TFC"

        val versionElement = Element()
        versionElement.gravity = Gravity.CENTER
        versionElement.title = "版本号: " + packageManager.getPackageInfo(packageName, 0).versionName

        val upgradeElement = Element()
        upgradeElement.gravity = Gravity.CENTER
        upgradeElement.title = "检测更新"
        upgradeElement.setOnClickListener {
            //checkUpdate.check(true)
            AlertDialog.Builder(this).apply{
                setTitle("检测更新")
                setMessage("暂无更新")
                setCancelable(false)
                setPositiveButton("确定") { dialog, which ->
                }
            show()
            }
        }

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
//            .addEmail("shanyaliux@qq.com")
//            .addWebsite("https://shanyaliux.cn/serialport/")
//            .addFacebook("the.medy")
//            .addTwitter("medyo80")
//            .addYoutube("UCdPQtdWIsg7_pi4mrRu46vA")
//            .addPlayStore("com.ideashower.readitlater.pro")
            .addGitHub("iqdxa\\SG90-open-the-door")
//            .addInstagram("medyo80")
            .addItem(mainWebsiteElement)
            .addItem(githubElement)
            .addItem(authorElement)
            .addItem(versionElement)
            .addItem(upgradeElement)
            .create()
        setContentView(aboutPage)
    }
}
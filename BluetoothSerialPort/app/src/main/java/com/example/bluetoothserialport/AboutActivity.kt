package com.example.bluetoothserialport

import android.content.*
import android.net.Uri
import android.os.Bundle
import android.view.Gravity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import mehdi.sakout.aboutpage.AboutPage
import mehdi.sakout.aboutpage.Element
import world.shanya.serialport.SerialPort
import world.shanya.serialport.SerialPortBuilder

class AboutActivity : AppCompatActivity() {

    private var toolMenu: Menu ?= null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_about)

        val feedbackGiteeElement = Element()
        feedbackGiteeElement.gravity = Gravity.START
        feedbackGiteeElement.title = "通过Gitee反馈"
        feedbackGiteeElement.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data=Uri.parse("https://gitee.com/tfc123/SG90-open-the-door/issues")
            startActivity(intent)
        }

        val feedbackGithubElement = Element()
        feedbackGithubElement.gravity = Gravity.START
        feedbackGithubElement.title = "通过GitHub反馈"
        feedbackGithubElement.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data=Uri.parse("https://github.com/iqdxa/SG90-open-the-door/issues")
            startActivity(intent)
        }

        val feedbackEmailElement = Element()
        feedbackEmailElement.gravity = Gravity.START
        feedbackEmailElement.title = "通过邮箱反馈"
        feedbackEmailElement.setOnClickListener {
            val clipboardManager = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            val clipData = ClipData.newPlainText("Label", "1429316040@qq.com")
            clipboardManager.setPrimaryClip(clipData)
            Toast.makeText(this,"已复制邮箱地址到剪贴板",Toast.LENGTH_SHORT).show()

        }

        val authorElement = Element()
        authorElement.gravity = Gravity.CENTER
        authorElement.title = "作者: TFC"
        authorElement.setOnClickListener {
            Toast.makeText(this,"别点啦！都点痛了",Toast.LENGTH_SHORT).show()
        }

        val versionElement = Element()
        val versionName = packageManager.getPackageInfo(packageName,0).versionName
        versionElement.gravity = Gravity.CENTER
        versionElement.title = "$versionName"
        versionElement.setOnClickListener {
            Toast.makeText(this,"软件版本号：$versionName",Toast.LENGTH_SHORT).show()
        }

        val updateElement = Element()
        updateElement.gravity = Gravity.CENTER
        updateElement.title = "检测更新"
        updateElement.setOnClickListener {
            Toast.makeText(this,"检测更新中……",Toast.LENGTH_SHORT).show()
        }

        val aboutPage: View = AboutPage(this)
            .isRTL(false)
            .setDescription("蓝牙开门手机端")
//            .setCustomFont(String) // or Typeface
            .setImage(R.mipmap.ic_launcher_logo)
//            .addItem(Element().setTitle(packageManager.getPackageInfo(packageName, 0).versionName))
//            .addItem(adsElement)
            .addGroup("Connect with us")
//            .addEmail("1429316040@qq.com")
            .addWebsite("https://iqdxa.github.io/SG90-open-the-door/")
            .addGitHub("iqdxa\\SG90-open-the-door")
            .addGroup("反馈：")
            .addItem(feedbackGiteeElement)
            .addItem(feedbackGithubElement)
            .addItem(feedbackEmailElement)
            .addItem(authorElement)
            .addItem(versionElement)
            .addItem(updateElement)
            .create()
        setContentView(aboutPage)
    }

    //该方法用于创建显示Menu
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_about, menu)
        toolMenu = menu
        return super.onCreateOptionsMenu(menu)
    }

    //该方法对菜单的item进行监听
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_main ->
                startActivity(Intent(this, MainActivity::class.java))
            R.id.menu_quiet -> {
//                serialPort?.disconnect()
                finish()
            }
        }
        return super.onOptionsItemSelected(item)
    }
}

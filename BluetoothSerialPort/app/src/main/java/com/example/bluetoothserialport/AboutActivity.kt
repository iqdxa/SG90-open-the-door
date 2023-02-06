package com.example.bluetoothserialport

import android.content.*
import android.net.Uri
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import mehdi.sakout.aboutpage.AboutPage
import mehdi.sakout.aboutpage.Element

class AboutActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_about)

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
            val builder = AlertDialog.Builder(this)
            builder.setMessage("暂无更新")
                .setPositiveButton("确定",
                    DialogInterface.OnClickListener { _, _ ->
                    })
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")

        val feedbackGithubElement = Element()
        feedbackGithubElement.gravity = Gravity.START
        feedbackGithubElement.title = "在GitHub反馈"
        feedbackGithubElement.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data=Uri.parse("https://github.com/iqdxa/SG90-open-the-door/issues")
            startActivity(intent)
        }

        //TODO 添加通过gitee反馈
        //TODO 修复点击后不能复制的问题
        val feedbackEmailElement = Element()
        feedbackEmailElement.gravity = Gravity.START
        feedbackEmailElement.title = "通过邮箱反馈"
        feedbackEmailElement.setOnClickListener {
            //获取剪切板管理器
            val clipboard = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager            //设置内容到剪切板
            // Creates a new text clip to put on the clipboard
            val clip: ClipData = ClipData.newPlainText("Email","1429316040@qq.com")
            Toast.makeText(this,"邮箱已经复制到剪切板",Toast.LENGTH_SHORT).show()
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
                serialPort?.disconnect()
                finish()
            }
        }
        return super.onOptionsItemSelected(item)
    }
}

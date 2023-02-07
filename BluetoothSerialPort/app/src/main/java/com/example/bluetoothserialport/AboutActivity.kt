package com.example.bluetoothserialport

import android.content.*
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.azhon.appupdate.manager.DownloadManager
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import mehdi.sakout.aboutpage.AboutPage
import mehdi.sakout.aboutpage.Element
import okhttp3.OkHttpClient
import okhttp3.Request
import kotlin.concurrent.thread

class AboutActivity : AppCompatActivity() {

    private var manager: DownloadManager? = null
    private var versionCode:Int = 0
    private var versionName:String = "1.0.0"
    private var apkName:String = "null.apk"
    private var apkSize:String = "0"
    private var updateDescription:String = "null"
    private var getDataWebsite:String ="https://gitee.com/tfc123/SG90-open-the-door/raw/master/BluetoothSerialPort/latestVersion.json"
    private val downloadWebsite:String ="https://gitee.com/tfc123/SG90-open-the-door/raw/master/BluetoothSerialPort/app/release/app-release.apk"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_about)

        //TODO:添加跳转到更新日志页面按钮

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
            Toast.makeText(this,"邮箱已复制到剪贴板",Toast.LENGTH_SHORT).show()
        }

        val authorElement = Element()
        authorElement.gravity = Gravity.CENTER
        authorElement.title = "作者: TFC"
        authorElement.setOnClickListener {
            Toast.makeText(this,"别点啦！都点痛了",Toast.LENGTH_SHORT).show()
        }

        val versionElement = Element()
        val localVersionName = packageManager.getPackageInfo(packageName,0).versionName
        versionElement.gravity = Gravity.CENTER
        versionElement.title = "版本：$localVersionName"
        versionElement.setOnClickListener {
            Toast.makeText(this,"软件版本号：$localVersionName",Toast.LENGTH_SHORT).show()
        }

        val updateElement = Element()
        updateElement.gravity = Gravity.CENTER
        updateElement.title = "检测更新"
        updateElement.setOnClickListener {
            Toast.makeText(this,"检测更新中……",Toast.LENGTH_SHORT).show()
            checkUpdate()
        }

        val aboutPage: View = AboutPage(this)
            .isRTL(false)
            .setDescription("蓝牙开门手机端")
            .setImage(R.mipmap.ic_launcher_logo)
            .addGroup("关注作者")
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

    private fun checkUpdate(){
        thread {
            try {
                val client = OkHttpClient()
                val request = Request.Builder()
                    .url(getDataWebsite)
                    .build()
                val response = client.newCall(request).execute()
                val responseData = response.body()?.string()
                if (responseData != null) {
                    val gson = Gson()
                    val typeOf = object : TypeToken<List<App>>() {}.type
                    val appList = gson.fromJson<List<App>>(responseData, typeOf)
                    for (app in appList) {
                        versionCode = app.versionCode
                        versionName = app.versionName
                        apkName = app.apkName
                        apkSize = app.apkSize
                        updateDescription= app.apkDescription
                        var localVersionCode=packageManager.getPackageInfo(packageName,0).versionCode
                        if(localVersionCode < versionCode ){
                            manager = DownloadManager.Builder(this).run {
                                apkUrl(downloadWebsite)
                                apkName("app-release.apk")
                                smallIcon(R.mipmap.ic_launcher)
                                showNewerToast(true)
                                apkVersionCode(versionCode)
                                apkVersionName(versionName)
                                apkSize(apkSize)
                                apkDescription(updateDescription)
                                enableLog(true)
//                      httpManager()
                                jumpInstallPage(true)
//                      dialogImage(R.drawable.ic_dialog)
//                        dialogButtonColor(Color.parseColor("#E743DA"))
//                      dialogProgressBarColor(Color.parseColor("#E743DA"))
                                dialogButtonTextColor(Color.WHITE)
                                showNotification(true)
                                showBgdToast(false)
                                forcedUpgrade(false)
                                build()
                            }
                            manager?.download()
                        }else{
                            Toast.makeText(this,"暂无更新",Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}

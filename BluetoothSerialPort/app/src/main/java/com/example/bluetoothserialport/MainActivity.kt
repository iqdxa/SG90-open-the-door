package com.example.bluetoothserialport

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.view.KeyEvent
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.azhon.appupdate.manager.DownloadManager
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import okhttp3.Request
import world.shanya.serialport.SerialPort
import world.shanya.serialport.SerialPortBuilder
import kotlin.concurrent.thread

class MainActivity : AppCompatActivity() {

    private var toolMenu: Menu ?= null
    private var serialPort:SerialPort ?= null
    private var manager: DownloadManager? = null
    private var versionCode:Int = 0
    private var versionName:String = "1.0.0"
    private var apkName:String = "null.apk"
    private var apkSize:String = "0"
    private var updateDescription:String = "null"
    private var getDataWebsite:String ="https://gitee.com/tfc123/SG90-open-the-door/raw/master/BluetoothSerialPort/app/release/latestVersion.json"
    private val downloadWebsite:String ="https://gitee.com/tfc123/SG90-open-the-door/raw/master/BluetoothSerialPort/app/release/app-release.apk"

    @SuppressLint("MissingPermission", "SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val textViewConnectInfo = findViewById<TextView>(R.id.textViewConnectInfo)
        val buttonConnect = findViewById<Button>(R.id.buttonConnect)
        val buttonDisconnect = findViewById<Button>(R.id.buttonDisconnect)
        val buttonOpenDoor = findViewById<Button>(R.id.buttonOpenDoor)
        val buttonCloseDoor = findViewById<Button>(R.id.buttonCloseDoor)

        //检测更新
        checkUpdate()

        serialPort = SerialPortBuilder
            .setConnectionStatusCallback { status, bluetoothDevice ->
                MainScope().launch {
                    if (status) {
                        textViewConnectInfo.text =
                            "设备名称:\t${bluetoothDevice?.name}\n" +
                                    "设备地址:\t${bluetoothDevice?.address}\n" +
                                    "设备类型:\t${bluetoothDevice?.type}"

                    }else {
                        textViewConnectInfo.text = "还未连接设备，请点击连接"
                    }
                }
            }
            .build(this)

        buttonConnect.setOnClickListener {
            serialPort!!.openDiscoveryActivity()
        }

        //开门按钮
        buttonOpenDoor.setOnClickListener {
            serialPort!!.sendData("1")
        }

        //关门按钮
        buttonCloseDoor.setOnClickListener {
            serialPort!!.sendData("0")
        }

        //断开按钮
        buttonDisconnect.setOnClickListener {
            serialPort!!.disconnect()
        }
    }

    //该方法用于创建显示Menu
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        toolMenu = menu
        return super.onCreateOptionsMenu(menu)
    }

    //该方法对菜单的item进行监听
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_about ->
                startActivity(Intent(this, AboutActivity::class.java))
            R.id.menu_quiet -> {
                serialPort?.disconnect()
                finish()
            }
            R.id.menu_connect -> {
                if (item.title == "连接") {
                    serialPort?.openDiscoveryActivity()
                } else {
                    serialPort?.disconnect()
                }
            }
        }
        return super.onOptionsItemSelected(item)
    }

    //应用更新
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
                        updateDescription= app.updateDescription
                        versionCode=3
//                            if (versionCode > ApkUtil.getVersionCode(application)) {
                        manager = DownloadManager.Builder(this).run {
                            apkUrl(downloadWebsite)
                            apkName("app-release.apk")
                            smallIcon(R.mipmap.ic_launcher)
                            showNewerToast(true)
                            apkVersionCode(versionCode)
                            apkVersionName(versionName)
                            apkSize(apkSize)
                            apkDescription(updateDescription)
//                      apkMD5("DC501F04BBAA458C9DC33008EFED5E7F")
                            enableLog(true)
//                      httpManager()
                            jumpInstallPage(true)
//                      dialogImage(R.drawable.ic_dialog)
//                      dialogButtonColor(Color.parseColor("#E743DA"))
//                      dialogProgressBarColor(Color.parseColor("#E743DA"))
                            dialogButtonTextColor(Color.WHITE)
                            showNotification(true)
                            showBgdToast(false)
                            forcedUpgrade(false)
                            build()
                        }
                        manager?.download()
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private var isExit: Boolean = false

    override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK)
            exitBy2Click() //调用双击退出函数
        return false
    }

    private fun exitBy2Click() {
        val handler = Handler()
        if ((!isExit)) {
            isExit = true
            Toast.makeText(this, "再按一次退出APP", Toast.LENGTH_LONG).show()
            handler.postDelayed({ isExit = false }, 1000 * 2) //x秒后没按就取消
        } else {
            serialPort?.disconnect()
            finish()
        }
    }
}
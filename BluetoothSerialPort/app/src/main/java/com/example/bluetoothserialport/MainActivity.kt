package com.example.bluetoothserialport

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.util.Log
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
    private var getDataWebsite:String ="https://gitee.com/tfc123/SG90-open-the-door/raw/master/BluetoothSerialPort/latestVersion.json"
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

        //????????????
        checkUpdate()

        //TODO:??????????????????

        serialPort = SerialPortBuilder
            .autoConnect(true)//????????????????????????
            .setConnectionStatusCallback { status, bluetoothDevice ->
                MainScope().launch {
                    if (status) {
                        textViewConnectInfo.text =
                            "????????????:\t${bluetoothDevice?.name}\n" +
                                    "????????????:\t${bluetoothDevice?.address}\n" +
                                    "????????????:\t${bluetoothDevice?.type}"

                    }else {
                        textViewConnectInfo.text = "????????????????????????????????????"
                    }
                }
            }
            .build(this)

        buttonConnect.setOnClickListener {
            serialPort!!.openDiscoveryActivity()
        }

        //????????????
        buttonOpenDoor.setOnClickListener {
            serialPort!!.sendData("1")
        }

        //????????????
        buttonCloseDoor.setOnClickListener {
            serialPort!!.sendData("0")
        }

        //????????????
        buttonDisconnect.setOnClickListener {
            serialPort!!.disconnect()
        }
    }

    //???????????????????????????Menu
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        toolMenu = menu
        return super.onCreateOptionsMenu(menu)
    }

    //?????????????????????item????????????
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_setting ->
                startActivity(Intent(this, SettingActivity::class.java))
            R.id.menu_about ->
                startActivity(Intent(this, AboutActivity::class.java))
            R.id.menu_quiet -> {
                serialPort?.disconnect()
                finish()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun checkUpdate() {
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
            exitBy2Click() //????????????????????????
        return false
    }

    private fun exitBy2Click() {
        val handler = Handler()
        if ((!isExit)) {
            isExit = true
            Toast.makeText(this, "??????????????????APP", Toast.LENGTH_LONG).show()
            handler.postDelayed({ isExit = false }, 1000 * 2) //x?????????????????????
        } else {
            serialPort?.disconnect()
            finish()
        }
    }
}
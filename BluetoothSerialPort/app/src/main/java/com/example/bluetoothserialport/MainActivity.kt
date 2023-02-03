package com.example.bluetoothserialport

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.KeyEvent
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import world.shanya.serialport.SerialPort
import world.shanya.serialport.SerialPortBuilder

class MainActivity : AppCompatActivity() {

    private var toolMenu: Menu ?= null
    private var serialPort:SerialPort ?= null

    @SuppressLint("MissingPermission", "SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val stringBuilder = StringBuilder()
        val textViewConnectInfo = findViewById<TextView>(R.id.textViewConnectInfo)
        val buttonConnect = findViewById<Button>(R.id.buttonConnect)
        val buttonDisconnect = findViewById<Button>(R.id.buttonDisconnect)

        val buttonOpenDoor = findViewById<Button>(R.id.buttonOpenDoor)
        val buttonCloseDoor = findViewById<Button>(R.id.buttonCloseDoor)

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
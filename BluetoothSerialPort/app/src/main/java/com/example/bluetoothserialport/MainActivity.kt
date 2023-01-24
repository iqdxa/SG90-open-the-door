package com.example.bluetoothserialport

import android.R
import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.view.KeyEvent
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.EditText
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
        val textViewReceived = findViewById<TextView>(R.id.textViewReceiced)
        val textViewConnectInfo = findViewById<TextView>(R.id.textViewConnectInfo)
        val buttonConnect = findViewById<Button>(R.id.buttonConnect)
        val buttonDisconnect = findViewById<Button>(R.id.buttonDisconnect)
        val editTextSendData = findViewById<EditText>(R.id.editTextTextSend)
        val buttonSend = findViewById<Button>(R.id.buttonSend)

        val buttonOpenDoor = findViewById<Button>(R.id.buttonOpenDoor)


        serialPort = SerialPortBuilder
            .setReceivedDataCallback {
                MainScope().launch {
                    stringBuilder.append(it)
                    textViewReceived.text = stringBuilder.toString()
                }
            }
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

        buttonDisconnect.setOnClickListener {
            serialPort!!.disconnect()
        }

        buttonSend.setOnClickListener {
            serialPort!!.sendData(editTextSendData.text.toString())
        }

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        toolMenu = menu
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_about ->
                startActivity(Intent(this,AboutActivity::class.java))
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


}
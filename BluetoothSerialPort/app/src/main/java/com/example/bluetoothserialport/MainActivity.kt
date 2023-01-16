package com.example.bluetoothserialport

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import org.w3c.dom.Text
import world.shanya.serialport.SerialPortBuilder

class MainActivity : AppCompatActivity() {

    private var toolMenu: Menu ?= null

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

        val serialPort = SerialPortBuilder
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
                        textViewConnectInfo.text = ""
                    }
                }
            }
            .build(this)

        buttonConnect.setOnClickListener {
            serialPort.openDiscoveryActivity()
        }

        buttonDisconnect.setOnClickListener {
            serialPort.disconnect()
        }

        buttonSend.setOnClickListener {
            serialPort.sendData(editTextSendData.text.toString())
        }

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        toolMenu = menu
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_about ->
                startActivity(Intent(this, AboutActivity::class.java))
        }
        return super.onOptionsItemSelected(item)
    }
}
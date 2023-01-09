package com.example.opendoor

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import world.shanya.serialport.SerialPortBuilder

class MainActivity : AppCompatActivity() {
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
}
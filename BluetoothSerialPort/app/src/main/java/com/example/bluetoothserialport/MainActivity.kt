package com.example.bluetoothserialport

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.azhon.appupdate.manager.DownloadManager
import com.azhon.appupdate.view.NumberProgressBar
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import world.shanya.serialport.SerialPort
import world.shanya.serialport.SerialPortBuilder
import java.util.Currency.getInstance


class MainActivity : AppCompatActivity() {

    private var toolMenu: Menu ?= null
    private var SerialPort: SerialPort?= null
    private val progressBar: NumberProgressBar? = null
    private var manager: DownloadManager? = null
    private val url =
        "https://imtt.dd.qq.com/16891/apk/FA48766BA12A41A1D619CB4B152889C6.apk?fsname=com.estrongs.android.pop_4.2.3.3_10089.apk&csr=1bbd"


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
                        textViewConnectInfo.text = "还未连接设备，请点击连接"
                    }
                }
            }
            .build(this)

        buttonConnect.setOnClickListener {
            serialPort.openDiscoveryActivity()
        }

        //开门按钮
        buttonOpenDoor.setOnClickListener {
            serialPort.sendData("1")
        }

        buttonDisconnect.setOnClickListener {
            serialPort.disconnect()
        }

        buttonSend.setOnClickListener {
            serialPort.sendData(editTextSendData.text.toString())
        }

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        toolMenu = menu
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            //R.id.menu_about ->
                //checkUpdate()
            R.id.menu_quiet ->
                finish()
            /*R.id.menu_connect -> {
                if (item.title == "连接") {
                    SerialPort?.openDiscoveryActivity()
                } else {
                    SerialPort?.disconnect()
                }
            }
             */
        }
        return super.onOptionsItemSelected(item)
    }
}
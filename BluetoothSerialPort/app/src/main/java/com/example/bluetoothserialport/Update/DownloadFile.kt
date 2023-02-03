package com.example.bluetoothserialport.Update

import android.app.Activity
import android.graphics.Color
import com.azhon.appupdate.manager.DownloadManager

class DownloadFile {

    private var manager: DownloadManager? = null

    fun startDownload(
        apkName: String,
        apkSize: String,
        versionName: String,
        updateDescription: String
    )
    {
        manager = DownloadManager.Builder(Activity()).run {
            apkUrl("https://gitee.com/tfc123/SG90-open-the-door/raw/master/BluetoothSerialPort/app/release/app-release.apk")
            apkName(apkName)
            showNewerToast(true)
//            apkVersionCode(2)
            apkVersionName(versionName)
            apkSize(apkSize)
            apkDescription(updateDescription)
//            apkMD5("DC501F04BBAA458C9DC33008EFED5E7F")

            enableLog(true)
//            httpManager()
            jumpInstallPage(true)
//            dialogImage(R.drawable.ic_dialog)
//            dialogButtonColor(Color.parseColor("#E743DA"))
//            dialogProgressBarColor(Color.parseColor("#E743DA"))
            dialogButtonTextColor(Color.WHITE)
            showNotification(true)
            showBgdToast(false)
            forcedUpgrade(false)
            build()
        }
        manager?.download()
    }
}
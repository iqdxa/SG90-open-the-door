package com.example.bluetoothserialport.Update

import android.app.Activity
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CheckUpdate: AppCompatActivity()  {

    private var downloadfile = DownloadFile()

    fun check(activity: Activity) {

        //获取软件versionCode
        val local = activity.packageManager.getPackageInfo(activity.packageName, 0)
        var server:App?=null

        //获取服务器最新版本号
        val appService = ServiceCreator.create<AppService>()
        appService.getAppData().enqueue(object : Callback<App> {
            override fun onResponse(call: Call<App>, response: Response<App>) {
                server = response.body()
            }
            override fun onFailure(call: Call<App>, t: Throwable) {
                t.printStackTrace()
            }
        })

        //比对本地软件versionCode和服务器versionCode
        if ( local.versionCode< server!!.versionCode) {
            //服务器versionCode更大，提示更新
            downloadfile.startDownload(server!!.apkName,
                server!!.apkSize,
                server!!.versionName,
                server!!.updateDescription)
        }else{
            Toast.makeText(this, "暂无更新", Toast.LENGTH_LONG).show()
        }
    }
}

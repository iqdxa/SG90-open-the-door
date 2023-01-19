package com.example.bluetoothserialport.utils

import com.ejlchina.okhttps.HTTP
import com.ejlchina.okhttps.gson.GsonMsgConvertor
import com.ejlchina.okhttps.HttpResult
import android.app.Activity
import com.azhon.appupdate.manager.DownloadManager
import com.azhon.appupdate.util.ApkUtil
import com.example.bluetoothserialport.R
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch

private const val jsonUrl = "https://gitee.com/Shanya/serialportappupdate/raw/master/update.json"


/**
 * 检查更新
 */
class CheckUpdate(private val activity: Activity) {
    private val http = HTTP.builder()
        .baseUrl(jsonUrl)
        .addMsgConvertor(GsonMsgConvertor())
        .build()

    fun check(showToast: Boolean=false) {
        http.async("") //  http://api.example.com/users/1
            .setOnResponse { res: HttpResult ->
                // 得到目标数据
                val updateBean = res.body.toBean(UpdateBean::class.java)
                updateBean?.let {
                    if (showToast) {
                        MainScope().launch {
                            update(
                                it.apkName,
                                it.downloadUrl,
                                it.versionCode,
                                it.versionName,
                                it.apkSize,
                                it.apkMd5,
                                it.updateDescription,
                                showToast
                            )
                        }
                    } else {
                        update(
                            it.apkName,
                            it.downloadUrl,
                            it.versionCode,
                            it.versionName,
                            it.apkSize,
                            it.apkMd5,
                            it.updateDescription
                        )
                    }

                    val b = ApkUtil.deleteOldApk(
                        activity,
                        activity.externalCacheDir?.path.toString() + "/${it.apkName}"
                    )


                }
            }
            .get()
    }


    private fun update(
        apkName: String,
        downloadUrl: String,
        versionCode: Int,
        versionName: String,
        apkSize: String,
        apkMd5: String,
        updateDescription: String,
        showToast: Boolean = false
    ) {
        val manager: DownloadManager = DownloadManager.getInstance(activity)
        manager.setApkName(apkName)
            .setApkUrl(downloadUrl)
            .setSmallIcon(R.mipmap.ic_launcher_logo)
//            .setConfiguration(configuration) //设置了此参数，那么会自动判断是否需要更新弹出提示框
            .setApkVersionCode(versionCode)
            .setApkVersionName(versionName)
            .setApkSize(apkSize)
            .setApkMD5(apkMd5)
            .setApkDescription(updateDescription)
            .setShowNewerToast(showToast)
            .download()
    }


}

data class UpdateBean(
    var apkName: String,
    var downloadUrl: String,
    var versionCode: Int,
    var versionName: String,
    var apkSize: String,
    var apkMd5: String,
    var updateDescription: String
)
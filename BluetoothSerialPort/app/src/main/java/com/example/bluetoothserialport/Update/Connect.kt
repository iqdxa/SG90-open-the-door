package com.example.bluetoothserialport.Update

import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

class App(val apkName:String,
          val apkSize:String,
          val versionCode: Int,
          val versionName: String,
          val updateDescription: String)

interface AppService {
    @GET("latestVersion.json")
    fun getAppData(): Call<App>
}

object ServiceCreator {

    private const val BASE_URL = "https://gitee.com/tfc123/SG90-open-the-door/raw/master/BluetoothSerialPort/app/release/"

    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    fun <T> create(serviceClass: Class<T>): T = retrofit.create(serviceClass)

    inline fun <reified T> create(): T = create(T::class.java)

}

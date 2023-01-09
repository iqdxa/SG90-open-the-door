package serialport

import android.content.Context
import serialport.server.SerialPortServer
import serialport.server.ServerConnectStatusCallback
import serialport.server.ServerReceivedDataCallback

object SerialPortServerBuilder {

    private val serialPortServerConfig = SerialPortServer.Config()

    fun setServerReceivedDataCallback(serverReceivedDataCallback: ServerReceivedDataCallback): SerialPortServerBuilder {
        SerialPortServer._setServerReceivedDataCallback(serverReceivedDataCallback)
        return this
    }

    fun setServerConnectStatusCallback(serverConnectStatusCallback: ServerConnectStatusCallback): SerialPortServerBuilder {
        SerialPortServer._setServerConnectStatusCallback(serverConnectStatusCallback)
        return this
    }

    fun setServerName(name: String): SerialPortServerBuilder {
        serialPortServerConfig.serverName = name
        return this
    }

    fun setServerUUID(uuid: String): SerialPortServerBuilder {
        serialPortServerConfig.serverUUID = uuid
        return this
    }

    fun build(context: Context): SerialPortServer {
        return SerialPortServer(serialPortServerConfig, context)
    }
}
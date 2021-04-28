package com.elpwc.socket_test

import java.net.Socket

class Speaker(host: String = "127.0.0.1", port: Int = 4096) {
    private val mHost = host
    private val mPort = port
    private val mBytes = ByteArray(1024)
    private fun once(string: String = "你好") {
        try {
            Socket(mHost, mPort).apply {
                getOutputStream().write(string.encodeToByteArray())
                getInputStream().read(mBytes)
                println(String(mBytes))
                close()
            }
        } catch (e: Exception) {
            println(e)
            throw e
        }
    }

    init {
        //println("已进入发送者模式")
        try {
            while (true) {
                //once(sc.nextLine())
            }
        } catch (e: Exception) {
        }
    }
}
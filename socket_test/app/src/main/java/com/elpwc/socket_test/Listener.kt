package com.elpwc.socket_test

import java.net.ServerSocket

class Listener(host: Int) {
    private val server = ServerSocket(host)
    private val bts = ByteArray(20480)

    init {
        Thread {
            while (true) {
                server.accept().apply {
                    getInputStream().read(bts)
                    println(String(bts))
                    //getOutputStream().write(sc.nextLine().encodeToByteArray())
                }
            }
        }.start()
    }
}
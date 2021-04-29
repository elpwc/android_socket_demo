package com.elpwc.socket_test

import java.net.*


class Client(host_: String, port_: Int, isudp: Boolean = false){
    var host = host_
    var port = port_
    var is_udp = isudp
    lateinit var socket : Socket
    lateinit var u_socket : DatagramSocket
    lateinit var client_socket : Thread

    fun Connect(host: String = this.host, port: Int = this.port) : Boolean{
        this.host = host
        this.port = port
        try{
            client_socket = Thread{
                socket = Socket(host, port)
            }

            client_socket.start()
            return true
        } catch (e: SocketException){
            throw
            return false
        }
    }

    fun udp_connect(port: Int = this.port) : Boolean{
        this.port = port
        try{
            client_socket = Thread{
                u_socket = DatagramSocket(port)
            }

            client_socket.start()
            return true
        } catch (e: SocketException){
            throw
            return false
        }
    }

    fun Disconnect(){
        socket.close()
        client_socket.stop()
    }

    fun Send(data: ByteArray){
        try{
            Thread{
                socket.apply {
                    getOutputStream().write(data)
                }
            }.start()
        } catch (e: SocketException){
            throw e
        }
    }

    fun udp_Send(ip: String, port: Int, data: ByteArray) {
        try {
            Thread {
                if (this::u_socket.isInitialized) {
                    u_socket.apply {
                        u_socket.send(DatagramPacket(data, data.size, InetAddress.getByName(ip), port))
                    }
                }

            }.start()
        } catch (e: SocketException) {
            throw e
        }

    }
    fun udp_Receive() {

        var by = ByteArray(1024*1024)
        lateinit var packet: DatagramPacket

        try {
            Thread {
                while (true) {
                    if (this::u_socket.isInitialized) {
                        packet = DatagramPacket(by, by.size)
                        u_socket.receive(packet)

                        var len = packet.length

                        if (len <= 0) {
                            rcv = false
                            continue
                        } else {
                            rcv_data = packet.data.take(len).toByteArray()
                            rcv = true
                        }
                    } else {
                        rcv = false
                        continue
                    }

                }
            }.start()
        } catch (e: SocketException) {
            throw e
        }
    }


    var rcv : Boolean = false
    var rcv_data : ByteArray? = null

    fun Receive(){
        var res = ByteArray(1024*1024)
        var len = 0
        try{
            Thread{
                while(true){
                    if(this::socket.isInitialized){

                            socket.apply {
                                len = getInputStream().read(res)
                            }

                        if(len <= 0){
                            rcv = false
                            continue
                        }else{
                            rcv_data = res.take(len).toByteArray()
                            rcv = true
                        }
                    } else{
                        rcv = false
                        continue
                    }
                }
            }.start()
        } catch (e: SocketException){
            throw e
        }
    }



}
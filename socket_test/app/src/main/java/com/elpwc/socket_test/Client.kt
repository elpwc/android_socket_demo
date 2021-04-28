package com.elpwc.socket_test

import android.net.wifi.WifiConfiguration
import java.io.InputStream
import java.net.DatagramPacket
import java.net.ProtocolFamily
import java.net.Socket
import java.net.SocketException

class Client (host_: String, port_: Int){
    var host = host_
    var port = port_
    lateinit var socket : Socket
    lateinit var client_socket : Thread

    fun Connect(host : String = this.host, port : Int = this.port) : Boolean{
        this.host = host
        this.port = port
        try{
            client_socket = Thread{
                socket = Socket(host, port)
            }

            client_socket.start()
            return true
        } catch (e : SocketException){
            throw
            return false
        }
    }

    fun Disconnect(){
        socket.close()
        client_socket.stop()
    }

    fun Send(data : ByteArray){
        try{
            Thread{
                socket.apply {
                    getOutputStream().write(data)
                }
            }.start()
        } catch(e : SocketException){
            throw e
        }
    }

    var rcv : Boolean = false
    var rcv_data : ByteArray? = null

    fun Receive(){
        var res = ByteArray(1024)
        var len = 0
        try{
            Thread{
                while(true){
                    if(this::socket.isInitialized){

                            socket.apply {
                                len = getInputStream().read(res)
                            }

                        if(len == 0){
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
        } catch(e : SocketException){
            throw e
        }
    }



}
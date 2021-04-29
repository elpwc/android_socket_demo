package com.elpwc.socket_test

import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import kotlin.experimental.and


object PacketManager {
    fun encode(packet: Packet) : ByteArray{
        var baos = ByteArrayOutputStream()

        baos.write(packet.magic_number)
        baos.write(arrayOf(packet.version).toByteArray(), 0, 1)
        baos.write(arrayOf(packet.source).toByteArray(), 0, 1)
        baos.write(arrayOf(packet.isSbyte).toByteArray(), 0, 1)
        baos.write(arrayOf(packet.command.id.toByte()).toByteArray(), 0, 1)
        baos.write(packet.datalen)
        baos.write(packet.data)

        return baos.toByteArray()
    }

    fun decode(data: ByteArray) : Packet?{
        lateinit var packet : Packet

        var bais = ByteArrayInputStream(data)


        var version: Byte = 0
        var source: Byte = 0
        var isSbyte : Byte = 1
        var command: Commands = Commands.no_op
        var datalen = 0
        var innerdata = byteArrayOf()

        var buffer = ByteArray(1024 * 1024)

        bais.read(buffer, 0, 4)

        if (buffer.take(4).toByteArray() contentEquals byteArrayOf(0, 114, 51, 4)) {


            version = bais.read().toByte()
            when (version.toInt()) {
                1 -> {
                    source = bais.read().toByte()
                    isSbyte = bais.read().toByte()
                    command = Commands.fromInt(bais.read())
                    bais.read(buffer, 0, 4)
                    datalen = byteArrayToInt(buffer.take(4).toByteArray())
                    bais.read(buffer, 0, datalen)
                    if(isSbyte == 1.toByte()){
                        innerdata = buffer.take(datalen).toByteArray()
                    }else{
                        innerdata = buffer.take(datalen).toByteArray()
                        //Byte to Sbyte
                    }

                    packet = Packet(1, source,isSbyte,command, innerdata)
                }
                else -> {
                }
            }
        } else {
            return null
        }
        return packet
    }

    open fun byteArrayToInt(b: ByteArray): Int {
        val a = ByteArray(4)
        var i = a.size - 1
        var j = b.size - 1
        while (i >= 0) {
            //从b的尾部(即int值的低位)开始copy数据
            if (j >= 0) a[i] = b[j] else a[i] = 0 //如果b.length不足4,则将高位补0
            i--
            j--
        }
        val v0: Int = (a[0] and 0xff.toByte()).toInt() shl 24 //&0xff将byte值无差异转成int,避免Java自动类型提升后,会保留高位的符号位
        val v1: Int = (a[1] and 0xff.toByte()).toInt() shl 16
        val v2: Int = (a[2] and 0xff.toByte()).toInt() shl 8
        val v3: Int = (a[3] and 0xff.toByte()).toInt()
        return v0 + v1 + v2 + v3
    }



    open fun Execute(packet : Packet){
        
    }
}
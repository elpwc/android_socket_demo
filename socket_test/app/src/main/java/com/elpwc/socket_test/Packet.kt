package com.elpwc.socket_test

enum class Commands(val id: Int){
    no_op(0),
    heartbeat(1),
    send_img_to(2),
    login(3),
    signin(4),
    send_msg_to(5)
}

class Packet(version_ : Byte = 1, source_ : Byte = 1, command_ : Commands = Commands.no_op, data_ : ByteArray = ByteArray(0)) {
    val magic_number : ByteArray = arrayOf(0 as Byte, 114, 51, 4).toByteArray()
    var version : Byte = version_
    var source : Byte = source_
    var command : Commands = command_
    var datalen : Int = 0
        get() = data.count()
    var data : ByteArray = data_
    
}
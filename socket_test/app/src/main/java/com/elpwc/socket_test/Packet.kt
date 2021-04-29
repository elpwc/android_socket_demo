package com.elpwc.socket_test

enum class Commands(val id: Int){
    no_op(0),
    heartbeat(1),
    send_img_to(2),
    login(3),
    signin(4),
    send_msg_to(5);

    companion object {
        fun fromInt(value: Int) = Commands.values().first { it.id == value }
    }
}

class Packet(version_ : Byte = 1, source_ : Byte = 1, isSbyte_ : Byte = 1, command_ : Commands = Commands.no_op, data_ : ByteArray = ByteArray(0)) {
    val magic_number : ByteArray = byteArrayOf(0, 114, 51, 4)
    var version : Byte = version_
    var source : Byte = source_
    var series : Int = series_
    var parts_count : Byte = parts_count_
    var current_part : Byte = current_part_
    var command : Commands = command_
    var datalen : Int = 0
        get() = data.count()
    var data : ByteArray = data_
    
}
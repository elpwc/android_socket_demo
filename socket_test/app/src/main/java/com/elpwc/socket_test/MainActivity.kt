package com.elpwc.socket_test

import android.animation.TypeConverter
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import java.net.InetAddress
import java.net.ServerSocket
import java.net.Socket
import java.util.*

class MainActivity : AppCompatActivity() {

    lateinit var button1 : Button
    lateinit var button2 : Button
    lateinit var textBox1 : EditText
    lateinit var textBox2 : EditText
    lateinit var textBox3 : EditText
    lateinit var textBox4 : EditText
    lateinit var textBox5 : EditText


    val sc = Scanner(System.`in`)
    //lateinit var server : ServerSocket //= ServerSocket(host)
    //lateinit var client : Socket //= server.accept()

    //lateinit var socket : Socket //= Socket(mHost, mPort)
    lateinit var tcpClient: Client
    lateinit var udpClient: Client


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        button1 = findViewById<Button>(R.id.button)
        button2 = findViewById<Button>(R.id.button2)
        textBox1 = findViewById<EditText>(R.id.editTextTextPersonName)
        textBox2 = findViewById<EditText>(R.id.editTextTextPersonName2)
        textBox3 = findViewById<EditText>(R.id.editTextTextPersonName3)
        textBox4 = findViewById<EditText>(R.id.editTextTextMultiLine)
        textBox5 = findViewById<EditText>(R.id.editTextTextPersonName4)
    }
    fun button1_onClick(view: View){
            //socket = Socket(textBox1.text.toString(),  textBox2.text.toString().toInt())
        tcpClient = Client(textBox1.text.toString(),  textBox2.text.toString().toInt())
        tcpClient.Connect()

        udpClient = Client(textBox1.text.toString(),  textBox5.text.toString().toInt())
        udpClient.Connect()

        Thread{
            tcpClient.Receive()
        }.start()

        Thread{
            udpClient.Receive()
        }.start()

        Thread{
            while(true){
                if(tcpClient.rcv && tcpClient.rcv_data != null){
                    textBox4.append("[T]server: " + String(tcpClient.rcv_data!!) + "\r\n")
                    tcpClient.rcv = false
                }
            }

        }.start()

        Thread{
            while(true){
                if(udpClient.rcv && udpClient.rcv_data != null){
                    textBox4.append("[U]server: " + String(udpClient.rcv_data!!) + "\r\n")
                    udpClient.rcv = false
                }
            }

        }.start()
    }


    fun button2_onClick(view: View){
        udpClient.Send(textBox3.text.toString().encodeToByteArray())
        textBox4.append("[U]client: " + textBox3.text.toString() + "\r\n")
    }


}
package com.elpwc.socket_test

import android.animation.TypeConverter
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.SurfaceView
import android.view.View
import android.widget.Button
import android.widget.EditText
import java.net.InetAddress
import java.net.ServerSocket
import java.net.Socket
import java.util.*
import android.hardware.Camera
import android.view.SurfaceHolder
import layout.ZLibUtils
import java.io.IOException

class MainActivity : AppCompatActivity() {

    lateinit var button1 : Button
    lateinit var button2 : Button
    lateinit var textBox1 : EditText
    lateinit var textBox2 : EditText
    lateinit var textBox3 : EditText
    lateinit var textBox4 : EditText
    lateinit var textBox5 : EditText
    lateinit var sv1 : SurfaceView



    lateinit var camera: Camera

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
        sv1 = findViewById<SurfaceView>(R.id.surfaceView)

        Thread(){
            Thread.sleep(3000)
            camerainit()
        }.start()
    }

    fun camerainit(){
        PermissionUtils.camera(this) {
            if (CameraUtils.isCameraAvailable()) {
                camera = CameraUtils.getCamera()!!
                CameraUtils.setParameters(camera)
            }
            startPreview(sv1.holder)
        }
    }

    fun button1_onClick(view: View){
            //socket = Socket(textBox1.text.toString(),  textBox2.text.toString().toInt())
        tcpClient = Client(textBox1.text.toString(),  textBox2.text.toString().toInt())
        tcpClient.Connect()

        udpClient = Client(textBox1.text.toString(),  textBox5.text.toString().toInt())
        udpClient.udp_connect()

        Thread{
            tcpClient.Receive()
        }.start()

        Thread{
            udpClient.udp_Receive()
        }.start()

        Thread{
            while(true){
                if(tcpClient.rcv && tcpClient.rcv_data != null){
                    textBox4.append("[T]server: " + String(tcpClient.rcv_data!!) + "\r\n")
                    var packet = PacketManager.decode(tcpClient.rcv_data!!)
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
        //tcpClient.Send(textBox3.text.toString().encodeToByteArray())
        udpClient.udp_Send(textBox1.text.toString(),  textBox5.text.toString().toInt(),textBox3.text.toString().encodeToByteArray())
        textBox4.append("[U]client: " + textBox3.text.toString() + "\r\n")
    }

    fun button3_onClick(view: View){
        taskPicture()
    }

    fun startPreview(holder: SurfaceHolder?) {
        if (camera != null) {
            try {
                camera.setPreviewDisplay(holder) // 先绑定显示的画面
                camera.stopPreview()
                camera.setDisplayOrientation(90)
            } catch (e: IOException) {
                e.printStackTrace()
            }

            camera.startPreview() // 这里才是开始预览
        }
    }

    fun stopPreview() {
        if (camera != null) {
            camera.stopPreview() // 停止预览
        }
    }


    val pictureCallback = Camera.PictureCallback { data, camera ->
        lateinit var packet : Packet

        var zipped_data = ImageUtils.bitmapCom1(data)

        packet = Packet(1,1,1,Commands.send_img_to,zipped_data)
        var send_data = PacketManager.encode(packet)
        tcpClient.Send(send_data)
    }


    private fun taskPicture() {
        camera?.autoFocus { success, camera ->
            run {
                camera?.takePicture(null, null, pictureCallback);
            }
        }
    }

}
package com.cx.goatlin

import android.content.Context
import android.os.Bundle
import android.support.v7.app.AlertDialog.*
import android.support.v7.app.AppCompatActivity
import android.widget.Button
import android.widget.EditText


class ServerInfoActivity : AppCompatActivity() {
    private lateinit var serverIPAddress: String
    private lateinit var serverPort: String
    val IP_ADDRESS = "ip_address"
    val PORT = "port"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_server_info)

        val prefs = applicationContext.getSharedPreferences(applicationContext.packageName,
                Context.MODE_PRIVATE)
        this.serverIPAddress = prefs!!.getString("ip_address","127.0.0.1")
        this.serverPort = prefs!!.getString("port","8080")

        findViewById<EditText>(R.id.IPAddress).setText(this.serverIPAddress)
        findViewById<EditText>(R.id.port).setText(this.serverPort)

        var buttonSave:Button = findViewById(R.id.buttonSave)

        buttonSave.setOnClickListener {
            this.serverIPAddress = findViewById<EditText>(R.id.IPAddress).text.toString()
            this.serverPort = findViewById<EditText>(R.id.port).text.toString()

            if(this.serverIPAddress.isNullOrEmpty() or
                    this.serverPort.isNullOrEmpty()){
                //Do nothing

                this.displayAlert()
            }
            else {
                val prefs = applicationContext.getSharedPreferences(applicationContext.packageName,
                        Context.MODE_PRIVATE)
                val editor = prefs!!.edit()
                editor.putString(this.IP_ADDRESS,this.serverIPAddress)
                editor.putString(this.PORT,this.serverPort)
                editor.apply()
            }


        }
    }

    private fun displayAlert(){
        val alert = Builder(this)
        // Builder
        with (alert) {
            setTitle("Error")
            setMessage("IP Address or Port setting is empty!")

            setPositiveButton("OK") {
                dialog, _ ->
                dialog.dismiss()
            }
        }

        // Dialog
        val dialog = alert.create()
        dialog.show()
    }
}
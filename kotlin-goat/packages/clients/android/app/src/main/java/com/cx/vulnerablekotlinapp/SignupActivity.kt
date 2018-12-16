package com.cx.vulnerablekotlinapp

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.Volley
import kotlinx.android.synthetic.main.activity_signup.*
import android.util.Log
import android.widget.Toast
import com.android.volley.toolbox.JsonObjectRequest
import org.json.JSONObject
import android.view.Gravity



class SignupActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)

        this.signup_button.setOnClickListener { attemptSignup() }
    }

    /**
     * Attempts to create a new account on back-end
     */
    private fun attemptSignup() {
        // @todo confirm password and confirm_password match
        val queue = Volley.newRequestQueue(this)
        // val url: String = "http://172.25.0.3:8080/accounts"
        //val url: String = "http://192.168.1.65:8080/accounts"
        val url:String = "http://" + this.getServerIPAddress() + ":" + this.getServerPort() + "/accounts"

        val name: String = this.name.text.toString()
        val email: String = this.email.text.toString()
        val password: String = this.password.text.toString()

        val data:JSONObject = JSONObject()
        data.put("name", name)
        data.put("email", email)
        data.put("password", password)

        val request = JsonObjectRequest(Request.Method.POST, url, data,
                Response.Listener<JSONObject> {
                    response ->
                        val status: Boolean
                        status = DatabaseHelper(applicationContext).createAccount(email, password)
                        if (status == true) {
                            Log.i("SignupActivity","Account creation")
                            Log.i("SingUpActivity", "$email : $password")
                            val intent = Intent(this, LoginActivity::class.java)
                            startActivity(intent)
                        } else {
                            showError("Failed to create local account")
                        }
                },
                Response.ErrorListener {
                    error ->
                        var message:String

                        when (error.networkResponse.statusCode) {
                            409 -> {
                                message = "This account already exists"
                                this.email.setError(message)
                                this.email.requestFocus()
                            }
                            else -> {
                                message = "Something went wrong"
                                showError(message)
                            }
                        }
                })

        queue.add(request)
    }

    private fun getServerIPAddress (): String? {
        val prefs = applicationContext.getSharedPreferences(applicationContext.packageName,
                Context.MODE_PRIVATE)
        return prefs!!.getString("ip_address","127.0.0.1")
    }

    private fun getServerPort ():String? {
        val prefs = applicationContext.getSharedPreferences(applicationContext.packageName,
                Context.MODE_PRIVATE)
        return prefs!!.getString("port","8080")
    }
    /**
     * Show a Toast with given error message
     *
     * @param message CharSequence  Error message to display
     * @return void
     */
    private fun showError(message: CharSequence) {
        val toast: Toast = Toast.makeText(this@SignupActivity, message, Toast.LENGTH_LONG)

        toast.setGravity(Gravity.CENTER_VERTICAL or Gravity.CENTER_HORIZONTAL, 0, 0)
        toast.show()
    }
}
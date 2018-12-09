package com.cx.vulnerablekotlinapp

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import kotlinx.android.synthetic.main.activity_signup.*
import android.util.Log
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.RequestFuture
import org.json.JSONObject

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
        // @todo API request
        val queue = Volley.newRequestQueue(this)
            val url: String = "http://172.25.0.3:8080/accounts"

        val data:JSONObject = JSONObject()
        data.put("name", this.name.text)
        data.put("email", this.email.text)
        data.put("password", this.password.text)

        val request = JsonObjectRequest(Request.Method.POST, url, data,
                Response.Listener<JSONObject> {
                    response -> Log.v("Signup", response.toString(2))
                },
                Response.ErrorListener { Log.v("Signup", "Signup failed") })

        queue.add(request)
    }
}
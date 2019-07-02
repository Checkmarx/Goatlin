package com.cx.goatlin

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import kotlinx.android.synthetic.main.activity_signup.*
import android.widget.Toast
import android.view.Gravity
import android.widget.AutoCompleteTextView
import com.cx.goatlin.api.model.Account
import com.cx.goatlin.api.service.Client
import com.cx.goatlin.helpers.DatabaseHelper
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SignupActivity : AppCompatActivity() {
    private val apiService by lazy {
        Client.create()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)

        this.signup_button.setOnClickListener { attemptSignup() }
    }

    /**
     * Attempts to create a new account on back-end
     */
    private fun attemptSignup() {
        val name: String = this.name.text.toString()
        val email: String = this.email.text.toString()
        val password: String = this.password.text.toString()
        val confirmPassword: String = this.confirmPassword.text.toString()

        if (confirmPassword != password) {
            this.confirmPassword.error = "Passwords don't match"
            this.confirmPassword.requestFocus()
            return;
        }

        val account: Account = Account(name, email, password)

        val call: Call<Void> = apiService.signup(account)

        call.enqueue(object: Callback<Void> {
            override fun onFailure(call: Call<Void>, t: Throwable) {
                Log.e("SingupActivity", t.message.toString())
            }

            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                val emailField: AutoCompleteTextView = findViewById(R.id.email)
                var message:String = ""

                when (response.code()) {
                    201 -> {
                        if (createLocalAccount(account)) {
                            val intent = Intent(this@SignupActivity,
                                    LoginActivity::class.java)

                            startActivity(intent)
                        } else {
                            message = "Failed to create local account"
                        }
                    }
                    409 -> {
                        message = "This account already exists"
                        emailField.error = message
                        emailField.requestFocus()
                    }
                    else -> {
                        message = "Failed to create account"
                    }
                }

                showError(message)
            }
        })
    }

    /**
     * Creates local account
     */
    private fun createLocalAccount(account: Account): Boolean {
        return DatabaseHelper(applicationContext).createAccount(account.email, account.password)
    }

    /**
     * Shows a Toast with given message
     */
    private fun showError(message: CharSequence) {
        val toast: Toast = Toast.makeText(this@SignupActivity, message, Toast.LENGTH_LONG)

        toast.setGravity(Gravity.CENTER_VERTICAL or Gravity.CENTER_HORIZONTAL, 0, 0)
        toast.show()
    }
}
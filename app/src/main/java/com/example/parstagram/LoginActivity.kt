package com.example.parstagram

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.parse.ParseUser

class LoginActivity : AppCompatActivity() {

    private lateinit var mUsernameEditText: EditText
    private lateinit var mPasswordEditText: EditText
    private lateinit var mLoginButton: Button
    private lateinit var mSignUpButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        if (ParseUser.getCurrentUser()!=null) {
            goToMainActivity()
        }

        mUsernameEditText = findViewById(R.id.etUsername)
        mPasswordEditText = findViewById(R.id.etPassword)
        mLoginButton = findViewById(R.id.btLogin)
        mSignUpButton = findViewById(R.id.btSignUp)

        mLoginButton.setOnClickListener {
            val username = mUsernameEditText.text.toString()
            val password = mPasswordEditText.text.toString()
            loginUser(username,password)
        }

        mSignUpButton.setOnClickListener {
            val username = mUsernameEditText.text.toString()
            val password = mPasswordEditText.text.toString()
            singUpUser(username,password)
        }
    }

    private fun singUpUser(username: String,password: String) {
        val user = ParseUser()
        user.username = username
        user.setPassword(password)

        user.signUpInBackground { e->
            if (e==null) {

            } else {
                e.printStackTrace()
            }
        }
    }

    private fun loginUser(username: String, password: String) {
        ParseUser.logInInBackground(username,password,({ user,e->
            if (user!=null) {
                Log.d(TAG, "loginUser: login success")
                goToMainActivity()
            } else {
                e.printStackTrace()
                Toast.makeText(this,"Error logging in ",Toast.LENGTH_SHORT).show()
            }
        }))
    }

    private fun goToMainActivity() {
        val intent = Intent(this,MainActivity::class.java)
        startActivity(intent)
        finish()
    }

    companion object {
        private const val TAG = "loginActivity"
    }
}
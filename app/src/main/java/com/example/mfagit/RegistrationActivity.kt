package com.example.mfagit

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.mfagit.data.DBHelper
import com.example.mfagit.databinding.ActivityRegistrationBinding

class RegistrationActivity : AppCompatActivity() {

    private lateinit var dbHelper: DBHelper
    private lateinit var binding: ActivityRegistrationBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegistrationBinding.inflate(layoutInflater)

        setContentView(binding.root)

        dbHelper = DBHelper(this@RegistrationActivity)

        binding.registrationSignupRegister.setOnClickListener {

            val fullName: String = binding.registrationFullName.getEditText()?.getText().toString()
            val email: String = binding.registrationEmail.getEditText()?.getText().toString()
            val mobile: String =
                binding.registrationMobileNumber.getEditText()?.getText().toString()
            val pin: String = binding.registrationPin.getEditText()?.getText().toString()
            val password: String =
                binding.registrationSignupPassword.getEditText()?.getText().toString()
            val rePassword: String =
                binding.registrationSignupPasswordAgain.getEditText()?.getText().toString()

            if (fullName == "" || email == "" || mobile == "" || pin == ""
                || password == "" || rePassword == ""
            ) {
                Toast.makeText(this@RegistrationActivity, "Enter all fields", Toast.LENGTH_SHORT)
                    .show();
            } else {
                if (password != rePassword) {
                    Toast.makeText(this, "Passwords are not same", Toast.LENGTH_SHORT).show();
                } else {
                    val checkUser: Boolean = dbHelper.checkUsername(email);
                    if (!checkUser) {
                        val correctInsert: Boolean =
                            dbHelper.insertData(fullName, email, mobile, pin, password);
                        if (correctInsert) {
                            Toast.makeText(
                                this@RegistrationActivity,
                                "Registered successfully",
                                Toast.LENGTH_SHORT
                            ).show();
                            endActivity();
                        } else {
                            Toast.makeText(
                                this@RegistrationActivity,
                                "Registration failed",
                                Toast.LENGTH_SHORT
                            ).show();
                        }
                    } else {
                        Toast.makeText(
                            this@RegistrationActivity,
                            "There is already such user",
                            Toast.LENGTH_SHORT
                        ).show();
                    }
                }
            }
        }

        binding.registrationCancel.setOnClickListener {
            endActivity()
        }

    }

    private fun endActivity() {
        finish()
    }
}
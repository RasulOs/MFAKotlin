package com.example.mfagit

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.biometric.BiometricPrompt
import com.example.mfagit.data.DBHelper
import com.example.mfagit.databinding.ActivityMainBinding
import java.util.concurrent.Executor
import androidx.core.app.ActivityCompat.startActivityForResult

import android.content.Intent
import android.os.Build
import android.provider.Settings
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricManager.Authenticators.BIOMETRIC_STRONG
import androidx.biometric.BiometricManager.Authenticators.DEVICE_CREDENTIAL
import androidx.core.content.ContextCompat

private const val TAG = "MainActivity"

class MainActivity : AppCompatActivity() {



    companion object {
        private const val REQUEST_CODE = 10101
        private var SECURITY_LEVEL = 0
        private var accountEmail = ""

        fun getSecurityLevel(): Int = SECURITY_LEVEL

        fun setSecurityLevel(securityLevel: Int) {
            SECURITY_LEVEL = securityLevel
        }

        fun getAccountEmail(): String = accountEmail

    }

    private val dbHelper = DBHelper(this)

    private lateinit var executor: Executor
    private lateinit var biometricPrompt: BiometricPrompt
    private lateinit var promptInfo: BiometricPrompt.PromptInfo

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val biometricManager = BiometricManager.from(this)
        when (biometricManager.canAuthenticate(BIOMETRIC_STRONG or DEVICE_CREDENTIAL)) {
            BiometricManager.BIOMETRIC_SUCCESS ->
                Log.d(TAG, "App can authenticate using biometrics.")
            BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE ->
                Log.e(TAG, "No biometric features available on this device.")
            BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE ->
                Log.e(TAG, "Biometric features are currently unavailable.")
            BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED -> {
                // Prompts the user to create credentials that your app accepts.
                val enrollIntent = Intent(Settings.ACTION_BIOMETRIC_ENROLL).apply {
                    putExtra(Settings.EXTRA_BIOMETRIC_AUTHENTICATORS_ALLOWED,
                        BIOMETRIC_STRONG or DEVICE_CREDENTIAL)
                }
                startActivityForResult(enrollIntent, REQUEST_CODE)
            }
        }

        executor = ContextCompat.getMainExecutor(this)

        biometricPrompt = BiometricPrompt(this, executor,
            object : BiometricPrompt.AuthenticationCallback() {

                override fun onAuthenticationError(errorCode: Int,
                                                   errString: CharSequence) {
                    super.onAuthenticationError(errorCode, errString)
                    Toast.makeText(applicationContext,
                        "Authentication error: $errString", Toast.LENGTH_SHORT)
                        .show()
                }

                override fun onAuthenticationSucceeded(
                    result: BiometricPrompt.AuthenticationResult) {
                    super.onAuthenticationSucceeded(result)
                    Toast.makeText(applicationContext,
                        "Authentication succeeded!", Toast.LENGTH_SHORT)
                        .show()
                    setSecurityLevel(3);
                    startActivity(Intent(this@MainActivity, MenuActivity::class.java))
                }

                override fun onAuthenticationFailed() {
                    super.onAuthenticationFailed()
                    Toast.makeText(applicationContext, "Authentication failed",
                        Toast.LENGTH_SHORT)
                        .show()
                }
            })

        promptInfo = BiometricPrompt.PromptInfo.Builder()
            .setTitle("Biometric login for the app")
            .setSubtitle("Log in using your biometric credential")
            .setNegativeButtonText("Use account password")
            .setAllowedAuthenticators(BiometricManager.Authenticators.BIOMETRIC_STRONG or
        BiometricManager.Authenticators.BIOMETRIC_WEAK)
            .build()


        binding.mainTouchIdImage.setOnClickListener {
            biometricPrompt.authenticate(promptInfo)
        }

        binding.mainSignin.setOnClickListener {
            val  email = binding.mainEditEmail.editText?.text.toString()
            val password = binding.mainEditPassword.editText?.text.toString()

            if (email.equals("") || password.equals("")) {
                Toast.makeText(this, "Enter all fields", Toast.LENGTH_SHORT).show();
            } else {

                val checkUser: Boolean = dbHelper.checkUsername(email);
                if (checkUser) {
                    val correctInsert: Boolean = dbHelper.checkUsernameAndPassword(email, password);
                    if (correctInsert) {
                        Toast.makeText(this, "Entered successfully", Toast.LENGTH_SHORT).show();
                        accountEmail = email;
                        setSecurityLevel(1);
                        startActivity(Intent(this@MainActivity, MenuActivity::class.java));
                        finish()
                    } else {
                        Toast.makeText(this@MainActivity, "Password does not match", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(this@MainActivity, "There is no such user", Toast.LENGTH_SHORT).show();
                }
            }
        }



        binding.mainSignup.setOnClickListener {
            startActivity(Intent(this@MainActivity, RegistrationActivity::class.java))
        }

    }


}
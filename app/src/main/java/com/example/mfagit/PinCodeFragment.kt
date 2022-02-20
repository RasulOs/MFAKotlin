package com.example.mfagit

import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentManager
import com.example.mfagit.data.DBHelper
import com.example.mfagit.databinding.FragmentPinCodeBinding
import java.util.concurrent.Executor
import java.util.concurrent.Executors

private const val TAG = "PinCodeFragment"

class PinCodeFragment : Fragment() {

    companion object {
        var activityFrom = ""
        private const val REQUEST_CODE = 101010

        private var PIN_FROM_DB = ""

        fun setPinFromDB(pin: String) {
            PIN_FROM_DB = pin
        }
    }

    private lateinit var binding: FragmentPinCodeBinding

    private var destination: String = ""

    private lateinit var dbHelper: DBHelper

    private lateinit var fragManager: FragmentManager

    private lateinit var executor: Executor
    private lateinit var biometricPrompt: BiometricPrompt
    private lateinit var promptInfo: BiometricPrompt.PromptInfo

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        binding = FragmentPinCodeBinding.inflate(layoutInflater, container, false)
        val view = binding.root

        dbHelper = DBHelper(requireContext())

        fragManager = requireActivity().supportFragmentManager


        executor = ContextCompat.getMainExecutor(requireContext())

        biometricPrompt = BiometricPrompt(this, executor,
            object : BiometricPrompt.AuthenticationCallback() {

                override fun onAuthenticationError(errorCode: Int,
                                                   errString: CharSequence) {
                    super.onAuthenticationError(errorCode, errString)
                    Toast.makeText(requireContext(),
                        "Authentication error: $errString", Toast.LENGTH_SHORT)
                        .show()
                }

                override fun onAuthenticationSucceeded(
                    result: BiometricPrompt.AuthenticationResult) {
                    super.onAuthenticationSucceeded(result)
                    Toast.makeText(requireContext(),
                        "Authentication succeeded!", Toast.LENGTH_SHORT)
                        .show()
                    MainActivity.setSecurityLevel(3);
                    authenticationSucceed(destination);
                }

                override fun onAuthenticationFailed() {
                    super.onAuthenticationFailed()
                    Toast.makeText(requireContext(), "Authentication failed",
                        Toast.LENGTH_SHORT)
                        .show()
                }
            })

        promptInfo = BiometricPrompt.PromptInfo.Builder()
            .setTitle("Biometric login for the app")
            .setSubtitle("Log in using your biometric credential")
            .setNegativeButtonText("Use account password")
            .setAllowedAuthenticators(
                BiometricManager.Authenticators.BIOMETRIC_STRONG or
                    BiometricManager.Authenticators.BIOMETRIC_WEAK)
            .build()

        binding.pincodeTouchIdImage.setOnClickListener {
            biometricPrompt.authenticate(promptInfo)
        }

        binding.pincodeSubmit.setOnClickListener {
            val pin = binding.pincodeEditPin.editText?.text.toString()

            if (pin.equals("")) {
                Toast.makeText(requireContext(), "Enter a PIN", Toast.LENGTH_SHORT).show();
            }
            else {
                //val email = MainActivity.getAccountEmail();

                dbHelper.initializePIN(MainActivity.getAccountEmail());

                if (pin == PIN_FROM_DB) {
                    Toast.makeText(requireContext(), "You entered PIN successfully", Toast.LENGTH_SHORT).show();
                    MainActivity.setSecurityLevel(2);
                    authenticationSucceed(destination);
                }
                else {
                    Toast.makeText(requireContext(), "PIN does not match", Toast.LENGTH_SHORT).show();
                }
            }
        }

        binding.pincodeCancel.setOnClickListener {
            fragManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)
        }

        destination = activityFrom

        return view
    }

    private fun authenticationSucceed(s: String) {
        if (s == "Transfer") {
            fragManager.beginTransaction()
                .replace(R.id.frameLayoutContainer, MoneyTransferFragment())
                .addToBackStack(null)
                .commit()
        }
        else if(s == "Password") {
            fragManager.beginTransaction()
                .replace(R.id.frameLayoutContainer, PasswordChangeFragment())
                .addToBackStack(null)
                .commit()
        }
    }



}
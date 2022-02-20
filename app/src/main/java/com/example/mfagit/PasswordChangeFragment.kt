package com.example.mfagit

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.mfagit.databinding.FragmentPasswordChangeBinding


class PasswordChangeFragment : Fragment() {

    private lateinit var binding: FragmentPasswordChangeBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentPasswordChangeBinding.inflate(layoutInflater, container, false)
        // Inflate the layout for this fragment
        val view = binding.root

        val fragManager = requireActivity().supportFragmentManager

        if (MainActivity.getSecurityLevel() <= 2) {
            PinCodeFragment.activityFrom = "Password"
            fragManager.beginTransaction()
                .replace(R.id.frameLayoutContainer, PinCodeFragment())
                .addToBackStack(null)
                .commit()
        }

        binding.passwordSubmit.setOnClickListener {
            fragManager.popBackStack()
        }

        binding.passwordCancel.setOnClickListener {
            fragManager.popBackStack()
        }

        return view
    }


}
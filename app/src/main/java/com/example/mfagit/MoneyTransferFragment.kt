package com.example.mfagit

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import com.example.mfagit.databinding.FragmentMoneyTransferBinding


class MoneyTransferFragment : Fragment() {

    private lateinit var binding: FragmentMoneyTransferBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentMoneyTransferBinding.inflate(layoutInflater, container, false)
        // Inflate the layout for this fragment
        val view = binding.root

        val fragmentManager = activity?.supportFragmentManager

        if (MainActivity.getSecurityLevel() <= 1) {
            PinCodeFragment.activityFrom = "Transfer"

            fragmentManager?.let {
                it.beginTransaction()
                    .replace(R.id.frameLayoutContainer, PinCodeFragment())
                    .addToBackStack(null)
                    .commit()
            }
        }

        binding.transferSubmit.setOnClickListener {
            fragmentManager?.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)
        }

        binding.transferCancel.setOnClickListener {
            fragmentManager?.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)
        }


        return view
    }



}
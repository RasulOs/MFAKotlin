package com.example.mfagit

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.mfagit.data.DBHelper
import com.example.mfagit.databinding.FragmentMenuCategoriesBinding


class MenuCategoriesFragment : Fragment() {

    private lateinit var binding: FragmentMenuCategoriesBinding

    private lateinit var dbHelper: DBHelper

    companion object {
        private var name = ""

        fun setName(name: String) {
            this.name = name
        }
    }



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentMenuCategoriesBinding.inflate(layoutInflater, container, false)
        // Inflate the layout for this fragment
        val view = binding.root

        dbHelper = DBHelper(requireContext())

        dbHelper.initializeName(MainActivity.getAccountEmail()).also {
            if (name.equals(""))
                name = "admin"
        }

        binding.menuTitle.text = "Welcome, $name"

        val fragmentManager = activity?.supportFragmentManager

        binding.menuBalance.setOnClickListener {
            fragmentManager?.let {
                it.beginTransaction()
                    .replace(R.id.frameLayoutContainer, BalanceFragment())
                    .addToBackStack(null)
                    .commit()
            }
        }

        binding.menuMoneyTransfer.setOnClickListener {
            fragmentManager?.let {
                it.beginTransaction()
                    .replace(R.id.frameLayoutContainer, MoneyTransferFragment())
                    .addToBackStack(null)
                    .commit()
            }
        }

        binding.menuPassword.setOnClickListener {
            fragmentManager?.let {
                it.beginTransaction()
                    .replace(R.id.frameLayoutContainer, PasswordChangeFragment())
                    .addToBackStack(null)
                    .commit()
            }
        }

        return view
    }

    override fun onResume() {
        super.onResume()

        binding.menuSeurity.text = "Your security level is: ${MainActivity.getSecurityLevel()}"
    }



}
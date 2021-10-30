package com.todo.app.auth

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.android.material.button.MaterialButton
import com.todo.app.DisplayActivity
import com.todo.app.MainActivity
import com.todo.app.R
import com.todo.app.network.RequestState
import com.todo.app.prefs.Preferences

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [WelcomeFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class WelcomeFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private lateinit var prefs: Preferences
    private lateinit var vm: AuthViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        prefs = Preferences(requireContext())
        vm = ViewModelProvider(requireActivity()).get(AuthViewModel::class.java)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }


    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_welcome, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val login = view.findViewById<MaterialButton>(R.id.btn_login)
        val register = view.findViewById<MaterialButton>(R.id.btn_register)

        login.setOnClickListener {
            findNavController().navigate(R.id.action_welcomeFragment_to_loginFragment)
        }

        register.setOnClickListener {
            findNavController().navigate(R.id.action_welcomeFragment_to_registerFragment)
        }

        if (prefs.token != null && MainActivity.isConnected(requireContext())) {
            vm.authCheck(prefs.token.toString())
            login.isEnabled = false
            register.isEnabled = false
        }

        vm.status.observe({ lifecycle }, { status ->
            when (status) {
                RequestState.REQUEST_START -> {
                    Toast.makeText(context, "Checking Authentication Start", Toast.LENGTH_SHORT)
                        .show()
                }
                RequestState.REQEUST_END -> {
                    Toast.makeText(context, "Checking Authentication Finish", Toast.LENGTH_SHORT)
                        .show()
                    login.isEnabled = true
                    register.isEnabled = true
                }
                RequestState.REQUEST_ERROR -> {
                    Toast.makeText(context, "Something Error : ", Toast.LENGTH_SHORT).show()
                    login.isEnabled = true
                    register.isEnabled = true
                }
            }
        })

        vm.data.observe({ lifecycle }, { data ->
            Log.e("DATA", data.toString())
            if (data.status == true) {
                Toast.makeText(context, "Authentication Successfull, Welcome", Toast.LENGTH_SHORT)
                    .show()
                val intent = Intent(context, DisplayActivity::class.java)
                startActivity(intent)
            } else {
                Toast.makeText(
                    context,
                    "You are not authenticated, please login or register",
                    Toast.LENGTH_SHORT
                ).show()
            }
        })

        vm.error.observe({ lifecycle }, { error ->
            Toast.makeText(context, "Something Error : ", Toast.LENGTH_SHORT).show()
            Toast.makeText(context, error, Toast.LENGTH_SHORT).show()
        })
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment WelcomeFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            WelcomeFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}
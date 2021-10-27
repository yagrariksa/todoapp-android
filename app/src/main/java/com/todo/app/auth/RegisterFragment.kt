package com.todo.app.auth

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.navigation.fragment.findNavController
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputLayout
import com.todo.app.DisplayActivity
import com.todo.app.MainActivity
import com.todo.app.R

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [RegisterFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class RegisterFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity?.onBackPressedDispatcher?.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                activity?.finish()
            }

        })
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
        return inflater.inflate(R.layout.fragment_register, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val login = view.findViewById<MaterialButton>(R.id.btn_login)

        login.setOnClickListener {
            findNavController().navigate(R.id.action_registerFragment_to_loginFragment)
        }

        val inputName = view.findViewById<TextInputLayout>(R.id.input_nama)
        val inputEmail = view.findViewById<TextInputLayout>(R.id.input_email)
        val inputPassword = view.findViewById<TextInputLayout>(R.id.input_password)

        val register = view.findViewById<MaterialButton>(R.id.btn_register)

        register.isEnabled = false

        register.setOnClickListener {
            // Request API
            if (MainActivity.isConnected(requireContext())) {
                val intent = Intent(context, DisplayActivity::class.java)
                startActivity(intent)
                activity?.finish()
            } else {
                Toast.makeText(context, "No Internet Connection", Toast.LENGTH_SHORT).show()
            }
        }

        inputName.editText?.addTextChangedListener(object :
            MyTextWatcher(register, inputEmail, inputName, inputPassword) {})

        inputEmail.editText?.addTextChangedListener(object :
            MyTextWatcher(register, inputEmail, inputName, inputPassword) {})

        inputPassword.editText?.addTextChangedListener(object :
            MyTextWatcher(register, inputEmail, inputName, inputPassword) {})

    }

    open inner class MyTextWatcher(
        val register: MaterialButton,
        val inputEmail: TextInputLayout,
        val inputName: TextInputLayout,
        val inputPassword: TextInputLayout
    ) : TextWatcher {
        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
        }

        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
        }

        override fun afterTextChanged(p0: Editable?) {
            register.isEnabled = !(inputName.editText?.text.toString().isNullOrEmpty() ||
                    inputEmail.editText?.text.toString().isNullOrEmpty() ||
                    inputPassword.editText?.text.toString().isNullOrEmpty() ||
                    !android.util.Patterns.EMAIL_ADDRESS.matcher(inputEmail.editText?.text.toString())
                        .matches()
                    )
        }
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment RegisterFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            RegisterFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}
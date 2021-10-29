package com.todo.app.auth

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.core.content.ContextCompat.getSystemService
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputLayout
import com.google.android.material.textview.MaterialTextView
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
 * Use the [LoginFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class LoginFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private lateinit var vm: AuthViewModel
    private lateinit var prefs: Preferences

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

        vm = ViewModelProvider(requireActivity()).get(AuthViewModel::class.java)
        prefs = Preferences(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_login, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        val register = view.findViewById<MaterialButton>(R.id.btn_register)

        register.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_registerFragment)
        }

        val login = view.findViewById<MaterialButton>(R.id.btn_login)

        val inputEmail = view.findViewById<TextInputLayout>(R.id.input_email)
        val inputPassword = view.findViewById<TextInputLayout>(R.id.input_password)

        login.isEnabled = false

        login.setOnClickListener {
            activity?.currentFocus.let { v ->
                val imm = activity?.getSystemService(Activity.INPUT_METHOD_SERVICE) as? InputMethodManager
                imm?.hideSoftInputFromWindow(v?.windowToken, 0)
            }
            if (MainActivity.isConnected(requireContext())) {
                vm.doLogin(
                    email = inputEmail.editText?.text.toString(),
                    password = inputPassword.editText?.text.toString()
                )
            } else {
                Toast.makeText(context, "No Internet Connection", Toast.LENGTH_SHORT).show()
            }
        }

        inputEmail.editText?.addTextChangedListener(object :
            MyTextWatcher(login, inputPassword, inputEmail) {})

        inputPassword.editText?.addTextChangedListener(object :
            MyTextWatcher(login, inputPassword, inputEmail) {})

        vm.data.observe({ lifecycle }, { data ->
            Log.e("API", data.toString())
            if (data.status == true) {
                prefs.token = data.data?.token.toString()
                prefs.userName = data.data?.name
                prefs.userId = data.data?.id.toString()

                val intent = Intent(context, DisplayActivity::class.java)
                Toast.makeText(context, data.message, Toast.LENGTH_SHORT).show()
                startActivity(intent)
                activity?.finish()
            } else {
                Toast.makeText(context, "Gagal Login", Toast.LENGTH_SHORT).show()
                Toast.makeText(context, data.message, Toast.LENGTH_SHORT).show()
            }
        })

        vm.error.observe({ lifecycle }, { error ->
            Toast.makeText(context, error, Toast.LENGTH_SHORT).show()
        })

        vm.status.observe({ lifecycle }, { status ->
            when (status) {
                RequestState.REQUEST_ERROR -> {
                    login.isEnabled = true

                }

                RequestState.REQUEST_START -> {
                    login.isEnabled = false

                }

                RequestState.REQEUST_END -> {
                    login.isEnabled = true

                }
            }
        })
    }

    open inner class MyTextWatcher(
        val login: MaterialButton,
        val password: TextInputLayout,
        val email: TextInputLayout
    ) : TextWatcher {
        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

        override fun afterTextChanged(p0: Editable?) {
            login.isEnabled = !(password.editText?.text.toString().isNullOrEmpty() ||
                    email.editText?.text.toString().isNullOrEmpty() ||
                    !android.util.Patterns.EMAIL_ADDRESS.matcher(email.editText?.text.toString())
                        .matches())
        }
    }


    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment LoginFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            LoginFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}
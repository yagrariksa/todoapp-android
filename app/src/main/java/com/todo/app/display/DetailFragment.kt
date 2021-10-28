package com.todo.app.display

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.ImageButton
import androidx.constraintlayout.widget.ConstraintLayout
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputLayout
import com.todo.app.R
import com.todo.app.models.Todo

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [DetailFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class DetailFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private var todo_id: Int = 0
    private lateinit var todoItem: Todo

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }

        // catch ID
        todo_id = arguments?.getInt("todo_id")!!
        Log.e("ID", todo_id.toString())
        // fetch One Todo Item
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val sectionEdit = view.findViewById<ConstraintLayout>(R.id.constraint_edit)
        val sectionView = view.findViewById<ConstraintLayout>(R.id.constraint_view)

        val inputNama = view.findViewById<TextInputLayout>(R.id.input_nama)
        val inputUrl = view.findViewById<TextInputLayout>(R.id.input_url)
        val inputDay = view.findViewById<TextInputLayout>(R.id.input_day)

        val items = listOf("Material", "Design", "Components", "Android")
        val adapter = ArrayAdapter(requireContext(), R.layout.list_item, items)
        (inputDay.editText as? AutoCompleteTextView)?.setAdapter(adapter)

        val btnClose = view.findViewById<ImageButton>(R.id.btn_close)

        val btnDelete = view.findViewById<MaterialButton>(R.id.btn_delete)
        val btnEdit = view.findViewById<MaterialButton>(R.id.btn_edit)

        val btnSave = view.findViewById<MaterialButton>(R.id.btn_save)
        val btnCancel = view.findViewById<MaterialButton>(R.id.btn_cancel)

        val inputs = listOf<TextInputLayout>(inputNama, inputUrl, inputDay)
        inputs.forEach {
            it.editText?.addTextChangedListener(object : MyTextWatcher(
                btnSave,
                inputNama,
                inputUrl,
                inputDay
            ) {})
        }

        // check for Todo Item Availability
        if (todo_id != 0) {
            sectionEdit.visibility = View.GONE
            sectionView.visibility = View.VISIBLE
            // fetch data to section View
        } else {
            sectionEdit.visibility = View.VISIBLE
            sectionView.visibility = View.GONE

        }

        /**
         * Section View
         */
        btnEdit.setOnClickListener {
            sectionEdit.visibility = View.VISIBLE
            sectionView.visibility = View.GONE
            // fetch data to input-field
        }

        btnDelete.setOnClickListener {
            // request to Delete Todo Item
        }

        btnClose.setOnClickListener {
            activity?.onBackPressed()
        }

        /**
         * Section Edit
         */
        if (todo_id != 0) {
            btnCancel.setOnClickListener {
                sectionEdit.visibility = View.GONE
                sectionView.visibility = View.VISIBLE
            }
            btnSave.setOnClickListener {
                // request save Todo Item with ID
            }
        } else {
            btnSave.isEnabled = false
            btnCancel.setOnClickListener {
                activity?.onBackPressed()
            }
            btnSave.setOnClickListener {
                // request post new Todo Item
            }
        }
    }

    open inner class MyTextWatcher(
        val btn: MaterialButton,
        val nama: TextInputLayout,
        val url: TextInputLayout,
        val day: TextInputLayout
    ) : TextWatcher {
        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

        override fun afterTextChanged(p0: Editable?) {
            btn.isEnabled = !(nama.editText?.text.toString().isNullOrEmpty() ||
                    url.editText?.text.toString().isNullOrEmpty() ||
                    (day.editText as? AutoCompleteTextView)?.text.toString().isNullOrEmpty()
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
         * @return A new instance of fragment DetailFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            DetailFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}
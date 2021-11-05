package com.todo.app.display

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.activity.OnBackPressedCallback
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputLayout
import com.google.android.material.textview.MaterialTextView
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import com.todo.app.R
import com.todo.app.models.Todo
import com.todo.app.network.RequestState
import com.todo.app.prefs.Preferences
import java.time.DayOfWeek

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

    private var action: String? = null

    private val items = listOf("Senin", "Selasa", "Rabu", "Kamis", "Jumat", "Sabtu", "Minggu")

    private lateinit var todoItem: Todo
    private lateinit var prefs: Preferences
    private lateinit var vm: TodoViewModel

    private lateinit var sectionEdit: ConstraintLayout
    private lateinit var sectionView: ConstraintLayout

    private lateinit var spinnerView: ProgressBar
    private lateinit var spinnerEdit: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
        activity?.onBackPressedDispatcher?.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (sectionEdit.visibility == View.VISIBLE && todo_id != 0) {
                    sectionEdit.visibility = View.GONE
                    sectionView.visibility = View.VISIBLE
                } else {
                    todo_id = 0
                    findNavController().popBackStack()
                }
            }

        })

        todo_id = arguments?.getInt("todo_id")!!
        Log.e("ID", todo_id.toString())

        prefs = Preferences(requireContext())
        vm = ViewModelProvider(requireActivity()).get(TodoViewModel::class.java)
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

        var hour = 0
        var minute = 0

        sectionEdit = view.findViewById(R.id.constraint_edit)
        sectionView = view.findViewById(R.id.constraint_view)

        spinnerView = view.findViewById(R.id.pgb_view)
        spinnerEdit = view.findViewById(R.id.pgb_edit)

        val inputNama = view.findViewById<TextInputLayout>(R.id.input_nama)
        val inputUrl = view.findViewById<TextInputLayout>(R.id.input_url)
        val inputDay = view.findViewById<TextInputLayout>(R.id.input_day)
        val btnTimePicker = view.findViewById<MaterialButton>(R.id.btn_picker)
        val tvClock = view.findViewById<MaterialTextView>(R.id.tv_clock)

        val timePicker =
            MaterialTimePicker.Builder()
                .setTimeFormat(TimeFormat.CLOCK_24H)
                .setHour(12)
                .setMinute(10)
                .setTitleText("Pilih jam")
                .build()

        val tvNama = view.findViewById<MaterialTextView>(R.id.tv_name)
        val tvUrl = view.findViewById<MaterialTextView>(R.id.tv_url)
        val tvDay = view.findViewById<MaterialTextView>(R.id.tv_day)
        val tvDisplayClock = view.findViewById<MaterialTextView>(R.id.tv_display_clock)

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
            prefs.token?.let {
                vm.getOne(todo_id, it)
                action = "getOne"
            }
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
        }

        btnDelete.setOnClickListener {
            spinnerView.visibility = View.VISIBLE
            btnEdit.isEnabled = false
            btnDelete.isEnabled = false

            action = "delete"
            vm.delete(todo_id, prefs.token.toString())
        }

        btnClose.setOnClickListener {
            activity?.onBackPressed()
        }

        /**
         * Section Edit
         */
        btnTimePicker.setOnClickListener {
            timePicker.show(parentFragmentManager, "TPICK")
        }

        timePicker.addOnPositiveButtonClickListener {
            tvClock.text = timePicker.hour.toString() + ":" + timePicker.minute.toString()
            hour = timePicker.hour
            minute = timePicker.minute
        }
        if (todo_id != 0) {
            btnCancel.setOnClickListener {
                sectionEdit.visibility = View.GONE
                sectionView.visibility = View.VISIBLE
            }
            btnSave.setOnClickListener {
                // request save Todo Item with ID
                spinnerEdit.visibility = View.VISIBLE
                btnCancel.isEnabled = false
                btnSave.isEnabled = false

                action = "save"
                vm.update(
                    name = inputNama.editText?.text.toString(),
                    url = "http://" + inputUrl.editText?.text.toString(),
                    day = dayToInt(inputDay.editText?.text.toString()),
                    id = todo_id,
                    pref = prefs.token.toString(),
                    hour = hour,
                    minute = minute
                )
            }
        } else {
            btnSave.isEnabled = false
            btnCancel.setOnClickListener {
                activity?.onBackPressed()
            }
            btnSave.setOnClickListener {
                // request post new Todo Item
                spinnerEdit.visibility = View.VISIBLE
                btnCancel.isEnabled = false
                btnSave.isEnabled = false

                action = "create"
                vm.create(
                    name = inputNama.editText?.text.toString(),
                    url = "http://" + inputUrl.editText?.text.toString(),
                    day = dayToInt(inputDay.editText?.text.toString()),
                    pref = prefs.token.toString(),
                    hour = hour,
                    minute = minute
                )
            }
        }

        /**
         * View Model
         */
        vm.status.observe({ lifecycle }, { status ->
            when (status) {
                RequestState.REQUEST_START -> {

                }

                RequestState.REQEUST_END -> {
                    if (action == "delete") {
                        spinnerView.visibility = View.GONE
                        btnEdit.isEnabled = true
                        btnDelete.isEnabled = true
                    } else if (action == "save" || action == "create") {
                        spinnerEdit.visibility = View.GONE
                        btnCancel.isEnabled = true
                        btnSave.isEnabled = true
                    }
                }

                RequestState.REQUEST_ERROR -> {
                    if (action == "delete") {
                        spinnerView.visibility = View.GONE
                        btnEdit.isEnabled = true
                        btnDelete.isEnabled = true
                    } else if (action == "save" || action == "create") {
                        spinnerEdit.visibility = View.GONE
                        btnCancel.isEnabled = true
                        btnSave.isEnabled = true
                    }
                }
            }
        })

        vm.one.observe({ lifecycle }, { data ->
            Log.e("DATA", data.toString())
            Log.e("ACTION", action.toString())
            if (data.status == true && todo_id != 0 && action != "delete" && data.data != null) {
                todoItem = data.data
                todo_id = data.data.id!!
                inputNama.editText?.setText(data.data.name)
                tvNama.text = data.data.name
                inputUrl.editText?.setText(data.data.url?.split("//")?.get(1))
                tvUrl.text = data.data.url
                (inputDay.editText as? AutoCompleteTextView)?.setText(
                    (inputDay.editText as? AutoCompleteTextView)?.getAdapter()?.getItem(
                        data.data.day!!
                    ).toString(), false
                )
                tvDay.text = intToDay(data.data.day)
                data.data.hour?.let { hour = it }
                data.data.minute?.let { minute = it }
                tvClock.text = hour.toString() + ":" + minute.toString()
                tvDisplayClock.text = hour.toString() + ":" + minute.toString()

            } else if (data.status == true && action == "delete") {
                Toast.makeText(context, data.message, Toast.LENGTH_SHORT).show()
                findNavController().popBackStack()
            } else {
                Toast.makeText(context, data.message, Toast.LENGTH_SHORT).show()
            }

            if (action == "save") {
                sectionEdit.visibility = View.GONE
                sectionView.visibility = View.VISIBLE
            } else if (action == "create") {
                findNavController().popBackStack()
            }

        })

        vm.error.observe({ lifecycle }, { error ->
            Toast.makeText(context, "Something error : " + error, Toast.LENGTH_SHORT).show()
        })
    }

    fun intToDay(d: Int?): String {
        when (d) {
            0 -> {
                return "Senin"
            }
            1 -> {
                return "Selasa"
            }
            2 -> {
                return "Rabu"
            }
            3 -> {
                return "Kamis"
            }
            4 -> {
                return "Jumat"
            }
            5 -> {
                return "Sabtu"
            }
            6 -> {
                return "Minggu"
            }
            else -> {
                return ""
            }
        }

    }

    fun dayToInt(d: String?): Int {
        when (d) {
            items[0] -> {
                return 0
            }
            items[1] -> {
                return 1
            }
            items[2] -> {
                return 2
            }
            items[3] -> {
                return 3
            }
            items[4] -> {
                return 4
            }
            items[5] -> {
                return 5
            }
            items[6] -> {
                return 6
            }
            else -> {
                return 0
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
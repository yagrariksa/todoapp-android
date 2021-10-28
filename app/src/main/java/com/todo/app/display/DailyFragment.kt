package com.todo.app.display

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.button.MaterialButton
import com.google.android.material.button.MaterialButtonToggleGroup
import com.todo.app.R
import nl.bryanderidder.themedtogglebuttongroup.ThemedToggleButtonGroup

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [DailyFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class DailyFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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
        return inflater.inflate(R.layout.fragment_daily, container, false)
    }

    private var layoutManager: RecyclerView.LayoutManager? = null
    private var adapter: TodoAdapter? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val btnClose = view.findViewById<ImageButton>(R.id.btn_close)
        btnClose.setOnClickListener {
            activity?.onBackPressed()
        }

        val btnAddTodo = view.findViewById<MaterialButton>(R.id.btn_add_todo)
        btnAddTodo.setOnClickListener {
            findNavController().navigate(R.id.action_dailyFragment_to_detailFragment)
        }

        val rvDaily = view.findViewById<RecyclerView>(R.id.rv_daily)
        rvDaily.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = TodoAdapter(R.id.action_dailyFragment_to_detailFragment)
        }

        val btnDailyGroup = view.findViewById<ThemedToggleButtonGroup>(R.id.toggle_btn_daily)
        btnDailyGroup.selectButton(R.id.btn_day_0)
        btnDailyGroup.setOnSelectListener {
            when (it.id) {
                R.id.btn_day_0 -> {
                    Log.d("BTN", "Senin")
                }
                R.id.btn_day_1 -> {
                    Log.d("BTN", "Selasa")
                }
                else -> {
                    Log.d("BTN", "Another")
                }
            }
        }


    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment DailyFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            DailyFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}
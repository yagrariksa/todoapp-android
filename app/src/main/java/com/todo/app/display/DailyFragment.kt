package com.todo.app.display

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ProgressBar
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.android.material.button.MaterialButton
import com.google.android.material.button.MaterialButtonToggleGroup
import com.todo.app.R
import com.todo.app.network.RequestState
import com.todo.app.prefs.Preferences
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

    private lateinit var mAdapter: TodoAdapter
    private lateinit var prefs: Preferences
    private lateinit var vm: TodoViewModel
    private var day: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
        mAdapter = TodoAdapter(R.id.action_dailyFragment_to_detailFragment)
        prefs = Preferences(requireContext())
        vm = ViewModelProvider(requireActivity()).get(TodoViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_daily, container, false)
    }

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
            adapter = mAdapter
        }

        val btnDailyGroup = view.findViewById<ThemedToggleButtonGroup>(R.id.toggle_btn_daily)
        btnDailyGroup.selectButton(R.id.btn_day_0)
        btnDailyGroup.setOnSelectListener {
            when (it.id) {
                R.id.btn_day_0 -> {
                    day = 0
                }
                R.id.btn_day_1 -> {
                    day = 1
                }
                R.id.btn_day_2 -> {
                    day = 2
                }
                R.id.btn_day_3 -> {
                    day = 3
                }
                R.id.btn_day_4 -> {
                    day = 4
                }
                R.id.btn_day_5 -> {
                    day = 5
                }
                R.id.btn_day_6 -> {
                    day = 6
                }
            }
            vm.getAll(day, prefs.token.toString())
        }

        val spinner = view.findViewById<ProgressBar>(R.id.pgb)

        val swipeRefreshLayout = view.findViewById<SwipeRefreshLayout>(R.id.swipe_refresh)
        swipeRefreshLayout.setOnRefreshListener {
            vm.getAll(day, prefs.token.toString())
        }

        vm.status.observe({ lifecycle }, { status ->
            when (status) {
                RequestState.REQUEST_START -> {
                    mAdapter.clearData()
                    if (!swipeRefreshLayout.isRefreshing) {
                        spinner.visibility = View.VISIBLE
                    }
                }
                RequestState.REQEUST_END -> {
                    if (swipeRefreshLayout.isRefreshing) {
                        Toast.makeText(context, "finish collect data", Toast.LENGTH_SHORT)
                            .show()
                        swipeRefreshLayout.isRefreshing = false
                    } else {
                        spinner.visibility = View.GONE
                    }
                }
                RequestState.REQUEST_ERROR -> {
                    if (swipeRefreshLayout.isRefreshing) {
                        Toast.makeText(context, "finish collect data", Toast.LENGTH_SHORT)
                            .show()
                        swipeRefreshLayout.isRefreshing = false
                    } else {
                        spinner.visibility = View.GONE
                    }
                }
            }
        })

        vm.data.observe({ lifecycle }, { data ->
            Log.e("DATA", data.toString())
            if (data.status == true) {
                data.data?.let { mAdapter.supplyData(it) }
            } else {
                Toast.makeText(context, data.message, Toast.LENGTH_SHORT).show()
            }
        })

        vm.error.observe({ lifecycle }, { error ->
            Toast.makeText(context, "Something error : " + error, Toast.LENGTH_SHORT).show()
        })

        vm.getAll(day, prefs.token.toString())

    }

    override fun onResume() {
        super.onResume()
        vm.getAll(day, prefs.token.toString())
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
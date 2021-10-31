package com.todo.app.display

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.android.material.button.MaterialButton
import com.google.android.material.textview.MaterialTextView
import com.todo.app.R
import com.todo.app.network.RequestState
import com.todo.app.prefs.Preferences
import java.time.DayOfWeek
import java.time.LocalDate
import java.util.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [MainFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class MainFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private lateinit var mAdapter: TodoAdapter
    private lateinit var prefs: Preferences
    private lateinit var vm: TodoViewModel

    private lateinit var calendar: Calendar
    private var dayOfWeek: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }

        mAdapter = TodoAdapter(R.id.action_mainFragment_to_detailFragment)
        prefs = Preferences(requireContext())

        calendar = Calendar.getInstance()
        when (calendar.get(Calendar.DAY_OF_WEEK)) {
            Calendar.MONDAY -> {
                dayOfWeek = 0
            }
            Calendar.TUESDAY -> {
                dayOfWeek = 1
            }
            Calendar.WEDNESDAY -> {
                dayOfWeek = 2
            }
            Calendar.THURSDAY -> {
                dayOfWeek = 3
            }
            Calendar.FRIDAY -> {
                dayOfWeek = 4
            }
            Calendar.SATURDAY -> {
                dayOfWeek = 5
            }
            Calendar.SUNDAY -> {
                dayOfWeek = 6
            }
        }

        vm = ViewModelProvider(requireActivity()).get(TodoViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_main, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val tvTitle = view.findViewById<MaterialTextView>(R.id.tv_title)
        tvTitle.text = "Halo " + prefs.userName.toString()

        val btnDaily = view.findViewById<MaterialButton>(R.id.btn_jadwal_harian)
        btnDaily.setOnClickListener {
            findNavController().navigate(R.id.action_mainFragment_to_dailyFragment)
        }

        val btnMenu = view.findViewById<ImageButton>(R.id.btn_menu)
        btnMenu.setOnClickListener {
            findNavController().navigate(R.id.action_mainFragment_to_menuFragment)
        }

        val rvMain = view.findViewById<RecyclerView>(R.id.rv_main)
        rvMain.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = mAdapter
        }

        val swipeRefreshLayout = view.findViewById<SwipeRefreshLayout>(R.id.swipe_refresh)
        swipeRefreshLayout.setOnRefreshListener {
            vm.getAll(dayOfWeek, prefs.token.toString())
        }

        vm.status.observe({ lifecycle }, { status ->
            when (status) {
                RequestState.REQUEST_START -> {
                }
                RequestState.REQEUST_END -> {
                    if (swipeRefreshLayout.isRefreshing) {
                        Toast.makeText(context, "finish collect data", Toast.LENGTH_SHORT)
                            .show()
                    }
                    swipeRefreshLayout.isRefreshing = false
                }
                RequestState.REQUEST_ERROR -> {
                    swipeRefreshLayout.isRefreshing = false
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

        vm.getAll(dayOfWeek, prefs.token.toString())
    }

    override fun onResume() {
        super.onResume()
        vm.getAll(dayOfWeek, prefs.token.toString())
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment MainFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            MainFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}
package com.todo.app.display

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.button.MaterialButton
import com.google.android.material.textview.MaterialTextView
import com.todo.app.R
import com.todo.app.prefs.Preferences

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

    private lateinit var mLayoutManager: RecyclerView.LayoutManager
    private lateinit var mAdapter: TodoAdapter
    private lateinit var prefs: Preferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }

        mLayoutManager = LinearLayoutManager(context)
        mAdapter = TodoAdapter(R.id.action_mainFragment_to_detailFragment)
        prefs = Preferences(requireContext())
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
            layoutManager = mLayoutManager
            adapter = mAdapter
        }
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
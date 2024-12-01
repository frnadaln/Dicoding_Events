package com.dicoding.dicodingevents.ui.active

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.dicodingevents.databinding.FragmentActiveBinding
import com.dicoding.dicodingevents.helper.Result
import com.dicoding.dicodingevents.adapter.EventAdapter
import com.dicoding.dicodingevents.viewmodel.EventViewModel
import com.dicoding.dicodingevents.viewmodel.EventViewModelFactory


class ActiveEvent : Fragment() {

    private var _binding: FragmentActiveBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: EventAdapter
    private val viewmodel by viewModels<EventViewModel>{
        EventViewModelFactory.getInstance(requireActivity())
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentActiveBinding.inflate(inflater, container, false)
        val root = binding.root

        adapter = EventAdapter(EventAdapter.VIEW_TYPE_ACTIVE_AT_HOME)
        binding.activeEvent.layoutManager = LinearLayoutManager(context)
        binding.activeEvent.adapter = adapter

        with(binding) {
            searchView.setupWithSearchBar(searchBar)
            searchView
                .editText
                .setOnEditorActionListener { _,_,_ ->
                    val query = searchView.text.toString()
                    searchBar.setText(query)
                    searchView.hide()
                    viewmodel.searchEvent(query).observe(viewLifecycleOwner) {event ->
                        when(event)  {
                            is Result.Loading -> {
                                binding.progressBar1.visibility = View.VISIBLE
                            }
                            is Result.Success -> {
                                binding.progressBar1.visibility = View.GONE
                                adapter.submitList(event.data)
                            }
                            is Result.Error -> {
                                binding.apply {
                                    errorMessage.text = event.error
                                    errorMessage.visibility = View.VISIBLE
                                    titleActive.visibility = View.GONE
                                    progressBar1.visibility = View.GONE
                                    searchBar.visibility = View.GONE
                                    searchView.visibility = View.GONE
                                    activeEvent.visibility = View.GONE
                                }
                            }
                        }
                        Log.d("eventDataSearch", event.toString())
                    }
                    false
                }
        }
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewmodel.getActiveEvent().observe(viewLifecycleOwner) {event ->
            when(event) {
                is Result.Loading -> {
                    binding.progressBar1.visibility = View.VISIBLE
                }
                is Result.Success -> {
                    binding.progressBar1.visibility = View.GONE
                    adapter.submitList(event.data)
                }
                is Result.Error -> {
                    binding.apply {
                        Toast.makeText(context, "No internet connection", Toast.LENGTH_SHORT).show()
                        errorMessage.text = event.error
                        errorMessage.visibility = View.VISIBLE
                        progressBar1.visibility = View.GONE
                    }
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

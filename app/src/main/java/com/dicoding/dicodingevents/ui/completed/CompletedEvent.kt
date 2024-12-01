package com.dicoding.dicodingevents.ui.completed

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.dicodingevents.databinding.FragmentCompletedBinding
import com.dicoding.dicodingevents.helper.Result
import com.dicoding.dicodingevents.adapter.EventAdapter
import com.dicoding.dicodingevents.viewmodel.EventViewModel
import com.dicoding.dicodingevents.viewmodel.EventViewModelFactory

class CompletedEvent : Fragment() {

    private var _binding: FragmentCompletedBinding? = null
    private val binding get() = _binding!!
    private lateinit var eventAdapter: EventAdapter
    private val viewmodel by viewModels<EventViewModel>{
        EventViewModelFactory.getInstance(requireActivity())
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCompletedBinding.inflate(inflater, container, false)
        val root: View = binding.root

        eventAdapter = EventAdapter(EventAdapter.VIEW_TYPE_COMPLETED_AT_HOME)
        binding.completedEvent.layoutManager = LinearLayoutManager(context)
        binding.completedEvent.adapter = eventAdapter

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
                                eventAdapter.submitList(event.data)
                            }
                            is Result.Error -> {
                                binding.apply {
                                    errorMessage.text = event.error
                                    errorMessage.visibility = View.VISIBLE
                                    titleCompleted.visibility = View.GONE
                                    progressBar1.visibility = View.GONE
                                    searchBar.visibility = View.GONE
                                    searchView.visibility = View.GONE
                                    completedEvent.visibility = View.GONE
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

        viewmodel.getCompletedEvent().observe(viewLifecycleOwner) {event ->
            when(event)  {
                is Result.Loading -> {
                    binding.progressBar1.visibility = View.VISIBLE
                }
                is Result.Success -> {
                    binding.progressBar1.visibility = View.GONE
                    eventAdapter.submitList(event.data)
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
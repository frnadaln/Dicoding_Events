package com.dicoding.dicodingevents.ui.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.dicodingevents.databinding.FragmentHomeBinding
import com.dicoding.dicodingevents.helper.Result
import com.dicoding.dicodingevents.adapter.EventAdapter
import com.dicoding.dicodingevents.viewmodel.EventViewModel
import com.dicoding.dicodingevents.viewmodel.EventViewModelFactory


class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private lateinit var completedAdapter: EventAdapter
    private lateinit var activeAdapter : EventAdapter
    private val viewmodel by viewModels<EventViewModel>{
        EventViewModelFactory.getInstance(requireActivity())
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        viewmodel.getActiveEvent().observe(viewLifecycleOwner) {event->
            when(event)  {
                is Result.Loading -> {
                    binding.progressBar1.visibility = View.VISIBLE
                }
                is Result.Success -> {
                    binding.progressBar1.visibility = View.GONE
                    completedAdapter.submitList(event.data)
                }
                is Result.Error -> {
                    binding.apply {
                        Toast.makeText(context, "No internet connection", Toast.LENGTH_SHORT).show()
                        errorMessage.text = event.error
                        errorMessage.visibility = View.VISIBLE
                        progressBar1.visibility = View.GONE
                        progressBar2.visibility = View.GONE
                    }
                }
            }
        }

        viewmodel.getCompletedEvent().observe(viewLifecycleOwner) {event->
            Log.d("eventData : ", event.toString())
            when(event)  {
                is Result.Loading -> {
                    binding.progressBar1.visibility = View.VISIBLE
                }
                is Result.Success -> {
                    binding.progressBar2.visibility = View.GONE
                    activeAdapter.submitList(event.data.take(5).shuffled())
                }
                is Result.Error -> {
                    binding.apply {
                        Toast.makeText(context, "No internet connection", Toast.LENGTH_SHORT).show()
                        errorMessage.text = event.error
                        errorMessage.visibility = View.VISIBLE
                        progressBar1.visibility = View.GONE
                        progressBar2.visibility = View.GONE
                    }
                }
            }
        }

        setRv()
        return root
    }

    private fun setRv() {
        activeAdapter = EventAdapter(EventAdapter.VIEW_TYPE_COMPLETED_AT_HOME)
        completedAdapter = EventAdapter(EventAdapter.VIEW_TYPE_ACTIVE_AT_HOME)
        binding.completedEvent.layoutManager =LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        binding.activeEvent.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        binding.activeEvent.adapter = activeAdapter
        binding.completedEvent.adapter = completedAdapter
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

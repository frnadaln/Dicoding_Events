package com.dicoding.dicodingevents.ui.favorite

import android.annotation.SuppressLint
import androidx.fragment.app.viewModels
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.dicodingevents.databinding.FragmentFavoriteBinding
import com.dicoding.dicodingevents.adapter.EventAdapter
import com.dicoding.dicodingevents.viewmodel.EventViewModel
import com.dicoding.dicodingevents.viewmodel.EventViewModelFactory

class EventFavorite : Fragment() {
    private var _binding : FragmentFavoriteBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter : EventAdapter
    private val viewmodel by viewModels<EventViewModel> {
        EventViewModelFactory.getInstance(requireActivity())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFavoriteBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = EventAdapter(EventAdapter.VIEW_TYPE_ACTIVE_AT_HOME)
        binding.favoriteEvent.adapter = adapter
        binding.favoriteEvent.layoutManager = LinearLayoutManager(context)
        binding.favoriteEvent.setHasFixedSize(true)

        viewmodel.getFavoriteEvent().observe(viewLifecycleOwner) {favEvent ->
            binding.progressBar1.visibility = View.GONE
            if(favEvent.isEmpty()) {
                binding.apply {
                    favoriteEvent.visibility = View.GONE
                    favoriteCount.visibility = View.GONE
                    emptyMessage.text = EMPTY_MESSAGE
                }
            }
            adapter.submitList(favEvent)
            binding.favoriteEvent
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    companion object {
        const val EMPTY_MESSAGE = "There are no Favorite Events yet, please add an Event"
    }

}

package com.dicoding.dicodingevents.ui.detail

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.text.HtmlCompat
import com.dicoding.dicodingevents.R
import com.dicoding.dicodingevents.database.EventEntity
import com.dicoding.dicodingevents.databinding.ActivityDetailBinding
import com.dicoding.dicodingevents.viewmodel.EventViewModel
import com.dicoding.dicodingevents.viewmodel.EventViewModelFactory
import com.squareup.picasso.Picasso

@Suppress("DEPRECATION")
class EventDetail: AppCompatActivity() {
    private lateinit var binding : ActivityDetailBinding
    private val viewmodel by viewModels<EventViewModel> {
        EventViewModelFactory.getInstance(application)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)


        val data = intent.getParcelableExtra<EventEntity>("EXTRA_EVENT")
        data?.let { observeViewModel(it) }
        supportActionBar?.title = "Detail Event"
    }

    private fun observeViewModel(event: EventEntity) {
        binding.apply {
            changeFavoriteIcon(event.isFavorite)
            progressBar1.visibility = View.GONE
            eventName.text = event.name
            eventDescription.text = HtmlCompat.fromHtml(
                event.description, HtmlCompat.FROM_HTML_MODE_LEGACY
            )
            ownerName.text = event.ownerName
            eventStartTime.text = event.beginTime
            eventEndTime.text = event.endTime
            eventQuota.text = event.quota.toString()
            remainingQuota.text = (event.quota - event.registrants).toString()
            Picasso
                .get()
                .load(event.mediaCover)
                .into(ivMediaCoverEvent)
            btnRegister.setOnClickListener {
                val linkIntent = Intent(Intent.ACTION_VIEW)
                linkIntent.data = Uri.parse(event.link)
                startActivity(linkIntent)
            }
            favoriteFab.setOnClickListener {
                event.isFavorite = !event.isFavorite
                changeFavoriteIcon(event.isFavorite)
                if(event.isFavorite) {
                    viewmodel.addEventToFavorite(event)
                    Toast.makeText(this@EventDetail, "Successfully added event to Favorites", Toast.LENGTH_SHORT).show()
                } else {
                    viewmodel.removeEventFromFavorite(event)
                    Toast.makeText(this@EventDetail, "Successfully removed event from Favorites", Toast.LENGTH_SHORT).show()

                }
            }
        }
    }

    private fun changeFavoriteIcon(isFavorite : Boolean) {
        binding.favoriteFab.setImageResource(
            if (isFavorite) {
                R.drawable.favorite_filled
            } else {
                R.drawable.favorite_border
            }
        )
    }
}

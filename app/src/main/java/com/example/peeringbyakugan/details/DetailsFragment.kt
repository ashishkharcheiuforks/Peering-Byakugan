package com.example.peeringbyakugan.details


import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebChromeClient
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.peeringbyakugan.AnimeWebViewClient
import com.example.peeringbyakugan.ByakuganApplication
import com.example.peeringbyakugan.R
import com.example.peeringbyakugan.databinding.FragmentDetailsBinding
import com.squareup.picasso.Picasso


class DetailsFragment : Fragment() {


    private lateinit var binding: FragmentDetailsBinding
    private lateinit var viewModel: DetailsViewModel
    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


        val args = arguments?.let { DetailsFragmentArgs.fromBundle(it) }
        val appComponent = ((this.activity!!.application) as ByakuganApplication).getAppComponent()
        (activity as AppCompatActivity).title = args?.animeTitle
        binding = DataBindingUtil.inflate(inflater,
            R.layout.fragment_details, container, false)
        val viewModelFactory =
            DetailsViewModelFactory(appComponent)
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(DetailsViewModel::class.java)

        viewModel.queryJikanForAnime(args!!.animeId)

        binding.youtubeWebView.apply {
            settings.javaScriptEnabled = true
            webChromeClient = WebChromeClient()
            webViewClient = AnimeWebViewClient(binding.trailerLoadingProgressBar)
        }

        viewModel.currentAnime.observe(this, Observer {


            binding.premierDateTextView.text =
                if (it.premiered.isNullOrBlank()) getString(R.string.premier_date, it.aired!!.string)
                else getString(R.string.premier_date, it.premiered)
            binding.ratingsTextView.text = getString(R.string.ratings, it.score.toString())
            binding.episodesTextView.text = getString(R.string.episodes, it.episodes.toString())
            binding.statusTextView.text = getString(R.string.status, it.status)


            if (it.trailerUrl.isNullOrBlank()) {
                binding.youtubeWebView.visibility = View.INVISIBLE
                binding.animeImageView.apply {
                    visibility = View.VISIBLE
                    Picasso.get().load(it.imageUrl).into(this)
                    binding.trailerLoadingProgressBar.visibility = View.INVISIBLE
                }
            } else {
                binding.youtubeWebView.loadUrl(it.trailerUrl)
            }

            binding.synopsisTextView.text = getString(R.string.synopsis, it.synopsis)

            (activity as AppCompatActivity).apply {
                if (title.isNullOrBlank()) title = it.titleEnglish
            }


        })



        return binding.root
    }


}
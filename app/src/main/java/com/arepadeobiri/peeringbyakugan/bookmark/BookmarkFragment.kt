package com.arepadeobiri.peeringbyakugan.bookmark


import android.content.res.Configuration
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.arepadeobiri.peeringbyakugan.ByakuganApplication
import com.arepadeobiri.peeringbyakugan.GenericViewModelFactory

import com.arepadeobiri.peeringbyakugan.R
import com.arepadeobiri.peeringbyakugan.databinding.FragmentBookmarkBinding
import com.google.android.material.snackbar.Snackbar


class BookmarkFragment : Fragment() {

    private lateinit var binding: FragmentBookmarkBinding
    private lateinit var viewModel: BookmarkViewModel
    private lateinit var databaseAnimeAdapter: BookmarkAnimeRecyclerAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        this.activity!!.title = getString(R.string.bookmarks)
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_bookmark, container, false)
        val viewModelFactory =
            GenericViewModelFactory(((this.activity!!.application) as ByakuganApplication).getAppComponent())

        viewModel = ViewModelProviders.of(this, viewModelFactory).get(BookmarkViewModel::class.java)

        databaseAnimeAdapter =
            BookmarkAnimeRecyclerAdapter(BookmarkAnimeClickListener { animeId, animeTitle ->
                when {
                    viewModel.isInternetConnection() -> this.findNavController()
                        .navigate(
                            BookmarkFragmentDirections.actionBookmarkFragmentToDetailsFragment(
                                animeId,
                                animeTitle
                            )
                        )
                    else -> Snackbar.make(
                        binding.root,
                        getString(R.string.error_internet),
                        Snackbar.LENGTH_LONG
                    ).show()
                }
            })


        binding.bookmarkRecyclerView.apply {
            adapter = databaseAnimeAdapter
            layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        }

        viewModel.animeRepo.bookmarkAnimeList.observe(this, Observer {
            databaseAnimeAdapter.submitList(it)
            if (it.isNullOrEmpty()) binding.errorView.visibility = View.VISIBLE else binding.errorView.visibility = View.INVISIBLE
        })


        when (context?.resources?.configuration?.uiMode?.and(Configuration.UI_MODE_NIGHT_MASK)) {
            Configuration.UI_MODE_NIGHT_YES -> {binding.errorImageView.setImageResource(R.drawable.nightmode_error)}
            Configuration.UI_MODE_NIGHT_NO -> {binding.errorImageView.setImageResource(R.drawable.error)}
            Configuration.UI_MODE_NIGHT_UNDEFINED -> {binding.errorImageView.setImageResource(R.drawable.error)}
        }




        return binding.root
    }


}

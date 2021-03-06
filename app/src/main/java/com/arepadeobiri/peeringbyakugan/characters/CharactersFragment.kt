package com.arepadeobiri.peeringbyakugan.characters


import android.content.res.Configuration
import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.arepadeobiri.peeringbyakugan.AnimeClickListener
import com.arepadeobiri.peeringbyakugan.AnimeRecyclerAdapter
import com.arepadeobiri.peeringbyakugan.ByakuganApplication

import com.arepadeobiri.peeringbyakugan.R
import com.arepadeobiri.peeringbyakugan.databinding.FragmentCharactersBinding
import com.arepadeobiri.peeringbyakugan.GenericViewModelFactory


class CharactersFragment : Fragment() {

    private lateinit var binding: FragmentCharactersBinding
    private lateinit var viewModel: CharactersViewModel
    private lateinit var animeAdapter: AnimeRecyclerAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_characters, container, false
        )

        binding.lifecycleOwner = this
        val args = arguments?.let { CharactersFragmentArgs.fromBundle(it) }

        val viewModelFactory =
            GenericViewModelFactory(((this.activity!!.application) as ByakuganApplication).getAppComponent())
        viewModel =
            ViewModelProviders.of(this, viewModelFactory).get(CharactersViewModel::class.java)

        when (context?.resources?.configuration?.uiMode?.and(Configuration.UI_MODE_NIGHT_MASK)) {
            Configuration.UI_MODE_NIGHT_YES -> {binding.errorImageView.setImageResource(R.drawable.nightmode_error)}
            Configuration.UI_MODE_NIGHT_NO -> {binding.errorImageView.setImageResource(R.drawable.error)}
            Configuration.UI_MODE_NIGHT_UNDEFINED -> {binding.errorImageView.setImageResource(R.drawable.error)}
        }



        animeAdapter =
            AnimeRecyclerAdapter(AnimeClickListener { characterId, characterName ->
                if (viewModel.isInternetConnection()) {
                    this.findNavController().navigate(
                        CharactersFragmentDirections.actionCharactersFragmentToCharacterDetailsFragment(
                            characterName,
                            characterId
                        )
                    )
                }

            })

        binding.characterRecyclerView.apply {
            adapter = animeAdapter
            layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)

        }
        viewModel.queryJikanForAnimeCharacters(args!!.animeId)

        viewModel.currentAnimeCharacterList.observe(this, Observer {
            binding.loadingProgressBar.visibility = View.INVISIBLE
            if (it == null) binding.errorView.visibility = View.VISIBLE
            animeAdapter.submitList(it)
        })




        return binding.root
    }


    override fun onResume() {
        super.onResume()
        (activity as AppCompatActivity).title = getString(R.string.characters)
    }
}

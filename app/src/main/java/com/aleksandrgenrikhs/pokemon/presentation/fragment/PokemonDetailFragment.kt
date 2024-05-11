package com.aleksandrgenrikhs.pokemon.presentation.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.navArgs
import com.aleksandrgenrikhs.pokemon.R
import com.aleksandrgenrikhs.pokemon.app
import com.aleksandrgenrikhs.pokemon.databinding.FragmentPokemonDetailBinding
import com.aleksandrgenrikhs.pokemon.presentation.factory.PokemonDetailViewModelAssistedFactory
import com.aleksandrgenrikhs.pokemon.presentation.viewmodel.PokemonDetailViewModel
import com.bumptech.glide.Glide
import kotlinx.coroutines.launch
import javax.inject.Inject

class PokemonDetailFragment : Fragment() {

    private var _binding: FragmentPokemonDetailBinding? = null
    private val binding: FragmentPokemonDetailBinding get() = _binding!!

    private val args by navArgs<PokemonDetailFragmentArgs>()

    private val navController: NavController by lazy {
        (requireActivity().supportFragmentManager.findFragmentById(R.id.main_activity_nav_host) as NavHostFragment)
            .navController
    }

    @Inject
    lateinit var pokemonDetailViewModel: PokemonDetailViewModelAssistedFactory
    private val viewModel: PokemonDetailViewModel by viewModels {
        pokemonDetailViewModel.create(
            args.pokemonId
        )
    }

    override fun onAttach(context: Context) {
        (app.appComponent.inject(this))
        super.onAttach(context)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPokemonDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        onClickBack()
        subscribe()
        onClickCriesButton()
    }

    private fun onClickBack() {
        binding.backButton.setOnClickListener {
            navController.popBackStack()
        }
    }

    private fun onClickCriesButton() {
        binding.cries.setOnClickListener {
            viewModel.cries()
        }
    }

    private fun subscribe() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.pokemonDetail.collect { pokemon ->
                with(binding) {
                    experience.text = getString(R.string.base_experience, pokemon?.experience)
                    height.text = getString(R.string.height, pokemon?.height)
                    weight.text = getString(R.string.weight, pokemon?.weight)
                    title.text = pokemon?.name?.uppercase()
                    Glide.with(app)
                        .asGif()
                        .load(pokemon?.iconFrontUrl)
                        .into(iconFront)
                    Glide.with(app)
                        .asGif()
                        .load(pokemon?.iconBackUrl)
                        .into(iconBackPokemon)
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.isProgressBarVisible.collect { isVisible ->
                binding.progressBar.isVisible = isVisible
            }
        }
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.isCardViewVisible.collect { isVisible ->
                binding.pokemonCard.isVisible = isVisible
            }
        }
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.toastMessageError.collect { error ->
                Toast.makeText(requireContext(), error, Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
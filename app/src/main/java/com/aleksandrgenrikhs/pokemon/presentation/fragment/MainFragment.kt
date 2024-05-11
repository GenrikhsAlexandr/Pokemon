package com.aleksandrgenrikhs.pokemon.presentation.fragment

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
import androidx.recyclerview.widget.RecyclerView
import com.aleksandrgenrikhs.pokemon.R
import com.aleksandrgenrikhs.pokemon.databinding.FragmentMainBinding
import com.aleksandrgenrikhs.pokemon.presentation.BottomOffsetDecoration
import com.aleksandrgenrikhs.pokemon.presentation.PokemonAdapter
import com.aleksandrgenrikhs.pokemon.presentation.viewmodel.MainViewModel
import com.aleksandrgenrikhs.pokemon.viewModelFactory
import kotlinx.coroutines.launch

class MainFragment : Fragment() {

    private var isScrollingUp = false

    private var _binding: FragmentMainBinding? = null
    private val binding: FragmentMainBinding get() = _binding!!

    private val viewModel: MainViewModel by viewModels { viewModelFactory }

    private val navController: NavController by lazy {
        (requireActivity().supportFragmentManager.findFragmentById(R.id.main_activity_nav_host) as NavHostFragment)
            .navController
    }

    private val adapter: PokemonAdapter by lazy {
        PokemonAdapter(onClick = { pokemonId -> onItemClick(pokemonId) })
    }

    private fun onItemClick(pokemonId: Int) {
        navController.navigate(
            MainFragmentDirections.actionMainFragmentToDetailFragment(
                pokemonId
            )
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.pokemonRV.adapter = adapter
        subscribe()
        scrollListenerRecycle()
        clickButton()
    }

    private fun scrollListenerRecycle() {
        val offset = resources.getDimensionPixelSize(R.dimen.button_group_height)
        val bottomOffsetDecoration = BottomOffsetDecoration(offset)
        binding.pokemonRV.addItemDecoration(bottomOffsetDecoration)
        binding.pokemonRV.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (dy > 0) {
                    isScrollingUp = false
                } else if (dy < 0) {
                    isScrollingUp = true
                }
            }

            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (!recyclerView.canScrollVertically(1) && !isScrollingUp) {
                    viewModel.showButtonGroup()
                } else {
                    viewModel.hideButtonGroup()
                }
            }
        })
    }

    private fun subscribe() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.page.collect { page ->
                if (page != null) {
                    adapter.submitList(page.pokemon)
                    binding.previousButton.isVisible = page.hasPreviousPage
                    binding.nextButton.isVisible = page.hasNextPage
                }
            }
        }
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.toastMessageError.collect { error ->
                Toast.makeText(requireContext(), error, Toast.LENGTH_SHORT).show()
            }
        }
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.isProgressBarVisible.collect { isVisible ->
                binding.progressBar.isVisible = isVisible
            }
        }
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.isButtonGroupVisible.collect { isVisible ->
                binding.buttonGroup.isVisible = isVisible
            }
        }
    }

    private fun clickButton() {
        binding.nextButton.setOnClickListener {
            viewModel.getNextPage()
            viewModel.hideButtonGroup()
        }
        binding.previousButton.setOnClickListener {
            viewModel.getPreviousPage()
            viewModel.hideButtonGroup()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
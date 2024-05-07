package com.aleksandrgenrikhs.pokemon.presentation

import android.content.Context
import android.graphics.Rect
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.recyclerview.widget.RecyclerView
import com.aleksandrgenrikhs.pokemon.R
import com.aleksandrgenrikhs.pokemon.app
import com.aleksandrgenrikhs.pokemon.databinding.FragmentMainBinding
import com.aleksandrgenrikhs.pokemon.viewModelFactory
import kotlinx.coroutines.launch

class MainFragment : Fragment() {

    private var nextPage: Int? = null
    private var previousPage: Int? = null
    private var isScrollingUp = false

    private val viewModel: MainViewModel by viewModels { viewModelFactory }

    private val navController: NavController by lazy {
        (requireActivity().supportFragmentManager.findFragmentById(R.id.main_activity_nav_host) as NavHostFragment)
            .navController
    }

    private var _binding: FragmentMainBinding? = null
    private val binding: FragmentMainBinding get() = _binding!!

    private val adapter: PokemonAdapter by lazy {
        PokemonAdapter(
            onClick = { pokemon ->
                onItemClick(pokemon)
            })
    }

    private fun onItemClick(pokemon: Int) {
        navController.navigate(
            MainFragmentDirections.actionMainFragmentToDetailFragment(pokemon)
        )
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        (app.appComponent.inject(this))
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMainBinding.inflate(inflater, container, false)
        binding.pokemonRV.adapter = adapter
        subscribe()
        scrollListenerRecycle()
        clickButton()
        return binding.root
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
                    binding.buttonGroup.visibility = View.VISIBLE
                    showButton()
                } else {
                    binding.buttonGroup.visibility = View.GONE
                }
            }
        })
    }

    private fun showButton() {
        if (nextPage == null) {
            binding.nextButton.visibility = View.GONE
        } else {
            binding.nextButton.visibility = View.VISIBLE
        }
        if (previousPage == null) {
            binding.previousButton.visibility = View.GONE
        } else {
            binding.previousButton.visibility = View.VISIBLE
        }
    }

    private fun subscribe() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.pokemon.collect { pokemon ->
                adapter.submitList(pokemon)
            }
        }
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.toastMessageError.collect { error ->
                Toast.makeText(requireContext(), error.message, Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun clickButton() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.offset.collect { offset ->
                nextPage = offset?.next
                previousPage = offset?.previous
                binding.nextButton.setOnClickListener {
                    viewModel.getPokemon(offset?.next)
                    binding.buttonGroup.visibility = View.GONE
                }
                binding.previousButton.setOnClickListener {
                    viewModel.getPokemon(offset?.previous)
                    binding.buttonGroup.visibility = View.GONE
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
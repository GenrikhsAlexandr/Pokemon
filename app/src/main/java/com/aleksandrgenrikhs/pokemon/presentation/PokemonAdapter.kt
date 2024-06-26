package com.aleksandrgenrikhs.pokemon.presentation

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.aleksandrgenrikhs.pokemon.R
import com.aleksandrgenrikhs.pokemon.databinding.PokemonItemBinding
import com.aleksandrgenrikhs.pokemon.domain.Pokemon
import com.bumptech.glide.Glide

private val diffCallBack = object : DiffUtil.ItemCallback<Pokemon>() {

    override fun areItemsTheSame(
        oldItem: Pokemon,
        newItem: Pokemon
    ): Boolean = oldItem.id == newItem.id

    override fun areContentsTheSame(
        oldItem: Pokemon,
        newItem: Pokemon
    ): Boolean = oldItem == newItem
}

class PokemonAdapter(
    val onClick: (Int) -> Unit
) : ListAdapter<Pokemon, PokemonAdapter.PokemonViewHolder>(diffCallBack) {

    inner class PokemonViewHolder(private val binding: PokemonItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(listItem: Pokemon) {
            with(binding) {
                pokemonTitle.text = listItem.name
                binding.root.setOnClickListener {
                    onClick(listItem.id)
                }
                Glide.with(itemView.context)
                    .load(listItem.iconUrl)
                    .placeholder(R.drawable.ic_launcher_foreground)
                    .error(R.drawable.ic_launcher_foreground)
                    .into(pokemonIcon)
            }
       }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PokemonViewHolder {
        return PokemonViewHolder(
            PokemonItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: PokemonViewHolder, position: Int) =
        holder.bind(currentList[position])
}
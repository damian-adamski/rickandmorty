package com.example.rickandmorty.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.rickandmorty.R
import com.example.rickandmorty.model.Results
import kotlinx.android.synthetic.main.item_character.view.*

class CharactersAdapter : RecyclerView.Adapter<CharactersAdapter.CharactersViewHolder>() {

    inner class CharactersViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    private val differCallback = object : DiffUtil.ItemCallback<Results>() {
        override fun areItemsTheSame(oldItem: Results, newItem: Results): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Results, newItem: Results): Boolean {
            return oldItem.equals(newItem)
        }
    }

    val differ = AsyncListDiffer(this, differCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CharactersViewHolder {
        return CharactersViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_character, parent, false)
            )
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    override fun onBindViewHolder(holder: CharactersViewHolder, position: Int) {
        val character = differ.currentList[position]
        holder.itemView.apply {
            Glide.with(this).load(character.image).into(ivPicture)
            tvName.text = character.characterName
            setOnClickListener {
                onItemClickListener?.let { it(character) } // 2 wywołuję przekazaną metodę na obiekcie postaci (o ile nie jest nullem)
            }
        }
    }

    private var onItemClickListener: ((Results) -> Unit)? = null

    fun setOnItemClickListener(listener: (Results) -> Unit) { // 1 przekazuję metodę jako parametr funkcji
        onItemClickListener = listener
    }
}
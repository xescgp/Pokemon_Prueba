package com.example.pokemon;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pokemon.pokeapi.Pokemon;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class PokemonAdapter extends RecyclerView.Adapter<PokemonAdapter.PokemonViewHolder> {

    Context context;
    List<Pokemon> pokemonList;

    public PokemonAdapter(Context context, List<Pokemon> pokemonList) {
        this.context = context;
        this.pokemonList = pokemonList;
    }

    @NonNull
    @Override
    public PokemonViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_pokemon, parent, false);
        final PokemonViewHolder holder = new PokemonViewHolder(view);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                        Intent intent = new Intent (v.getContext(), PokemonDetail.class);
                        intent.putExtra("id", holder.getAdapterPosition()+1);
                        intent.putExtra("name", pokemonList.get(holder.getAdapterPosition()).getName());
                        context.startActivity(intent);
            }
        });
        return holder;
        //return new PokemonViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PokemonViewHolder holder, int position) {
        holder.pokemonName.setText(pokemonList.get(position).getName().toUpperCase());
        Picasso.with(context)
                .load("https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/" + pokemonList.get(position).getNumber() + ".png")
                .resize(200, 200)
                .into(holder.pokemonImg);
    }

    @Override
    public int getItemCount() {
        return pokemonList.size();
    }

    public class PokemonViewHolder extends RecyclerView.ViewHolder {

        TextView pokemonName, pokemonUrl;
        ImageView pokemonImg;

        public PokemonViewHolder(@NonNull View itemView) {
            super(itemView);

            pokemonName = itemView.findViewById(R.id.txtPokemonName);
            pokemonImg = itemView.findViewById(R.id.imgPokemon);

        }
    }

    public void filtrar(ArrayList<Pokemon> filtroPokemon){
        this.pokemonList = filtroPokemon;
        notifyDataSetChanged();
    }
}

package com.example.pokemon.pokeapi;

import com.example.pokemon.pokeapi.PokemonRetrieve;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface PokeApiInterface {

    @GET("pokemon")
    Call<PokemonRetrieve> getPokemons();

    @GET("{id}")
    Call<PokemonRetrieve> getPokemonInfo(@Path("id") String id);

}

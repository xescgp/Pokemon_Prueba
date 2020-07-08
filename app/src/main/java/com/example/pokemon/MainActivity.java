package com.example.pokemon;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import com.example.pokemon.pokeapi.PokeApiInterface;
import com.example.pokemon.pokeapi.Pokemon;
import com.example.pokemon.pokeapi.PokemonRetrieve;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    private Retrofit retrofit;

    private static final String TAG = "POKEMONAPP";

    EditText etSearch;
    RecyclerView rvList;
    PokemonAdapter adapter;
    ArrayList<Pokemon> pokemonList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();

        etSearch = findViewById(R.id.etSearch);
        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                filtrar(editable.toString());
            }
        });

        rvList = findViewById(R.id.rvList);
        rvList.setLayoutManager((new GridLayoutManager(this, 1)));

        //Indicamos la url de la PokeApi
        retrofit = new Retrofit.Builder().baseUrl("https://pokeapi.co/api/v2/").addConverterFactory(GsonConverterFactory.create()).build();

        obtenerInfo();
    }

    public void filtrar(String searchTxt){
        ArrayList<Pokemon> filtrarLista = new ArrayList<>();

        for(Pokemon pokemon : pokemonList) {
            if(pokemon.getName().toLowerCase().contains(searchTxt.toLowerCase())){
                filtrarLista.add(pokemon);
            }
        }

        adapter.filtrar(filtrarLista);
    }

    private void obtenerInfo()
    {
        //Ejecutamos las llamadas a la PokeApi
        PokeApiInterface pAinterface = retrofit.create(PokeApiInterface.class);
        Call<PokemonRetrieve> pokemonRetrieveCall = pAinterface.getPokemons();

        pokemonRetrieveCall.enqueue(new Callback<PokemonRetrieve>() {
            @Override
            public void onResponse(Call<PokemonRetrieve> call, Response<PokemonRetrieve> response) {
                if(response.isSuccessful())
                {
                    //Recibimos la lista de Pokemons y los añadimos a nuestro Adapter
                    PokemonRetrieve pokemonRetrieve = response.body();
                    pokemonList = pokemonRetrieve.getInfo();

                    adapter = new PokemonAdapter(MainActivity.this, pokemonList);
                    rvList.setAdapter(adapter);

                }else{
                    Log.e(TAG, " Respuesta: " + response.errorBody());
                }
            }
            @Override
            public void onFailure(Call<PokemonRetrieve> call, Throwable t) {
                Log.e(TAG, " Respuesta fallida API: " + t.getMessage());

                Toast errorToast = Toast.makeText(getApplicationContext(),"Error al obtener el listado de Pokemons", Toast.LENGTH_SHORT);
                errorToast.show();
            }
        });
    }
}
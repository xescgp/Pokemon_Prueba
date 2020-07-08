package com.example.pokemon;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.pokemon.pokeapi.PokeApiInterface;
import com.example.pokemon.pokeapi.PokemonRetrieve;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class PokemonDetail extends AppCompatActivity {

    private Retrofit retrofit;
    private static final String TAG = "POKEMONAPP";

    TextView tvPokemonName, tvPokemonColor, tvPokemonEvo, tvPokemonInfo;
    ImageView pokemonImg;
    Button pokemonBack;

    public Integer pokemonId;
    public String pokemonName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pokemon_detail);
        getSupportActionBar().hide();

        //Obtenemos los datos que recibimos del activity anterior
        pokemonId = (Integer) getIntent().getExtras().getSerializable("id");
        pokemonName = (String) getIntent().getExtras().getSerializable("name");

        tvPokemonName = findViewById(R.id.txtPokemonName);
        tvPokemonInfo = findViewById(R.id.txtInfo);
        tvPokemonColor = findViewById(R.id.txtPokemonColor);
        tvPokemonEvo = findViewById(R.id.txtPokemonEvo);
        pokemonImg = findViewById(R.id.imgPokemon);
        pokemonBack = findViewById(R.id.btBack);
        tvPokemonInfo.setMovementMethod(new ScrollingMovementMethod());

        tvPokemonName.setText(pokemonName.toUpperCase());

        Picasso.with(getApplicationContext())
                .load("https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/" + pokemonId.toString() + ".png")
                .resize(600, 600)
                .into(pokemonImg);

        //Creamos el link a la wikipedia para encontrar los datos del pokemon
        String WIKIPEDIA_URL = "https://en.wikipedia.org/w/api.php?action=query&titles=" + pokemonName + "&prop=revisions&rvprop=content&format=json&prop=extracts";

        // Start AsyncTask
        FetchWikiDataAsync fetchWikiDataAsync = new FetchWikiDataAsync();
        fetchWikiDataAsync.execute(WIKIPEDIA_URL);

        pokemonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent (v.getContext(), MainActivity.class);
                startActivity(intent);
            }
        });

        //Indicamos la url de la PokeApi
        retrofit = new Retrofit.Builder().baseUrl("https://pokeapi.co/api/v2/pokemon-species/").addConverterFactory(GsonConverterFactory.create()).build();

        obtenerInfo();
    }

    private void obtenerInfo()
    {
        PokeApiInterface pAinterface = retrofit.create(PokeApiInterface.class);
        Call<PokemonRetrieve> pokemonRetrieveCall = pAinterface.getPokemonInfo(pokemonId.toString());

        pokemonRetrieveCall.enqueue(new Callback<PokemonRetrieve>() {
            @Override
            public void onResponse(Call<PokemonRetrieve> call, Response<PokemonRetrieve> response) {
                if(response.isSuccessful())
                {
                    PokemonRetrieve pokemonRetrieve = response.body();

                    tvPokemonColor.setText("Color: " + pokemonRetrieve.getColor().toUpperCase());
                    tvPokemonEvo.setText("Evolution: " + pokemonRetrieve.getEvolves_from_species().toUpperCase());

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

    //Obtenemos los datos de la wikipedia
    private class FetchWikiDataAsync extends AsyncTask<String, Void, String>
    {
        @Override
        protected void onPreExecute()
        {
        }

        @Override
        protected String doInBackground(String[] params)
        {
            try
            {
                String sURL = params[0];

                URL url = new URL(sURL);        // Convert String URL to java.net.URL
                // Connection: to Wikipedia API
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                InputStream inputStream = urlConnection.getInputStream();
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream,                  "UTF-8");
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

                StringBuilder stringBuilder = new StringBuilder();
                String line;

                while ((line = bufferedReader.readLine()) != null)
                {
                    stringBuilder.append(line);
                }

                String wikiData = stringBuilder.toString();

                // Parse JSON Data
                String formattedData = parseJSONData(wikiData);

                return formattedData;

            }
            catch (IOException e)
            {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(String formattedData)
        {
            super.onPostExecute(formattedData);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
            {
                // HTML Data
                tvPokemonInfo.setText(Html.fromHtml
                        ((!formattedData.equals("")?formattedData:"No data on Wikipedia"),Html.FROM_HTML_MODE_LEGACY));
            }
            else
            {
                // HTML Data
                tvPokemonInfo.setText(Html.fromHtml(formattedData));
            }
        }
    }

    private String parseJSONData(String wikiData)
    {
        try
        {
            // Convert String JSON (wikiData) to JSON Object
            JSONObject rootJSON = new JSONObject(wikiData);
            JSONObject query = rootJSON.getJSONObject("query");
            JSONObject pages = query.getJSONObject("pages");
            JSONObject number = pages.getJSONObject(pages.keys().next());
            String formattedData = number.getString("extract");

            return formattedData;
        }
        catch (JSONException json)
        {
            json.printStackTrace();
        }

        return null;
    }
}

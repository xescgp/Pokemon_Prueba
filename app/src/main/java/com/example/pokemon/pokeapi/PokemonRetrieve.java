package com.example.pokemon.pokeapi;

import java.util.ArrayList;

public class PokemonRetrieve {

    class Color
    {
        public String name;
    };

    class Evolves_from_species
    {
        public String name;
    };

    private ArrayList<Pokemon> results;
    private Color color;
    private Evolves_from_species evolves_from_species;

    public ArrayList<Pokemon> getInfo()
    {
        return results;
    }

    public String getColor()
    {
        return color.name;
    }

    public String getEvolves_from_species()
    {
        return (evolves_from_species==null?"Pokemon Base":evolves_from_species.name);
    }

    public void setInfo(ArrayList<Pokemon> info)
    {
        this.results = info;
    }

    public void setColor(String color)
    {
        this.color.name = color;
    }

    public void setEvolves_from_species(String name)
    {
        this.evolves_from_species.name = name;
    }
}

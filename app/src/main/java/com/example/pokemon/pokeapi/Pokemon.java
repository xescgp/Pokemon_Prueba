package com.example.pokemon.pokeapi;

public class Pokemon {

    private int number;
    private String name;
    private String url;

    public String getName() {
        return name;
    }

    public int getNumber() {
        String[] urlSplit = url.split("/");
        return  Integer.parseInt((urlSplit[urlSplit.length - 1]));
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}

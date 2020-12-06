package com.harveymannering.android.pokedex;

import android.graphics.Bitmap;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;

public class Pokemon {

    public JSONObject pokemon_json;
    private Bitmap sprite;

    public Pokemon(JSONObject json){
        pokemon_json = json;
    }

    public Bitmap getSprite() {
        return sprite;
    }

    public void setSprite(Bitmap sprite) {
        this.sprite = sprite;
    }

    //Returns the pokemon's ID in the pokedex
    public int getId() {
        try {
            int poke_id = pokemon_json.getInt("id");
            return poke_id;
        }
        catch (JSONException e){
            e.printStackTrace();
            return 0;
        }
    }

    //Accesses the name property inside of the JSON
    public String getName() {
        try {
            String poke_name = pokemon_json.getString("name");
            //Capitalize the pokemon's name
            poke_name = poke_name.substring(0, 1).toUpperCase() + poke_name.substring(1);
            return poke_name;
        }
        catch (JSONException e){
            e.printStackTrace();
            return "";
        }
    }

    //Returns url of a pokemon's default sprite
    public String getSpriteUrl() {
        try {
            String sprite_url = pokemon_json.getJSONObject("sprites").getString("front_default");
            return sprite_url;
        }
        catch (JSONException e){
            e.printStackTrace();
            return "";
        }
    }

    //Returns height of a pokemon
    public int getHeight() {
        try {
            int height = pokemon_json.getInt("height");
            return height;
        }
        catch (JSONException e){
            e.printStackTrace();
            return 0;
        }
    }

    //Returns pokemon's weight from JSON object
    public int getWeight() {
        try {
            int weight = pokemon_json.getInt("weight");
            return weight;
        }
        catch (JSONException e){
            e.printStackTrace();
            return 0;
        }
    }

    //Returns pokemon's HP base stat
    public int getHP() {
        try {
            JSONArray stats_array = pokemon_json.getJSONArray("stats");
            int hp = ((JSONObject) stats_array.get(0)).getInt("base_stat");
            return hp;
        }
        catch (JSONException e){
            e.printStackTrace();
            return 0;
        }
    }

    //Returns pokemon's HP base stat
    public int getAttack() {
        try {
            JSONArray stats_array = pokemon_json.getJSONArray("stats");
            int attack = ((JSONObject) stats_array.get(1)).getInt("base_stat");
            return attack;
        }
        catch (JSONException e){
            e.printStackTrace();
            return 0;
        }
    }

    //Returns pokemon's HP base stat
    public int getDefence() {
        try {
            JSONArray stats_array = pokemon_json.getJSONArray("stats");
            int defence = ((JSONObject) stats_array.get(2)).getInt("base_stat");
            return defence;
        }
        catch (JSONException e){
            e.printStackTrace();
            return 0;
        }
    }

    //Returns pokemon's HP base stat
    public int getSpAttack() {
        try {
            JSONArray stats_array = pokemon_json.getJSONArray("stats");
            int sp_attack = ((JSONObject) stats_array.get(3)).getInt("base_stat");
            return sp_attack;
        }
        catch (JSONException e){
            e.printStackTrace();
            return 0;
        }
    }

    //Returns pokemon's HP base stat
    public int getSpDefence() {
        try {
            JSONArray stats_array = pokemon_json.getJSONArray("stats");
            int sp_defence = ((JSONObject) stats_array.get(4)).getInt("base_stat");
            return sp_defence;
        }
        catch (JSONException e){
            e.printStackTrace();
            return 0;
        }
    }

    //Returns pokemon's HP base stat
    public int getSpeed() {
        try {
            JSONArray stats_array = pokemon_json.getJSONArray("stats");
            int speed = ((JSONObject) stats_array.get(5)).getInt("base_stat");
            return speed;
        }
        catch (JSONException e){
            e.printStackTrace();
            return 0;
        }
    }

    public ArrayList<String> getTypes(){
        ArrayList<String> types = new ArrayList<String>();
        try {
            JSONArray stats_array = pokemon_json.getJSONArray("types");
            for (int i = 0; i < stats_array.length(); i++) {
                String type = ((JSONObject) stats_array.get(i)).getJSONObject("type").getString("name");
                types.add(type);
            }
        }
        catch (JSONException e){
            e.printStackTrace();
        }

        return types;
    }
}

package com.harveymannering.android.pokedex;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class MainActivity extends AppCompatActivity {

    public static ArrayList<Pokemon> data;
    private static int count = 0;
    public static int sprite_size;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Get the width of the screen and use it top determine the size of the pokemon sprites
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        sprite_size = displayMetrics.widthPixels / 4;

        //Instantiate UI and data components
        GridView gridView = (GridView) findViewById(R.id.pokedex_list);
        SearchView search_box = (SearchView) findViewById(R.id.search_box);

        // Instantiate the RequestQueue
        RequestQueue queue = Volley.newRequestQueue(this);
        String base_url ="https://pokeapi.co/api/v2/pokemon/";
        data = new ArrayList<Pokemon>();

        //Make API requests to the pokeapi, one request for each pokemon in Kanto pokedex
        for (int i = 1; i <= 151; i++) {
            JsonObjectRequest request = new JsonObjectRequest(base_url + i, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            //Add a pokemon to the data variable
                            data.add(new Pokemon(response));
                            count++;
                            //If statement will only run for the final pokemon
                            if (count >= 150){
                                //Sort items by id
                                Collections.sort(data, new Comparator<Pokemon>() {
                                    @Override
                                    public int compare(Pokemon p1, Pokemon p2) {
                                        return p1.getId() < p2.getId() ? -1 : (p1.getId()  > p2.getId() ) ? 1 : 0;
                                    }
                                });

                                //Add list of pokemon to the UI
                                PokemonAdapter pokemonAdapter = new PokemonAdapter(getBaseContext(), data);
                                gridView.setAdapter(pokemonAdapter);

                                //Set up the search box
                                search_box.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                                    @Override
                                    public boolean onQueryTextSubmit(String query) {
                                        return false;
                                    }

                                    @Override
                                    public boolean onQueryTextChange(String newText) {
                                        pokemonAdapter.getFilter().filter(newText);
                                        return false;
                                    }
                                });
                            }
                        }
                        catch (Exception ex) {
                            ex.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });

            // Add the request to the RequestQueue
            queue.add(request);
        }

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView parent, View view, int position, long id) {
                Intent intent = new Intent(getBaseContext(), PokemonActivity.class);
                intent.putExtra("id", position);
                startActivity(intent);
            }
        });
    }
}


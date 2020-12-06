package com.harveymannering.android.pokedex;

import androidx.appcompat.app.AppCompatActivity;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SearchView;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class MainActivity extends AppCompatActivity {

    public static ArrayList<Pokemon> data;
    public static int sprite_size;
    private int pokedex_limit = 151;
    private static int count = 0;

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

        //Start loading animation
        ImageView loading_image = (ImageView) findViewById(R.id.loading_image);
        loading_image.setLayoutParams(new LinearLayout.LayoutParams(sprite_size / 2, sprite_size / 2));
        ObjectAnimator animator = ObjectAnimator.ofFloat(loading_image, View.ROTATION,-360f, 0f);
        animator.setDuration(1000);
        animator.setInterpolator(new LinearInterpolator());
        animator.setRepeatCount(ObjectAnimator.INFINITE);
        animator.setRepeatMode(ObjectAnimator.RESTART);
        animator.start();

        // Instantiate the RequestQueue
        RequestQueue queue = Volley.newRequestQueue(this);
        String base_url ="https://pokeapi.co/api/v2/pokemon/";
        if (data == null || data.size() != pokedex_limit) {
            data = new ArrayList<Pokemon>();

            //Make API requests to the pokeapi, one request for each pokemon in Kanto pokedex
            for (int i = 1; i <= pokedex_limit; i++) {
                JsonObjectRequest request = new JsonObjectRequest(base_url + i, null,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                try {
                                    //Add a pokemon to the data variable
                                    data.add(new Pokemon(response));
                                    count++;

                                    //If statement will only run for the final pokemon
                                    if (count >= pokedex_limit - 1) {
                                        //Sort items by id
                                        Collections.sort(data, new Comparator<Pokemon>() {
                                            @Override
                                            public int compare(Pokemon p1, Pokemon p2) {
                                                return p1.getId() < p2.getId() ? -1 : (p1.getId() > p2.getId()) ? 1 : 0;
                                            }
                                        });
                                        setupUI(gridView, search_box);
                                    }
                                } catch (Exception ex) {
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
        }
        else {
            setupUI(gridView, search_box);
        }

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView parent, View view, int position, long id) {
                Intent intent = new Intent(getBaseContext(), PokemonActivity.class);
                intent.putExtra("id", PokemonAdapter.filteredDataSet.get(position).getId() - 1);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        //Stops keyboard from automatically popping up when the activity is reloaded
        SearchView search_box = findViewById(R.id.search_box);
        search_box.clearFocus();
    }

    private void setupUI(GridView gridView, SearchView search_box) {
        //Add list of pokemon to the UI
        PokemonAdapter pokemonAdapter = new PokemonAdapter(getBaseContext(), data);
        gridView.setAdapter(pokemonAdapter);

        //Hide the loading section
        LinearLayout loading_layout = (LinearLayout) findViewById(R.id.loading_layout);
        loading_layout.setVisibility(View.GONE);

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


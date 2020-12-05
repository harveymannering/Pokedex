package com.harveymannering.android.pokedex;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.json.JSONArray;

import java.io.InputStream;
import java.io.StringBufferInputStream;
import java.util.ArrayList;

public class PokemonActivity extends AppCompatActivity {

    int id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pokemon);

        //Get pokemon
        Bundle extras = getIntent().getExtras();
        id = extras.getInt("id");
        Pokemon pokemon = MainActivity.data.get(id);

        //Get references to heading views

        ImageView poke_image = (ImageView) findViewById(R.id.pokemon_image);
        TextView name_text = (TextView) findViewById(R.id.name_label);

        //Set image dimensions
        LinearLayout.LayoutParams layout_params = new LinearLayout.LayoutParams(MainActivity.sprite_size, MainActivity.sprite_size);
        layout_params.gravity = Gravity.CENTER;
        poke_image.setLayoutParams(layout_params);

        //Set the value of ImageView
        if (pokemon.getSprite() != null)
            poke_image.setImageBitmap(Bitmap.createScaledBitmap(pokemon.getSprite(), MainActivity.sprite_size, MainActivity.sprite_size, true));
        else{
            //start downloading sprite in new thread
            DownloadImageTask download = new DownloadImageTask(poke_image, id);
            download.execute(pokemon.getSpriteUrl());
        }

        //Set value of the name field
        name_text.setText("#" + pokemon.getId() + " " + pokemon.getName());

        //Add type labels
        addTypeLabels(pokemon);

        //Get references to stats views
        TextView height_text = (TextView) findViewById(R.id.pokemon_height);
        TextView weight_text = (TextView) findViewById(R.id.pokemon_weight);
        TextView hp_text = (TextView) findViewById(R.id.pokemon_hp);
        TextView attack_text = (TextView) findViewById(R.id.pokemon_attack);
        TextView defence_text = (TextView) findViewById(R.id.pokemon_defence);
        TextView sp_attack_text = (TextView) findViewById(R.id.pokemon_sp_attack);
        TextView sp_defence_text = (TextView) findViewById(R.id.pokemon_sp_defence);
        TextView speed_text = (TextView) findViewById(R.id.pokemon_speed);

        //Set the data fields for this activity
        height_text.setText(pokemon.getHeight() + "");
        weight_text.setText(pokemon.getWeight() + "");
        hp_text.setText(pokemon.getHP() + "");
        attack_text.setText(pokemon.getAttack() + "");
        defence_text.setText(pokemon.getDefence() + "");
        sp_attack_text.setText(pokemon.getSpAttack() + "");
        sp_defence_text.setText(pokemon.getSpDefence() + "");
        speed_text.setText(pokemon.getSpeed() + "");
    }

    public void addTypeLabels(Pokemon pokemon){
        //Get a reference to the layout
        LinearLayout layout = (LinearLayout) findViewById(R.id.types_layout) ;

        //Iterate through all types a certain pokemon has
        ArrayList<String> types = pokemon.getTypes();
        for (String type : types){
            //Create new text view object
            TextView type_view = new TextView(this);
            type_view.setText(capitalize(type));
            type_view.setPadding(0,0,8,0);

            //Set the colour of the label
            switch (type){
                case "normal":
                    type_view.setTextColor(getResources().getColor(R.color.normal));
                    break;
                case "fire":
                    type_view.setTextColor(getResources().getColor(R.color.fire));
                    break;
                case "water":
                    type_view.setTextColor(getResources().getColor(R.color.water));
                    break;
                case "electric":
                    type_view.setTextColor(getResources().getColor(R.color.electric));
                    break;
                case "grass":
                    type_view.setTextColor(getResources().getColor(R.color.grass));
                    break;
                case "ice":
                    type_view.setTextColor(getResources().getColor(R.color.ice));
                    break;
                case "fighting":
                    type_view.setTextColor(getResources().getColor(R.color.fighting));
                    break;
                case "poison":
                    type_view.setTextColor(getResources().getColor(R.color.poison));
                    break;
                case "ground":
                    type_view.setTextColor(getResources().getColor(R.color.ground));
                    break;
                case "flying":
                    type_view.setTextColor(getResources().getColor(R.color.flying));
                    break;
                case "psychic":
                    type_view.setTextColor(getResources().getColor(R.color.psychic));
                    break;
                case "bug":
                    type_view.setTextColor(getResources().getColor(R.color.bug));
                    break;
                case "rock":
                    type_view.setTextColor(getResources().getColor(R.color.rock));
                    break;
                case "ghost":
                    type_view.setTextColor(getResources().getColor(R.color.ghost));
                    break;
                case "dragon":
                    type_view.setTextColor(getResources().getColor(R.color.dragon));
                    break;
                case "dark":
                    type_view.setTextColor(getResources().getColor(R.color.dark));
                    break;
                case "steel":
                    type_view.setTextColor(getResources().getColor(R.color.steel));
                    break;
                case "fairy":
                    type_view.setTextColor(getResources().getColor(R.color.fairy));
                    break;
            }
            //Add view to the UI
            layout.addView(type_view);
        }
    }

    public String capitalize(String s){
        return s.substring(0, 1).toUpperCase() + s.substring(1);
    }

    //Save bitmap so that it wont need to be set again
    public void saveBitmap(Bitmap sprite, int position){
        Pokemon p = MainActivity.data.get(position);
        p.setSprite(sprite);
        MainActivity.data.set(position, p);
    }

    class DownloadImageTask extends DownloadSpriteTask {

        public DownloadImageTask(ImageView imageView, int position) {
            super(imageView, position);
        }

        protected void onPostExecute (Bitmap result){
            //Upscales and sets image view to bitmap
            imageView.setImageBitmap(Bitmap.createScaledBitmap(result, 400, 400, false));
            //Save bitmap in pokemon object so it wont need to be downloaded again
            saveBitmap(result, position);
        }
    }
}
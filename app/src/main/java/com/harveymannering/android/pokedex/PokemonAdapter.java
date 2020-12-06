package com.harveymannering.android.pokedex;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import java.util.ArrayList;

public class PokemonAdapter extends BaseAdapter implements Filterable {

    private ArrayList<Pokemon> dataSet;
    private Context context;

    //Objects used for searching
    public static ArrayList<Pokemon> filteredDataSet;
    private ValueFilter valueFilter;

    public PokemonAdapter(Context context, ArrayList<Pokemon> dataSet){
        this.context = context;
        this.dataSet = dataSet;
        filteredDataSet =  dataSet;
    }

    @Override
    public int getCount() {
        return filteredDataSet.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    //Returns a single pokemon card
    public View getView(int position, View convertView, ViewGroup parent) {
        //Overall layout is a LinearLayout
        View view = LayoutInflater.from(context).inflate(R.layout.card_pokemon, parent, false);

        //Views within the layout
        TextView text_id = (TextView) view.findViewById(R.id.pokemon_number);
        TextView text_name = (TextView) view.findViewById(R.id.pokemon_name);
        ImageView image_pokemon = (ImageView) view.findViewById(R.id.pokemon_sprite);

        //Set the text for each pokemon
        text_name.setText(filteredDataSet.get(position).getName());
        text_id.setText("#" + filteredDataSet.get(position).getId());


        //Set image dimensions
        LinearLayout.LayoutParams layout_params = new LinearLayout.LayoutParams(MainActivity.sprite_size, MainActivity.sprite_size);
        layout_params.gravity = Gravity.CENTER;
        image_pokemon.setLayoutParams(layout_params);

        //Set image of the pokemon
        try {
            //If sprite hasn't already been downloaded
            if (filteredDataSet.get(position).getSprite() == null) {
                //start downloading sprite in new thread
                DownloadImagesTask download = new DownloadImagesTask(image_pokemon, filteredDataSet.get(position).getId() - 1);
                download.execute(filteredDataSet.get(position).getSpriteUrl());
            }
            else {
                //Set image view to the sprite that has already been downloaded
                image_pokemon.setImageBitmap(Bitmap.createScaledBitmap(filteredDataSet.get(position).getSprite(), MainActivity.sprite_size, MainActivity.sprite_size, true));
            }
        }
        catch (Exception ex){
            ex.printStackTrace();
        }

        //Returns a LinearLayout object
        return view;
    }

    //Saves pokemon sprite in memory
    public void saveBitmap(Bitmap sprite, int listPosition){
        if (dataSet.size() > listPosition)
            dataSet.get(listPosition).setSprite(sprite);
    }

    @Override
    public Filter getFilter() {
        //Gets or creates a new filter object used for searching
        if(valueFilter==null) {
            valueFilter=new ValueFilter();
        }

        return valueFilter;
    }

    private class ValueFilter extends Filter {
        //Invoked in a worker thread to filter the data according to the constraint
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            //Return value
            FilterResults results = new FilterResults();

            //Perform no filtering for an empty string
            if(constraint!=null && constraint.length()>0){
                ArrayList<Pokemon> filterList=new ArrayList<Pokemon>();

                //Iterate through all pokemon
                for(int i = 0; i < dataSet.size(); i++){
                    //If string is in the pokemon's name, add them to the search results
                    if((dataSet.get(i).getName().toUpperCase()).contains(constraint.toString().toUpperCase())) {
                        Pokemon pokemon = new Pokemon(dataSet.get(i).pokemon_json);
                        filterList.add(pokemon);
                    }
                }
                results.count = filterList.size();
                results.values = filterList;
            }
            else{
                results.count = dataSet.size();
                results.values = dataSet;
            }
            return results;
        }


        //Invoked in the UI thread to publish the filtering results in the user interface
        @SuppressWarnings("unchecked")
        @Override
        protected void publishResults(CharSequence constraint,  FilterResults results) {
            filteredDataSet = (ArrayList<Pokemon>) results.values;
            notifyDataSetChanged();
        }
    }

    //Thread used to download pokemon sprites
    class DownloadImagesTask extends DownloadSpriteTask {

        public DownloadImagesTask(ImageView imageView, int position) {
            super(imageView, position);
        }

        protected void onPostExecute (Bitmap result){
            //Upscales and sets image view to bitmap
            if (result != null) {
                imageView.setImageBitmap(Bitmap.createScaledBitmap(result, MainActivity.sprite_size, MainActivity.sprite_size, true));
                //Save bitmap in pokemon object so it wont need to be downloaded again
                saveBitmap(result, position);
            }
        }
    }
}


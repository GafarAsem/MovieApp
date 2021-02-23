package com.movies.movieapp;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

class arrayAdapter extends ArrayAdapter {

    ArrayList<String> id;
    ArrayList<String> nameMovie;
    ArrayList<String> publish;
    ArrayList<String> rating;
    ArrayList<String> poster;

    public arrayAdapter(@NonNull Context context,ArrayList<String> id,ArrayList<String> namemovie,
                        ArrayList<String> publish,
                        ArrayList<String> rating,ArrayList<String> poster )
    {
        super(context,R.layout.moviedetail,R.id.idText, id);

        this.id=id;
        this.nameMovie=namemovie;
        this.publish=publish;
        this.rating=rating;
        this.poster=poster;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        LayoutInflater infalter=(LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View List=infalter.inflate(R.layout.moviedetail,parent,false);


        TextView ID=List.findViewById(R.id.idText);
        ID.setText(this.id.get(position));

        TextView nameMovie=List.findViewById(R.id.UpdatenameMovieText);
        nameMovie.setText(this.nameMovie.get(position));

        TextView publishText=List.findViewById(R.id.UpdatepublishText);
        publishText.setText(this.publish.get(position));

        TextView rating=List.findViewById(R.id.ratingtext);
        rating.setText(" ⭐    "+this.rating.get(position));

       ImageView poster=List.findViewById(R.id.UpdateposterView);
        poster.setImageURI(Uri.parse(this.poster.get(position)));
        return List;
    }


}

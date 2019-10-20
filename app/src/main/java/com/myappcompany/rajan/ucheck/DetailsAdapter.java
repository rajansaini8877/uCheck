package com.myappcompany.rajan.ucheck;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import java.net.URI;
import java.util.List;

public class DetailsAdapter extends ArrayAdapter<ImageData> {

    DetailsAdapter(Context context, List<ImageData> images) {
        super(context, 0, images);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View currentView = convertView;
        if(currentView==null) {
            currentView = LayoutInflater.from(getContext()).inflate(R.layout.list_item, parent, false);
        }

        ImageData imageData = getItem(position);

        ImageView magnitude = currentView.findViewById(R.id.image);
        String url = imageData.getImgurl();
        //magnitude.setImageURI();




        return currentView;

    }

}

package com.redcrescent.www.redcrescent;

import android.app.Activity;
import android.content.Context;
import android.graphics.Movie;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;

import java.util.List;
/**
 * Created by muhammadarbab on 28/01/2016.
 */

public class CustomAdapter extends BaseAdapter {
    private Activity activity;
    private LayoutInflater inflater;
    private List<DoctorModal> doctorItems;
    ImageLoader imageLoader = AppController.getInstance().getImageLoader();

    public CustomAdapter(Activity activity, List<DoctorModal> doctorItems) {
        this.activity = activity;
        this.doctorItems = doctorItems;
    }

    @Override
    public int getCount() {
        return doctorItems.size();
    }

    @Override
    public Object getItem(int location) {
        return doctorItems.get(location);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (inflater == null)
            inflater = (LayoutInflater) activity
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null)
            convertView = inflater.inflate(R.layout.doctor_list_item, null);

        if (imageLoader == null)
            imageLoader = AppController.getInstance().getImageLoader();
        NetworkImageView thumbNail = (NetworkImageView) convertView
                .findViewById(R.id.doctorImage);
        TextView name = (TextView) convertView.findViewById(R.id.doctorName);
        TextView education = (TextView) convertView.findViewById(R.id.doctorEducation);
        TextView speciality = (TextView) convertView.findViewById(R.id.doctorSpeciality);


        // getting movie data for the row
        DoctorModal m = doctorItems.get(position);

        // thumbnail image
        thumbNail.setImageUrl(m.getimgSrc(), imageLoader);

        // title
        name.setText(m.getdoctorName());

        // rating
        education.setText(m.geteducations());

        speciality.setText(m.getSpecialities());

//        // genre
//        String genreStr = "";
//        for (String str : m.getGenre()) {
//            genreStr += str + ", ";
//        }
//        genreStr = genreStr.length() > 0 ? genreStr.substring(0,
//                genreStr.length() - 2) : genreStr;
//        genre.setText(genreStr);
//
//        // release year
//        year.setText(String.valueOf(m.getYear()));

        return convertView;
    }
}
package com.cloudfoyo.magazine.wrappers;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.cloudfoyo.magazine.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class ImageFragment extends Fragment {



    public ImageFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_image, container, false);
        ImageView imageView = (ImageView) view.findViewById(R.id.imageview);
        Bundle bundle = getArguments();
        int position = bundle.getInt("pos");
       // imageView.setImageResource(images[position]);
        return view;
    }

    static ImageFragment addInstance(int position) {
        ImageFragment imageFragment = new ImageFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("position", position);
        imageFragment.setArguments(bundle);
        return imageFragment;
    }
}

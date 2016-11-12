package com.cloudfoyo.magazine;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by HEMANDS on 12-11-2016.
 */

public class MyRecyclerAdapter extends RecyclerView.Adapter<MyRecyclerAdapter.MyViewHolder> {

     private List<Contributor> contributors=new ArrayList<>();
    private  Context context;
    private  int[] imageids;
    private int size;



     public MyRecyclerAdapter(List<Contributor> contributors,int [] imageids,int size,Context context){

         this.contributors=contributors;
         this.context=context;
         this.imageids=imageids;
         this.size=size;
     }


    public class MyViewHolder extends RecyclerView.ViewHolder{

        public ImageView imageView;
        public TextView textView;




        public MyViewHolder(View view){

             super(view);

              imageView=(ImageView) view.findViewById(R.id.iv);
              textView=(TextView) view.findViewById(R.id.text);


         }

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view=(LayoutInflater.from(parent.getContext())).inflate(R.layout.grid_item,parent,false);

        MyViewHolder viewHolder=new MyViewHolder(view);

        return  viewHolder;

    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        Contributor contrib=contributors.get(position);
        holder.textView.setText(contrib.name);
        Picasso.with(context)
                .load(imageids[position]).resize(size,size).centerCrop()
                .into(holder.imageView);



    }

    @Override
    public int getItemCount() {
        return contributors.size();
    }
}

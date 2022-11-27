package com.snhuprojects.weighttrackerbasic;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class RecycleViewAdapter extends RecyclerView.Adapter<RecycleViewAdapter.MyViewHolder> {

    // initialize list using WeightModel objects for return to the recycler view.
    List<WeightModel> weightList;
    Context context;

    // constructor
    public RecycleViewAdapter(List<WeightModel> weightList, Context context) {
        this.weightList = weightList;
        this.context = context;
    }

    // view holder identifies the one line layout (xml layout file) and inflater transforms the spacing to match parent object
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.one_line_weight, parent, false);
        MyViewHolder holder = new MyViewHolder(view);
        return holder;
    }

    // method called when bind view holder -> sets the data for the three columns
    //TODO
    // add in fourth column for type of workout done
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.tv_date.setText(weightList.get(position).getDate());
        holder.tv_weight.setText(String.valueOf(weightList.get(position).getWeight()));
        holder.tv_delta.setText(String.valueOf(weightList.get(position).getDelta()));
        holder.tv_category.setText(String.valueOf(weightList.get(position).getWorkoutType()));
    }

    // returns size of weightList
    @Override
    public int getItemCount() {
        return weightList.size();
    }

    // subclass for view holder used to bind objects to corresponding layout items
    public class MyViewHolder extends RecyclerView.ViewHolder{
        TextView tv_date;
        TextView tv_weight;
        TextView tv_delta;
        TextView tv_category;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_date = itemView.findViewById(R.id.tv_date);
            tv_weight = itemView.findViewById(R.id.tv_weight);
            tv_delta = itemView.findViewById(R.id.tv_delta);
            tv_category = itemView.findViewById(R.id.tv_category);
        }
    }
}

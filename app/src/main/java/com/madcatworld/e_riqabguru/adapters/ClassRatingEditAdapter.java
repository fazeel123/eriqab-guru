package com.madcatworld.e_riqabguru.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.madcatworld.e_riqabguru.R;

import java.util.List;

import com.madcatworld.e_riqabguru.model.ClassRatingEditModel;
import com.madcatworld.e_riqabguru.ui.ClassListEdit;
import com.madcatworld.e_riqabguru.ui.ClassRatingEdit;

public class ClassRatingEditAdapter extends RecyclerView.Adapter<ClassRatingEditAdapter.ClassRatingEditViewHolder> {

    private Context context;
    private List<ClassRatingEditModel> ratingList;

    public ClassRatingEditAdapter(Context context, List<ClassRatingEditModel>ratingList) {
        this.context = context;
        this.ratingList = ratingList;
    }

    @NonNull
    @Override
    public ClassRatingEditViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.pengesahan_rekod_card, parent, false);
        return new ClassRatingEditViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ClassRatingEditAdapter.ClassRatingEditViewHolder holder, int position) {

        ClassRatingEditModel list = ratingList.get(position);

        int var = list.getClientId();
        holder.studentNameRating.setText(list.getClientName());
        holder.ratingBarStudent.setRating(list.getClientRating());

    }


    @Override
    public int getItemCount() {
        return ratingList == null ? 0 : ratingList.size();
    }

    public class ClassRatingEditViewHolder extends RecyclerView.ViewHolder {

        TextView studentNameRating;
        RatingBar ratingBarStudent;

        public ClassRatingEditViewHolder(@NonNull View itemView) {
            super(itemView);

            studentNameRating = itemView.findViewById(R.id.studentNameRatingEdit);
            ratingBarStudent = itemView.findViewById(R.id.rateView);
        }
    }
}

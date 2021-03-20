package com.madcatworld.e_riqabguru.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.madcatworld.e_riqabguru.R;

import java.util.List;

import com.madcatworld.e_riqabguru.model.StudentRatingModel;
import com.madcatworld.e_riqabguru.ui.StudentRating;

public class StudentRatingAdapter extends RecyclerView.Adapter<StudentRatingAdapter.RatingViewHolder> {

    private Context context;
    private List<StudentRatingModel> ratingList;
    private StudentRatingModel studentRating;

    public StudentRatingAdapter(Context context, List<StudentRatingModel>ratingList)
    {
        this.context = context;
        this.ratingList = ratingList;
    }

    @NonNull
    @Override
    public RatingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(context).inflate(R.layout.pembelajaran_kehadiran_card, parent, false);
        return new RatingViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull StudentRatingAdapter.RatingViewHolder holder, int position)
    {
        studentRating = ratingList.get(position);

        holder.textView.setText(studentRating.getName().toUpperCase());
        holder.ratingBarStudent.setRating(studentRating.getRating());

        holder.ratingBarStudent.setOnRatingBarChangeListener((ratingBar, rating, fromUser) ->
        {
            StudentRating studentRatingclass = (StudentRating) context;
            studentRatingclass.updateRating(position, Math.round(rating));

        });
    }

    @Override
    public int getItemCount()
    {
        return ratingList == null ? 0 : ratingList.size();
    }

    class RatingViewHolder extends RecyclerView.ViewHolder
    {
        TextView textView;
        RatingBar ratingBarStudent;

        RatingViewHolder(@NonNull View itemView) {
            super(itemView);

            textView = itemView.findViewById(R.id.nameView);
            ratingBarStudent = itemView.findViewById(R.id.rateView);
        }
    }
}

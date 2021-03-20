package com.madcatworld.e_riqabguru.adapters;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.madcatworld.e_riqabguru.R;
import com.madcatworld.e_riqabguru.model.RateCorrectionModel;
import com.madcatworld.e_riqabguru.ui.RateCorrection;

import java.util.List;

public class RateCorrectionAdapter extends RecyclerView.Adapter<RateCorrectionAdapter.RateEditViewHolder> {

    private Context context;
    private List<RateCorrectionModel> rateList;

    public RateCorrectionAdapter(Context context, List<RateCorrectionModel> rateList) {
        this.context = context;
        this.rateList = rateList;
    }

    @NonNull
    @Override
    public RateCorrectionAdapter.RateEditViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.rate_correction, parent, false);
        return new RateEditViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RateCorrectionAdapter.RateEditViewHolder holder, int position) {
        RateCorrectionModel list = rateList.get(position);

        int var = list.getClientId();
        holder.studentNameRating.setText(list.getClientName().toUpperCase());
        holder.ratingBarStudent.setRating(list.getClientRating());

        holder.ratingBarStudent.setOnRatingBarChangeListener((ratingBar, rating, fromUser) ->
        {
            RateCorrection studentRatingEdit = (RateCorrection) context;
            studentRatingEdit.updateRating(position, Math.round(rating));

        });
        Log.i("Rating: ", String.valueOf(list.getClientRating()));

        Intent intent = new Intent(context, RateCorrection.class);
        intent.putExtra("clientId", list.getClientId());
        intent.putExtra("clientName", list.getClientName());
        intent.putExtra("clientRating", list.getClientRating());
        context.startActivity(intent);

    }

    @Override
    public int getItemCount() {
        return rateList == null ? 0 : rateList.size();
    }

    public class RateEditViewHolder extends RecyclerView.ViewHolder {

        TextView studentNameRating;
        RatingBar ratingBarStudent;

        public RateEditViewHolder(@NonNull View itemView) {
            super(itemView);

            studentNameRating = itemView.findViewById(R.id.nameView);
            ratingBarStudent = itemView.findViewById(R.id.rateView);
        }
    }
}

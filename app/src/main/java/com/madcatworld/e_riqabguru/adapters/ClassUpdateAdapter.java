package com.madcatworld.e_riqabguru.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.madcatworld.e_riqabguru.R;

import java.util.List;

import com.madcatworld.e_riqabguru.model.ClassUpdateModel;

public class ClassUpdateAdapter extends RecyclerView.Adapter<ClassUpdateAdapter.ViewHolder> {

//    private List<RekodModel> rkModel = new ArrayList<>();
//    private final View.OnClickListener mOnClickListener;
//
//    public RekodAdapter(final List<RekodModel> viewModels, View.OnClickListener mOnClickListener) {
//        this.mOnClickListener = mOnClickListener;
//        if(viewModels != null) {
//            this.rkModel.addAll(viewModels);
//        }
//    }
//
//    @NonNull
//    @Override
//    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//
//        final View view = LayoutInflater.from(parent.getContext()).inflate(viewType, parent, false);
//        view.setOnClickListener(mOnClickListener);
//        return new RekodViewHolder(view);
//    }
//
//    @Override
//    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
//        ((RekodViewHolder) holder).bindData(rkModel.get(position));
//    }
//
//    @Override
//    public int getItemCount() {
//        return rkModel.size();
//    }
//
//    @Override
//    public int getItemViewType(final int position) {
//        return R.layout.rekod_card;
//    }

    private List<ClassUpdateModel> rekodList;
    private OnRekodListener mOnRekodListener;

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView textViewName, textViewDate;
        ImageView imageViewIcon;
        OnRekodListener onRekodListener;

        public ViewHolder(@NonNull View itemView, OnRekodListener onRekodListener) {
            super(itemView);
            this.textViewName = (TextView) itemView.findViewById(R.id.studentNameRating);
            this.textViewDate = (TextView) itemView.findViewById(R.id.textView2);
            this.imageViewIcon = (ImageView) itemView.findViewById(R.id.imageView);

            this.onRekodListener = onRekodListener;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            onRekodListener.onRekodListener(getAdapterPosition());
        }
    }

    public ClassUpdateAdapter(List<ClassUpdateModel> rekodList, OnRekodListener onRekodListener) {
        this.rekodList = rekodList;
        this.mOnRekodListener = onRekodListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.rekod_card, parent, false);
        return new ViewHolder(itemView, mOnRekodListener);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {

        ClassUpdateModel rekod = rekodList.get(position);
        holder.textViewName.setText(rekod.getName());
        holder.textViewDate.setText(rekod.getDate());
        //holder.imageViewIcon.setImageResource(rekodList.get(position).getImage());
    }

    @Override
    public int getItemCount() {
        return rekodList.size();
    }

    public interface OnRekodListener {
        void onRekodListener(int position);
    }
}

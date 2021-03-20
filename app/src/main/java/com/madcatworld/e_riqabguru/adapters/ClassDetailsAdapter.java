package com.madcatworld.e_riqabguru.adapters;
import com.madcatworld.e_riqabguru.model.ClassDetailsModel;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.madcatworld.e_riqabguru.R;
import com.madcatworld.e_riqabguru.ui.ClassCorrection;
import com.madcatworld.e_riqabguru.ui.StudentRating;

import java.util.ArrayList;
import java.util.List;

public class ClassDetailsAdapter extends RecyclerView.Adapter<ClassDetailsAdapter.ClassViewHolder> {

    private Context context;
    private List<ClassDetailsModel> classDetailList;
    private ClassDetailsModel list;

    public ClassDetailsAdapter(Context context, List<ClassDetailsModel>kelasList) {
        this.context = context;
        this.classDetailList = kelasList;
    }

    @NonNull
    @Override
    public ClassViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.kelas_card, parent, false);;
        return new ClassViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ClassViewHolder holder, int position) {

        list = classDetailList.get(position);
        int var = list.getId();

        holder.textViewName.setText(list.getTitle());
        holder.textViewDate.setText(list.getDate());

        String value = list.getDate().replaceFirst("2020-", "");
        String valueTwo = value.substring(0, value.length()-3);
//        Toast.makeText(context, String.valueOf(valueTwo), Toast.LENGTH_LONG).show();

        if(valueTwo.contains("01")) {
            list.setMonth("Januari");
            holder.textViewMonth.setText("Januari");
            holder.textViewDate.setText(list.getDate());
        } else if(valueTwo.contains("02")) {
            list.setMonth("Februari");
            holder.textViewMonth.setText("Februari");
            holder.textViewDate.setText(list.getDate());
        } else if(valueTwo.contains("03")) {
            list.setMonth("Mac");
            holder.textViewMonth.setText("Mac");
            holder.textViewDate.setText(list.getDate());
        } else if(valueTwo.contains("04")) {
            list.setMonth("April");
            holder.textViewMonth.setText("April");
            holder.textViewDate.setText(list.getDate());
        } else if(valueTwo.contains("05")) {
            list.setMonth("Mei");
            holder.textViewMonth.setText("Mei");
            holder.textViewDate.setText(list.getDate());
        } else if(valueTwo.contains("06")) {
            list.setMonth("Jun");
            holder.textViewMonth.setText("Jun");
            holder.textViewDate.setText(list.getDate());
        } else if(valueTwo.contains("07")) {
            list.setMonth("Julai");
            holder.textViewMonth.setText("Julai");
            holder.textViewDate.setText(list.getDate());
        } else if(valueTwo.contains("08")) {
            list.setMonth("Ogos");
            holder.textViewMonth.setText("Ogos");
            holder.textViewDate.setText(list.getDate());
        } else if(valueTwo.contains("09")) {
            list.setMonth("September");
            holder.textViewMonth.setText("September");
            holder.textViewDate.setText(list.getDate());
        } else if(valueTwo.contains("10")) {
            list.setMonth("Oktober");
            holder.textViewMonth.setText("Oktober");
            holder.textViewDate.setText(list.getDate());
        } else if(valueTwo.contains("11")) {
            list.setMonth("November");
            holder.textViewMonth.setText("November");
            holder.textViewDate.setText(list.getDate());
        } else if(valueTwo.contains("12")) {
            list.setMonth("Disember");
            holder.textViewMonth.setText("Disember");
            holder.textViewDate.setText(list.getDate());
        }

        if(list.getStatus() == "null") {
            holder.imageViewName.setImageResource(R.drawable.ic_pause_circle_filled);

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    Toast.makeText(context, "Class Status Pending...", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(context, StudentRating.class);
                    intent.putExtra("id", var);
                    context.startActivity(intent);
                }
            });

        } else if (list.getStatus().equals("1")) {
            holder.imageViewName.setImageResource(R.drawable.ic_check_circle);

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(context, "Class Checked Already", Toast.LENGTH_SHORT).show();
                }
            });

        } else {
            holder.imageViewName.setImageResource(R.drawable.ic_cancel);

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent();
                    int positionView = holder.getAdapterPosition();
                    intent = new Intent(context, ClassCorrection.class);
                    intent.putExtra("id", var);
                    context.startActivity(intent);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return classDetailList == null ? 0 : classDetailList.size();
    }

    @Override
    public int getItemViewType(int position) {

        return super.getItemViewType(position);
    }

    public class ClassViewHolder extends RecyclerView.ViewHolder {

        TextView textViewName, textViewDate, textViewMonth;
        ImageView imageViewName;

        public ClassViewHolder(@NonNull View itemView) {
            super(itemView);

            textViewName = itemView.findViewById(R.id.nameView);
            textViewDate = itemView.findViewById(R.id.textView2);
            imageViewName = itemView.findViewById(R.id.imageView);
            textViewMonth = itemView.findViewById(R.id.month);
        }
    }

    public void setSearchOperation(List<ClassDetailsModel> newList) {
        classDetailList = new ArrayList<>();
        classDetailList.addAll(newList);
        notifyDataSetChanged();
    }
}

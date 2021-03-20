package com.madcatworld.e_riqabguru.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.madcatworld.e_riqabguru.R;
import com.madcatworld.e_riqabguru.model.ClassDetailsModel;
import com.madcatworld.e_riqabguru.model.ClassListEditModel;
import com.madcatworld.e_riqabguru.ui.ClassEdit;
import com.madcatworld.e_riqabguru.ui.ClassListEdit;

import java.util.ArrayList;
import java.util.List;

public class ClassListEditAdapter extends RecyclerView.Adapter<ClassListEditAdapter.ClassListEditViewHolder> {

    private Context context;
    private List<ClassListEditModel> classLists;
    private ClassListEditModel list;

    public ClassListEditAdapter(Context context, List<ClassListEditModel>classList) {
        this.context = context;
        this.classLists = classList;
    }

    @NonNull
    @Override
    public ClassListEditViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.rekod_card, parent, false);
        return new ClassListEditViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull ClassListEditViewHolder holder, int position) {

        list = classLists.get(position);

        int var = list.getId();

        holder.textViewName.setText(list.getTitle());
        holder.textViewDate.setText(list.getDate());

        String value = list.getDate().replaceFirst("2020-", "");
        String valueTwo = value.substring(0, value.length()-3);

        holder.imageViewName.setImageResource(R.drawable.ic_keyboard_arrow_right);

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

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ClassEdit.class );
                intent.putExtra("id", var);
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return classLists == null ? 0 : classLists.size();
    }

    public class ClassListEditViewHolder extends RecyclerView.ViewHolder {

        TextView textViewName, textViewDate, textViewMonth;
        ImageView imageViewName;

        public ClassListEditViewHolder(@NonNull View itemView) {
            super(itemView);

            textViewName = itemView.findViewById(R.id.nameView);
            textViewDate = itemView.findViewById(R.id.textView2);
            imageViewName = itemView.findViewById(R.id.imageView);
            textViewMonth = itemView.findViewById(R.id.month);
        }
    }

    public void setSearchOperation(List<ClassListEditModel> newList) {
        classLists = new ArrayList<>();
        classLists.addAll(newList);
        notifyDataSetChanged();
    }
}

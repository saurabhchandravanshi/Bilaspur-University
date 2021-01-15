package com.edgesoft.resulthour.edesoft;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ViewResultAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<Result> dataList;
    public ViewResultAdapter(List<Result> dataList) {
        this.dataList = dataList;
    }
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.result_list,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final Result data = dataList.get(position);
        final MyViewHolder myViewHolder = (MyViewHolder)holder;
        myViewHolder.header.setText("Roll No. "+data.getId());
        myViewHolder.title.setText("Name - "+data.getTitle());
        myViewHolder.sub_title.setText("Bilaspur University, Bilaspur");


        myViewHolder.negativeBtn.setText("");
        myViewHolder.posBtn.setText("GET RESULT");

        myViewHolder.posBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(),ResultWebViewActivity.class);
                intent.putExtra("url",data.getDate()); // here getDate() returns url.
                view.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }
    class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView header,title,sub_title;
        private Button posBtn,negativeBtn;
        public MyViewHolder(View itemView) {
            super(itemView);
            header = itemView.findViewById(R.id.result_list_header);
            title = itemView.findViewById(R.id.result_list_title);
            sub_title = itemView.findViewById(R.id.result_list_sub_title);
            negativeBtn = itemView.findViewById(R.id.result_list_negative_btn);
            posBtn = itemView.findViewById(R.id.result_list_positive_btn);
        }
    }
}

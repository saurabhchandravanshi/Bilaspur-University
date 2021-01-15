package com.edgesoft.resulthour.edesoft;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ResultListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<Result> dataList;
    public ResultListAdapter(List<Result> dataList) {
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
        myViewHolder.header.setText(data.getDate().replaceAll("-"," "));
        myViewHolder.title.setText(data.getTitle());
        myViewHolder.sub_title.setText("Bilaspur University, Bilaspur");


        myViewHolder.negativeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(Intent.ACTION_SEND);
                intent.setType("text/plain");
                intent.putExtra(Intent.EXTRA_SUBJECT,"Result Declared For "+data.getTitle());
                intent.putExtra(Intent.EXTRA_TEXT,"Result Declared For "+data.getTitle()+" on "+data.getDate()
                +" By  Bilaspur University."+
                        "\nGet it on App click below link\nhttps://play.google.com/store/apps/details?id=com.i3developer.bu");
                myViewHolder.itemView.getContext().startActivity(Intent.createChooser(intent,"Share Via"));
            }
        });

        myViewHolder.posBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(myViewHolder.itemView.getContext(),SearchResultActivity.class);
                intent.putExtra("exam_id",data.getId());
                intent.putExtra("uid","1");
                myViewHolder.itemView.getContext().startActivity(intent);
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

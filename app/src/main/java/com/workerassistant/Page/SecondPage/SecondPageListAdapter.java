package com.workerassistant.Page.SecondPage;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.workerassistant.R;

import java.util.ArrayList;
import java.util.List;

public class SecondPageListAdapter extends RecyclerView.Adapter<SecondPageListAdapter.MasonryView> {

    private List<String> datas = new ArrayList<>();

    public SecondPageListAdapter(List<String> list) {

        this.datas =list;
    }

    @Override
    public MasonryView onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.second_page_item,parent,false);
        return new MasonryView(view);
    }

    @Override
    public void onBindViewHolder(MasonryView holder, int position) {
        holder.textView.setText(datas.get(position));
//        holder.textView.setPadding(0, 20 * position, 0, 0);
    }

    @Override
    public int getItemCount() {
        return datas.size();
    }

    class MasonryView extends RecyclerView.ViewHolder {
        TextView textView;

        public MasonryView(View itemView) {
            super(itemView);
            textView = (TextView) itemView.findViewById(R.id.second_page_item_statu);


        }

    }

    public void addDataList(List<String> d){
        datas.addAll(d);
    }

    public void addFirstData(String d){
        datas.add(0,d);
    }

    public void addLastData(String d){
        datas.add(getItemCount(),d);
    }
}
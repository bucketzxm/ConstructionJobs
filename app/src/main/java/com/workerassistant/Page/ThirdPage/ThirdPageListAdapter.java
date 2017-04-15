package com.workerassistant.Page.ThirdPage;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.workerassistant.R;

import java.util.List;

/**
 * Created by eva on 2017/4/14.
 */

public class ThirdPageListAdapter extends RecyclerView.Adapter<ThirdPageListAdapter.MasonryView> {

    private final List<String> datas;
    private Context context;
    public ThirdPageListAdapter(Context context, List<String> list) {
        this.context = context;
        this.datas=list;
    }

    @Override
    public MasonryView onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.third_page_item,parent,false);
        return new MasonryView(view);
    }

    @Override
    public void onBindViewHolder(MasonryView holder, int position) {
        holder.textView.setText(datas.get(position));
    }

    @Override
    public int getItemCount() {
        return datas.size();
    }

    class MasonryView extends RecyclerView.ViewHolder {
        TextView textView;

        public MasonryView(View itemView) {
            super(itemView);
            textView = (TextView) itemView.findViewById(R.id.third_page_item_city);


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
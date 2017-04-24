package com.workerassistant.Page.ThirdPage;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.workerassistant.R;
import com.workerassistant.bean.ProjectBean;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by eva on 2017/4/14.
 */

public class ThirdPageListAdapter extends RecyclerView.Adapter<ThirdPageListAdapter.MasonryView> {

    private final List<ProjectBean> datas;
    private Context context;
    public ThirdPageListAdapter(Context context, List<ProjectBean> list) {
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
        holder.tvNumberNeeded.setText(datas.get(position).getNumNeed());
        holder.tvCity.setText(datas.get(position).getCity());
        holder.tvWorkType.setText(datas.get(position).getWorkType());
        holder.tvEndTime.setText(datas.get(position).getEndTime());
        holder.tvStartTime.setText(datas.get(position).getStartTime());
        holder.tvContactPhone.setText(datas.get(position).getContactPhone());
        holder.tvContactPerson.setText(datas.get(position).getContactName());
    }

    @Override
    public int getItemCount() {
        return datas.size();
    }

    class MasonryView extends RecyclerView.ViewHolder {
        private TextView tvContactPhone,tvContactPerson,tvStartTime,tvEndTime;
        private TextView tvNumberNeeded,tvWorkType,tvCity;
        public MasonryView(View itemView) {
            super(itemView);
            tvCity = (TextView) itemView.findViewById(R.id.third_page_item_city);
            tvContactPerson  = (TextView) itemView.findViewById(R.id.third_page_item_contact_person);
            tvContactPhone = (TextView) itemView.findViewById(R.id.third_page_item_contact_phone);
            tvEndTime  = (TextView) itemView.findViewById(R.id.third_page_item_end_time);
            tvStartTime  = (TextView) itemView.findViewById(R.id.third_page_item_start_time);
            tvNumberNeeded = (TextView) itemView.findViewById(R.id.third_page_item_number_needed);
            tvWorkType = (TextView) itemView.findViewById(R.id.third_page_item_work_type);

        }

    }
    public void ClearaddDataList(List<ProjectBean> d){
        datas.clear();
        addAllDatas(d);

    }
    public void addDataList(List<ProjectBean> d){
        addAllDatas(d);
    }

    private void addAllDatas(List<ProjectBean> d){
        datas.addAll(d);
        Collections.sort(datas, new Comparator<ProjectBean>() {
            @Override
            public int compare(ProjectBean o1, ProjectBean o2) {
                return o2.getTimeStamp().compareTo(o1.getTimeStamp());
            }
        });
    }

    public void addFirstData(ProjectBean d){
        datas.add(0,d);
    }

    public void addLastData(ProjectBean d){
        datas.add(getItemCount(),d);
    }
}
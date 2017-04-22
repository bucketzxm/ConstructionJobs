package com.workerassistant.Page.SecondPage;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.workerassistant.R;
import com.workerassistant.bean.PersonBean;

import java.util.List;

import me.zhanghai.android.materialratingbar.MaterialRatingBar;

public class SecondPageListAdapter extends RecyclerView.Adapter<SecondPageListAdapter.MasonryView> {

    private List<PersonBean> datas;

    public SecondPageListAdapter(List<PersonBean> list) {
        this.datas=list;
    }

    @Override
    public MasonryView onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.second_page_item,parent,false);
        return new MasonryView(view);
    }

    @Override
    public void onBindViewHolder(MasonryView holder, int position) {
        holder.tvName.setText(datas.get(position).getName());
        holder.tvType.setText(datas.get(position).getWorkType());
        holder.tvLevel.setText(datas.get(position).getLevel());
        holder.tvPhone.setText(datas.get(position).getPhone());
//        statue 空闲状态没有动 -
        holder.ratingStar.setRating(4.5f);
//        holder.textView.setPadding(0, 20 * position, 0, 0);
    }

    @Override
    public int getItemCount() {
        return datas.size();
    }

    class MasonryView extends RecyclerView.ViewHolder {
        private TextView tvName,tvStatu,tvLevel,tvType,tvPhone;
        private MaterialRatingBar ratingStar;
        public MasonryView(View itemView) {
            super(itemView);
            tvName = (TextView) itemView.findViewById(R.id.second_page_item_name);
            tvStatu =(TextView) itemView.findViewById(R.id.second_page_item_statu);
            tvLevel=(TextView) itemView.findViewById(R.id.second_page_item_level);
            tvPhone=(TextView) itemView.findViewById(R.id.second_page_item_phone);
            tvType=(TextView) itemView.findViewById(R.id.second_page_item_type);
            ratingStar = (MaterialRatingBar) itemView.findViewById(R.id.second_page_item_star);
        }

    }

    public void addDataList(List<PersonBean> d){
        datas.addAll(d);
    }
    public void ClearaddDataList(List<PersonBean> d){
        datas.clear();
        datas.addAll(d);
    }

    public void addFirstData(PersonBean d){
        datas.add(0,d);
    }

    public void addLastData(PersonBean d){
        datas.add(getItemCount(),d);
    }
}
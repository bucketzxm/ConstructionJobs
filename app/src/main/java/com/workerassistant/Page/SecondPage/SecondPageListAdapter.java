package com.workerassistant.Page.SecondPage;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.workerassistant.R;
import com.workerassistant.bean.PersonBean;

import java.util.Collections;
import java.util.Comparator;
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
        holder.tvCity.setText(datas.get(position).getCity());
//        statue 空闲状态没有动 -
        holder.ratingStar.setRating(4.5f);
//        holder.textView.setPadding(0, 20 * position, 0, 0);
    }

    @Override
    public int getItemCount() {
        return datas.size();
    }

    class MasonryView extends RecyclerView.ViewHolder {
        private TextView tvName,tvStatu,tvLevel,tvType,tvPhone,tvCity;
        private MaterialRatingBar ratingStar;
        public MasonryView(View itemView) {
            super(itemView);
            tvName = (TextView) itemView.findViewById(R.id.second_page_item_name);
            tvStatu =(TextView) itemView.findViewById(R.id.second_page_item_statu);
            tvLevel=(TextView) itemView.findViewById(R.id.second_page_item_level);
            tvPhone=(TextView) itemView.findViewById(R.id.second_page_item_phone);
            tvType=(TextView) itemView.findViewById(R.id.second_page_item_type);
            tvCity=(TextView) itemView.findViewById(R.id.second_page_item_city);
            ratingStar = (MaterialRatingBar) itemView.findViewById(R.id.second_page_item_star);
        }

    }

    public void addDataList(List<PersonBean> d){
        addAllDatas(d);
    }
    public void ClearaddDataList(List<PersonBean> d){
        datas.clear();
        addAllDatas(d);
    }
    private void addAllDatas(List<PersonBean> d){
        datas.addAll(d);
        Collections.sort(datas, new Comparator<PersonBean>() {
            @Override
            public int compare(PersonBean o1, PersonBean o2) {
//                Log.d("o2.getTimeStamp()"+o2.getTimeStamp(),
//                        " o1.getTimeStamp()"+(o1.getTimeStamp()));
                return o2.getTimeStamp().compareTo(o1.getTimeStamp());
            }
        });
    }

    public void addFirstData(PersonBean d){
        datas.add(0,d);
    }

    public void addLastData(PersonBean d){
        datas.add(getItemCount(),d);
    }
}
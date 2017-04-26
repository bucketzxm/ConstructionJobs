package com.workerassistant.Page.FirstPage;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.workerassistant.R;
import com.workerassistant.bean.PersonBean;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import me.zhanghai.android.materialratingbar.MaterialRatingBar;

public class LeftPersonAdapter extends BaseAdapter {
    private Context context;
    private List<PersonBean> list;

    public LeftPersonAdapter(Context context, List<PersonBean> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list == null ? 0 : list.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.top_person_item, null);
            holder = new ViewHolder();
            holder.tvName = (TextView) convertView.findViewById(R.id.hottest_man_item_name);
            holder.tvWorkType = (TextView) convertView.findViewById(R.id.hottest_man_item_work_type);
            holder.ratingBar = (MaterialRatingBar) convertView.findViewById(R.id.hottest_man_star);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.tvWorkType.setText(list.get(position).getWorkType());
        holder.tvName.setText(list.get(position).getName());
        float rating = 0;
        if (list.get(position).getRating() == null){
            rating = Float.parseFloat(Math.ceil(Math.random()*5)+"");
        }else{
            rating = Integer.parseInt(list.get(position).getRating());
        }
        holder.ratingBar.setRating(rating);
        return convertView;
    }
    public void clearAddDataList(List<PersonBean> beanList){
        list.clear();
        addAllDatas(beanList);
    }

    private void addAllDatas(List<PersonBean> d){
        list.addAll(d);
        Collections.sort(list, new Comparator<PersonBean>() {
            @Override
            public int compare(PersonBean o1, PersonBean o2) {
                return o2.getTimeStamp().compareTo(o1.getTimeStamp());
            }
        });
    }

    private int selectedPosition = 0;

    public void setSelectedPosition(int selectedPosition) {
        this.selectedPosition = selectedPosition;
    }

    public int getSelectedPosition() {
        return selectedPosition;
    }

    private class ViewHolder {
        TextView tvName,tvWorkType;
        MaterialRatingBar ratingBar;
    }

}

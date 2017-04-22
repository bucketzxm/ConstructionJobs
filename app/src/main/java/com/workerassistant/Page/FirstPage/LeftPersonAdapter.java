package com.workerassistant.Page.FirstPage;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.workerassistant.R;
import com.workerassistant.bean.PersonBean;

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
            rating = 2.5f;
        }else{
            rating = Integer.parseInt(list.get(position).getRating());
        }
        holder.ratingBar.setRating(rating);

        return convertView;
    }
    public void clearAddDataList(List<PersonBean> beanList){
        list.clear();
        list.addAll(beanList);
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





//        //选中和没选中时，设置不同的颜色
//        if (position == selectedPosition){
//            convertView.setBackgroundResource(R.color.popup_right_bg);
//        }else{
//            convertView.setBackgroundResource(R.drawable.tab_shape_filter_white_button_text_color);
//        }

//        if (list.get(position).getSecondList() != null && list.get(position).getSecondList().size() > 0) {
//            holder.nameTV.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_keyboard_arrow_right_black_18dp, 0);
//        } else {
//            holder.nameTV.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
//        }
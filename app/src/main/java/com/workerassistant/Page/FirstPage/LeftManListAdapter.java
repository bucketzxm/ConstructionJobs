package com.workerassistant.Page.FirstPage;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.workerassistant.R;

import java.util.List;

public class LeftManListAdapter extends BaseAdapter {
    private Context context;
    private List<String> list;

    public LeftManListAdapter(Context context, List<String> list) {
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
            convertView = LayoutInflater.from(context).inflate(R.layout.second_page_item, null);
            holder = new ViewHolder();
//            holder.nameTV = (TextView) convertView.findViewById(R.id.left_item_name);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
//
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

        return convertView;
    }

    private int selectedPosition = 0;

    public void setSelectedPosition(int selectedPosition) {
        this.selectedPosition = selectedPosition;
    }

    public int getSelectedPosition() {
        return selectedPosition;
    }

    private class ViewHolder {
        TextView nameTV;
    }
}
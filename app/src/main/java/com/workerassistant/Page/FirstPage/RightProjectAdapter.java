package com.workerassistant.Page.FirstPage;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.workerassistant.R;
import com.workerassistant.bean.ProjectBean;

import java.util.List;

/**
 * Created by eva on 2017/4/22.
 */

public class RightProjectAdapter extends BaseAdapter {
    private Context context;
    private List<ProjectBean> list;

    public RightProjectAdapter(Context context, List<ProjectBean> list) {
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
            convertView = LayoutInflater.from(context).inflate(R.layout.top_project_item, null);
            holder = new ViewHolder();
            holder.tvCity = (TextView) convertView.findViewById(R.id.top_project_item_city);
            holder.tvNumber = (TextView) convertView.findViewById(R.id.top_project_item_number_needed);
            holder.tvWorkType = (TextView) convertView.findViewById(R.id.top_project_item_work_type);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.tvNumber.setText(list.get(position).getNumNeed());
        holder.tvCity.setText(list.get(position).getCity());
        holder.tvWorkType.setText(list.get(position).getWorkType());
        return convertView;
    }
    public void clearAddDataList(List<ProjectBean> beanList){
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
        TextView tvNumber,tvWorkType,tvCity;
    }

}

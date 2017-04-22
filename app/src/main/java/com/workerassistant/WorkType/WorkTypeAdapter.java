package com.workerassistant.WorkType;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.workerassistant.R;
import com.workerassistant.WorkType.bean.WorkTypeBean;

import java.util.ArrayList;
import java.util.List;

public class WorkTypeAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context context;
    private List<WorkTypeBean> mDatas;

    //type
    public static final int TYPE_HOT_HEAD = 0xff02;
    public static final int TYPE_HOT = 0xff03;
    public static final int TYPE_TEXT_HEAD = 0xff04;
    public static final int TYPE_TEXT = 0xff05;

    public WorkTypeAdapter(Context context) {
        this.context = context;
        mDatas = new ArrayList<WorkTypeBean>();
    }

    public WorkTypeAdapter(Context context,List<WorkTypeBean> data) {
        this.context = context;
        mDatas = new ArrayList<WorkTypeBean>();
        mDatas.addAll(data);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType){
            case TYPE_HOT_HEAD:
            case TYPE_TEXT_HEAD:
                return new HolderTypeTextHead(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_tv_title, parent, false));
            case TYPE_HOT:
            case TYPE_TEXT:
                return new HolderTypeText(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_black_border, parent, false));
            default:
                Log.d("error","viewholder is null");
                return null;
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof HolderTypeTextHead){
            bindType2Head((HolderTypeTextHead) holder, position);
        }else if (holder instanceof HolderTypeText){
            bindType2((HolderTypeText) holder, position);
        }
    }

    @Override
    public int getItemCount() {
        return mDatas.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0){
            return TYPE_HOT_HEAD;
        }else if (1<=position && position <= 4){
            return TYPE_HOT;
        }else if (position == 5){
            return TYPE_TEXT_HEAD;
        }
        else {
            return TYPE_TEXT;
        }
    }

    @Override
    public void onAttachedToRecyclerView(final RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);

        RecyclerView.LayoutManager manager = recyclerView.getLayoutManager();
        if(manager instanceof GridLayoutManager) {
            final GridLayoutManager gridManager = ((GridLayoutManager) manager);
            gridManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    int type = getItemViewType(position);
                    switch (type){
                        case TYPE_HOT_HEAD:
                        case TYPE_TEXT_HEAD:
                            return gridManager.getSpanCount();
                        case TYPE_HOT:
                            return 1;
                        case TYPE_TEXT:
                            return 1;
                        default:
                            return 1;
                    }
                }
            });
        }
    }

    /////////////////////////////



    private void bindType2Head(HolderTypeTextHead holder, int position){
        if(position == 0){

        }else{
            holder.item_txt.setText("其他");
        }
    }

    private void bindType2(HolderTypeText holder, int position){
        holder.item_txt.setText(mDatas.get(position).getWorkTypeName());
//        这个只是 "热门"  "其他工种"等标题名
    }


    /////////////////////////////

    public class HolderTypeTextHead extends RecyclerView.ViewHolder {
        public TextView item_txt;
        public HolderTypeTextHead(View itemView) {
            super(itemView);
            item_txt = (TextView) itemView.findViewById(R.id.work_type_search_item_tv_title);
        }
    }
    public class HolderTypeText extends RecyclerView.ViewHolder {
        public TextView item_txt;

        public HolderTypeText(View itemView) {
            super(itemView);
            item_txt = (TextView) itemView.findViewById(R.id.work_type_tv_item);
        }
    }

}
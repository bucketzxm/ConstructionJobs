package com.workerassistant.WorkType;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;
import android.widget.Toast;

import com.workerassistant.R;
import com.workerassistant.Util.Constant;
import com.workerassistant.Util.rxbus.RxBus;
import com.workerassistant.WorkType.bean.WorkTypeBean;

import java.util.ArrayList;
import java.util.List;

/**
 *  搜索结果显示Fragment
 */
public class SearchWorkTypeFragment extends Fragment {
    private RecyclerView mRecyclerView;
    private TextView mTvNoResult;
    private SearchAdapter mAdapter;
    private List<WorkTypeBean> mDatas;

    private String mQueryText;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search_city, container, false);
        mTvNoResult = (TextView) view.findViewById(R.id.tv_no_result);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recy);
        return view;
    }

    public void bindDatas(List<WorkTypeBean> datas) {
        this.mDatas = datas;
        mAdapter = new SearchAdapter();
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setAdapter(mAdapter);
        if (mQueryText != null) {
            mAdapter.getFilter().filter(mQueryText);
        }
    }

    /**
     * 根据newText 进行查找, 显示
     */
    public void bindQueryText(String newText) {
        if (mDatas == null) {
            mQueryText = newText.toLowerCase();
        } else if (!TextUtils.isEmpty(newText)) {
            mAdapter.getFilter().filter(newText.toLowerCase());
        }
    }

    private class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.VH> implements Filterable {
        private List<WorkTypeBean> items = new ArrayList<>();

        public SearchAdapter() {
            items.clear();
            items.addAll(mDatas);
        }

        @Override
        public VH onCreateViewHolder(ViewGroup parent, int viewType) {
            final VH holder = new VH(LayoutInflater.from(getActivity()).inflate(R.layout.item_search_item, parent, false));
            holder.tvName.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = holder.getAdapterPosition();
                    Toast.makeText(getActivity(), "选择了" + items.get(position).getWorkTypeName(),Toast.LENGTH_SHORT).show();
                    TextView tv = (TextView)v;

//        返回选中值
                    WorkTypeBean workTypeBean = new WorkTypeBean();
                    workTypeBean.setWorkTypeName(tv.getText().toString());
                    RxBus.getDefault().post(workTypeBean);

//        设置OnActivityResult返回值
                    Intent intent = new Intent();
                    Bundle bundle = new Bundle();
                    bundle.putString("WorkType",workTypeBean.getWorkTypeName());
                    intent.putExtras(bundle);

                    Activity mainActivity = (Activity)getActivity();
                    mainActivity.setResult(Constant.requestThirdTopCity,intent);
                    mainActivity.finish();

                }
            });
            return holder;
        }

        @Override
        public int getItemCount() {
            return items.size();
        }

        @Override
        public void onBindViewHolder(VH holder, int position) {
            holder.tvName.setText(items.get(position).getWorkTypeName());
        }

        @Override
        public Filter getFilter() {
            return new Filter() {
                @Override
                protected FilterResults performFiltering(CharSequence constraint) {
                    ArrayList<WorkTypeBean> list = new ArrayList<>();
                    for (WorkTypeBean item : mDatas) {
                        if ( item.getWorkTypeName().contains(constraint)) {//item.getWorkTypeName().startsWith(constraint.toString()) ||
                            list.add(item);
                        }
                    }
                    FilterResults results = new FilterResults();
                    results.count = list.size();
                    results.values = list;
                    return results;
                }

                @Override
                @SuppressWarnings("unchecked")
                protected void publishResults(CharSequence constraint, FilterResults results) {
                    ArrayList<WorkTypeBean> list = (ArrayList<WorkTypeBean>) results.values;
                    items.clear();
                    items.addAll(list);
                    if (results.count == 0) {
                        mTvNoResult.setVisibility(View.VISIBLE);
                    } else {
                        mTvNoResult.setVisibility(View.INVISIBLE);
                    }
                    notifyDataSetChanged();
                }
            };
        }

        class VH extends RecyclerView.ViewHolder {
            private TextView tvName;

            public VH(View itemView) {
                super(itemView);
                tvName = (TextView) itemView.findViewById(R.id.tv_item_search);
            }
        }
    }
}

package com.workerassistant.Page.SecondPage;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.workerassistant.CustomUI.LFRecycleview.LFRecyclerView;
import com.workerassistant.CustomUI.LFRecycleview.OnItemClickListener;
import com.workerassistant.CustomUI.RecyclerViewDivider;
import com.workerassistant.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by eva on 2017/4/13.
 */

public class SecondFragment extends Fragment implements OnItemClickListener, LFRecyclerView.LFRecyclerViewListener, LFRecyclerView.LFRecyclerViewScrollChange {//implements SwipeRefreshLayout.OnRefreshListener {

    public static SecondFragment newInstance() {
        SecondFragment f = new SecondFragment();
        return f;
    }

    private View rootView = null;//缓存Fragment view
    private RecyclerView recycleview;
//    private ListView listView;
    private SecondPageListAdapter adapter;
    private  List<String> data;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.secondpage_main, container, false);
        initBase();
        return rootView;
    }

    private void initBase() {

        data = new ArrayList<String>();
        data.add("11");
        data.add("11");
        data.add("11");
        recycleview = (RecyclerView)rootView.findViewById(R.id.second_page_rv_list);
        recycleview.setLayoutManager(new LinearLayoutManager(getActivity()));
        recycleview.setItemAnimator(new DefaultItemAnimator());
        recycleview.addItemDecoration(new RecyclerViewDivider(getActivity(),LinearLayoutManager.VERTICAL));
        adapter = new SecondPageListAdapter(data);
//        recycleview.addItemDecoration(new DividerItemDecoration(getActivity(),DividerItemDecoration.HORIZONTAL));
        recycleview.setAdapter(adapter);

           }


    @Override
    public void onClick(int position) {
        Toast.makeText(getActivity(), "" + position, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onLongClick(int po) {
        Toast.makeText(getActivity(), "Long:" + po, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRefresh() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                data.add(0,   "==onRefresh");
//                recycleview.stopRefresh(true);
                adapter.notifyItemInserted(0);
                adapter.notifyItemRangeChanged(0,data.size());

            }
        }, 2000);
    }

    @Override
    public void onLoadMore() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
//                recycleview.stopLoadMore();
                data.add(data.size(),   "==onLoadMore");
//                list.add(list.size(), "leefeng.me" + "==onLoadMore");
                adapter.notifyItemRangeInserted(data.size()-1,1);

            }
        }, 2000);
    }

    @Override
    public void onRecyclerViewScrollChange(View view, int i, int i1) {

    }

}
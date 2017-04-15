package com.workerassistant.Page.SecondPage;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.workerassistant.CustomUI.RecyclerViewDivider;
import com.workerassistant.R;

import java.util.ArrayList;
import java.util.List;

import me.leefeng.lfrecyclerview.LFRecyclerView;
import me.leefeng.lfrecyclerview.OnItemClickListener;

import static com.workerassistant.R.id.second_page_rv_list;

/**
 * Created by eva on 2017/4/13.
 */

public class SecondFragment extends Fragment  implements OnItemClickListener, LFRecyclerView.LFRecyclerViewListener, LFRecyclerView.LFRecyclerViewScrollChange {//implements SwipeRefreshLayout.OnRefreshListener {

    public static SecondFragment newInstance() {
        SecondFragment f = new SecondFragment();
        return f;
    }

    private View rootView = null;//缓存Fragment view
    private LFRecyclerView recycleview;
//    private ListView listView;
    private SecondPageListAdapter adapter;
    private  List<String> datas;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        rootView = inflater.inflate(R.layout.secondpage_main, container, false);
        initBase();
        return rootView;
    }

    private void initBase() {
        recycleview = (LFRecyclerView) rootView.findViewById(second_page_rv_list);
        recycleview.setLoadMore(true);
        recycleview.setRefresh(true);
        recycleview.setNoDateShow();
        recycleview.setAutoLoadMore(true);
        recycleview.setOnItemClickListener(this);
        recycleview.setLFRecyclerViewListener(this);
        recycleview.setScrollChangeListener(this);
        recycleview.setItemAnimator(new DefaultItemAnimator());
        recycleview.addItemDecoration(new RecyclerViewDivider(getActivity(), LinearLayoutManager.VERTICAL,2, Color.GRAY));
        datas = new ArrayList<String>();
        for(int i =0; i<19;i++) {
            datas.add("头目");
        }
        adapter = new SecondPageListAdapter(datas);
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
//                b = !b;
//                recycleview.stopRefresh(b);
                recycleview.stopRefresh(true);
                adapter.addFirstData("onRefresh");
//                adapter.notifyItemInserted(0);
//                adapter.notifyItemRangeChanged(0,datas.size());
                adapter.notifyDataSetChanged();
                Log.d( "onRefresh: ",adapter.getItemCount()+"");
            }
        }, 2000);
    }

    @Override
    public void onLoadMore() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                recycleview.stopLoadMore();
                adapter.addLastData("onLoadMore");
//                adapter.notifyItemRangeInserted(datas.size()-1,1);
                adapter.notifyDataSetChanged();
                Log.d( "onRefresh: ",adapter.getItemCount()+"");

            }
        }, 2000);
    }
    @Override
    public void onRecyclerViewScrollChange(View view, int i, int i1) {

    }


    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if(hidden){
            //when visible to user
        }else{
            //when inVisible to user

        }
    }
}



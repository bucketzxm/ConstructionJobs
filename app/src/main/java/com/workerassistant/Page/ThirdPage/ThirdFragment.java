package com.workerassistant.Page.ThirdPage;

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

import static com.workerassistant.R.id.third_page_rv_list;

/**
 * Created by eva on 2017/4/13.
 */

public class ThirdFragment extends Fragment  implements OnItemClickListener, LFRecyclerView.LFRecyclerViewListener, LFRecyclerView.LFRecyclerViewScrollChange {//implements SwipeRefreshLayout.OnRefreshListener {
    public static ThirdFragment newInstance() {
        ThirdFragment f = new ThirdFragment();
        return f;
    }

    private View rootView = null;//缓存Fragment view
//    private OneRecycleAdapter adapter;
//    private SwipeRefreshLayout lay_fresh;
//    private static List<Cloth>dataHot = new ArrayList<>();
//    priv static List<Cloth>newDatashow = new ArrayList<>();
    private LFRecyclerView recycleview;
    private ThirdPageListAdapter adapter;
    private List<String> datas;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.third_page_main, container, false);
        initBase();
//        lay_fresh = (SwipeRefreshLayout) rootView.findViewById(R.id.nearpage_refresh);
//        lay_fresh.setColorSchemeResources(R.color.colorPrimary, R.color.colorPrimaryDark);
//        lay_fresh.setOnRefreshListener(this);
//        initBase();

        return rootView;
    }
    private void initBase() {

        recycleview = (LFRecyclerView) rootView.findViewById(third_page_rv_list);
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
            datas.add("上海");
        }
        adapter = new ThirdPageListAdapter(getActivity(),datas);
        recycleview.setAdapter(adapter);
    }
    @Override
    public void onClick(int position) {
        Toast.makeText(getActivity(), "onClick" + position, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onLongClick(int po) {
        Toast.makeText(getActivity(), "onLongClick" + po, Toast.LENGTH_SHORT).show();
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



}




/*
    private void initBase() {
//        adapter = new ThirdPageListAdapter(getActivity(),datas);
//        recyclerView = (LFRecyclerView)rootView.findViewById(R.id.third_page_rv_list);
////        recyclerView.addItemDecoration(new RecyclerViewDivider(getActivity(),LinearLayoutManager.VERTICAL));
////        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(),DividerItemDecoration.HORIZONTAL));
//        recyclerView.addItemDecoration(new RecyclerViewDivider(getActivity(), LinearLayoutManager.VERTICAL));
//        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
//        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
//            @Override
//            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
//                if (newState == RecyclerView.SCROLL_STATE_IDLE &&
//                        (layoutManager.findLastVisibleItemPosition() ==
//                                layoutManager.getItemCount() - 1)
//                        && !isRefreshing) {
//                    isRefreshing = true;
//                    //处理逻辑
//                }
//            }
//        });
//        recyclerView.setLayoutManager(new LinearLayoutManager(recyclerView.getContext(), LinearLayoutManager.VERTICAL, false));
        recyclerView.setLayoutManager(new GridLayoutManager(recyclerView.getContext(), 6, GridLayoutManager.VERTICAL, false));
//        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        List<Cloth> results = new ArrayList<>();

        for(int i=0;i<18;i++)
        {
            results.add(new Cloth());
        }

        if(newDatashow!=null&&!newDatashow.isEmpty()) {
//            for (Cloth item : newDatashow) {
//                results.remove(1);
//            }
            results.addAll(1, newDatashow);
        }
        dataHot = results;
        recyclerView.setAdapter(adapter = new OneRecycleAdapter(getActivity(),dataHot));
    }

    @Override
    public void onRefresh() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                lay_fresh.setRefreshing(false);

//                List<Cloth>newdata = adapter.InitPhotoList();
//                if(!newdata.equals(newDatashow)&&!newDatashow.isEmpty())
//                {
//                    newDatashow = newdata;
//                }
//
//
//                if(!newDatashow.isEmpty()) {
//                    adapter.addALL(1, newDatashow);
//                }
                adapter.postAsynHttp();
                adapter.notifyDataSetChanged();
            }
        }, 1000);
    }*/
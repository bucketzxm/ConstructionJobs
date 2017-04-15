package com.workerassistant.Page.FourthPage;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.workerassistant.R;

import jp.wasabeef.glide.transformations.BlurTransformation;
import jp.wasabeef.glide.transformations.CropCircleTransformation;

/**
 * Created by eva on 2017/4/13.
 */

public class FourthFragment extends Fragment {//implements SwipeRefreshLayout.OnRefreshListener {
    public static FourthFragment newInstance() {
        FourthFragment f = new FourthFragment();
        return f;
    }
    public static int ForSelfInfo = 40;
    private View rootView = null;//缓存Fragment view
//    private OneRecycleAdapter adapter;
//    private SwipeRefreshLayout lay_fresh;
//    private static List<Cloth>dataHot = new ArrayList<>();
//    private static List<Cloth>newDatashow = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fourth_page_main, container, false);
        InitBase();
//        lay_fresh = (SwipeRefreshLayout) rootView.findViewById(R.id.nearpage_refresh);
//        lay_fresh.setColorSchemeResources(R.color.colorPrimary, R.color.colorPrimaryDark);
//        lay_fresh.setOnRefreshListener(this);

        return rootView;
    }
    private ImageView sefi;
    private ImageView sefiBg;
    private TextView btnEditInfo;
    private TextView tvName,tvWorkType,tvIdentity,tvcity,tvphone;
    private void InitBase()
    {

        sefiBg = (ImageView)rootView.findViewById(R.id.fourth_page_avatarBG);
        Glide.with(this)
                .load("http://a3.topitme.com/7/a8/6f/11220277163fa6fa87o.jpg")
                .error(R.drawable.default_ptr_flip)
                .bitmapTransform(new BlurTransformation(getActivity(), 15))
                .crossFade()
                .into(sefiBg);
        sefi = (ImageView)rootView.findViewById(R.id.fourth_page_avatar);
        Glide.with(this)
//                .load("http://a4.topitme.com/o/201011/28/12909463348177.jpg")
                .load("http://a3.topitme.com/f/3f/f0/110247399101ef03ffl.jpg")
                .bitmapTransform(new CropCircleTransformation(getActivity()))
                .into(sefi);
        btnEditInfo = (TextView)rootView.findViewById(R.id.fourth_page_edit_btn);
        btnEditInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(getActivity(), EditSelfInfoActivity.class),ForSelfInfo);
            }
        });

    }


/*
    private void initBase() {
        RecyclerView recyclerView = (RecyclerView) this.rootView.findViewById(R.id.nearpage_rv_list);
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
}
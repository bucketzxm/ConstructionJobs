package com.workerassistant.Page.FirstPage;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.workerassistant.CityPick.CityPickActivity;
import com.workerassistant.R;
import com.workerassistant.Util.Constant;
import com.workerassistant.WorkType.WorkTypeActivity;

import java.util.ArrayList;
import java.util.List;

import jp.wasabeef.glide.transformations.BlurTransformation;
import rx.Subscription;

public class FirstFragment extends Fragment {//implements SwipeRefreshLayout.OnRefreshListener {
    public static FirstFragment newInstance() {
        FirstFragment f = new FirstFragment();
        return f;
    }
    private Subscription mSubscriptionCity,mSubscriptionWorkType;
    private View rootView = null;//缓存Fragment view
    private ListView manList,workList;
    private ImageView imgBg;
    private TextView tvCity,tvWorkType;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.firstpage_main, container, false);
//        lay_fresh = (SwipeRefreshLayout) rootView.findViewById(R.id.nearpage_refresh);
//        lay_fresh.setColorSchemeResources(R.color.colorPrimary, R.color.colorPrimaryDark);
//        lay_fresh.setOnRefreshListener(this);
//        initBase();
        initTopBar();
        initBase();
        return rootView;
    }
    private void initTopBar(){
        imgBg = (ImageView) rootView.findViewById(R.id.first_page_bg);
        Glide.with(this)
                .load("http://img.hb.aicdn.com/8a8801b28b81d03dbc870a7ad9b4aa731206704c8578b-QNk5yn_fw658")
                .error(R.drawable.default_ptr_flip)
                .bitmapTransform(new BlurTransformation(getActivity(), 5))
                .crossFade()
                .into(imgBg);
        tvCity = (TextView)rootView.findViewById(R.id.topbar_page_1_current_city);
        rootView.findViewById(R.id.topbar_page_1_pick_city).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivityForResult(new Intent(v.getContext(), CityPickActivity.class),Constant.requestThirdTopCity);
                    }
                }
        );
        tvWorkType = (TextView)rootView.findViewById(R.id.topbar_page_1_current_work_type);


        rootView.findViewById(R.id.topbar_page_1_pick_work_type).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivityForResult(new Intent(v.getContext(), WorkTypeActivity.class),Constant.requestThirdTopWorkType);
                    }
                }
        );
    }
    private void initBase() {
        List<String> datas = new ArrayList<>();
        for(int i = 0 ;i<18;i++) {
            datas.add("");
        }

        manList = (ListView)rootView.findViewById(R.id.first_page_hottest_man_list);
        workList = (ListView)rootView.findViewById(R.id.first_page_newest_work_list);
        LeftManListAdapter manListAdapter = new LeftManListAdapter(getActivity(),datas);
        manList.setAdapter(manListAdapter);
        workList.setAdapter(manListAdapter);
        manList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getContext(),""+position,Toast.LENGTH_SHORT);
            }
        });
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data == null) {
            return;
        }
        Bundle bundle = data.getExtras();
        if (bundle == null) {
            return;
        }
        switch (requestCode) {
            case Constant.requestThirdTopCity:
                tvCity.setText(bundle.getString("City", "没有选择"));
                break;
            case Constant.requestThirdTopWorkType:
                tvWorkType.setText(bundle.getString("WorkType", "没有选择"));
                break;
        }
    }
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
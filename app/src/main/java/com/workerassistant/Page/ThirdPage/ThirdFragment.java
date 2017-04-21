package com.workerassistant.Page.ThirdPage;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.PaintDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.workerassistant.CityPick.CityBean;
import com.workerassistant.CityPick.CityPickActivity;
import com.workerassistant.CustomUI.RecyclerViewDivider;
import com.workerassistant.R;
import com.workerassistant.Util.ScreenUtils;
import com.workerassistant.Util.rxbus.RxBus;
import com.workerassistant.WorkType.WorkTypeActivity;
import com.workerassistant.bean.PersonBean;
import com.workerassistant.network.netConfigure;

import java.util.ArrayList;
import java.util.List;

import me.leefeng.lfrecyclerview.LFRecyclerView;
import me.leefeng.lfrecyclerview.OnItemClickListener;
import rx.Subscription;
import rx.functions.Action1;

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
        initList();
        initPopUpWindow();
        return rootView;
    }
    private PopupWindow popupWindow;
    private View darkView;
    private Button btnSubmit,btnReset;
    private Animation animIn, animOut;
    private EditText etName,etPhone,etLevel;
    private TextView tvCity,tvWorkType;
    private EditText etAge;

    private void initPopUpWindow(){
        darkView = rootView.findViewById(R.id.third_page_darkview);
        animIn = AnimationUtils.loadAnimation(getActivity(), R.anim.fade_in_anim);
        animOut = AnimationUtils.loadAnimation(getActivity(), R.anim.fade_out_anim);
        rootView.findViewById(R.id.topbar_page_3_popup_new_person_info).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindowClick();
            }
        });
        popupWindow = new PopupWindow(getActivity());
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.third_page_popup, null);
        /*  init 弹出窗口 */
        popupWindow.setContentView(view);
        popupWindow.setBackgroundDrawable(new PaintDrawable(Color.WHITE));
        popupWindow.setFocusable(true);
        popupWindow.setHeight(ScreenUtils.getScreenH(getActivity()) * 3  / 5);
        popupWindow.setWidth(ScreenUtils.getScreenW(getActivity()));
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                darkView.startAnimation(animOut);
                darkView.setVisibility(View.GONE);
            }
        });
        /* init 弹出窗口（选课界面）的控件  */
        etName = (EditText)view.findViewById(R.id.third_popup_name);
        etPhone = (EditText)view.findViewById(R.id.third_popup_phone);
        etAge =(EditText)view.findViewById(R.id.third_popup_age);
        etLevel = (EditText)view.findViewById(R.id.third_popup_level);
        tvCity = (TextView)view.findViewById(R.id.third_popup_city);
        tvCity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(v.getContext(), CityPickActivity.class));
            }
        });
        Subscription mSubscriptionCity = RxBus.getDefault().toObserverable(CityBean.class)
                .subscribe(new Action1<CityBean>() {
                    @Override
                    public void call(CityBean cityBean) {
                        tvCity.setText(cityBean.getCity());
                    }
                });
        tvWorkType = (TextView)view.findViewById(R.id.third_popup_work_type);
        tvWorkType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(v.getContext(), WorkTypeActivity.class));
            }
        });

//        seccampus.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                String[] district = getResources().getStringArray(R.array.distirct);
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {
//
//            }
//        });
        btnSubmit = (Button)view.findViewById(R.id.third_popup_submit);
        btnReset = (Button)view.findViewById(R.id.third_popoup_reset);
        btnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //清空

            }
        });
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //送出结果
                PersonBean personBean  = new PersonBean();
//                personBean.setAge(Integer.parseInt(etAge.getText().toString()));
                personBean.setAge( etAge.getText().toString() );
                personBean.setCity(tvCity.getText().toString());
                personBean.setLevel(etLevel.getText().toString());
                personBean.setName(etName.getText().toString());
                personBean.setPhone(etPhone.getText().toString());
                personBean.setWorkType(tvWorkType.getText().toString());
                netConfigure net = netConfigure.getInstance();
                net.insertPerson(personBean);
                popupWindow.dismiss();
            }
        });
    }
    private void popupWindowClick()
    {
        if (popupWindow.isShowing()) {
            popupWindow.dismiss();
        } else {
            // popupWindow.showAsDropDown(rootView.findViewById(R.id.topbar_page_2_as_drop_down));
            popupWindow.showAtLocation(rootView.findViewById(R.id.topbar_page_3_as_drop_down), Gravity.BOTTOM,0,0);
            popupWindow.setAnimationStyle(-1);
            //背景变暗
            darkView.startAnimation(animIn);
            darkView.setVisibility(View.VISIBLE);
        }
    }
    private void initBase() {
        rootView.findViewById(R.id.topbar_page_3_pick_city).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivity(new Intent(v.getContext(), CityPickActivity.class));
                    }
                }
        );
        rootView.findViewById(R.id.topbar_page_3_pick_work_type).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivity(new Intent(v.getContext(), WorkTypeActivity.class));
                    }
                }
        );
    }
    private void initList() {
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
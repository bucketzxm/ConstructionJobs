package com.workerassistant.Page.SecondPage;

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

import com.workerassistant.CityPick.CityPickActivity;
import com.workerassistant.CustomUI.RecyclerViewDivider;
import com.workerassistant.R;
import com.workerassistant.Util.ScreenUtils;
import com.workerassistant.WorkType.WorkTypeActivity;

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
        initList();
        initPopUpWindow();
        return rootView;
    }
    private PopupWindow popupWindow;
    private View darkView;
    private Button btnSubmit,btnReset;
    private Animation animIn, animOut;
    private EditText etName,etPhone,etNumber;
    private TextView tvCity,tvWorkType;
    private EditText etStartTime,etEndTime;

    private void initPopUpWindow(){
        darkView = rootView.findViewById(R.id.second_page_darkview);
        animIn = AnimationUtils.loadAnimation(getActivity(), R.anim.fade_in_anim);
        animOut = AnimationUtils.loadAnimation(getActivity(), R.anim.fade_out_anim);
        rootView.findViewById(R.id.topbar_page_2_popup_new_work).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindowClick();
            }
        });
        popupWindow = new PopupWindow(getActivity());
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.second_page_popup, null);
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
        btnSubmit = (Button)view.findViewById(R.id.second_popup_submit);
        btnReset = (Button)view.findViewById(R.id.second_popoup_reset);
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
            popupWindow.showAtLocation(rootView.findViewById(R.id.topbar_page_2_as_drop_down), Gravity.BOTTOM,0,0);
            popupWindow.setAnimationStyle(-1);
            //背景变暗
            darkView.startAnimation(animIn);
            darkView.setVisibility(View.VISIBLE);
        }
    }


    private void initBase() {
        rootView.findViewById(R.id.topbar_page_2_pick_city).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivity(new Intent(v.getContext(), CityPickActivity.class));
                    }
                }
        );
        rootView.findViewById(R.id.topbar_page_2_pick_work_type).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivity(new Intent(v.getContext(), WorkTypeActivity.class));
                    }
                }
        );
    }
    private void initList() {
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



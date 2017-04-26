package com.workerassistant.Page.FourthPage;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.workerassistant.R;
import com.workerassistant.Util.Constant;

import jp.wasabeef.glide.transformations.BlurTransformation;
import jp.wasabeef.glide.transformations.CropCircleTransformation;

import static com.workerassistant.Util.Constant.responseEditSelfInfo;

/**
 * Created by eva on 2017/4/13.
 */

public class FourthFragment extends Fragment {//implements SwipeRefreshLayout.OnRefreshListener {
    public static FourthFragment newInstance() {
        FourthFragment f = new FourthFragment();
        return f;
    }

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

    private TextView tvName,tvWorkType,tvLevel,tvCity,tvphone,tvAge;
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
                startActivityForResult(new Intent(getActivity(), EditSelfInfoActivity.class), Constant.requestEditSelfInfo);

            }
        });

        tvName =  (TextView) rootView.findViewById(R.id.fourth_page_my_name);
        tvCity =  (TextView) rootView.findViewById(R.id.fourth_page_my_city);
        tvLevel  =  (TextView) rootView.findViewById(R.id.fourth_page_my_level);
        tvAge  =  (TextView) rootView.findViewById(R.id.fourth_page_my_age);
        tvWorkType =  (TextView) rootView.findViewById(R.id.fourth_page_my_work_type);
        tvphone =  (TextView) rootView.findViewById(R.id.fourth_page_my_phone);
        //        从SharedPreferences获取数据:
        SharedPreferences preferences=getActivity().getSharedPreferences("UserInfo", Context.MODE_PRIVATE);
        String name=preferences.getString("name", "");
        String age=preferences.getString("age", "");
        String phone=preferences.getString("phone", "");
        String level=preferences.getString("level", "");
        String city=preferences.getString("city", "");
        String workType=preferences.getString("workType", "");
        tvName.setText(name);
        tvCity.setText(age);
        tvLevel.setText(phone);
        tvAge.setText(level);
        tvWorkType.setText(city);
        tvphone.setText(workType);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(data == null){
            return;
        }
        Bundle bundle = data.getExtras();
        if(bundle == null){
            return;
        }
        if(requestCode!=Constant.requestEditSelfInfo)
            return;
        switch (resultCode){
            case responseEditSelfInfo:
                tvName.setText(bundle.getString("name",""));
                tvphone.setText(bundle.getString("phone",""));
                tvAge.setText(bundle.getString("age",""));
                tvLevel.setText(bundle.getString("level",""));
                tvCity.setText(bundle.getString("city",""));
                tvWorkType.setText(bundle.getString("workType",""));

                SharedPreferences preferences=getActivity().getSharedPreferences("UserInfo", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor=preferences.edit();
                editor.putString("name", bundle.getString("name",""));
                editor.putString("phone", bundle.getString("phone",""));
                editor.putString("age", bundle.getString("age",""));
                editor.putString("level", bundle.getString("level",""));
                editor.putString("city", bundle.getString("city",""));
                editor.putString("workType", bundle.getString("workType",""));
                editor.apply();

                break;
        }
    }
}
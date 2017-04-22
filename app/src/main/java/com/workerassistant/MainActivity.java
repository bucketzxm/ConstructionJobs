package com.workerassistant;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.workerassistant.Page.FirstPage.FirstFragment;
import com.workerassistant.Page.FourthPage.FourthFragment;
import com.workerassistant.Page.SecondPage.SecondFragment;
import com.workerassistant.Page.TextTabFragment;
import com.workerassistant.Page.ThirdPage.ThirdFragment;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
//    private TextView toolbarTitle;
    private TextTabFragment mTextTabFragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initBaseWidgets();
        initBottomTab();
    }
    private void initBaseWidgets(){

//        toolbarTitle = (TextView)findViewById(R.id.toolbar_title_txt);
//        findViewById(R.id.top_bar_pick_city).setOnClickListener(
//                new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        startActivity(new Intent(v.getContext(), CityPickActivity.class));
//                    }
//                }
//        );
//        findViewById(R.id.top_bar_pick_work_type).setOnClickListener(
//                new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        startActivity(new Intent(v.getContext(), WorkTypeActivity.class));
//                    }
//                }
//        );

    }
//    protected void setTitle(String msg) {
//        if (toolbarTitle != null) {
//            toolbarTitle.setText(msg);
//        }
//    }


    private TextView mTHome, mSecond, mThird, mTMe;
    public FirstFragment firstFragment;
    public SecondFragment secondFragment;
    public ThirdFragment thirdFragment;
    public FourthFragment fourthFragment;
     public void initBottomTab(){

        mTHome = (TextView) findViewById(R.id.tv_home);
        mSecond = (TextView)findViewById(R.id.tv_second);
        mThird = (TextView) findViewById(R.id.tv_third);
        mTMe = (TextView)findViewById(R.id.tv_person);
        mTHome.setOnClickListener(this);
        mSecond.setOnClickListener(this);
        mThird.setOnClickListener(this);
        mTMe.setOnClickListener(this);

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        firstFragment = FirstFragment.newInstance();
        secondFragment = SecondFragment.newInstance();
        thirdFragment = ThirdFragment.newInstance();
        fourthFragment = FourthFragment.newInstance();
        transaction.add(R.id.sub_content,firstFragment,"FirstFragment");
        transaction.add(R.id.sub_content,secondFragment,"SecondFragment");
        transaction.add(R.id.sub_content,thirdFragment,"ThirdFragment");
        transaction.add(R.id.sub_content,fourthFragment,"FourthFragment");
        transaction.commit();


        setDefaultFragment();
    }

    /**
     * set the default Fragment
     */
    private void setDefaultFragment() {
        switchFrgment(0);
        //set the defalut tab state
        setTabState(mTHome, R.drawable.ic_home_black_24dp, getMyColor(R.color.color1));
    }

    @Override
    public void onClick(View view) {
        resetTabState();//reset the tab state
        switch (view.getId()) {
            case R.id.tv_home:
                setTabState(mTHome, R.drawable.ic_home_black_24dp, getMyColor(R.color.color1));
                switchFrgment(0);
                break;
            case R.id.tv_second:
                setTabState(mSecond, R.drawable.ic_face_black_24dp, getMyColor(R.color.color1));
                switchFrgment(1);
                break;
            case R.id.tv_third:
                setTabState(mThird, R.drawable.ic_line_style_black_24dp, getMyColor(R.color.color1));
                switchFrgment(2);
                break;
            case R.id.tv_person:
                setTabState(mTMe, R.drawable.ic_account_box_black_24dp, getMyColor(R.color.color1));
                switchFrgment(3);
                break;

        }
    }

    /**
     * switch the fragment accordting to id
     * @param i id
     */
    public void switchFrgment(int i) {
        if(i>4)return;
        // 只有4个页面
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        if(firstFragment!=null){
            transaction.hide(firstFragment);
        }
        if(secondFragment!=null){
            transaction.hide(secondFragment);
        }
        if(thirdFragment !=null){
            transaction.hide(thirdFragment);
        }
        if(fourthFragment!=null){
            transaction.hide(fourthFragment);
        }

        switch (i) {
            case 0:
                transaction.show(firstFragment);
                break;
            case 1:
                transaction.show(secondFragment);
                break;
            case 2:
                transaction.show(thirdFragment);
                break;
            case 3:
                transaction.show(fourthFragment);
                break;
        }
        transaction.commit();
    }

    /**
     * set the tab state of bottom navigation bar
     *
     * @param textView the text to be shown
     * @param image    the image
     * @param color    the text color
     */
    private void setTabState(TextView textView, int image, int color) {
        textView.setCompoundDrawablesRelativeWithIntrinsicBounds(0, image, 0, 0);//Call requires API level 17
        textView.setTextColor(color);
    }


    /**
     * revert the image color and text color to black
     */
    private void resetTabState() {
        setTabState(mTHome, R.drawable.ic_home_grey_500_24dp, getMyColor(R.color.md_grey_500));
        setTabState(mSecond, R.drawable.ic_face_grey_500_24dp, getMyColor(R.color.md_grey_500));
        setTabState(mThird, R.drawable.ic_line_style_grey_500_24dp, getMyColor(R.color.md_grey_500));
        setTabState(mTMe, R.drawable.ic_account_box_grey_500_24dp, getMyColor(R.color.md_grey_500));

    }

    /**
     * @param i the color id
     * @return color
     */
    private int getMyColor(int i) {
        return ContextCompat.getColor(this, i);
    }



}

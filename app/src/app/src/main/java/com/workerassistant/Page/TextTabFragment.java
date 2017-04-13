package com.workerassistant.Page;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.workerassistant.Page.FirstPage.FirstFragment;
import com.workerassistant.Page.FourthPage.FourthFragment;
import com.workerassistant.Page.SecondPage.SecondFragment;
import com.workerassistant.Page.ThirdPage.ThirdFragment;
import com.workerassistant.R;

public class TextTabFragment extends Fragment implements View.OnClickListener {
    private TextView mTHome, mSecond, mThird, mTMe;
    public FirstFragment firstFragment;
    public SecondFragment secondFragment;
    public ThirdFragment thirdFragment;
    public FourthFragment fourthFragment;

    public static TextTabFragment newInstance() {
        TextTabFragment viewPagerFragment = new TextTabFragment();
        return viewPagerFragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_text_tab, container, false);

        mTHome = (TextView) view.findViewById(R.id.tv_home);
        mSecond = (TextView) view.findViewById(R.id.tv_second);
        mThird = (TextView) view.findViewById(R.id.tv_third);
        mTMe = (TextView) view.findViewById(R.id.tv_person);
        Bundle bundle = getArguments();
        if (bundle != null) {
            String s = bundle.getString("TAG");
            if (!TextUtils.isEmpty(s)) {

            }
        }
        mTHome.setOnClickListener(this);
        mSecond.setOnClickListener(this);
        mThird.setOnClickListener(this);
        mTMe.setOnClickListener(this);
        setDefaultFragment();
        return view;
    }

    /**
     * set the default Fragment
     */
    private void setDefaultFragment() {
        switchFrgment(0);
        //set the defalut tab state
        setTabState(mTHome, R.drawable.ic_home_black_24dp, getColor(R.color.color1));
    }

    @Override
    public void onClick(View view) {
        resetTabState();//reset the tab state
        switch (view.getId()) {
            case R.id.tv_home:
                setTabState(mTHome, R.drawable.ic_home_black_24dp, getColor(R.color.color1));
                switchFrgment(0);
                break;
            case R.id.tv_second:
                setTabState(mSecond, R.drawable.ic_face_black_24dp, getColor(R.color.color1));
                switchFrgment(1);
                break;
            case R.id.tv_third:
                setTabState(mThird, R.drawable.ic_line_style_black_24dp, getColor(R.color.color1));
                switchFrgment(2);
                break;
            case R.id.tv_person:
                setTabState(mTMe, R.drawable.ic_account_box_black_24dp, getColor(R.color.color1));
                switchFrgment(3);
                break;

        }
    }

    /**
     * switch the fragment accordting to id
     * @param i id
     */
    public void switchFrgment(int i) {
        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
        switch (i) {
            case 0:
                if (firstFragment == null) {
                    firstFragment = FirstFragment.newInstance();
                }
                transaction.replace(R.id.sub_content, firstFragment);
                break;
            case 1:
                if (secondFragment == null) {
                    secondFragment = SecondFragment.newInstance();
                }
                transaction.replace(R.id.sub_content, secondFragment);
                break;
            case 2:
                if (thirdFragment == null) {
                    thirdFragment = ThirdFragment.newInstance();
                }
                transaction.replace(R.id.sub_content, thirdFragment);
                break;
            case 3:
                if (fourthFragment == null) {
                    fourthFragment = FourthFragment.newInstance();
                }
                transaction.replace(R.id.sub_content, fourthFragment);
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
        setTabState(mTHome, R.drawable.ic_home_grey_500_24dp, getColor(R.color.md_grey_500));
        setTabState(mSecond, R.drawable.ic_face_grey_500_24dp, getColor(R.color.md_grey_500));
        setTabState(mThird, R.drawable.ic_line_style_grey_500_24dp, getColor(R.color.md_grey_500));
        setTabState(mTMe, R.drawable.ic_account_box_grey_500_24dp, getColor(R.color.md_grey_500));

    }

    /**
     * @param i the color id
     * @return color
     */
    private int getColor(int i) {
        return ContextCompat.getColor(getActivity(), i);
    }
}
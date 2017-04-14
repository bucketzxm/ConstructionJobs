package com.workerassistant;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.workerassistant.CityPick.CityPickActivity;
import com.workerassistant.Page.TextTabFragment;
import com.workerassistant.WorkType.WorkTypeActivity;

public class MainActivity extends AppCompatActivity {
//    private TextView toolbarTitle;
    private TextTabFragment mTextTabFragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initBaseWidgets();
        setCurrentFragment();

    }
    private void initBaseWidgets(){
//        toolbarTitle = (TextView)findViewById(R.id.toolbar_title_txt);
        findViewById(R.id.top_bar_pick_city).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivity(new Intent(v.getContext(), CityPickActivity.class));
                    }
                }
        );
        findViewById(R.id.top_bar_pick_work_type).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivity(new Intent(v.getContext(), WorkTypeActivity.class));
                    }
                }
        );

    }
//    protected void setTitle(String msg) {
//        if (toolbarTitle != null) {
//            toolbarTitle.setText(msg);
//        }
//    }

    private void setCurrentFragment() {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        if (mTextTabFragment == null) {
            mTextTabFragment = TextTabFragment.newInstance();
        }
        transaction.replace(R.id.frame_content, mTextTabFragment);
        transaction.commit();
    }
}

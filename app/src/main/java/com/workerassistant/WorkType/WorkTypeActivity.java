package com.workerassistant.WorkType;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.workerassistant.R;
import com.workerassistant.WorkType.bean.WorkTypeBean;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class WorkTypeActivity extends AppCompatActivity {
    private List<WorkTypeBean> mDatas;
    private SearchWorkTypeFragment mSearchFragment;
    private LinearLayout mainLayout;
    private SearchView mSearchView;
    private FrameLayout mProgressBar;
    private RecyclerView recyclerView;

    private WorkTypeAdapter workTypeAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_work_type);

        mainLayout = (LinearLayout)findViewById(R.id.work_type_main_layout);
        mSearchView = (SearchView) findViewById(R.id.work_type_searchview);
        mProgressBar = (FrameLayout) findViewById(R.id.work_type_progress);

        mDatas = initDatas();
        recyclerView = (RecyclerView) findViewById(R.id.work_type_rv_list);
        workTypeAdapter = new WorkTypeAdapter(this,mDatas);
        //        recyclerView.setLayoutManager(new LinearLayoutManager(recyclerView.getContext(), LinearLayoutManager.VERTICAL, false));
        recyclerView.setLayoutManager(new GridLayoutManager(recyclerView.getContext(), 4, GridLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(workTypeAdapter);


        mSearchFragment = (SearchWorkTypeFragment) getSupportFragmentManager().findFragmentById(R.id.work_type_search_fragment);
        mainLayout.postDelayed(new Runnable() {
            @Override
            public void run() {
                mSearchFragment.bindDatas(mDatas);
                mProgressBar.setVisibility(View.GONE);

            }
        },1000);
        initSearch();
    }



    private void initSearch() {
        getSupportFragmentManager().beginTransaction().hide(mSearchFragment).commit();

        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (newText.trim().length() > 0) {
                    if (mSearchFragment.isHidden()) {
                        getSupportFragmentManager().beginTransaction().show(mSearchFragment).commit();
                    }
                } else {
                    if (!mSearchFragment.isHidden()) {
                        getSupportFragmentManager().beginTransaction().hide(mSearchFragment).commit();
                    }
                }

                mSearchFragment.bindQueryText(newText);
                return false;
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (!mSearchFragment.isHidden()) {
            // 隐藏 搜索
            mSearchView.setQuery(null, false);
            getSupportFragmentManager().beginTransaction().hide(mSearchFragment).commit();
            return;
        }
        super.onBackPressed();
    }
    // 数据处理完成后回调


//    public void update(View view) {
//        List<CityBean> list = new ArrayList<>();
//        list.add(new CityBean("杭州市"));
//        list.add(new CityBean("北京市"));
//        list.add(new CityBean("上海市"));
//        list.add(new CityBean("广州市"));
//        mHotCityAdapter.addDatas(list);
//        Toast.makeText(this, "更新数据", Toast.LENGTH_SHORT).show();
//    }


    private List<WorkTypeBean> initDatas() {
        List<WorkTypeBean> list = new ArrayList<>();
        List<String> workTypeStrings = Arrays.asList(getResources().getStringArray(R.array.provinces));
        for (String item : workTypeStrings) {
            WorkTypeBean WorkTypeEntity = new WorkTypeBean();
            WorkTypeEntity.setWorkTypeName(item);
            list.add(WorkTypeEntity);
        }
        return list;
    }

}




package com.workerassistant.WorkType;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.workerassistant.CustomUI.TagFlow.TagFlowFilterAdapter;
import com.workerassistant.R;
import com.workerassistant.WorkType.bean.WorkTypeBean;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WorkTypeActivity extends AppCompatActivity {
    private List<WorkTypeBean> mDatas;
    private SearchWorkTypeFragment mSearchFragment;
    private LinearLayout mainLayout;
    private SearchView mSearchView;
    private FrameLayout mProgressBar;
//    private RecyclerView recyclerView;

    //    private WorkTypeAdapter workTypeAdapter;
    private ListView mListView;
    private Button btnReset,btnSubmit;
    private TagFlowFilterAdapter tagFlowFilterAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_work_type);

        mainLayout = (LinearLayout)findViewById(R.id.work_type_main_layout);
        mSearchView = (SearchView) findViewById(R.id.work_type_searchview);
        mProgressBar = (FrameLayout) findViewById(R.id.work_type_progress);
        mListView = (ListView)findViewById(R.id.work_type_list);

        mDatas = initDatas();
        List<String> dataTitle = new ArrayList<>();
        dataTitle.add("热门工种");
        dataTitle.add("所有工种");
        Map<String,List<String>> dataTag = new HashMap<>();
        dataTag.putAll(CustomDatas());
        tagFlowFilterAdapter = new TagFlowFilterAdapter(this,dataTitle,dataTag);
        mListView.setAdapter(tagFlowFilterAdapter);


        btnReset = (Button)findViewById(R.id.tab_sort_filter_reset);
        btnSubmit = (Button)findViewById(R.id.tab_sort_filter_submit);
        btnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tagFlowFilterAdapter.resetSelectedTagInt();
            }
        });
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String res = tagFlowFilterAdapter.handleResult();

                 /****************************/
                /******** 处理筛选结果  ******* /
                /*****************************/

                Log.d("res:",res);
                String[]classfied = res.split(" ");
//                adapter.classfiedByTagCollection(classfied);
            }
        });

        mSearchFragment = (SearchWorkTypeFragment) getSupportFragmentManager().findFragmentById(R.id.work_type_search_fragment);
        mainLayout.postDelayed(new Runnable() {
            @Override
            public void run() {
                mSearchFragment.bindDatas(mDatas);
                mProgressBar.setVisibility(View.GONE);

            }
        },500);
        initSearch();
        initTopbar("选择工种");

    }
    private TextView title;
    private ImageView back;
    public void initTopbar(String titleString){
        title=(TextView)findViewById(R.id.title_toolbar);
        title.setText(titleString);
        back= (ImageView)findViewById(R.id.back_toolbar);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
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

    /**
     *  自定义数据
     *  分类标题List,工种数据List
     *  @return 自定义的工种数据
     */

    private Map<String,List<String>> CustomDatas(){
        Map<String,List<String>> dataTag = new HashMap<>();
        List<String> tag1 = new ArrayList<>();

        for(int i=0 ;i<4;i++){
            tag1.add(mDatas.get(i).getWorkTypeName());
        }
        dataTag.put("热门工种",tag1);
        List<String> tag2 = new ArrayList<>();
        for(int i=4 ;i<mDatas.size();i++){
            tag2.add(mDatas.get(i).getWorkTypeName());
        }
        dataTag.put("所有工种",tag2);
        return dataTag;
    }


    /**
     * 读取String.xml中的数据
     * @return
     */
    private List<WorkTypeBean> initDatas() {
        List<WorkTypeBean> list = new ArrayList<>();
        List<String> workTypeStrings = Arrays.asList(getResources().getStringArray(R.array.worktype));
        for (String item : workTypeStrings) {
            WorkTypeBean WorkTypeEntity = new WorkTypeBean();
            WorkTypeEntity.setWorkTypeName(item);
            list.add(WorkTypeEntity);
        }
        return list;
    }

}




package com.workerassistant.CityPick;


import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.workerassistant.CustomUI.IndexBar.widget.IndexBar;
import com.workerassistant.CustomUI.suspension.SuspensionDecoration;
import com.workerassistant.R;

import java.util.ArrayList;
import java.util.List;

public class CityPickActivity extends AppCompatActivity {
    private static final String TAG = "zxt";
    private RecyclerView mRv;
    private CityAdapter mAdapter;
    private HeaderRecyclerAndFooterWrapperAdapter mHeaderAdapter;
    private LinearLayoutManager mManager;
    private List<CityBean> mDatas;

    private SuspensionDecoration mDecoration;

    /**
     * 右侧边栏导航区域
     */
    private IndexBar mIndexBar;

    /**
     * 显示指示器DialogText
     */
    private TextView mTvSideBarHint;
    private TextView title;
    private ImageView back;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_city_pick);
        initTopbar("选择城市");
        mRv = (RecyclerView) findViewById(R.id.rv);
        mRv.setLayoutManager(mManager = new LinearLayoutManager(this));

        mAdapter = new CityAdapter(this, mDatas);
        mHeaderAdapter = new HeaderRecyclerAndFooterWrapperAdapter(mAdapter) {
            @Override
            protected void onBindHeaderHolder(ViewHolder holder, int headerPos, int layoutId, Object o) {
                if( layoutId == R.layout.search_view){
                    holder.itemView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                           // startActivity(new Intent(v.getContext(), CityPickActivity.class));
                        }
                    });
                    return;
                }
                holder.setText(R.id.tvCity, (String) o);
            }
        };
//        mHeaderAdapter.setHeaderView(0,R.layout.item_city, "头部");
        mHeaderAdapter.setHeaderView(0,R.layout.search_view, "搜索框");
        mRv.setAdapter(mHeaderAdapter);
        mRv.addItemDecoration(mDecoration = new SuspensionDecoration(this, mDatas).setHeaderViewCount(mHeaderAdapter.getHeaderViewCount()));

        //如果add两个，那么按照先后顺序，依次渲染。
        mRv.addItemDecoration(new DividerItemDecoration(CityPickActivity.this, DividerItemDecoration.VERTICAL));

        //使用indexBar
        mTvSideBarHint = (TextView) findViewById(R.id.tvSideBarHint);//HintTextView
        mIndexBar = (IndexBar) findViewById(R.id.indexBar);//IndexBar

        mIndexBar.setmPressedShowTextView(mTvSideBarHint)//设置HintTextView
                .setNeedRealIndex(true)//设置需要真实的索引
                .setmLayoutManager(mManager);//设置RecyclerView的LayoutManager

        initDatas(getResources().getStringArray(R.array.provinces));
    }

    /**
     * 组织数据源
     *
     * @param data
     * @return
     */
    private void initDatas(final String[] data) {
        //延迟200ms 模拟加载数据中....
        getWindow().getDecorView().postDelayed(new Runnable() {
            @Override
            public void run() {
                mDatas = new ArrayList<>();
                for (int i = 0; i < data.length; i++) {
                    CityBean cityBean = new CityBean();
                    cityBean.setCity(data[i]);//设置城市名称
                    mDatas.add(cityBean);
                }

                mIndexBar.setmSourceDatas(mDatas)//设置数据
                        .setHeaderViewCount(mHeaderAdapter.getHeaderViewCount())//设置HeaderView数量
                        .invalidate();

                mAdapter.setDatas(mDatas);
                mHeaderAdapter.notifyDataSetChanged();
                mDecoration.setmDatas(mDatas);
            }
        }, 200);

    }

    /**
     * 更新数据源
     *
     * @param view
     */
    public void updateDatas(View view) {
        for (int i = 0; i < 5; i++) {
            mDatas.add(new CityBean("东京"));
            mDatas.add(new CityBean("大阪"));
        }
        mIndexBar.setmSourceDatas(mDatas)
                .invalidate();
        mHeaderAdapter.notifyDataSetChanged();
    }
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
}

package com.workerassistant.CityPick;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.TypedValue;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.workerassistant.CustomUI.IndexBar.bean.BaseIndexPinyinBean;
import com.workerassistant.CustomUI.IndexBar.widget.IndexBar;
import com.workerassistant.CustomUI.adapter.CommonAdapter;
import com.workerassistant.CustomUI.suspension.SuspensionDecoration;
import com.workerassistant.MainActivity;
import com.workerassistant.R;
import com.workerassistant.Util.Constant;
import com.workerassistant.Util.rxbus.RxBus;

import java.util.ArrayList;
import java.util.List;

/*  manifest添加此句使得软键盘不上移
*    <activity android:name=".CityPick.CityPickActivity"
            android:windowSoftInputMode="adjustPan|stateHidden"/>
* */

public class CityPickActivity extends AppCompatActivity {
    private static final String TAG = "zxt";
    private RecyclerView mRv;
    private CityAdapter mAdapter;
    private HeaderRecyclerAndFooterWrapperAdapter mHeaderAdapter;
    private LinearLayoutManager mManager;

    //设置给InexBar、ItemDecoration的完整数据集
    private List<BaseIndexPinyinBean> mSourceDatas;
    //头部数据源
    private List<CityListHeaderBean> mHeaderDatas;
    //主体部分数据源（城市数据）
    private List<CityBean> mBodyDatas;

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
    private SearchView mSearchView;
    private FrameLayout mProgressBar;
    private TextView tvSelectAll;
    private SearchCityFragment mSearchFragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_city_pick);
        initTopbar("选择城市");
        initList();
        initSearch();

    }
    private void initList(){
        mRv = (RecyclerView) findViewById(R.id.rv);
        mRv.setLayoutManager(mManager = new LinearLayoutManager(this));
        mSourceDatas = new ArrayList<>();
        mHeaderDatas = new ArrayList<>();
        List<String> hotCitys = new ArrayList<>();
        mHeaderDatas.add(new CityListHeaderBean(hotCitys, "热门城市", "热"));
        mSourceDatas.addAll(mHeaderDatas);

//        mAdapter = new Adapter(this, R.layout.meituan_item_select_city, mBodyDatas);
        mAdapter = new CityAdapter(this, mBodyDatas);
        mHeaderAdapter = new HeaderRecyclerAndFooterWrapperAdapter(mAdapter) {
            @Override
            protected void onBindHeaderHolder(ViewHolder holder, int headerPos, int layoutId, Object o) {
//            头部最热待添加
                switch (layoutId) {
                    case R.layout.meituan_item_header:
                        final CityListHeaderBean meituanHeaderBean = (CityListHeaderBean) o;
                        //网格
                        RecyclerView recyclerView = holder.getView(R.id.rvCity);
                        recyclerView.setAdapter(
                                new CommonAdapter<String>(CityPickActivity.this, R.layout.meituan_item_header_item, meituanHeaderBean.getCityList()) {
                                    @Override
                                    public void convert(ViewHolder holder, final String cityName) {
                                        holder.setText(R.id.tvName, cityName);
                                        holder.getConvertView().setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
//                                                Toast.makeText(CityPickActivity.this, "HotcityName:" + cityName, Toast.LENGTH_SHORT).show();
                                        CityBean city = new CityBean();
                                        city.setCity(cityName);
                                        RxBus.getDefault().post(city);
//                另外一种方法返回值
                                        Intent intent = new Intent();
                                        Bundle bundle = new Bundle();
                                        bundle.putString("City",city.getCity());
                                        intent.putExtras(bundle);
                                        intent.setClass(CityPickActivity.this, MainActivity.class);
                                        setResult(Constant.requestThirdTopCity,intent);
                                        finish();
                                            }
                                        });
                                    }
                                });
                        recyclerView.setLayoutManager(new GridLayoutManager(CityPickActivity.this, 3));
                        break;
//                    case R.layout.meituan_item_header_top:
//                        MeituanTopHeaderBean meituanTopHeaderBean = (MeituanTopHeaderBean) o;
//                        holder.setText(R.id.tvCurrent, meituanTopHeaderBean.getTxt());
//                        break;
                    default:
                        break;
                }
            }
        };
        mHeaderAdapter.setHeaderView(0,R.layout.search_view, "搜索框");
//        mHeaderAdapter.setHeaderView(1, R.layout.meituan_item_header_top, new MeituanTopHeaderBean("当前位置：上海徐汇"));
        mHeaderAdapter.setHeaderView(1, R.layout.meituan_item_header, mHeaderDatas.get(0));

//        mRv.addItemDecoration(mDecoration = new SuspensionDecoration(this, mDatas).setHeaderViewCount(mHeaderAdapter.getHeaderViewCount()));
        mRv.setAdapter(mHeaderAdapter);
        mRv.addItemDecoration(mDecoration = new SuspensionDecoration(this, mSourceDatas)
                .setmTitleHeight((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 35, getResources().getDisplayMetrics()))
                .setColorTitleBg(0xffefefef)
                .setTitleFontSize((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 16, getResources().getDisplayMetrics()))
                .setColorTitleFont(CityPickActivity.this.getResources().getColor(android.R.color.black))
                .setHeaderViewCount(mHeaderAdapter.getHeaderViewCount() - mHeaderDatas.size()));
        mRv.addItemDecoration(new DividerItemDecoration(CityPickActivity.this, LinearLayoutManager.VERTICAL));

        //使用indexBar
        mTvSideBarHint = (TextView) findViewById(R.id.tvSideBarHint);//HintTextView
        mIndexBar = (IndexBar) findViewById(R.id.indexBar);//IndexBar

        mIndexBar.setmPressedShowTextView(mTvSideBarHint)//设置HintTextView
                .setNeedRealIndex(true)//设置需要真实的索引
                .setmLayoutManager(mManager)//设置RecyclerView的LayoutManager
                .setHeaderViewCount(mHeaderAdapter.getHeaderViewCount() - mHeaderDatas.size());

        initDatas(getResources().getStringArray(R.array.provinces));
    }
    private void initSearch() {
        mProgressBar = (FrameLayout) findViewById(R.id.activity_city_progress);
        mSearchView = (SearchView)findViewById(R.id.activity_city_searchview);
        mSearchFragment = (SearchCityFragment) getSupportFragmentManager().findFragmentById(R.id.activity_city_search_fragment);
        title.postDelayed(new Runnable() {
            @Override
            public void run() {
                mSearchFragment.bindDatas(mBodyDatas);
                mProgressBar.setVisibility(View.GONE);
            }
        },200);
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
                mBodyDatas = new ArrayList<>();
                for (int i = 0; i < data.length; i++) {
                    CityBean cityBean = new CityBean();
                    cityBean.setCity(data[i]);//设置城市名称
                    mBodyDatas.add(cityBean);
                }
                //先排序
                mIndexBar.getDataHelper().sortSourceDatas(mBodyDatas);

                mAdapter.setDatas(mBodyDatas);
                mHeaderAdapter.notifyDataSetChanged();
                mSourceDatas.addAll(mBodyDatas);

                mIndexBar.setmSourceDatas(mSourceDatas)//设置数据
                        .invalidate();
                mDecoration.setmDatas(mSourceDatas);
            }
        }, 200);

        //延迟两秒加载头部
        getWindow().getDecorView().postDelayed(new Runnable() {
            @Override
            public void run() {
                CityListHeaderBean header = mHeaderDatas.get(0);
                List<String> hotCitys = new ArrayList<>();
                hotCitys.add("上海");
                hotCitys.add("北京");
                hotCitys.add("广州");
                hotCitys.add("深圳");

                header.setCityList(hotCitys);
                mHeaderAdapter.notifyItemRangeChanged(0, 1);

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
            mBodyDatas.add(new CityBean("新城市"));
            mBodyDatas.add(new CityBean("新城市1"));
        }
        //先排序
        mIndexBar.getDataHelper().sortSourceDatas(mBodyDatas);
        mSourceDatas.clear();
        mSourceDatas.addAll(mHeaderDatas);
        mSourceDatas.addAll(mBodyDatas);

        mHeaderAdapter.notifyDataSetChanged();
        mIndexBar.invalidate();
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
        tvSelectAll = (TextView) findViewById(R.id.select_all_toolbar);
        tvSelectAll.setVisibility(View.VISIBLE);
        tvSelectAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(CityPickActivity.this, "选择所有城市", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent();
                Bundle bundle = new Bundle();
                bundle.putString("City","全部");
                intent.putExtras(bundle);
                intent.setClass(CityPickActivity.this, MainActivity.class);
                setResult(Constant.requestThirdTopCity,intent);
                finish();
            }
        });
    }
}

package com.workerassistant.Page.FirstPage;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.workerassistant.CityPick.CityPickActivity;
import com.workerassistant.MainActivity;
import com.workerassistant.R;
import com.workerassistant.Util.Constant;
import com.workerassistant.WorkType.WorkTypeActivity;
import com.workerassistant.bean.PersonBean;
import com.workerassistant.bean.ProjectBean;
import com.workerassistant.network.netConfigure;
import com.workerassistant.network.netService;

import java.util.ArrayList;
import java.util.List;

import jp.wasabeef.glide.transformations.BlurTransformation;
import retrofit2.Call;
import retrofit2.Response;
import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static android.content.ContentValues.TAG;

public class FirstFragment extends Fragment {//implements SwipeRefreshLayout.OnRefreshListener {
    public static FirstFragment newInstance() {
        FirstFragment f = new FirstFragment();
        return f;
    }
//    private Subscription mSubscriptionCity,mSubscriptionWorkType;
    private netConfigure net = netConfigure.getInstance();
    private View rootView = null;//缓存Fragment view
    private ListView topPersonList, topProjectList;
    private ImageView imgBg;
    private TextView tvCity,tvWorkType;
//    private SwipeRefreshLayout lay_fresh;
    private LeftPersonAdapter topPersonAdapter;
    private static int PAGE_SIZE = 5;
    private List<PersonBean> topPersonData = new ArrayList<>();
    private List<ProjectBean> topProjectData = new ArrayList<>();
    private RightProjectAdapter topProjectAdapter;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.firstpage_main, container, false);
//        lay_fresh = (SwipeRefreshLayout) rootView.findViewById(R.id.first_page_refresh);
//        lay_fresh.setColorSchemeResources(R.color.color1, R.color.color1);
//        lay_fresh.setOnRefreshListener(this);
        initList();
        initTopBar();
        initBase();
        Refresh();
        return rootView;
    }
    private void initTopBar(){
        imgBg = (ImageView) rootView.findViewById(R.id.first_page_bg);
        Glide.with(this)
                .load("http://img.hb.aicdn.com/8a8801b28b81d03dbc870a7ad9b4aa731206704c8578b-QNk5yn_fw658")
                .error(R.drawable.default_ptr_flip)
                .bitmapTransform(new BlurTransformation(getActivity(), 5))
                .crossFade()
                .into(imgBg);
        tvCity = (TextView)rootView.findViewById(R.id.topbar_page_1_current_city);
        rootView.findViewById(R.id.topbar_page_1_pick_city).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivityForResult(new Intent(v.getContext(), CityPickActivity.class),Constant.requestThirdTopCity);
                    }
                }
        );
        tvWorkType = (TextView)rootView.findViewById(R.id.topbar_page_1_current_work_type);


        rootView.findViewById(R.id.topbar_page_1_pick_work_type).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivityForResult(new Intent(v.getContext(), WorkTypeActivity.class),Constant.requestThirdTopWorkType);
                    }
                }
        );
    }
    private void initList(){

        topPersonList = (ListView)rootView.findViewById(R.id.first_page_hottest_man_list);
        topProjectList = (ListView)rootView.findViewById(R.id.first_page_newest_work_list);
        topPersonAdapter = new LeftPersonAdapter(getActivity(), topPersonData);
        topProjectAdapter = new RightProjectAdapter(getContext(),topProjectData);
        topPersonList.setAdapter(topPersonAdapter);
        topProjectList.setAdapter(topProjectAdapter);
        topPersonList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getContext(),""+position,Toast.LENGTH_SHORT);
            }
        });
    }
    private void initBase() {
        rootView.findViewById(R.id.first_page_find_person).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        MainActivity mainActivity = (MainActivity)getActivity();
                        mainActivity.jumpToFragment(2);
                    }
                }
        );
        rootView.findViewById(R.id.first_page_find_work).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        MainActivity mainActivity = (MainActivity)getActivity();
                        mainActivity.jumpToFragment(3);
                    }
                }
        );
    }
//    此处tvCity是顶部Top的TextView，相当于其他page的tvTopCity
    private String getSelectCity(){
        String res = null;
        if(!tvCity.getText().toString().equals("全部")){
            res = tvCity.getText().toString();
        }
        return res;
    }
    private String getSelectWorkType(){
        String res = null;
        if(!tvWorkType.getText().toString().equals("全部")){
            res = tvWorkType.getText().toString();
        }
        return res;
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data == null) {
            return;
        }
        Bundle bundle = data.getExtras();
        if (bundle == null) {
            return;
        }
        switch (requestCode) {
            case Constant.requestThirdTopCity:
                tvCity.setText(bundle.getString("City", "没有选择"));
                break;
            case Constant.requestThirdTopWorkType:
                tvWorkType.setText(bundle.getString("WorkType", "没有选择"));
                break;
        }
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if(hidden) {
            Log.d(TAG, "hidden " + hidden);

        }else{
//           hidden false   表示界面可见
            Log.d(TAG,"hidden "+ hidden);
            Refresh();
        }
    }
    private Observable<List<ProjectBean>> projectTopRefresh(){
        netService.ApiService apiService = net.getPersonService();
        final Call<List<ProjectBean>> callIndexProject =
//                apiService.getIndexProject(0,0+PAGE_SIZE,null,null);
                apiService.getProject();
        Observable<List<ProjectBean>> observable = Observable.create(new Observable.OnSubscribe<List<ProjectBean>>() {
            @Override
            public void call(Subscriber<? super List<ProjectBean>> subscriber) {
                Response<List<ProjectBean>> beanResponse = null;
                try {
                    beanResponse = callIndexProject.execute();
                    subscriber.onNext(beanResponse.body());
                }catch (Exception e){
                    Toast.makeText(getActivity(),"Error：服务器连接失败 "+e.getMessage(),Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
                subscriber.onCompleted();
            }
        });
        observable.subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new Observer<List<ProjectBean>>() {
            @Override
            public void onCompleted() {
                Log.d("FirstPage","ProjectList更新完毕");
            }
            @Override
            public void onError(Throwable e) {
//                lay_fresh.setRefreshing(false);
                Toast.makeText(getActivity(),"Error：服务器连接失败 "+e.getMessage(),Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onNext(List<ProjectBean> beanList) {
                topProjectAdapter.clearAddDataList(beanList);
                topProjectAdapter.notifyDataSetChanged();
            }
        });
        return observable;
    }

    private Observable<List<PersonBean>> personTopRefresh(){
        netService.ApiService apiService = net.getPersonService();
        final Call<List<PersonBean>> callIndexPerson =
//                apiService.getIndexPerson(0,0+PAGE_SIZE,null,null); // 两个null表示不带这两个参数
                apiService.getPerson();
        Observable<List<PersonBean>> observable = Observable.create(new Observable.OnSubscribe<List<PersonBean>>() {
            @Override
            public void call(Subscriber<? super List<PersonBean>> subscriber) {
                Response<List<PersonBean>> beanResponse = null;
                try {
                    beanResponse = callIndexPerson.execute();
                    subscriber.onNext(beanResponse.body());
                }catch (Exception e){
                    e.printStackTrace();
                }
                subscriber.onCompleted();
            }
        });
        observable.subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new Observer<List<PersonBean>>() {
            @Override
            public void onCompleted() {
                Log.d("FirstPage","PersonList更新完毕");
//                lay_fresh.setRefreshing(false);
            }
            @Override
            public void onError(Throwable e) {
//                lay_fresh.setRefreshing(false);
                Toast.makeText(getActivity(),"Error：服务器连接失败",Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onNext(List<PersonBean> beanList) {
                topPersonAdapter.clearAddDataList(beanList);
                topPersonAdapter.notifyDataSetChanged();
            }
        });
        return observable;
    }

    public void Refresh() {
        personTopRefresh();
        projectTopRefresh();

//        lay_fresh.setRefreshing(false);
    }
}

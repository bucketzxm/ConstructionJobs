package com.workerassistant.Page.ThirdPage;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.PaintDrawable;
import android.os.Bundle;
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
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.workerassistant.CityPick.CityBean;
import com.workerassistant.CityPick.CityPickActivity;
import com.workerassistant.CustomUI.RecyclerViewDivider;
import com.workerassistant.MainActivity;
import com.workerassistant.R;
import com.workerassistant.Util.Constant;
import com.workerassistant.Util.ScreenUtils;
import com.workerassistant.Util.rxbus.ChangeAnswerEvent;
import com.workerassistant.Util.rxbus.RxBus;
import com.workerassistant.WorkType.WorkTypeActivity;
import com.workerassistant.WorkType.bean.WorkTypeBean;
import com.workerassistant.bean.PersonBean;
import com.workerassistant.bean.ProjectBean;
import com.workerassistant.network.netConfigure;
import com.workerassistant.network.netService;

import java.util.ArrayList;
import java.util.List;

import me.leefeng.lfrecyclerview.LFRecyclerView;
import me.leefeng.lfrecyclerview.OnItemClickListener;
import retrofit2.Call;
import retrofit2.Response;
import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

import static com.workerassistant.R.id.third_page_rv_list;

/**
 * Created by eva on 2017/4/13.
 */

public class ThirdFragment extends Fragment  implements OnItemClickListener, LFRecyclerView.LFRecyclerViewListener, LFRecyclerView.LFRecyclerViewScrollChange {//implements SwipeRefreshLayout.OnRefreshListener {
    public static ThirdFragment newInstance() {
        ThirdFragment f = new ThirdFragment();
        return f;
    }
    private static final String TAG = "ThirdFragment";
    private View rootView = null;//缓存Fragment view
    private netConfigure net = netConfigure.getInstance();
    private static int PAGE_SIZE = 5;
    private LFRecyclerView recycleview;
    private ThirdPageListAdapter adapter;
    private List<ProjectBean> datas  = new ArrayList<ProjectBean>();
    private int startIndex;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.third_page_main, container, false);
        initBase();
        initList();
        initPopUpWindow();
        return rootView;
    }
    private PopupWindow popupWindow;
    private View darkView;
    private Button btnSubmit,btnReset;
    private Animation animIn, animOut;
    private EditText etName,etPhone;
    private Spinner etLevel;

    private TextView tvCity,tvWorkType,tvTopCity,tvTopWorkType;
    private EditText etAge;
    private Subscription mSubscriptionWorkType,mSubscriptionCity;
//    观察更新
    private Subscription mSubscriptionOnfresh;
    private void initEndIndex(){
        startIndex = 0;
    }

    public int getStartIndex(){
        return startIndex;
    }


    public void setStartIndex(int endIndex) {
        this.startIndex = endIndex;
    }

    public void nextStartIndex(){
        this.startIndex += PAGE_SIZE;
    }

    private void initPopUpWindow(){
        darkView = rootView.findViewById(R.id.third_page_darkview);
        animIn = AnimationUtils.loadAnimation(getActivity(), R.anim.fade_in_anim);
        animOut = AnimationUtils.loadAnimation(getActivity(), R.anim.fade_out_anim);
        rootView.findViewById(R.id.topbar_page_3_popup_new_person_info).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindowClick();
            }
        });
        popupWindow = new PopupWindow(getActivity());
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.third_page_popup, null);
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
        etName = (EditText)view.findViewById(R.id.third_popup_name);
        etPhone = (EditText)view.findViewById(R.id.third_popup_phone);
        etAge =(EditText)view.findViewById(R.id.third_popup_age);
        etLevel = (Spinner)view.findViewById(R.id.third_popup_level);
//        etLevel.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {
//
//            }
//        });
        tvCity = (TextView)view.findViewById(R.id.third_popup_city);
        tvCity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(v.getContext(), CityPickActivity.class));
            }
        });
        mSubscriptionCity = RxBus.getDefault().toObserverable(CityBean.class)
                .subscribe(new Action1<CityBean>() {
                    @Override
                    public void call(CityBean cityBean) {
                        tvCity.setText(cityBean.getCity());
                        Refresh();
//                        选中后更新
                    }
                });
        tvWorkType = (TextView)view.findViewById(R.id.third_popup_work_type);
        tvWorkType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(v.getContext(), WorkTypeActivity.class));
            }
        });
        mSubscriptionWorkType = RxBus.getDefault().toObserverable(WorkTypeBean.class)
                .subscribe(new Action1<WorkTypeBean>() {
                    @Override
                    public void call(WorkTypeBean workTypeBean) {
                        tvWorkType.setText(workTypeBean.getWorkTypeName());
                        Refresh();
//                        选中后更新
                    }
                });

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
        btnSubmit = (Button)view.findViewById(R.id.third_popup_submit);
        btnReset = (Button)view.findViewById(R.id.third_popoup_reset);
        btnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //清空
                btnReset.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //清空
                        btnReset.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                etPhone.setText("");
                                etAge.setText("");
                                etName.setText("");
                            }
                        });
                    }
                });
            }
        });
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                送出结果
                if(etName.getText().equals("")){
                    Toast.makeText(getActivity(),"姓名不能为空",Toast.LENGTH_SHORT).show();
                    return;
                }
                else if(etPhone.getText().equals("")){
                    Toast.makeText(getActivity(),"联系方式不能为空",Toast.LENGTH_SHORT).show();
                    return;
                }
                PersonBean personBean  = new PersonBean();
//                personBean.setAge(Integer.parseInt(etAge.getText().toString()));
                personBean.setAge( etAge.getText().toString() );
                personBean.setCity(tvCity.getText().toString());
                personBean.setLevel((String)etLevel.getSelectedItem());
                personBean.setName(etName.getText().toString());
                personBean.setPhone(etPhone.getText().toString());
                personBean.setWorkType(tvWorkType.getText().toString());
                netConfigure net = netConfigure.getInstance();
                net.insertPerson(personBean);
//                送出后刷新页面


                MainActivity mainActivity = (MainActivity)getActivity();
                ChangeAnswerEvent changeAnswerEvent = new ChangeAnswerEvent();
                changeAnswerEvent.setTarget("secondFragment");
                changeAnswerEvent.setAnswer("onFresh");
                RxBus.getDefault().post(changeAnswerEvent);
                mainActivity.jumpToFragment(2);
                popupWindow.dismiss();
            }
        });
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if(hidden) {
            Log.d(TAG, "hidden " + hidden);

        }else{
//           hidden false   表示界面可见
            Log.d(TAG,"hidden "+ hidden);
//            Refresh();
        }
    }
    private void popupWindowClick()
    {
        if (popupWindow.isShowing()) {
            popupWindow.dismiss();
        } else {
            // popupWindow.showAsDropDown(rootView.findViewById(R.id.topbar_page_2_as_drop_down));
            popupWindow.showAtLocation(rootView.findViewById(R.id.topbar_page_3_as_drop_down), Gravity.BOTTOM,0,0);
            popupWindow.setAnimationStyle(-1);
            //背景变暗
            darkView.startAnimation(animIn);
            darkView.setVisibility(View.VISIBLE);
        }
    }
    private void initBase() {
        mSubscriptionOnfresh = RxBus.getDefault().toObserverable(ChangeAnswerEvent.class)
                .subscribe(new Action1<ChangeAnswerEvent>() {
                    @Override
                    public void call(ChangeAnswerEvent changeAnswerEvent) {
                            String target = changeAnswerEvent.getTarget();
                            String answer = changeAnswerEvent.getAnswer();
                        Log.d("thirdFragment","mSubscriptionOnfresh:"+target+" "+answer);
                            if(target!=null && answer!=null){
                                if(target.equals("thirdFragment")
                                        && answer.equals("onFresh")){
                                    Refresh();
                                }
                            }
                    }
                });

        tvTopCity = (TextView)rootView.findViewById(R.id.topbar_page_3_current_city);
        tvTopCity.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivityForResult(new Intent(v.getContext(), CityPickActivity.class), Constant.requestThirdTopCity);
                    }
                }
        );
        tvTopWorkType = (TextView)rootView.findViewById(R.id.topbar_page_3_current_work_type);
        tvTopWorkType.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivityForResult(new Intent(v.getContext(), WorkTypeActivity.class), Constant.requestThirdTopWorkType);
                    }
                }
        );

    }
    private void initList() {
        recycleview = (LFRecyclerView) rootView.findViewById(third_page_rv_list);
        recycleview.setLoadMore(true);
        recycleview.setRefresh(true);
        recycleview.setNoDateShow();
        recycleview.setAutoLoadMore(true);
        recycleview.setOnItemClickListener(this);
        recycleview.setLFRecyclerViewListener(this);
        recycleview.setScrollChangeListener(this);
        recycleview.setItemAnimator(new DefaultItemAnimator());
        recycleview.addItemDecoration(new RecyclerViewDivider(getActivity(), LinearLayoutManager.VERTICAL,2, Color.GRAY));
        Refresh();
        adapter = new ThirdPageListAdapter(getActivity(),datas);
        recycleview.setAdapter(adapter);
    }
    @Override
    public void onClick(int position) {
        Toast.makeText(getActivity(), "onClick" + datas.get(position), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onLongClick(int po) {
        Toast.makeText(getActivity(), "onLongClick" + po, Toast.LENGTH_SHORT).show();
    }
    private String getSelectCity(){
        String res = null;
        if(!tvTopCity.getText().toString().equals("全部")){
            res = tvTopCity.getText().toString();
        }
        return res;
    }
    private String getSelectWorkType(){
        String res = null;
        if(!tvTopWorkType.getText().toString().equals("全部")){
            res = tvTopWorkType.getText().toString();
        }
        return res;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mSubscriptionWorkType.unsubscribe();
        mSubscriptionCity.unsubscribe();
        mSubscriptionOnfresh.unsubscribe();
    }
    private void Refresh(){
        //         每次页还原

        initEndIndex();
        netService.ApiService apiService = net.getPersonService();
        final Call<List<ProjectBean>> callIndexProject =
                apiService.getIndexProject(getStartIndex(), getStartIndex()+PAGE_SIZE
                        ,getSelectCity(),getSelectWorkType());
        Log.d(TAG,"endIndex:"+ getStartIndex()+"+"+ String.valueOf(getStartIndex()+PAGE_SIZE));
        Observable.create(new Observable.OnSubscribe<List<ProjectBean>>() {
            @Override
            public void call(Subscriber<? super List<ProjectBean>> subscriber) {
                Response<List<ProjectBean>> beanResponse = null;
                try {
                    beanResponse = callIndexProject.execute();

                }catch (Exception e){
                    Toast.makeText(getActivity(),"Error：网络接口执行出错",Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }

//                出现返回为null，Response{protocol=http/1.1, code=500, message=Internal Server Error
//                此时若可以连接服务器，应该是由于返回的是[] 空数组，即没有对应的数据
//                并让subscriber正常结束onCompleted
                if (beanResponse.body() == null) {
                    Log.d(TAG,"beanResponsebody is null,筛选城市与工种没有相关数据");
                    subscriber.onCompleted();
                    return;
                }
                    Log.d(TAG,beanResponse.raw().code()+"");
                    Log.d(TAG,beanResponse.raw().toString());

                if (beanResponse.isSuccessful()) {
                    subscriber.onNext(beanResponse.body());
                }else{
                    Log.d(TAG, "Error：服务器返回码为"+beanResponse.raw().code());
                }

                subscriber.onCompleted();
            }
            // 指定 subscribe() 发生在 IO 线程
            // 指定 Subscriber 的回调发生在主线程
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<ProjectBean>>() {
                    @Override
                    public void onCompleted() {
                        recycleview.stopRefresh(true);
                        Log.d(TAG,"onCompleted");
                        //每次下拉更新，完全更新后更新页startIndex
                        //现在改为在onNext中更新得到数组的长度
//                        nextStartIndex();
                    }

                    @Override
                    public void onError(Throwable e) {
                        recycleview.stopRefresh(false);
                        Toast.makeText(getActivity(),"Error：服务器连接失败",Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onNext(List<ProjectBean> beanList) {
                        adapter.ClearaddDataList(beanList);
                        adapter.notifyDataSetChanged();
                        setStartIndex(getStartIndex()+beanList.size());
                    }
                });
//        单个插入时更新
//                adapter.notifyItemInserted(0);
//                adapter.notifyItemRangeChanged(0,datas.size());
    }
    @Override
    public void onRefresh() {
        Refresh();
    }

    @Override
    public void onLoadMore() {
        netService.ApiService apiService = net.getPersonService();
        final Call<List<ProjectBean>> callIndexProject =
                apiService.getIndexProject(getStartIndex(), getStartIndex()+PAGE_SIZE
                ,getSelectCity(),getSelectWorkType());
        Observable.create(new Observable.OnSubscribe<List<ProjectBean>>() {
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
            // 指定 subscribe() 发生在 IO 线程
            // 指定 Subscriber 的回调发生在主线程
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<ProjectBean>>() {
                    @Override
                    public void onCompleted() {
                        recycleview.stopLoadMore();
                        //更新页endIndex
                    }
                    @Override
                    public void onError(Throwable e) {
                        recycleview.stopLoadMore();
                        Toast.makeText(getActivity(),"Error：服务器连接失败",Toast.LENGTH_SHORT).show();
                    }
                    @Override
                    public void onNext(List<ProjectBean> beanList) {
                        adapter.addDataList(beanList);
                        adapter.notifyDataSetChanged();
                        setStartIndex(getStartIndex()+beanList.size());
                    }
                });
//                adapter.notifyItemRangeInserted(datas.size()-1,1);

    }

    @Override
    public void onRecyclerViewScrollChange(View view, int i, int i1) {

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
        switch (requestCode){
            case Constant.requestThirdTopCity:
                tvTopCity.setText(bundle.getString("City","没有选择"));
                break;
            case Constant.requestThirdTopWorkType:
                tvTopWorkType.setText(bundle.getString("WorkType","没有选择"));
                break;
        }
    }
}

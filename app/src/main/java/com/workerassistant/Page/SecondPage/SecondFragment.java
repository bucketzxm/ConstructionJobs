package com.workerassistant.Page.SecondPage;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.PaintDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.workerassistant.CityPick.CityBean;
import com.workerassistant.CityPick.CityPickActivity;
import com.workerassistant.CustomUI.RecyclerViewDivider;
import com.workerassistant.CustomUI.TimePick.ChooseTimeDialog;
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

import static com.workerassistant.R.id.second_page_rv_list;

/**
 * Created by eva on 2017/4/13.
 */

public class SecondFragment extends Fragment  implements OnItemClickListener, LFRecyclerView.LFRecyclerViewListener, LFRecyclerView.LFRecyclerViewScrollChange {//implements SwipeRefreshLayout.OnRefreshListener {

    public static SecondFragment newInstance() {
        SecondFragment f = new SecondFragment();
        return f;
    }
    private netConfigure net = netConfigure.getInstance();
    private View rootView = null;//缓存Fragment view
    private LFRecyclerView recycleview;
    private SecondPageListAdapter adapter;
    private  List<PersonBean> datas = new ArrayList<PersonBean>();

    private PopupWindow popupWindow;
    private Subscription mTimeZoneSubscription;
//    private Subscription mSubscriptionTopCity,mSubscriptionTopWorkType;
    private Subscription mSubscriptionOnfresh;
    private Subscription mSubscriptionCity,mSubscriptionWorkType;
    private View darkView;
    private Button btnSubmit,btnReset;
    private Animation animIn, animOut;
    private EditText etContactPerson,etPhone,etNumber;
    private EditText etStartTime,etEndTime;
    private TextView tvTopCity, tvTopWorkType;
    private TextView tvCity, tvWorkType;
    private int endIndex;
    private static int PAGE_SIZE = 5;

    final public static int HANDLE_TIME_ZONE = 29;
    public static Handler handlerThirdPage  = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Bundle bundle = null;
            switch (msg.what) {


            }

        }
    };
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        rootView = inflater.inflate(R.layout.secondpage_main, container, false);
        initBase();
        initList();
        initPopUpWindow();
        return rootView;
    }

    private void initPopUpWindow(){
        darkView = rootView.findViewById(R.id.second_page_darkview);
        animIn = AnimationUtils.loadAnimation(getActivity(), R.anim.fade_in_anim);
        animOut = AnimationUtils.loadAnimation(getActivity(), R.anim.fade_out_anim);
        rootView.findViewById(R.id.topbar_page_2_popup_new_work).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindowClick();
            }
        });
        popupWindow = new PopupWindow(getActivity());
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.second_page_popup, null);
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
        etContactPerson = (EditText)view.findViewById(R.id.second_popup_contact_person);
        etNumber  = (EditText)view.findViewById(R.id.second_popup_number_needed);
        etPhone  = (EditText)view.findViewById(R.id.second_popup_contact_phone);
        etStartTime  = (EditText)view.findViewById(R.id.second_popup_start_time);
        etEndTime = (EditText)view.findViewById(R.id.second_popup_end_time);
        mTimeZoneSubscription = RxBus.getDefault().toObserverable(ChangeAnswerEvent.class)
                .subscribe(new Action1<ChangeAnswerEvent>() {
                    @Override
                    public void call(ChangeAnswerEvent changeAnswerEvent) {
                        if(changeAnswerEvent.getTarget()!=null){
                            if(changeAnswerEvent.getTarget().equals("timeZone")){
                                String timeZone = changeAnswerEvent.getAnswer();
                                String[] timeZon = timeZone.split("-");
                                etStartTime.setText(timeZon[0]);
                                if(timeZon.length<2)return;
                                etEndTime.setText(timeZon[1]);
                            }
                        }
                    }
                });
        etStartTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChooseTimeDialog chooseTimeDialog = new ChooseTimeDialog();
                chooseTimeDialog.show(getChildFragmentManager(),"ChooseTimeDialog");
//                chooseTimeDialog.setHandler(handlerThirdPage);
            }
        });
        etEndTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChooseTimeDialog chooseTimeDialog = new ChooseTimeDialog();
                chooseTimeDialog.show(getChildFragmentManager(),"ChooseTimeDialog");
            }
        });

        tvCity = (TextView) view.findViewById(R.id.second_popup_city);
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
                    }
                });
        tvWorkType = (TextView) view.findViewById(R.id.second_popup_work_type);
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
                    }
                });
        btnSubmit = (Button)view.findViewById(R.id.second_popup_submit);
        btnReset = (Button)view.findViewById(R.id.second_popup_reset);
        btnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //清空

            }
        });
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //送出结果
                ProjectBean projectBean = new ProjectBean();
                projectBean.setCity(tvCity.getText().toString());
                projectBean.setContactName(etContactPerson.getText().toString());
                projectBean.setContactPhone(etPhone.getText().toString());
                projectBean.setEndTime(etEndTime.getText().toString());
                projectBean.setStartTime(etStartTime.getText().toString());
                projectBean.setNumNeed(etNumber.getText().toString());
                projectBean.setWorkType(tvWorkType.getText().toString());
                net.insertProject(projectBean);
                //送出后刷新另一个页面
                ChangeAnswerEvent changeAnswerEvent = new ChangeAnswerEvent();
                changeAnswerEvent.setTarget("thirdFragment");
                changeAnswerEvent.setAnswer("onFresh");
                RxBus.getDefault().post(changeAnswerEvent);
                popupWindow.dismiss();
            }
        });
    }
    private void popupWindowClick()
    {
        if (popupWindow.isShowing()) {
            popupWindow.dismiss();
        } else {
           // popupWindow.showAsDropDown(rootView.findViewById(R.id.topbar_page_2_as_drop_down));
            popupWindow.showAtLocation(rootView.findViewById(R.id.topbar_page_2_as_drop_down), Gravity.BOTTOM,0,0);
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
                        onRefresh();
//                        String target = changeAnswerEvent.getTarget();
//                        String answer = changeAnswerEvent.getAnswer();
//                        if(target!=null && answer!=null){
//                            if(target.equals("secondFragment")
//                                    && answer.equals("onFresh")){
//                                onRefresh();
//                            }
//                        }
                    }
                });
        tvTopCity = (TextView)rootView.findViewById(R.id.topbar_page_2_current_city);

        tvTopCity.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivityForResult(new Intent(v.getContext(), CityPickActivity.class), Constant.requestThirdTopCity);
                    }
                }
        );

        tvTopWorkType = (TextView)rootView.findViewById(R.id.topbar_page_2_current_work_type);
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
        recycleview = (LFRecyclerView) rootView.findViewById(second_page_rv_list);
        recycleview.setLoadMore(true);
        recycleview.setRefresh(true);
        recycleview.setNoDateShow();
        recycleview.setAutoLoadMore(true);
        recycleview.setOnItemClickListener(this);
        recycleview.setLFRecyclerViewListener(this);
        recycleview.setScrollChangeListener(this);
        recycleview.setItemAnimator(new DefaultItemAnimator());
        recycleview.addItemDecoration(new RecyclerViewDivider(getActivity(), LinearLayoutManager.VERTICAL,2, Color.GRAY));
//        datas.addAll(initData());
        onRefresh();
        adapter = new SecondPageListAdapter(datas);
        recycleview.setAdapter(adapter);
        endIndex = 0;
    }
    private List<PersonBean> initData(){
        List<PersonBean>beanList = new ArrayList<>();
        return beanList;

    }
    private void initEndIndex(){
        endIndex = 0;
    }
    public void setEndIndex(int endIndex) {
        this.endIndex = endIndex;
    }

    public int getEndIndex() {
        return endIndex;
    }
    public void nextIndex(){
        this.endIndex += PAGE_SIZE;
    }

    @Override
    public void onClick(int position) {
        Toast.makeText(getActivity(), "" + position, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onLongClick(int po) {
        Toast.makeText(getActivity(), "Long:" + po, Toast.LENGTH_SHORT).show();
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        mSubscriptionWorkType.unsubscribe();
        mSubscriptionCity.unsubscribe();
        mSubscriptionOnfresh.unsubscribe();
        mTimeZoneSubscription.unsubscribe();
    }
    @Override
    public void onRefresh() {
//         每次页还原
        initEndIndex();
//        Observable observable = net.getObservableAllPersonData();
        netService.ApiService apiService = net.getPersonService();
        final Call<List<PersonBean>> callIndexPerson =
                apiService.getIndexPerson(getEndIndex(),getEndIndex()+PAGE_SIZE);
        Observable.create(new Observable.OnSubscribe<List<PersonBean>>() {
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
            // 指定 subscribe() 发生在 IO 线程
            // 指定 Subscriber 的回调发生在主线程
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<PersonBean>>() {
                    @Override
                    public void onCompleted() {
                        recycleview.stopRefresh(true);
                        //更新页endIndex
                        nextIndex();
                    }

                    @Override
                    public void onError(Throwable e) {
                        recycleview.stopRefresh(false);
                        Toast.makeText(getActivity(),"Error：服务器连接失败",Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onNext(List<PersonBean> beanList) {
                        adapter.ClearaddDataList(beanList);
                        adapter.notifyDataSetChanged();
                    }
                });
////                recycleview.stopRefresh(b);
//                adapter.addFirstData(new PersonBean());
////                adapter.notifyItemInserted(0);
////                adapter.notifyItemRangeChanged(0,datas.size());

    }

    @Override
    public void onLoadMore() {
            netService.ApiService apiService = net.getPersonService();
            final Call<List<PersonBean>> callIndexPerson =
                    apiService.getIndexPerson(getEndIndex(),getEndIndex()+PAGE_SIZE);
            Observable.create(new Observable.OnSubscribe<List<PersonBean>>() {
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
                // 指定 subscribe() 发生在 IO 线程
                // 指定 Subscriber 的回调发生在主线程
            }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<PersonBean>>() {
                    @Override
                    public void onCompleted() {
                        recycleview.stopLoadMore();
                        //更新页endIndex
                        nextIndex();
                    }

                    @Override
                    public void onError(Throwable e) {
                        recycleview.stopLoadMore();
                        Toast.makeText(getActivity(),"Error：服务器连接失败",Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onNext(List<PersonBean> beanList) {
                        adapter.addDataList(beanList);
                        adapter.notifyDataSetChanged();
                    }
                });
//                recycleview.stopLoadMore();
//                adapter.addLastData(new PersonBean());
////                adapter.notifyItemRangeInserted(datas.size()-1,1);
//                adapter.notifyDataSetChanged();
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
                tvTopCity.setText(bundle.getString("City", "没有选择"));
                break;
            case Constant.requestThirdTopWorkType:
                tvTopWorkType.setText(bundle.getString("WorkType", "没有选择"));
                break;
        }
    }
    @Override
    public void onRecyclerViewScrollChange(View view, int i, int i1) {

    }


}


//        mSubscriptionTopWorkType = RxBus.getDefault().toObserverable(WorkTypeBean.class)
//                .subscribe(new Action1<WorkTypeBean>() {
//                    @Override
//                    public void call(WorkTypeBean workTypeBean) {
//                        tvTopWorkType.setText(workTypeBean.getWorkTypeName());
//                    }
//                });

//        mSubscriptionTopCity = RxBus.getDefault().toObserverable(CityBean.class)
//                .subscribe(new Action1<CityBean>() {
//                    @Override
//                    public void call(CityBean cityBean) {
//
//                        tvTopCity.setText(cityBean.getCity());
//                    }
//                });
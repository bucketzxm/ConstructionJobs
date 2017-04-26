package com.workerassistant.Page.FourthPage;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.workerassistant.CityPick.CityBean;
import com.workerassistant.MainActivity;
import com.workerassistant.R;
import com.workerassistant.Util.Constant;
import com.workerassistant.Util.rxbus.RxBus;
import com.workerassistant.WorkType.WorkTypeActivity;
import com.workerassistant.WorkType.bean.WorkTypeBean;

import rx.Subscription;
import rx.functions.Action1;


public class EditSelfInfoActivity extends AppCompatActivity {
    private TextView title;
    private ImageView back;
    private Button btnSubmit,btnReset;
    private EditText etName,etPhone;
    private Spinner etLevel;
    private TextView tvCity,tvWorkType;
    private EditText etAge;
    private String[] strLevel = {"老板","工人","包工头"};
    private Subscription mSubscriptionWorkType,mSubscriptionCity;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_self_info);
        initTopbar("修改个人信息");
        initBase();


        //        从SharedPreferences获取数据:
        SharedPreferences preferences=getSharedPreferences("UserInfo", Context.MODE_PRIVATE);
        String name=preferences.getString("name", "");
        String age=preferences.getString("age", "");
        String phone=preferences.getString("phone", "");
        String level=preferences.getString("level", "");
        String city=preferences.getString("city", "");
        String workType=preferences.getString("workType", "");
        etName.setText(name);
        etAge.setText(age);
        etPhone.setText(phone);
        tvWorkType.setText(city);
        tvWorkType.setText(workType);
        int indexLevel = 0;
        for (int i = 0 ;i<strLevel.length;i++){
            if(strLevel[i].equals(level)){
                indexLevel = i;
                break;
            }
        }
        etLevel.setSelection(indexLevel);

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

    private void initBase(){
        etName = (EditText)findViewById(R.id.edit_self_info_name);
        etPhone = (EditText)findViewById(R.id.edit_self_info_phone);
        etLevel = (Spinner) findViewById(R.id.edit_self_info_level);
        etAge = (EditText)findViewById(R.id.edit_self_info_age);
        tvCity = (TextView) findViewById(R.id.edit_self_info_city);
        tvCity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(v.getContext(), WorkTypeActivity.class), Constant.requestEditSelfCity);
            }
        });
        tvWorkType = (TextView) findViewById(R.id.edit_self_info_work_type);
        tvWorkType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(v.getContext(), WorkTypeActivity.class), Constant.requestEditSelfWorkType);
            }
        });
        mSubscriptionWorkType = RxBus.getDefault().toObserverable(WorkTypeBean.class)
                .subscribe(new Action1<WorkTypeBean>() {
                    @Override
                    public void call(WorkTypeBean workTypeBean) {
                        tvWorkType.setText(workTypeBean.getWorkTypeName());
                    }
                });
        mSubscriptionCity = RxBus.getDefault().toObserverable(CityBean.class)
                .subscribe(new Action1<CityBean>() {
                    @Override
                    public void call(CityBean cityBean) {
                        tvCity.setText(cityBean.getCity());
                    }
                });


        btnReset =(Button)findViewById(R.id.edit_self_info_reset);
        btnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                etPhone.setText("");
                etName.setText("");
                etAge.setText("");
            }
        });
        btnSubmit =(Button)findViewById(R.id.edit_self_info_submit);
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                Bundle bundle = new Bundle();
                bundle.putString("name",etName.getText().toString());
                bundle.putString("phone",etPhone.getText().toString());
                bundle.putString("age",etAge.getText().toString());
                bundle.putString("level",(String)etLevel.getSelectedItem());
                bundle.putString("city",tvCity.getText().toString());
                bundle.putString("workType",tvWorkType.getText().toString());
                intent.putExtras(bundle);
                intent.setClass(EditSelfInfoActivity.this, MainActivity.class);
                setResult(Constant.responseEditSelfInfo,intent);
                finish();
            }
        });
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        mSubscriptionWorkType.unsubscribe();
        mSubscriptionCity.unsubscribe();
    }
//    @Override
//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if(data == null){
//            return;
//        }
//        Bundle bundle = data.getExtras();
//        if(bundle == null){
//            return;
//        }
//        switch (requestCode){
//            case Constant.requestThirdTopCity:
//                tvCity.setText(bundle.getString("City","没有选择"));
//                break;
//            case Constant.requestThirdTopWorkType:
//                tvWorkType.setText(bundle.getString("WorkType","没有选择"));
//                break;
//        }
//    }

}

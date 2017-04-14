package com.workerassistant.Page.FourthPage;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.workerassistant.R;

public class EditSelfInfoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_self_info);
        initTopbar("修改个人信息");
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




}

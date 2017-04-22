package com.workerassistant.CustomUI.TimePick;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.TimePicker;

import com.workerassistant.R;
import com.workerassistant.Util.rxbus.ChangeAnswerEvent;
import com.workerassistant.Util.rxbus.RxBus;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class ChooseTimeDialog extends DialogFragment {
    private DatePicker date_picker,end_picker;
//    private TimePicker time_picker;
    private Button advance_order_btn;
    private Button come_order_btn;
    private View view;
    private String hour = "";
    private String minute = "";
    private Handler handler;
    public ChooseTimeDialog() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ContextThemeWrapper wrapper = new ContextThemeWrapper(getActivity(),android.R.style.Theme_Holo_Light_NoActionBar);
        inflater = inflater.cloneInContext(wrapper);
        view = inflater.inflate(R.layout.dialog_choose_time,container,false);
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        date_picker = (DatePicker)view.findViewById(R.id.date_picker_start_time);
        end_picker = (DatePicker)view.findViewById(R.id.date_picker_end_time);

        advance_order_btn = (Button)view.findViewById(R.id.date_picker_ok);
        come_order_btn = (Button)view.findViewById(R.id.date_picker_cancel);

        final Calendar minCalendar = Calendar.getInstance();
        minCalendar.set(Calendar.DAY_OF_MONTH, minCalendar.get(Calendar.DAY_OF_MONTH));
        date_picker.setMinDate(System.currentTimeMillis()-1000);//添加范围的最小值
        end_picker.setMinDate(System.currentTimeMillis()-1000);//添加范围的最小值

        Calendar maxCalendar = Calendar.getInstance();
        maxCalendar.add(Calendar.YEAR, 10);//设置年的范围（今年是2016，第二个参数是2则，datepicker范围为2016-2018）
        date_picker.setMaxDate(maxCalendar.getTimeInMillis());//添加范围的最小值
        end_picker.setMaxDate(maxCalendar.getTimeInMillis());//添加范围的最小值

        Calendar curCalendar = Calendar.getInstance();
        date_picker.init(curCalendar.get(Calendar.YEAR),
                curCalendar.get(Calendar.MONTH),
                curCalendar.get(Calendar.DAY_OF_MONTH), new DatePicker.OnDateChangedListener() {
                    @Override
                    public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
//                        应检查日期是否合法，是否是结束时间大于开始时间
                        Calendar minCalendar = Calendar.getInstance();
                        minCalendar.set(year,monthOfYear,dayOfMonth);
//                        设置失败
//                        minCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
//                        minCalendar.set(Calendar.YEAR, year);
//                        minCalendar.set(Calendar.MONTH, monthOfYear);
//                        end_picker.setMinDate(minCalendar.getTimeInMillis());//添加范围的最小值
                        end_picker.init(year,monthOfYear,dayOfMonth,null);
                    }
                });

//        time_picker.setIs24HourView(true);
        resizePicker(date_picker);
//        resizePicker(time_picker);
        advance_order_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                setTime(date_picker,time_picker,1);
                setDate(date_picker,end_picker,1);
                ChooseTimeDialog.this.dismiss();
            }
        });
        come_order_btn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                ChooseTimeDialog.this.dismiss();
            }
        });
        return view;
    }
    private void setTime(DatePicker datePicker, TimePicker timePicker, int tag) {
        String month = datePicker.getMonth() + 1 + "";
        String day = datePicker.getDayOfMonth() + "";
        hour = timePicker.getCurrentHour() + "";
        minute = timePicker.getCurrentMinute() + "";
        timePicker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            public void onTimeChanged(TimePicker view, int hourOfDay, int minuteOfHour) {
                hour = hourOfDay + "";
                minute = minuteOfHour + "";
            }
        });


        if (month.length() == 1) {
            month = "0" + month;
        }
        if (day.length() == 1) {
            day = "0" + day;
        }
        if (hour.length() == 1) {
            hour = "0" + hour;
        }
        if (minute.length() == 1) {
            minute = "0" + minute;
        }

        String timeStr = "";
        timeStr = datePicker.getYear() + "-" + month + "-" + day + " " + hour+ ":" + minute+":"+"00";
        //Toast.makeText(GlobalContext.getInstance(), timeStr, Toast.LENGTH_LONG).show();
        Message message = Message.obtain();
        message.obj = timeStr;
        handler.sendMessage(message);
    }
private void setDate(DatePicker datePicker, DatePicker endPicker,int tag) {
        String timeStr = "";
        String month = datePicker.getMonth() + 1 + "";
        String monthEnd = endPicker.getMonth() + 1 + "";
        String day = datePicker.getDayOfMonth() + "";
        String dayEnd = endPicker.getDayOfMonth() + "";
        if (month.length() == 1) {
            month = "0" + month;
        }
        if (day.length() == 1) {
            day = "0" + day;
        }
        if (monthEnd.length() == 1) {
            monthEnd = "0" +monthEnd;
        }
        if (day.length() == 1) {
            dayEnd = "0" + dayEnd;
        }
        timeStr = datePicker.getYear() + "/" + month + "/" + day ;
        timeStr += "-" + endPicker.getYear() + "/" + monthEnd + "/" + dayEnd ;
//      发出消息
        ChangeAnswerEvent changeAnswerEvent = new ChangeAnswerEvent();
        changeAnswerEvent.setAnswer(timeStr);
        changeAnswerEvent.setTarget("timeZone");
        RxBus.getDefault().post(changeAnswerEvent);

        //Toast.makeText(GlobalContext.getInstance(), timeStr, Toast.LENGTH_LONG).show();
//        Message message = Message.obtain();
//        message.obj = timeStr;
//        handler.sendMessage(message);
    }
    private void resizePicker(FrameLayout frame)
    {
        List<NumberPicker> list = findNumberPicker(frame);
        for(NumberPicker picker : list)
        {
            resizeNumberPicker(picker);
        }
    }

    private void resizeNumberPicker(NumberPicker picker)
    {
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        params.setMargins(10,0,10,0);
        picker.setLayoutParams(params);
    }

    private List<NumberPicker> findNumberPicker(ViewGroup viewGroup){
        List<NumberPicker> npList = new ArrayList<NumberPicker>();
        View child = null;
        if(null != viewGroup){
            for(int i = 0; i < viewGroup.getChildCount(); i++){
                child = viewGroup.getChildAt(i);
                if(child instanceof NumberPicker){
                    npList.add((NumberPicker)child);
                }
                else if(child instanceof LinearLayout){
                    List<NumberPicker> result = findNumberPicker((ViewGroup)child);
                    if(result.size()>0){
                        return result;
                    }
                }
            }
        }
        return npList;
    }

    public void setHandler(Handler handler)
    {
        this.handler = handler;
    }
}
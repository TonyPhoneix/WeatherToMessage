package com.tony.weathertomessage.view;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.icu.util.Calendar;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.widget.TimePicker;

import com.orhanobut.logger.Logger;
import com.tony.weathertomessage.utils.SharedPreferenceUtil;

/**
 * Created by Tony on 2017/10/16.
 */

public class TimePickerDialogFragment extends DialogFragment implements TimePickerDialog.OnTimeSetListener{

    private onSaveTimeSetListener listener;

    private SharedPreferenceUtil util = SharedPreferenceUtil.getInstance();

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);  // 选择器的初始小时值
        int minute = c.get(Calendar.MINUTE);  // 选择器的初始分钟值
        return new TimePickerDialog(getActivity(), this, hour, minute, true);  // 最后一个参数是选择是否使用24小时为单位，true为是
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
//        关闭时
//        在onTimeSet接口中将用户设定的时间值保存在一个Calendar对象中
        long time=System.currentTimeMillis();
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(time);
        util.setHour(hourOfDay);
        util.setMinute(minute);
        Logger.i("hour:%s,minute:%s",hourOfDay,minute);

        if (listener != null) {
            listener.onFinish();
        }
    }

    public interface  onSaveTimeSetListener{
        void onFinish();
    }

    public void setSaveTimeSetListener(onSaveTimeSetListener listener) {
        this.listener = listener;
    }

    public void removeSaveTimeSetListener() {
        this.listener = null;
    }
}

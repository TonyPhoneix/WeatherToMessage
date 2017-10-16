package com.tony.weathertomessage;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.icu.util.Calendar;
import android.icu.util.TimeZone;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.annotation.IdRes;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.orhanobut.logger.Logger;
import com.tony.weathertomessage.service.WeatherService;
import com.tony.weathertomessage.utils.SharedPreferenceUtil;
import com.tony.weathertomessage.utils.StringUtils;
import com.tony.weathertomessage.view.TimePickerDialogFragment;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    @Bind(R.id.phone)
    EditText phone;
    @Bind(R.id.savePhone)
    Button savePhone;
    @Bind(R.id.city)
    EditText city;
    @Bind(R.id.saveCity)
    Button saveCity;
    @Bind(R.id.time)
    TextView time;
    @Bind(R.id.saveTime)
    Button saveTime;
    @Bind(R.id.full)
    RadioButton full;
    @Bind(R.id.half)
    RadioButton half;
    @Bind(R.id.radioGroup)
    RadioGroup radioGroup;
    @Bind(R.id.start_Btn)
    Button startBtn;
    private SharedPreferenceUtil preferenceUtil;
    //    一天的毫秒数
    public static final int DAY = 1 * 60 * 1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        preferenceUtil = SharedPreferenceUtil.getInstance();
//        初始化一些默认值
        phone.setHint(preferenceUtil.getPhoneNumber());
        city.setHint(preferenceUtil.getCityName());
        int timeInterval = preferenceUtil.getTimeInterval();
        if (timeInterval == 24) {
            full.setChecked(true);
        } else {
            half.setChecked(true);
        }
        time.setText(String.format("发送时间为：%s:%s", preferenceUtil.getHour(), preferenceUtil.getMinute()));
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                int id = group.getCheckedRadioButtonId();
                if (id == R.id.full) {
                    //选择的是24小时
                    preferenceUtil.setTimeInterval(24);
                } else if (id == R.id.half) {
                    //选择的是12小时
                    preferenceUtil.setTimeInterval(12);
                }
            }
        });
    }

    @OnClick(R.id.savePhone)
    public void savePhone() {
        String content = this.phone.getText().toString().trim();
        if (StringUtils.isEmpty(content)) {
            return;
        }
        //
        preferenceUtil.setPhoneNumber(content);
        phone.setHint(content);
        Toast.makeText(MainActivity.this, "号码修改成功", Toast.LENGTH_SHORT).show();
    }

    @OnClick(R.id.saveCity)
    public void setSaveCity() {
        String content = this.city.getText().toString().trim();
        if (StringUtils.isEmpty(content)) {
            return;
        }
        preferenceUtil.setCityName(content);
        city.setHint(content);
        Toast.makeText(MainActivity.this, "地址修改成功", Toast.LENGTH_SHORT).show();
    }

    @OnClick(R.id.saveTime)
    public void saveTime() {
        final TimePickerDialogFragment fragment = new TimePickerDialogFragment();
        fragment.show(getSupportFragmentManager(), "TimePickerFragment");
        fragment.setSaveTimeSetListener(new TimePickerDialogFragment.onSaveTimeSetListener() {
            @Override
            public void onFinish() {
                time.setText(String.format("发送时间为：%s:%s", preferenceUtil.getHour(), preferenceUtil.getMinute()));
                Toast.makeText(MainActivity.this, "发送时间修改成功", Toast.LENGTH_SHORT).show();
                fragment.removeSaveTimeSetListener();
            }
        });
    }

    @OnClick(R.id.start_Btn)
    public void onClickStart() {
        int timeInterval = preferenceUtil.getTimeInterval();
        int hour = preferenceUtil.getHour();
        int minute = preferenceUtil.getMinute();
//        默认为7：00
        setAlarmTime(timeInterval, hour, minute);
    }


    /**
     * 设置系统闹钟，并且重复唤醒服务
     */
    private void setAlarmTime(int timeInterval, int hour, int minute) {
        //        然后再设置时间，去唤醒Service
        long systemTime = System.currentTimeMillis();
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(systemTime);
// 这里时区需要设置一下，不然会有8个小时的时间差
        calendar.setTimeZone(TimeZone.getTimeZone("GMT+8"));
        calendar.set(Calendar.MINUTE, minute);
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
// 选择的定时时间
        long selectTime = calendar.getTimeInMillis();
// 如果当前时间大于设置的时间，那么就从第二天的设定时间开始
        if (systemTime > selectTime) {
//            Toast.makeText(MainActivity.this, "设置的时间小于当前时间", Toast.LENGTH_SHORT).show();
            calendar.add(Calendar.DAY_OF_MONTH, 1);
            selectTime = calendar.getTimeInMillis();
        }
// 计算现在时间到设定时间的时间差
        long time = selectTime - systemTime;
        systemTime += time;
// 进行闹铃注册
        AlarmManager am = (AlarmManager) getSystemService(ALARM_SERVICE);
        Intent clock = new Intent(this, WeatherService.class);
        PendingIntent sender = PendingIntent.getService(this, 1, clock, PendingIntent.FLAG_UPDATE_CURRENT);
        am.setRepeating(AlarmManager.RTC_WAKEUP,
                systemTime, timeInterval * 60 * 60 * 1000, sender);
        Toast.makeText(MainActivity.this, "服务开启成功! ", Toast.LENGTH_SHORT).show();
    }
}

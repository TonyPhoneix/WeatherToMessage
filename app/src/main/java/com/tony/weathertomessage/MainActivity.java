package com.tony.weathertomessage;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.PowerManager;
import android.provider.Settings;
import android.support.annotation.IdRes;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.tony.weathertomessage.service.AlarmWeatherService;
import com.tony.weathertomessage.utils.SharedPreferenceUtil;
import com.tony.weathertomessage.utils.StringUtils;
import com.tony.weathertomessage.view.TimePickerDialogFragment;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity{

    private static final int REQUEST_IGNORE_BATTERY_CODE = 1;

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
    private AlarmWeatherService alarmWeatherService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        preferenceUtil = SharedPreferenceUtil.getInstance();
        alarmWeatherService = new AlarmWeatherService(this);
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

//        判断是否忽略电池优化
        if (!isIgnoringBatteryOptimizations(MainActivity.this)) {
            isIgnoreBatteryOption(MainActivity.this);
        }
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
        alarmWeatherService.setAlarmTime();
    }

    @TargetApi(Build.VERSION_CODES.M)
    public static boolean isIgnoringBatteryOptimizations(Activity activity){
        String packageName = activity.getPackageName();
        PowerManager pm = (PowerManager) activity
                .getSystemService(Context.POWER_SERVICE);
        if (pm.isIgnoringBatteryOptimizations(packageName)) {
            return true;
        }else {
            return false;
        }
    }

    /**
     * 针对N以上的Doze模式
     *
     * @param activity
     */
    public static void isIgnoreBatteryOption(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            try {
                Intent intent = new Intent();
                String packageName = activity.getPackageName();
                PowerManager pm = (PowerManager) activity.getSystemService(Context.POWER_SERVICE);
                if (!pm.isIgnoringBatteryOptimizations(packageName)) {
//               intent.setAction(Settings.ACTION_IGNORE_BATTERY_OPTIMIZATION_SETTINGS);
                    intent.setAction(Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS);
                    intent.setData(Uri.parse("package:" + packageName));
                    activity.startActivityForResult(intent, REQUEST_IGNORE_BATTERY_CODE);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_IGNORE_BATTERY_CODE){
                Toast.makeText(MainActivity.this, "已忽略电池优化", Toast.LENGTH_SHORT).show();
            }
        }else if (resultCode == RESULT_CANCELED){
            if (requestCode == REQUEST_IGNORE_BATTERY_CODE){
                Toast.makeText(MainActivity.this, "请开启忽略电池优化", Toast.LENGTH_SHORT).show();
            }
        }
    }
}

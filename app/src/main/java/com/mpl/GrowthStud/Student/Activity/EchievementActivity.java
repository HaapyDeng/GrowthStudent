package com.mpl.GrowthStud.Student.Activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.support.v4.app.FragmentActivity;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.mpl.GrowthStud.R;
import com.mpl.GrowthStud.Student.Adapter.CategoryListViewAdapter;
import com.mpl.GrowthStud.Student.Bean.CategoryListItem;
import com.mpl.GrowthStud.Student.Bean.StudentInfo;
import com.mpl.GrowthStud.Student.Tools.MyRadioGroup;
import com.mpl.GrowthStud.Student.Tools.NetworkUtils;
import com.mpl.GrowthStud.Student.View.RingView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.zip.Inflater;

import cz.msebera.android.httpclient.Header;

public class EchievementActivity extends FragmentActivity implements View.OnClickListener {
    private ChengJiuYiWanChengFragment fragment1;
    private ChengJiuJinXingZhongFragment fragment2;
    private ChengJiuDaiWanChengFragment fragment3;
    private android.app.FragmentManager fm;//管理器
    private TextView completed, underway, todo;
    private LinearLayout back;
    private TextView tv_count, tv_choose;
    private String totalNumber, completeNumber;
    private RingView ringView;
    private DrawerLayout drawerLayout;
    private TextView tv_start_time, tv_end_time, tv_clear, tv_ok;
    private RadioButton rb_youeryuan, rb_xiaoxue, rb_chuzhong, rb_gaozhong;
    private MyRadioGroup radioGroup;
    private ListView lv_category, lv_label;
    private int school_scope;
    private CharSequence choose_start_time, choose_end_time;
    private CategoryListItem categoryListItem;
    private List<CategoryListItem> listCategoryListItem;
    private List<CategoryListItem> listLableListItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_echievement);
        drawerLayout = (DrawerLayout) findViewById(R.id.activity_na);
        drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED); //关闭手势滑动
        radioGroup = (MyRadioGroup) findViewById(R.id.radioGroupID);
        //设置监听
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {
                switch (checkedId) {
                    case R.id.tv_youeryuan:
                        school_scope = 1;
                        rb_youeryuan.setTextColor(getResources().getColor(R.color.white));
                        rb_xiaoxue.setTextColor(getResources().getColor(R.color.tab_checked));
                        rb_chuzhong.setTextColor(getResources().getColor(R.color.tab_checked));
                        break;
                    case R.id.tv_xiaoxue:
                        school_scope = 2;
                        rb_xiaoxue.setTextColor(getResources().getColor(R.color.white));
                        rb_youeryuan.setTextColor(getResources().getColor(R.color.tab_checked));
                        rb_chuzhong.setTextColor(getResources().getColor(R.color.tab_checked));
                        break;
                    case R.id.tv_chuzhong:
                        school_scope = 3;
                        rb_chuzhong.setTextColor(getResources().getColor(R.color.white));
                        rb_youeryuan.setTextColor(getResources().getColor(R.color.tab_checked));
                        rb_xiaoxue.setTextColor(getResources().getColor(R.color.tab_checked));
                        break;

                }
                Log.d("school_scope==>>", "" + school_scope);
            }
        });
        rb_youeryuan = findViewById(R.id.tv_youeryuan);
        rb_xiaoxue = findViewById(R.id.tv_xiaoxue);
        rb_chuzhong = findViewById(R.id.tv_chuzhong);
        rb_gaozhong = findViewById(R.id.tv_gaozhong);

        tv_start_time = findViewById(R.id.tv_start_time);
        tv_start_time.setOnClickListener(this);
        tv_end_time = findViewById(R.id.tv_end_time);
        tv_end_time.setOnClickListener(this);
        tv_clear = findViewById(R.id.tv_clear);
        tv_clear.setOnClickListener(this);
        tv_ok = findViewById(R.id.tv_ok);
        tv_ok.setOnClickListener(this);
        lv_category = findViewById(R.id.lv_category);
        lv_category.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String categoryid = listCategoryListItem.get(position).getId();
                String name = listCategoryListItem.get(position).getName();
                Log.d("Categoryid==>>>>", categoryid);
                doGetLableList(categoryid);
            }
        });
        lv_label = findViewById(R.id.lv_label);
        lv_label.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String lableid = listLableListItem.get(position).getId();
                Log.d("lableid==>>>>", lableid);
            }
        });

        back = findViewById(R.id.back);
        back.setOnClickListener(this);

        ringView = (RingView) findViewById(R.id.ringView);

        completed = findViewById(R.id.completed);
        completed.setOnClickListener(this);
        tv_choose = findViewById(R.id.tv_choose);
        tv_choose.setOnClickListener(this);
//        View view = LayoutInflater.from(this).inflate(R.layout.choose_side_bar, null);
//        drawerlayout = view.findViewById(R.id.drawerlayout);

        underway = findViewById(R.id.underway);
        underway.setOnClickListener(this);

        todo = findViewById(R.id.todo);
        todo.setOnClickListener(this);

        tv_count = findViewById(R.id.tv_count);
        initData();
        selectFragment(0);

    }

    private void initData() {
        if (!NetworkUtils.checkNetWork(EchievementActivity.this)) {
            Toast.makeText(EchievementActivity.this, R.string.no_network, Toast.LENGTH_LONG).show();
            return;
        }
        SharedPreferences sharedPreferences = this.getSharedPreferences("myinfo", MODE_PRIVATE);
        String token = sharedPreferences.getString("token", "");
        String uid = sharedPreferences.getString("userid", "");
        String url = getResources().getString(R.string.local_url) + "/v1/achievement/default/statistical/" + 1 + "/" + 0 + "/" + 0 + "/" + 0 + "/" + 0 + "/" + 0;
        Log.d("url==>>", url);
        AsyncHttpClient client = new AsyncHttpClient();
        client.addHeader("X-Api-Token", token);
        client.get(url, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                Log.d("response==>>", response.toString());
                try {
                    int code = response.getInt("code");
                    if (code == 0) {
                        JSONObject data = response.getJSONObject("data");
                        totalNumber = data.getString("totalNumber");
                        completeNumber = data.getString("completeNumber");
                        Message message = new Message();
                        message.what = 1;
                        handler.sendMessage(message);
                    } else {
                        Toast.makeText(EchievementActivity.this, response.getString("message"), Toast.LENGTH_LONG).show();
                        return;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                Toast.makeText(EchievementActivity.this, R.string.no_network, Toast.LENGTH_LONG).show();
                return;
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                Toast.makeText(EchievementActivity.this, R.string.no_network, Toast.LENGTH_LONG).show();
                return;
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                Toast.makeText(EchievementActivity.this, R.string.no_network, Toast.LENGTH_LONG).show();
                return;
            }
        });
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    tv_count.setText(completeNumber + "/" + totalNumber);
                    ringView.setProgress(Integer.parseInt(totalNumber), Integer.parseInt(totalNumber) - Integer.parseInt(completeNumber));
                    ringView.setReminderColor(Color.parseColor("#3699ED"));
                    ringView.setProgressColor(Color.parseColor("#EEEEEE"));
                    ringView.setCircleWidth(40);
                    break;
            }
        }
    };

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.tv_choose:
                drawerLayout.openDrawer(Gravity.RIGHT);
                getCategoryList();
                break;
            case R.id.completed:
                selectFragment(0);
                break;
            case R.id.underway:
                selectFragment(1);
                break;
            case R.id.todo:
                selectFragment(2);
                break;
            case R.id.tv_start_time:
                final Calendar c;
                c = Calendar.getInstance();
                DatePickerDialog dialog = new DatePickerDialog(EchievementActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        c.set(year, monthOfYear, dayOfMonth);
                        Log.d("data>>>", "" + DateFormat.format("yyy-MM-dd", c));
                        tv_start_time.setText(DateFormat.format("yyy-MM-dd", c));
                        choose_start_time = DateFormat.format("yyy-MM-dd", c);
                    }
                }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH));
                dialog.show();
                break;
            case R.id.tv_end_time:
                final Calendar c2;
                c2 = Calendar.getInstance();
                DatePickerDialog dialog2 = new DatePickerDialog(EchievementActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        c2.set(year, monthOfYear, dayOfMonth);
                        Log.d("data>>>", "" + DateFormat.format("yyy-MM-dd", c2));
                        tv_end_time.setText(DateFormat.format("yyy-MM-dd", c2));
                        choose_end_time = DateFormat.format("yyy-MM-dd", c2);
                    }
                }, c2.get(Calendar.YEAR), c2.get(Calendar.MONTH), c2.get(Calendar.DAY_OF_MONTH));
                dialog2.show();
                break;
        }
    }

    private void getCategoryList() {
        if (NetworkUtils.checkNetWork(EchievementActivity.this) == false) {
            Toast.makeText(EchievementActivity.this, R.string.no_network, Toast.LENGTH_LONG).show();
            return;
        }
        SharedPreferences sharedPreferences = EchievementActivity.this.getSharedPreferences("myinfo", MODE_PRIVATE);
        String token = sharedPreferences.getString("token", "");
        String url = getResources().getString(R.string.local_url) + "/v1/achievement/category";
        AsyncHttpClient client = new AsyncHttpClient();
        client.addHeader("X-Api-Token", token);
        client.get(url, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                Log.d("response==>>", response.toString());
                try {
                    int code = response.getInt("code");
                    if (code == 0) {
                        JSONObject data = response.getJSONObject("data");
                        JSONArray list = data.getJSONArray("list");
                        listCategoryListItem = new ArrayList<>();
                        if (list.length() <= 0) {

                        } else {
                            for (int i = 0; i < list.length(); i++) {
                                JSONObject object = list.getJSONObject(i);
                                String id = object.getString("id");
                                String name = object.getString("name");
                                categoryListItem = new CategoryListItem(id, name);
                                listCategoryListItem.add(categoryListItem);
                            }
                            CategoryListViewAdapter categoryListViewAdapter = new CategoryListViewAdapter(EchievementActivity.this, listCategoryListItem);
                            lv_category.setAdapter(categoryListViewAdapter);
                        }
                    } else {
                        Toast.makeText(EchievementActivity.this, response.getString("message"), Toast.LENGTH_LONG).show();
                        return;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                Toast.makeText(EchievementActivity.this, R.string.no_network, Toast.LENGTH_LONG).show();
                return;
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                Toast.makeText(EchievementActivity.this, R.string.no_network, Toast.LENGTH_LONG).show();
                return;
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                Toast.makeText(EchievementActivity.this, R.string.no_network, Toast.LENGTH_LONG).show();
                return;
            }
        });
    }

    // 切换Fragment

    private void selectFragment(int i) {

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        hideFragment(transaction);

        changeView(i);// 设置选项颜色

        switch (i) {

            case 0:

                if (fragment1 == null) {

                    fragment1 = new ChengJiuYiWanChengFragment();

                    transaction.add(R.id.fragment, fragment1);

                }

                transaction.show(fragment1);

                break;


            case 1:

                if (fragment2 == null) {

                    fragment2 = new ChengJiuJinXingZhongFragment();

                    transaction.add(R.id.fragment, fragment2);

                }

                transaction.show(fragment2);

                break;
            case 2:

                if (fragment3 == null) {

                    fragment3 = new ChengJiuDaiWanChengFragment();

                    transaction.add(R.id.fragment, fragment3);

                }

                transaction.show(fragment3);

                break;

        }

        transaction.commit();

    }
    // 隐藏fragment

    private void hideFragment(FragmentTransaction transaction) {

        if (fragment1 != null) {

            transaction.hide(fragment1);

        }

        if (fragment2 != null) {

            transaction.hide(fragment2);

        }
        if (fragment3 != null) {

            transaction.hide(fragment3);

        }


    }
    //改变字体和背景色状态

    @SuppressLint("NewApi")

    private void changeView(int i) {

        if (i == 0) {

            //设置背景色及字体颜色

            completed.setBackgroundColor(getResources().getColor(R.color.getstarinfo));

            completed.setTextColor(getResources().getColor(R.color.text));

            underway.setBackground(getDrawable(R.drawable.textview_border));

            underway.setTextColor(getResources().getColor(R.color.getstarinfo));

            todo.setBackground(getDrawable(R.drawable.textview_border));

            todo.setTextColor(getResources().getColor(R.color.getstarinfo));

        } else if (i == 1) {

            underway.setBackgroundColor(getResources().getColor(R.color.getstarinfo));

            underway.setTextColor(getResources().getColor(R.color.text));

            completed.setBackground(getDrawable(R.drawable.textview_border));

            completed.setTextColor(getResources().getColor(R.color.getstarinfo));

            todo.setBackground(getDrawable(R.drawable.textview_border));

            todo.setTextColor(getResources().getColor(R.color.getstarinfo));

        } else if (i == 2) {
            todo.setBackgroundColor(getResources().getColor(R.color.getstarinfo));

            todo.setTextColor(getResources().getColor(R.color.text));

            completed.setBackground(getDrawable(R.drawable.textview_border));

            completed.setTextColor(getResources().getColor(R.color.getstarinfo));

            underway.setBackground(getDrawable(R.drawable.textview_border));

            underway.setTextColor(getResources().getColor(R.color.getstarinfo));
        }

    }


    private void doGetLableList(String id) {
        if (NetworkUtils.checkNetWork(EchievementActivity.this) == false) {
            Toast.makeText(EchievementActivity.this, R.string.no_network, Toast.LENGTH_LONG).show();
            return;
        }
        SharedPreferences sharedPreferences = EchievementActivity.this.getSharedPreferences("myinfo", MODE_PRIVATE);
        String token = sharedPreferences.getString("token", "");
        String url = getResources().getString(R.string.local_url) + "/v1/achievement/label/" + id;
        AsyncHttpClient client = new AsyncHttpClient();
        client.addHeader("X-Api-Token", token);
        client.get(url, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                Log.d("response==>>", response.toString());
                try {
                    int code = response.getInt("code");
                    if (code == 0) {
                        JSONObject data = response.getJSONObject("data");
                        JSONArray list = data.getJSONArray("list");
                        listLableListItem = new ArrayList<>();
                        if (list.length() <= 0) {

                        } else {
                            for (int i = 0; i < list.length(); i++) {
                                JSONObject object = list.getJSONObject(i);
                                String id = object.getString("id");
                                String name = object.getString("name");
                                categoryListItem = new CategoryListItem(id, name);
                                listLableListItem.add(categoryListItem);
                            }
                            CategoryListViewAdapter categoryListViewAdapter = new CategoryListViewAdapter(EchievementActivity.this, listLableListItem);
                            lv_label.setAdapter(categoryListViewAdapter);
                        }
                    } else {
                        Toast.makeText(EchievementActivity.this, response.getString("message"), Toast.LENGTH_LONG).show();
                        return;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                Toast.makeText(EchievementActivity.this, R.string.no_network, Toast.LENGTH_LONG).show();
                return;
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                Toast.makeText(EchievementActivity.this, R.string.no_network, Toast.LENGTH_LONG).show();
                return;
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                Toast.makeText(EchievementActivity.this, R.string.no_network, Toast.LENGTH_LONG).show();
                return;
            }
        });
    }
}

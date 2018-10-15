package com.mpl.GrowthStud.Student.Activity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.mpl.GrowthStud.R;
import com.mpl.GrowthStud.Student.Tools.NetworkUtils;
import com.mpl.GrowthStud.Student.View.LoadingDialog;
import com.yangchangfu.pickview_lib.Item;
import com.yangchangfu.pickview_lib.PickView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import cz.msebera.android.httpclient.Header;

public class SyatemAchieveYouActivity extends AppCompatActivity implements View.OnClickListener {
    private LinearLayout back;
    private TextView tv_title, tv_commit, tv_male, tv_birthy, tv_animal;
    private String achieveId;
    private String headTitle;
    private EditText et_username, et_position;
    private String username, male, birthday, animal, postion;
    private PickView catePickView;

    int mYear, mMonth, mDay;
    Calendar c;

    private LoadingDialog loadingDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_syatem_achieve);
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        achieveId = extras.getString("achieveid");
        headTitle = extras.getString("headtitle");

        back = findViewById(R.id.back);
        back.setOnClickListener(this);

        tv_title = findViewById(R.id.tv_title);
        tv_title.setText(headTitle);

        tv_commit = findViewById(R.id.tv_commit);
        tv_commit.setOnClickListener(this);

        et_username = findViewById(R.id.et_username);
        tv_male = findViewById(R.id.tv_male);
        tv_male.setOnClickListener(this);
        tv_birthy = findViewById(R.id.tv_birthy);
        tv_birthy.setOnClickListener(this);
        tv_animal = findViewById(R.id.tv_animal);
        tv_animal.setOnClickListener(this);
        et_position = findViewById(R.id.et_position);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.tv_male:
                List<Item> items = new ArrayList<>();
                String[] data = {"男", "女"};
                for (int i = 0; i < data.length; i++) {
                    Item item = new Item();
                    item.name = data[i];
                    items.add(item);
                }
                catePickView = new PickView(SyatemAchieveYouActivity.this);
                catePickView.setPickerView(items, PickView.Style.SINGLE);
                catePickView.setShowSelectedTextView(false);
                catePickView.show();
                catePickView.setOnSelectListener(new PickView.OnSelectListener() {
                    @Override
                    public void OnSelectItemClick(View view, int[] selectedIndexs, String selectedText) {
                        Log.d("selectedText==>>", selectedText);
                        male = selectedText;
                        tv_male.setText(male);
                        tv_male.setTextColor(getResources().getColor(R.color.text_blank));
                    }
                });
                break;
            case R.id.tv_birthy:
                c = Calendar.getInstance();
                DatePickerDialog dialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        c.set(year, monthOfYear, dayOfMonth);
                        Log.d("data>>>", "" + DateFormat.format("yyy-MM-dd", c));
                    }
                }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH));
                dialog.show();
                birthday = DateFormat.format("yyy-MM-dd", c).toString();
                tv_birthy.setText(DateFormat.format("yyy-MM-dd", c));
                tv_birthy.setTextColor(getResources().getColor(R.color.text_blank));
                break;
            case R.id.tv_animal:
                List<Item> items2 = new ArrayList<>();
                String[] data2 = {"鼠", "牛", "虎", "兔", "龙", "蛇", "马", "羊", "猴", "鸡", "狗", "猪"};
                for (int i = 0; i < data2.length; i++) {
                    Item item = new Item();
                    item.name = data2[i];
                    items2.add(item);
                }
                catePickView = new PickView(SyatemAchieveYouActivity.this);
                catePickView.setPickerView(items2, PickView.Style.SINGLE);
                catePickView.setShowSelectedTextView(false);
                catePickView.show();
                catePickView.setOnSelectListener(new PickView.OnSelectListener() {
                    @Override
                    public void OnSelectItemClick(View view, int[] selectedIndexs, String selectedText) {
                        Log.d("selectedText==>>", selectedText);
                        animal = selectedText;
                        tv_animal.setText(animal);
                        tv_animal.setTextColor(getResources().getColor(R.color.text_blank));
                    }
                });
                break;
            case R.id.tv_commit:
                username = et_username.getText().toString().trim();
                postion = et_position.getText().toString().trim();
                if (username.equals("") || male.equals("") || birthday.equals("") || animal.equals("") || postion.equals("")) {
                    Toast.makeText(this, "请完善信息再提交", Toast.LENGTH_SHORT).show();
                    return;
                }
                //组装json格式
                JSONObject object = new JSONObject();
                try {
                    object.put("name", username);
                    object.put("gender", male);
                    object.put("birthday", birthday);
                    object.put("china_zodiac", animal);
                    object.put("position", postion);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                doCommit(object);
                break;
        }
    }

    private void doCommit(JSONObject object) {
        loadingDialog = new LoadingDialog(this, "加载中...", R.drawable.ic_dialog_loading);
        loadingDialog.show();
        if (!NetworkUtils.checkNetWork(SyatemAchieveYouActivity.this)) {
            Toast.makeText(SyatemAchieveYouActivity.this, R.string.no_network, Toast.LENGTH_LONG).show();
            return;
        }
        SharedPreferences sharedPreferences = this.getSharedPreferences("myinfo", MODE_PRIVATE);
        String token = sharedPreferences.getString("token", "");
        String url = getResources().getString(R.string.local_url) + "/v1/achievement/system/update/" + achieveId;
        Log.d("url==>>", url);
        AsyncHttpClient client = new AsyncHttpClient();
        client.addHeader("X-Api-Token", token);
        RequestParams params = new RequestParams();
        params.put("content", object);
        client.put(url, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                Log.d("response==>>>", response.toString());
                try {
                    int code = response.getInt("code");
                    if (code == 0) {
                        loadingDialog.dismiss();
                        Toast.makeText(SyatemAchieveYouActivity.this, "提交成功", Toast.LENGTH_LONG).show();
                        finish();
                    } else {
                        loadingDialog.dismiss();
                        Toast.makeText(SyatemAchieveYouActivity.this, response.getString("message"), Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                loadingDialog.dismiss();
                Toast.makeText(SyatemAchieveYouActivity.this, R.string.no_network, Toast.LENGTH_LONG).show();
                return;
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                loadingDialog.dismiss();
                Toast.makeText(SyatemAchieveYouActivity.this, R.string.no_network, Toast.LENGTH_LONG).show();
                return;
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                loadingDialog.dismiss();
                Toast.makeText(SyatemAchieveYouActivity.this, R.string.no_network, Toast.LENGTH_LONG).show();
                return;
            }
        });
    }

}

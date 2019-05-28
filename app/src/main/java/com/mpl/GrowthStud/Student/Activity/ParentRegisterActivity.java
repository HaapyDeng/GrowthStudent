package com.mpl.GrowthStud.Student.Activity;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.mpl.GrowthStud.R;
import com.mpl.GrowthStud.Student.Tools.CountDownButton;
import com.mpl.GrowthStud.Student.View.LoadingDialog;
import com.yangchangfu.pickview_lib.Item;
import com.yangchangfu.pickview_lib.PickView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;

/**
 * 家长注册
 */
public class ParentRegisterActivity extends Activity implements View.OnClickListener {
    private CountDownButton countDownButton;
    private LinearLayout back;
    private EditText et_name, et_child_name, et_child_cid, et_phone, et_code, et_password;
    private TextView tv_choosep;
    private Button btn_login;
    private String name, status, childName, childCid, phone, code, password, statusType;

    private PickView catePickView;
    private LoadingDialog loadingDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parent_register);
        countDownButton = ((CountDownButton) findViewById(R.id.countDownButton));
        countDownButton.setOnClickListener(this);
        back = findViewById(R.id.back);
        back.setOnClickListener(this);
        et_name = findViewById(R.id.et_name);
        tv_choosep = findViewById(R.id.tv_choosep);
        tv_choosep.setOnClickListener(this);
        et_child_name = findViewById(R.id.et_child_name);
        et_child_cid = findViewById(R.id.et_child_cid);
        et_phone = findViewById(R.id.et_phone);
        et_code = findViewById(R.id.et_code);
        et_password = findViewById(R.id.et_password);
        btn_login = findViewById(R.id.btn_login);
        btn_login.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.countDownButton:
                phone = et_phone.getText().toString().trim();
                if (phone.equals("")) {
                    Toast.makeText(this, "请输入手机号码", Toast.LENGTH_SHORT).show();
                    return;
                }
                doGetCode(phone);
                //这里判断是否倒计时结束，避免在倒计时时多次点击导致重复请求接口
                if (countDownButton.isFinish()) {
                    //发送验证码请求成功后调用
                    countDownButton.start();
                }
                break;
            case R.id.tv_choosep:
                List<Item> items = new ArrayList<>();
                String[] data = {"爸爸", "妈妈", "爷爷", "奶奶", "姥爷", "姥姥"};
                for (int i = 0; i < data.length; i++) {
                    Item item = new Item();
                    item.name = data[i];
                    items.add(item);
                }
                catePickView = new PickView(this);
                catePickView.setPickerView(items, PickView.Style.SINGLE);
                catePickView.setShowSelectedTextView(false);
                catePickView.show();
                catePickView.setOnSelectListener(new PickView.OnSelectListener() {
                    @Override
                    public void OnSelectItemClick(View view, int[] selectedIndexs, String selectedText) {
                        Log.d("selectedText==>>", selectedText);
                        status = selectedText;
                        tv_choosep.setText(status);
                    }
                });
                break;
            case R.id.btn_login:
                name = et_name.getText().toString().trim();
                if (status.equals("爸爸")) {
                    statusType = "1";
                } else if (status.equals("妈妈")) {
                    statusType = "2";
                } else if (status.equals("爷爷")) {
                    statusType = "3";
                } else if (status.equals("奶奶")) {
                    statusType = "4";
                } else if (status.equals("姥爷")) {
                    statusType = "5";
                } else if (status.equals("姥姥")) {
                    statusType = "6";
                }
                childName = et_child_name.getText().toString().trim();
                childCid = et_child_cid.getText().toString().trim();
                phone = et_phone.getText().toString().trim();
                password = et_password.getText().toString().trim();
                code = et_code.getText().toString().trim();
                if (name.equals("") || statusType.equals("") || childName.equals("") || childCid.equals("") || phone.equals("") || code.equals("") || password.equals("")) {
                    Toast.makeText(ParentRegisterActivity.this, "请完善信息再点击注册按钮", Toast.LENGTH_LONG).show();
                    return;
                }
                doRegister(name, childName, childCid, statusType, phone, code, password);

                break;
        }
    }

    //注册
    private void doRegister(String name, String childName, String childCid, String statusType, String phone, String code, String password) {
        loadingDialog = new LoadingDialog(this, "注册...", R.drawable.ic_dialog_loading);
        loadingDialog.show();
        String url = getResources().getString(R.string.local_url) + "/user/register";
        RequestParams params = new RequestParams();
        params.put("name", name);
        params.put("username", childName);
        params.put("idcard", childCid);
        params.put("type", statusType);
        params.put("mobile", phone);
        params.put("code", code);
        params.put("password", password);
        AsyncHttpClient client = new AsyncHttpClient();
        client.get(url, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                Log.d("response==>>>", response.toString());
                try {
                    int code = response.getInt("code");
                    if (code == 0) {
                        loadingDialog.dismiss();
                        Toast.makeText(ParentRegisterActivity.this, "注册成功", Toast.LENGTH_LONG).show();
                        finish();
                    } else {
                        Toast.makeText(ParentRegisterActivity.this, response.getString("message"), Toast.LENGTH_LONG).show();
                        loadingDialog.dismiss();
                        return;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                Toast.makeText(ParentRegisterActivity.this, R.string.no_network, Toast.LENGTH_LONG).show();
                loadingDialog.dismiss();
                return;
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                Toast.makeText(ParentRegisterActivity.this, R.string.no_network, Toast.LENGTH_LONG).show();
                loadingDialog.dismiss();
                return;
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                Toast.makeText(ParentRegisterActivity.this, R.string.no_network, Toast.LENGTH_LONG).show();
                loadingDialog.dismiss();
                return;
            }
        });
    }

    //获取手机验证码
    private void doGetCode(String phone) {
        loadingDialog = new LoadingDialog(this, "发送验证码...", R.drawable.ic_dialog_loading);
        loadingDialog.show();
        String url = getResources().getString(R.string.local_url) + "/send";
        AsyncHttpClient client = new AsyncHttpClient();
        client.get(url, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                Log.d("response==>>>", response.toString());
                try {
                    int code = response.getInt("code");
                    if (code == 0) {
                        loadingDialog.dismiss();
                        Toast.makeText(ParentRegisterActivity.this, "验证码已发送", Toast.LENGTH_LONG).show();
                        return;
                    } else {
                        Toast.makeText(ParentRegisterActivity.this, response.getString("message"), Toast.LENGTH_LONG).show();
                        loadingDialog.dismiss();
                        return;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                Toast.makeText(ParentRegisterActivity.this, R.string.no_network, Toast.LENGTH_LONG).show();
                loadingDialog.dismiss();
                return;
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                Toast.makeText(ParentRegisterActivity.this, R.string.no_network, Toast.LENGTH_LONG).show();
                loadingDialog.dismiss();
                return;
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                Toast.makeText(ParentRegisterActivity.this, R.string.no_network, Toast.LENGTH_LONG).show();
                loadingDialog.dismiss();
                return;
            }
        });
    }
}

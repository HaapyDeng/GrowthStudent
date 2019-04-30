package com.mpl.GrowthStud.Student.Activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.mpl.GrowthStud.R;
import com.mpl.GrowthStud.Student.Adapter.FormListAdapter;
import com.mpl.GrowthStud.Student.Bean.FormAnswerBean;
import com.mpl.GrowthStud.Student.Bean.FormListItem;
import com.mpl.GrowthStud.Student.Tools.NetworkUtils;
import com.mpl.GrowthStud.Student.Tools.Utils;
import com.mpl.GrowthStud.Student.View.LoadingDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import cz.msebera.android.httpclient.Header;

public class FormActivity extends Activity implements View.OnClickListener {
    private LinearLayout back;
    private TextView tv_title, tv_commit, tv_menu, tv_lable;
    private EditText et_username, et_male, et_like, et_birthy;
    private String achieveId;
    private String headTitle, labelTitle;
    private Context mContext;
    private List<FormListItem> mdatas;
    private FormListAdapter formListAdapter;

    private LinearLayout rl_commit;

    private LoadingDialog loadingDialog;
    private ListView listview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form);
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        achieveId = extras.getString("achieveid");
        headTitle = extras.getString("headtitle");
        mContext = this;
        back = findViewById(R.id.back);
        back.setOnClickListener(this);

        tv_title = findViewById(R.id.tv_title);
        tv_title.setText(headTitle);

        listview = findViewById(R.id.listview);

        tv_lable = findViewById(R.id.tv_lable);

        tv_menu = findViewById(R.id.tv_menu);
        tv_menu.setOnClickListener(this);

        rl_commit = findViewById(R.id.rl_commit);
        rl_commit.setOnClickListener(this);
        initFormLabel(achieveId);

    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    Log.d("label==>>>", labelTitle);
                    tv_lable.setText(labelTitle);
                    break;
            }

        }
    };

    public static void setListViewHeightBasedOnChildren(ListView listView) {
        //获得adapter
        FormListAdapter adapter = (FormListAdapter) listView.getAdapter();
        if (adapter == null) {
            return;
        }

        int totalHeight = 0;
        for (int i = 0; i < adapter.getCount(); i++) {
            View listItem = adapter.getView(i, null, listView);
            listItem.measure(0, 0);
            //计算总高度
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        //计算分割线高度
        params.height = totalHeight + (listView.getDividerHeight() * (adapter.getCount() - 1));
        //给listview设置高度
        listView.setLayoutParams(params);
    }

    private void initFormLabel(String id) {
        loadingDialog = new LoadingDialog(this, "加载中...", R.drawable.ic_dialog_loading);
        loadingDialog.show();
        if (!NetworkUtils.checkNetWork(this)) {
            loadingDialog.show();
            Toast.makeText(this, R.string.no_network, Toast.LENGTH_LONG).show();
            return;
        }
        SharedPreferences sharedPreferences = this.getSharedPreferences("myinfo", MODE_PRIVATE);
        String token = sharedPreferences.getString("token", "");
        String url = getResources().getString(R.string.local_url) + "/v1/achievement/form/show/" + id;
        Log.d("url==>>", url);
        AsyncHttpClient client = new AsyncHttpClient();
        client.addHeader("X-Api-Token", token);
        client.get(url, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                Log.d("response==>>>", response.toString());
                try {
                    int code = response.getInt("code");
                    if (code == 0) {
                        loadingDialog.dismiss();
                        JSONObject data = response.getJSONObject("data");
                        labelTitle = data.getJSONArray("label").get(0).toString();
                        JSONArray item = data.getJSONArray("item");
                        mdatas = new ArrayList<>();
                        for (int i = 0; i < item.length(); i++) {
                            JSONObject object = item.getJSONObject(i);
                            String id = object.getString("id");
                            String label = object.getString("label");
                            String prompt = object.getString("prompt");
                            int type = object.getInt("type");
                            String options = object.getString("options");
                            int order = object.getInt("order");
                            FormListItem formListItem = new FormListItem(id, label, prompt, type, options, order);
                            mdatas.add(formListItem);
                        }
//                        FormListItem formListItem2 = new FormListItem("0", "", "", 1, "", 1);
//                        mdatas.add(formListItem2);
                        formListAdapter = new FormListAdapter(FormActivity.this, mdatas);
                        listview.setAdapter(formListAdapter);
                        setListViewHeightBasedOnChildren(listview);
                        Message message = new Message();
                        message.what = 1;
                        handler.sendMessage(message);

                    } else {
                        loadingDialog.dismiss();
                        Toast.makeText(mContext, response.getString("message"), Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                loadingDialog.dismiss();
                Toast.makeText(mContext, R.string.no_network, Toast.LENGTH_LONG).show();
                return;
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                loadingDialog.dismiss();
                Toast.makeText(mContext, R.string.no_network, Toast.LENGTH_LONG).show();
                return;
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                loadingDialog.dismiss();
                Toast.makeText(mContext, R.string.no_network, Toast.LENGTH_LONG).show();
                return;
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back:
                Utils.hideInput(mContext, view);
                finish();
                break;
            case R.id.tv_menu:
                showOrderMenu(view);
                break;
            case R.id.rl_commit:
                int answerLength = 0;
                answerLength = formListAdapter.getData().size();
                Log.d("answerLength==>>>", "" + answerLength);
                JSONObject object = new JSONObject();
                if (answerLength == mdatas.size()) {
                    for (int j = 0; j < answerLength; j++) {
                        String id = "";
                        String answer = "";
                        id = formListAdapter.getData().get(j).getId();
                        answer = formListAdapter.getData().get(j).getAnswer();
                        try {
                            object.put(id, answer);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    Log.d("object==>>>", object.toString());
                } else {
                    Toast.makeText(mContext, "请完善信息再提交", Toast.LENGTH_SHORT).show();
                    return;
                }
                doputInfo(object);
                break;
        }

    }

    private void showOrderMenu(View v) {
        final PopupMenu popupMenu = new PopupMenu(this, v);
        popupMenu.getMenuInflater().inflate(R.menu.menu_order, popupMenu.getMenu());
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (item.getTitle().equals("拍照上传")) {
                    Log.d("item.getTitle()==>", "拍照上传");
                    Intent intent = new Intent(FormActivity.this, TuWenTakePhotoComActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("achieveid", achieveId);
                    intent.putExtras(bundle);
                    startActivity(intent);
                    return true;
                }
//                } else if (item.getTitle().equals("选择版式")) {
//                    et_wenzi = findViewById(R.id.et_wenzi);
//                    wenzi = et_wenzi.getText().toString().trim();
//                    if (wenzi.length() <= 0) {
//                        Toast.makeText(mContext, R.string.wenzi_lenth_low, Toast.LENGTH_LONG).show();
//                        return true;
//                    }
//                    if (mPicList.size() == 0) {
//                        Toast.makeText(mContext, R.string.pic_lenth_low, Toast.LENGTH_LONG).show();
//                        return true;
//                    }
//                    Intent intent = new Intent(mContext, ChangeBanShiActivity.class);
//                    Bundle bundle = new Bundle();
//                    bundle.putString("type", "2");
//                    bundle.putString("piccount", "" + mPicList.size());
//                    intent.putExtras(bundle);
//                    startActivityForResult(intent, 1); //REQUESTCODE--->1
//                    Log.d("item.getTitle()==>", "预览");
//                    Intent intent = new Intent(mContext, TuWenPreviewDoActivity.class);
//                    Bundle bundle = new Bundle();
//                    bundle.putString("achieveid", achieveId);
//                    bundle.putString("type", "2");
//                    intent.putExtra("mPicList", (Serializable) mPicList);
//                    bundle.putInt("piccount", mPicList.size());
//                    intent.putExtras(bundle);
//                    startActivity(intent);
//                return true;
//            }
//                } else if (item.getTitle().equals("选择背景")) {
//                    et_wenzi = findViewById(R.id.et_wenzi);
//                    wenzi = et_wenzi.getText().toString().trim();
//                    if (wenzi.length() <= 0) {
//                        Toast.makeText(mContext, R.string.wenzi_lenth_low, Toast.LENGTH_LONG).show();
//                        return true;
//                    }
//                    if (mPicList.size() == 0) {
//                        Toast.makeText(mContext, R.string.pic_lenth_low, Toast.LENGTH_LONG).show();
//                        return true;
//                    }
//                    if (banshiId.equals("")) {
//                        switch (mPicList.size()) {
//                            case 1:
//                                banshiId = "20001";
//                                break;
//                            case 2:
//                                banshiId = "20003";
//                                break;
//                            case 3:
//                                banshiId = "20005";
//                                break;
//                            case 4:
//                                banshiId = "20007";
//                                break;
//                        }
//                    }
//                    Intent intent = new Intent(mContext, ChangeDiTuActivity.class);
//                    Bundle bundle = new Bundle();
//                    bundle.putString("id", achieveId);
//                    bundle.putString("banshiId", "" + banshiId);
//                    intent.putExtras(bundle);
//                    startActivityForResult(intent, 1);
//                    return true;
//                }
                return false;
            }
        });
        popupMenu.show();
    }

    private void doputInfo(JSONObject object) {
        loadingDialog = new LoadingDialog(this, "提交中...", R.drawable.ic_dialog_loading);
        loadingDialog.show();
        if (!NetworkUtils.checkNetWork(this)) {
            loadingDialog.show();
            Toast.makeText(this, R.string.no_network, Toast.LENGTH_LONG).show();
            return;
        }
        SharedPreferences sharedPreferences = this.getSharedPreferences("myinfo", MODE_PRIVATE);
        String token = sharedPreferences.getString("token", "");
        String url = getResources().getString(R.string.local_url) + "/v1/achievement/form/update/" + achieveId;
        Log.d("url==>>", url);
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.put("content", object);
        client.addHeader("X-Api-Token", token);
        client.put(url, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                Log.d("response==>>", response.toString());
                try {
                    int code = response.getInt("code");
                    if (code == 0) {
                        loadingDialog.dismiss();
                        Toast.makeText(mContext, R.string.commit_success, Toast.LENGTH_LONG).show();
                        Intent intent = new Intent("android.intent.action.CART_BROADCAST");
                        intent.putExtra("data", "refresh");
                        LocalBroadcastManager.getInstance(mContext).sendBroadcast(intent);
                        sendBroadcast(intent);
                        finish();
                    } else {
                        loadingDialog.dismiss();
                        Toast.makeText(mContext, response.getString("message"), Toast.LENGTH_LONG).show();
                        return;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                loadingDialog.dismiss();
                Toast.makeText(mContext, R.string.no_network, Toast.LENGTH_LONG).show();
                return;
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                loadingDialog.dismiss();
                Toast.makeText(mContext, R.string.no_network, Toast.LENGTH_LONG).show();
                return;
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                loadingDialog.dismiss();
                Toast.makeText(mContext, R.string.no_network, Toast.LENGTH_LONG).show();
                return;
            }
        });
    }


}

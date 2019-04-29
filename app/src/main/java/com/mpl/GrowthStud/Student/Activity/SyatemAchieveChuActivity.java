package com.mpl.GrowthStud.Student.Activity;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.entity.LocalMedia;
import com.mpl.GrowthStud.R;
import com.mpl.GrowthStud.Student.Adapter.GridViewOnePictureAdapter;
import com.mpl.GrowthStud.Student.Bean.MainConstant;
import com.mpl.GrowthStud.Student.Tools.NetworkUtils;
import com.mpl.GrowthStud.Student.Tools.PictureSelectorOneConfig;
import com.mpl.GrowthStud.Student.Tools.Utils;
import com.mpl.GrowthStud.Student.View.LoadingDialog;
import com.yangchangfu.pickview_lib.Item;
import com.yangchangfu.pickview_lib.PickView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import cz.msebera.android.httpclient.Header;

public class SyatemAchieveChuActivity extends Activity implements View.OnClickListener {
    private LinearLayout back;
    private Context mContext;
    private TextView tv_title, tv_male, tv_birthy, tv_political_status, tv_gostudytime, tv_text_count1, tv_text_count2;
    private String achieveId;
    private String headTitle;
    private EditText et_username, et_nation, et_content1, et_content2, et_schoolnum;
    private String username = "", carId, nation = "", male = "", birthday = "", political_status = "", imagePath, gostudytime = "", schoolnum = "", text_count1 = "", text_count2 = "";
    private PickView catePickView;
    private ImageView delete_iv;
    private ArrayList<String> mPicList = new ArrayList<>(); //上传的图片凭证的数据源
    private GridViewOnePictureAdapter mGridViewAddImgAdapter; //展示上传的图片的适配器
    int mYear, mMonth, mDay;
    Calendar c, c1;
    private GridView gridView;
    private String backUrl = "";
    private int tag = 0;
    private LoadingDialog loadingDialog;
    private LinearLayout rl_commit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_syatem_achieve_chu);
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        achieveId = extras.getString("achieveid");
        headTitle = extras.getString("headtitle");
        mContext = this;
        back = findViewById(R.id.back);
        back.setOnClickListener(this);

        tv_title = findViewById(R.id.tv_title);
        tv_title.setText(headTitle);

        rl_commit = findViewById(R.id.rl_commit);
        rl_commit.setOnClickListener(this);

        delete_iv = findViewById(R.id.delete_iv);
        delete_iv.setOnClickListener(this);

        et_username = findViewById(R.id.et_username);
        tv_male = findViewById(R.id.tv_male);
        tv_male.setOnClickListener(this);
        et_nation = findViewById(R.id.et_nation);
        tv_birthy = findViewById(R.id.tv_birthy);
        tv_birthy.setOnClickListener(this);
        gridView = (GridView) findViewById(R.id.gridView);
        initGridView();
        tv_political_status = findViewById(R.id.tv_political_status);
        tv_political_status.setOnClickListener(this);

        tv_gostudytime = findViewById(R.id.tv_gostudytime);
        tv_gostudytime.setOnClickListener(this);

        et_content1 = findViewById(R.id.et_content1);
        et_content1.addTextChangedListener(mTextWatcher1);
        et_content2 = findViewById(R.id.et_content2);
        et_content2.addTextChangedListener(mTextWatcher2);

        tv_text_count1 = findViewById(R.id.tv_text_count1);
        tv_text_count2 = findViewById(R.id.tv_text_count2);
        et_schoolnum = findViewById(R.id.et_schoolnum);
    }

    TextWatcher mTextWatcher1 = new TextWatcher() {
        private CharSequence temp;

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            // TODO Auto-generated method stub
            temp = s;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count,
                                      int after) {
            // TODO Auto-generated method stub
//            et_wenzi.setText(s);//将输入的内容实时显示
        }

        @Override
        public void afterTextChanged(Editable s) {
            // TODO Auto-generated method stub

            tv_text_count1.setText("" + temp.length());
        }
    };
    TextWatcher mTextWatcher2 = new TextWatcher() {
        private CharSequence temp;

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            // TODO Auto-generated method stub
            temp = s;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count,
                                      int after) {
            // TODO Auto-generated method stub
//            et_wenzi.setText(s);//将输入的内容实时显示
        }

        @Override
        public void afterTextChanged(Editable s) {
            // TODO Auto-generated method stub

            tv_text_count2.setText("" + temp.length());
        }
    };

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_gostudytime:
                c1 = Calendar.getInstance();
                DatePickerDialog dialog1 = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        c1.set(year, monthOfYear, dayOfMonth);
                        Log.d("data>>>", "" + DateFormat.format("yyy-MM-dd", c1));
                    }
                }, c1.get(Calendar.YEAR), c1.get(Calendar.MONTH), c1.get(Calendar.DAY_OF_MONTH));
                dialog1.show();
                gostudytime = DateFormat.format("yyy-MM-dd", c1).toString();
                tv_gostudytime.setText(DateFormat.format("yyy-MM-dd", c1));
                tv_gostudytime.setTextColor(getResources().getColor(R.color.text_blank));
                break;
            case R.id.tv_political_status:
                List<Item> items = new ArrayList<>();
                String[] data = {"党员", "团员", "群众"};
                for (int i = 0; i < data.length; i++) {
                    Item item = new Item();
                    item.name = data[i];
                    items.add(item);
                }
                catePickView = new PickView(mContext);
                catePickView.setPickerView(items, PickView.Style.SINGLE);
                catePickView.setShowSelectedTextView(false);
                catePickView.show();
                catePickView.setOnSelectListener(new PickView.OnSelectListener() {
                    @Override
                    public void OnSelectItemClick(View view, int[] selectedIndexs, String selectedText) {
                        Log.d("selectedText==>>", selectedText);
                        political_status = selectedText;
                        tv_political_status.setText(political_status);
                        tv_political_status.setTextColor(getResources().getColor(R.color.text_blank));
                    }
                });
                break;
            case R.id.delete_iv:
                delete_iv.setVisibility(View.INVISIBLE);
                mPicList.clear();
                mGridViewAddImgAdapter.notifyDataSetChanged();
                break;
            case R.id.back:
                Utils.hideInput(mContext, view);
                finish();
                break;
            case R.id.rl_commit:
                et_username = findViewById(R.id.et_username);
                et_nation = findViewById(R.id.et_nation);
                et_content1 = findViewById(R.id.et_content1);
                et_content2 = findViewById(R.id.et_content2);
                et_schoolnum = findViewById(R.id.et_schoolnum);

                username = et_username.getText().toString().trim();
                nation = et_nation.getText().toString().trim();
                schoolnum = et_schoolnum.getText().toString().trim();
                text_count1 = et_content1.getText().toString().trim();
                text_count2 = et_content2.getText().toString().trim();
                if (username.equals("") || male.equals("") || nation.equals("") || political_status.equals("") || birthday.equals("") ||
                        gostudytime.equals("") || schoolnum.equals("") || text_count1.equals("") || text_count2.equals("") || mPicList.size() == 0) {
                    Toast.makeText(this, "请完善信息再提交", Toast.LENGTH_SHORT).show();
                } else {
                    doUploadImge(mPicList);
                }
                break;
            case R.id.tv_male:
                List<Item> items2 = new ArrayList<>();
                String[] data2 = {"男", "女"};
                for (int i = 0; i < data2.length; i++) {
                    Item item = new Item();
                    item.name = data2[i];
                    items2.add(item);
                }
                catePickView = new PickView(mContext);
                catePickView.setPickerView(items2, PickView.Style.SINGLE);
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

        }
    }

    //上传图片到图片服务器
    private void doUploadImge(final ArrayList<String> mPicList) {
        loadingDialog = new LoadingDialog(this, "提交中...", R.drawable.ic_dialog_loading);
        loadingDialog.show();
        if (!NetworkUtils.checkNetWork(this)) {
            Toast.makeText(this, R.string.no_network, Toast.LENGTH_LONG).show();
            return;
        }
        Log.d("imge==>>>", mPicList.toString());
        for (int i = 0; i < mPicList.size(); i++) {

            File file = new File(mPicList.get(i));
            String imgUrl = getResources().getString(R.string.uploadFile);
            AsyncHttpClient client = new AsyncHttpClient();
            RequestParams params = new RequestParams();
            try {
                params.put("file", file);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            client.post(imgUrl, params, new JsonHttpResponseHandler() {

                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    super.onSuccess(statusCode, headers, response);
                    Log.d("response==>>>", response.toString());
                    try {
                        int code = response.getInt("code");
                        if (code == 0) {
                            tag = tag + 1;
//                            JSONArray array = response.getJSONArray("data");
                            JSONObject object = response.getJSONObject("data");
                            String singUrl;
                            singUrl = object.getString("path");
                            if (singUrl.equals("")) {
                                loadingDialog.dismiss();
                                Toast.makeText(mContext, response.getString("message"), Toast.LENGTH_LONG).show();
                                return;
                            }
                            doUploadInfo(singUrl);
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
                    Toast.makeText(mContext, R.string.no_network, Toast.LENGTH_LONG).show();
                    return;
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                    super.onFailure(statusCode, headers, responseString, throwable);
                    Log.d("responseString==>>", responseString);
                    Toast.makeText(mContext, R.string.no_network, Toast.LENGTH_LONG).show();
                    return;
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                    super.onFailure(statusCode, headers, throwable, errorResponse);
                    Toast.makeText(mContext, R.string.no_network, Toast.LENGTH_LONG).show();
                    return;
                }
            });

        }

    }

    private void doUploadInfo(String imge) {
        //组装json格式
        JSONObject object = new JSONObject();
        try {
            object.put("name", username);
            object.put("gender", male);
            object.put("nation", nation);
            object.put("political_affiliation", political_status);
            object.put("birthday", birthday);
            object.put("year", gostudytime);
            object.put("number", schoolnum);
            object.put("address", text_count1);
            object.put("describe", text_count2);
            object.put("photo", imge);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        if (!NetworkUtils.checkNetWork(this)) {
            Toast.makeText(this, R.string.no_network, Toast.LENGTH_LONG).show();
            return;
        }
        Log.d("imge==>>>", imge);
        SharedPreferences sharedPreferences = this.getSharedPreferences("myinfo", MODE_PRIVATE);
        String token = sharedPreferences.getString("token", "");
        String url = getResources().getString(R.string.local_url) + "/v1/achievement/system/update/" + achieveId;
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

    //初始化展示上传图片的GridView
    private void initGridView() {
        mGridViewAddImgAdapter = new GridViewOnePictureAdapter(mContext, mPicList);
        gridView.setAdapter(mGridViewAddImgAdapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                if (position == 0) {
                    //如果“增加按钮形状的”图片的位置是最后一张，且添加了的图片的数量不超过5张，才能点击
                    if (mPicList.size() == MainConstant.ONE_SELECT_PIC_NUM) {
                        //最多添加5张图片
                        viewPluImg(0);
                    } else {
                        //添加凭证图片
                        selectPic(MainConstant.ONE_SELECT_PIC_NUM);
                    }
                } else {
                    viewPluImg(0);
                }
            }
        });
    }

    //查看大图
    private void viewPluImg(int position) {
        Intent intent = new Intent(mContext, PlusImageActivity.class);
        intent.putStringArrayListExtra(MainConstant.IMG_LIST, mPicList);
        intent.putExtra(MainConstant.POSITION, position);
        startActivityForResult(intent, MainConstant.REQUEST_CODE_MAIN);
    }

    /**
     * 打开相册或者照相机选择凭证图片，最多1张
     *
     * @param maxTotal 最多选择的图片的数量
     */
    private void selectPic(int maxTotal) {
        PictureSelectorOneConfig.initSingleConfig(this);
    }

    // 处理选择的照片的地址
    private void refreshAdapter(List<LocalMedia> picList) {
        for (LocalMedia localMedia : picList) {
            //被压缩后的图片路径
            if (localMedia.isCompressed()) {
                String compressPath = localMedia.getCompressPath(); //压缩后的图片路径
                Log.d("compressPath==..", compressPath);
                mPicList.add(compressPath); //把图片添加到将要上传的图片数组中
                mGridViewAddImgAdapter.notifyDataSetChanged();
                delete_iv.setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case PictureConfig.CHOOSE_REQUEST:
                    // 图片选择结果回调
                    refreshAdapter(PictureSelector.obtainMultipleResult(data));
                    // 例如 LocalMedia 里面返回三种path
                    // 1.media.getPath(); 为原图path
                    // 2.media.getCutPath();为裁剪后path，需判断media.isCut();是否为true
                    // 3.media.getCompressPath();为压缩后path，需判断media.isCompressed();是否为true
                    // 如果裁剪并压缩了，以取压缩路径为准，因为是先裁剪后压缩的
                    break;
            }
        }
        if (requestCode == MainConstant.REQUEST_CODE_MAIN && resultCode == MainConstant.RESULT_CODE_VIEW_IMG) {
            //查看大图页面删除了图片
            ArrayList<String> toDeletePicList = data.getStringArrayListExtra(MainConstant.IMG_LIST); //要删除的图片的集合
            mPicList.clear();
            mPicList.addAll(toDeletePicList);
            delete_iv.setVisibility(View.INVISIBLE);
            mGridViewAddImgAdapter.notifyDataSetChanged();
        }
    }
}

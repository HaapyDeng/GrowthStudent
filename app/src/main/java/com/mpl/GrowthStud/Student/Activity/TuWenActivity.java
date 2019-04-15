package com.mpl.GrowthStud.Student.Activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.entity.LocalMedia;
import com.mpl.GrowthStud.Student.Adapter.GridViewAdapter;
import com.mpl.GrowthStud.Student.Bean.MainConstant;
import com.mpl.GrowthStud.R;
import com.mpl.GrowthStud.Student.Tools.BitmapHelper;
import com.mpl.GrowthStud.Student.Tools.NetworkUtils;
import com.mpl.GrowthStud.Student.Tools.PictureSelectorConfig;
import com.mpl.GrowthStud.Student.Tools.SelectPicPopupWindow;
import com.mpl.GrowthStud.Student.View.LoadingDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;

public class TuWenActivity extends AppCompatActivity implements View.OnClickListener {
    private Context mContext;
    private GridView gridView;
    private ArrayList<String> mPicList = new ArrayList<>(); //上传的图片凭证的数据源
    private GridViewAdapter mGridViewAddImgAdapter; //展示上传的图片的适配器
    private TextView tv_title, tv_menu, tv_lable;
    private LinearLayout back;
    private RelativeLayout rl_commit;
    private EditText et_wenzi;
    private String wenzi;
    private String achieveId;
    private String headTitle, prompt, label;
    private ProgressDialog progress;
    private String backUrl = "";
    private int tag = 0;
    private TextView tv_text_count;
    private LoadingDialog loadingDialog;
    public static int screenWidth;//屏幕宽度

    private SelectPicPopupWindow menuWindow;

    private String banshiId = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tu_wen);
//        获取屏幕宽度
        WindowManager windowManager = getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        Point outSize = new Point();
        display.getSize(outSize);
        screenWidth = outSize.x;

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        achieveId = extras.getString("achieveid");
        headTitle = extras.getString("headtitle");
        mContext = this;
        gridView = (GridView) findViewById(R.id.gridView);
        back = findViewById(R.id.back);
        back.setOnClickListener(this);

        tv_menu = findViewById(R.id.tv_menu);
        tv_menu.setOnClickListener(this);

        tv_lable = findViewById(R.id.tv_lable);

        tv_title = findViewById(R.id.tv_title);
        tv_title.setText(headTitle);
        rl_commit = findViewById(R.id.rl_commit);
        rl_commit.setOnClickListener(this);

        et_wenzi = findViewById(R.id.et_wenzi);
        et_wenzi.addTextChangedListener(mTextWatcher);
        tv_text_count = findViewById(R.id.tv_text_count);
        doGetInfo();
        initGridView();

    }

    TextWatcher mTextWatcher = new TextWatcher() {
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

            tv_text_count.setText("" + temp.length());
        }
    };

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    et_wenzi.setHint(prompt);
                    Log.d("label==>>>", label);
                    tv_lable.setText(label);
                    break;
            }

        }
    };

    private void doGetInfo() {
        loadingDialog = new LoadingDialog(this, "加载中...", R.drawable.ic_dialog_loading);
        loadingDialog.show();
        if (!NetworkUtils.checkNetWork(TuWenActivity.this)) {
            Toast.makeText(TuWenActivity.this, R.string.no_network, Toast.LENGTH_LONG).show();
            return;
        }
        SharedPreferences sharedPreferences = this.getSharedPreferences("myinfo", MODE_PRIVATE);
        String token = sharedPreferences.getString("token", "");
        String url = getResources().getString(R.string.local_url) + "/v1/achievement/image/show/" + achieveId;
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
                        prompt = data.getString("prompt");
                        label = data.getString("label");
                        JSONArray array = data.getJSONArray("label");
                        label = array.getString(0);
                        Message message = new Message();
                        message.what = 1;
                        handler.sendMessage(message);
                    } else {
                        Toast.makeText(TuWenActivity.this, response.getString("message"), Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                loadingDialog.dismiss();
                Toast.makeText(TuWenActivity.this, R.string.no_network, Toast.LENGTH_LONG).show();
                return;
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                loadingDialog.dismiss();
                Toast.makeText(TuWenActivity.this, R.string.no_network, Toast.LENGTH_LONG).show();
                return;
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                loadingDialog.dismiss();
                Toast.makeText(TuWenActivity.this, R.string.no_network, Toast.LENGTH_LONG).show();
                return;
            }
        });
    }

    //初始化展示上传图片的GridView
    private void initGridView() {
        mGridViewAddImgAdapter = new GridViewAdapter(mContext, mPicList);
        gridView.setAdapter(mGridViewAddImgAdapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                if (position == parent.getChildCount() - 1) {
                    //如果“增加按钮形状的”图片的位置是最后一张，且添加了的图片的数量不超过5张，才能点击
                    if (mPicList.size() == MainConstant.MAX_SELECT_PIC_NUM) {
                        //最多添加5张图片
                        viewPluImg(position);
                    } else {
                        //实例化SelectPicPopupWindow
                        menuWindow = new SelectPicPopupWindow(TuWenActivity.this, itemsOnClick);
                        //显示窗口
                        menuWindow.showAtLocation(TuWenActivity.this.findViewById(R.id.ll_tuwen), Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0); //设置layout在PopupWindow中显示的位置

                    }
                } else {
                    viewPluImg(position);
                }
            }
        });
    }

    //为弹出窗口实现监听类
    private View.OnClickListener itemsOnClick = new View.OnClickListener() {

        public void onClick(View v) {
            menuWindow.dismiss();
            switch (v.getId()) {
                case R.id.btn_take_photo:
                    //添加凭证图片
                    selectPic(MainConstant.MAX_SELECT_PIC_NUM - mPicList.size());
                    break;
                case R.id.btn_pick_photo:
                    Intent intent = new Intent(TuWenActivity.this, CloudPhotosActivity.class);
                    startActivityForResult(intent, 1);
                    break;
                default:
                    break;
            }
        }
    };

    //查看大图
    private void viewPluImg(int position) {
        Intent intent = new Intent(mContext, PlusImageActivity.class);
        intent.putStringArrayListExtra(MainConstant.IMG_LIST, mPicList);
        intent.putExtra(MainConstant.POSITION, position);
        startActivityForResult(intent, MainConstant.REQUEST_CODE_MAIN);
    }

    /**
     * 打开相册或者照相机选择凭证图片，最多9张
     *
     * @param maxTotal 最多选择的图片的数量
     */
    private void selectPic(int maxTotal) {
        PictureSelectorConfig.initMultiConfig(this, maxTotal);
    }

    // 处理选择的照片的地址
    private void refreshAdapter(List<LocalMedia> picList) {
        for (LocalMedia localMedia : picList) {
            //被压缩后的图片路径
            if (localMedia.isCompressed()) {
                String compressPath = localMedia.getCompressPath(); //压缩后的图片路径
                mPicList.add(compressPath); //把图片添加到将要上传的图片数组中
                mGridViewAddImgAdapter.notifyDataSetChanged();
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
            mGridViewAddImgAdapter.notifyDataSetChanged();
        }
        if (resultCode == 2) {  //选择版式返回
            banshiId = data.getStringExtra("banshiId");
            Log.d("banshiid==>>>", banshiId);
        }
        if (resultCode == 3) {  //选择版式返回
            String beijingId = data.getStringExtra("beijingId");
            Log.d("banshiid==>>>", beijingId);
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back:
                finish();
                break;

            case R.id.tv_menu:
                showOrderMenu(v);
                break;

            case R.id.rl_commit:
                et_wenzi = findViewById(R.id.et_wenzi);
                wenzi = et_wenzi.getText().toString().trim();
                if (wenzi.length() <= 0) {
                    Toast.makeText(this, R.string.wenzi_lenth_low, Toast.LENGTH_LONG).show();
                    break;
                }
                if (mPicList.size() == 0) {
                    Toast.makeText(this, R.string.pic_lenth_low, Toast.LENGTH_LONG).show();
                    break;
                }
                if (banshiId.equals("")) {
                    switch (mPicList.size()) {
                        case 1:
                            banshiId = "20001";
                            break;
                        case 2:
                            banshiId = "20003";
                            break;
                        case 3:
                            banshiId = "20005";
                            break;
                        case 4:
                            banshiId = "20007";
                            break;
                    }
                }

                try {
                    doUploadImge(mPicList);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
//                doUploadTuWen(wenzi, mPicList);

//                Log.d("backUrl==>>>", backUrl);
//                doUploadTuWen(wenzi, backUrl);
                break;
        }
    }

    //上传图片到图片服务器
    private void doUploadImge(final ArrayList<String> mPicList) throws FileNotFoundException {
        loadingDialog = new LoadingDialog(this, "提交中...", R.drawable.ic_dialog_loading);
        loadingDialog.show();
        if (!NetworkUtils.checkNetWork(TuWenActivity.this)) {
            Toast.makeText(TuWenActivity.this, R.string.no_network, Toast.LENGTH_LONG).show();
            return;
        }
        Log.d("imge==>>>", mPicList.toString());
        for (int i = 0; i < mPicList.size(); i++) {
            File file = new File(mPicList.get(i));
            String imgUrl = getResources().getString(R.string.uploadFile);
            Bitmap bitmap = BitmapHelper.compressImage(BitmapFactory.decodeFile(file.getPath()));
            File imgFile = BitmapHelper.saveBitmapFile(bitmap, mPicList.get(i).toString());
            Log.d("imgFile-byte:", String.valueOf(imgFile.length()));
            AsyncHttpClient client = new AsyncHttpClient();
            RequestParams params = new RequestParams();
            params.put("image", imgFile);
            client.post(imgUrl, params, new JsonHttpResponseHandler() {

                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    super.onSuccess(statusCode, headers, response);
                    Log.d("response==>>>", response.toString());
                    try {
                        int code = response.getInt("code");
                        if (code == 0) {
                            tag = tag + 1;
                            JSONArray array = response.getJSONArray("data");
                            JSONObject object = array.getJSONObject(0);
                            String singUrl;
                            singUrl = object.getString("resource");
                            if (backUrl.equals("")) {
                                backUrl = singUrl;
                            } else {
                                backUrl = backUrl + "|" + singUrl;
                            }
                            Log.d("backUrl[][][]==>>>", backUrl.toString());
                            if (tag == mPicList.size()) {
                                Log.d("backUrlend==>>>", backUrl.toString());
                                doUploadTuWen(wenzi, backUrl, banshiId);
                            }
                        } else {
                            loadingDialog.dismiss();
                            Toast.makeText(TuWenActivity.this, response.getString("message"), Toast.LENGTH_LONG).show();
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
                    Toast.makeText(TuWenActivity.this, R.string.no_network, Toast.LENGTH_LONG).show();
                    return;
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                    super.onFailure(statusCode, headers, responseString, throwable);
                    loadingDialog.dismiss();
                    Log.d("responseString==>>", responseString);
                    Toast.makeText(TuWenActivity.this, R.string.no_network, Toast.LENGTH_LONG).show();
                    return;
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                    super.onFailure(statusCode, headers, throwable, errorResponse);
                    loadingDialog.dismiss();
                    Toast.makeText(TuWenActivity.this, R.string.no_network, Toast.LENGTH_LONG).show();
                    return;
                }
            });

        }

    }

    private void doUploadTuWen(String s, String wenzi, String imge) {

        if (!NetworkUtils.checkNetWork(TuWenActivity.this)) {
            Toast.makeText(TuWenActivity.this, R.string.no_network, Toast.LENGTH_LONG).show();
            return;
        }
        Log.d("imge==>>>", imge);
        SharedPreferences sharedPreferences = this.getSharedPreferences("myinfo", MODE_PRIVATE);
        String token = sharedPreferences.getString("token", "");
        String url = getResources().getString(R.string.local_url) + "/v1/achievement/image/update/" + achieveId;
        Log.d("url==>>", url);
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.put("content", wenzi);
        params.put("image", imge);
        params.put("style", s);
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
                        Toast.makeText(TuWenActivity.this, R.string.commit_success, Toast.LENGTH_LONG).show();
                        Intent intent = new Intent("android.intent.action.CART_BROADCAST");
                        intent.putExtra("data", "refresh");
                        LocalBroadcastManager.getInstance(TuWenActivity.this).sendBroadcast(intent);
                        sendBroadcast(intent);
                        finish();
                    } else {
                        loadingDialog.dismiss();
                        Toast.makeText(TuWenActivity.this, response.getString("message"), Toast.LENGTH_LONG).show();
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
                Toast.makeText(TuWenActivity.this, R.string.no_network, Toast.LENGTH_LONG).show();
                return;
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                loadingDialog.dismiss();
                Toast.makeText(TuWenActivity.this, R.string.no_network, Toast.LENGTH_LONG).show();
                return;
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                loadingDialog.dismiss();
                Toast.makeText(TuWenActivity.this, R.string.no_network, Toast.LENGTH_LONG).show();
                return;
            }
        });
    }

    private void showOrderMenu(View v) {
        final PopupMenu popupMenu = new PopupMenu(this, v);
        popupMenu.getMenuInflater().inflate(R.menu.menu_order, popupMenu.getMenu());
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (item.getTitle().equals("拍照上传")) {
                    Log.d("item.getTitle()==>", "拍照上传");
                    Intent intent = new Intent(TuWenActivity.this, TuWenTakePhotoComActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("achieveid", achieveId);
                    intent.putExtras(bundle);
                    startActivity(intent);
                    return true;
                } else if (item.getTitle().equals("选择版式")) {
                    et_wenzi = findViewById(R.id.et_wenzi);
                    wenzi = et_wenzi.getText().toString().trim();
                    if (wenzi.length() <= 0) {
                        Toast.makeText(mContext, R.string.wenzi_lenth_low, Toast.LENGTH_LONG).show();
                        return true;
                    }
                    if (mPicList.size() == 0) {
                        Toast.makeText(mContext, R.string.pic_lenth_low, Toast.LENGTH_LONG).show();
                        return true;
                    }
                    Intent intent = new Intent(mContext, ChangeBanShiActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("type", "2");
                    bundle.putString("piccount", "" + mPicList.size());
                    intent.putExtras(bundle);
                    startActivityForResult(intent, 1); //REQUESTCODE--->1
//                    Log.d("item.getTitle()==>", "预览");
//                    Intent intent = new Intent(mContext, TuWenPreviewDoActivity.class);
//                    Bundle bundle = new Bundle();
//                    bundle.putString("achieveid", achieveId);
//                    bundle.putString("type", "2");
//                    intent.putExtra("mPicList", (Serializable) mPicList);
//                    bundle.putInt("piccount", mPicList.size());
//                    intent.putExtras(bundle);
//                    startActivity(intent);
                    return true;
                } else if (item.getTitle().equals("选择背景")) {
                    et_wenzi = findViewById(R.id.et_wenzi);
                    wenzi = et_wenzi.getText().toString().trim();
                    if (wenzi.length() <= 0) {
                        Toast.makeText(mContext, R.string.wenzi_lenth_low, Toast.LENGTH_LONG).show();
                        return true;
                    }
                    if (mPicList.size() == 0) {
                        Toast.makeText(mContext, R.string.pic_lenth_low, Toast.LENGTH_LONG).show();
                        return true;
                    }
                    if (banshiId.equals("")) {
                        switch (mPicList.size()) {
                            case 1:
                                banshiId = "20001";
                                break;
                            case 2:
                                banshiId = "20003";
                                break;
                            case 3:
                                banshiId = "20005";
                                break;
                            case 4:
                                banshiId = "20007";
                                break;
                        }
                    }
                    Intent intent = new Intent(mContext, ChangeDiTuActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("id", achieveId);
                    bundle.putString("banshiId", "" + banshiId);
                    intent.putExtras(bundle);
                    startActivityForResult(intent, 1);
                    return true;
                }
                return false;
            }
        });
        popupMenu.show();
    }

}

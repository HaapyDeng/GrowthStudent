package com.mpl.GrowthStud.Student.Activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.mpl.GrowthStud.R;
import com.mpl.GrowthStud.Student.Adapter.FormListAdapter;
import com.mpl.GrowthStud.Student.Adapter.GridViewAdapter;
import com.mpl.GrowthStud.Student.Bean.FormListItem;
import com.mpl.GrowthStud.Student.Bean.MainConstant;
import com.mpl.GrowthStud.Student.Tools.NetworkUtils;
import com.mpl.GrowthStud.Student.Tools.PictureSelectorConfig;
import com.mpl.GrowthStud.Student.Tools.SelectPicPopupWindow;
import com.mpl.GrowthStud.Student.Tools.Utils;
import com.mpl.GrowthStud.Student.View.LoadingDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;

public class MixtureTwoActivity extends Activity implements View.OnClickListener {
    private JSONObject temple;
    private String achieveId;
    private String headTitle;
    private LoadingDialog loadingDialog;
    private Context mContext;
    private int templateLength;
    private JSONArray template;
    private TextView back, tv_title, tv_menu, tv_cm, tv_lable, tv_text_count;
    private LinearLayout addview;
    private ListView listview;
    private List<FormListItem> mdatas;
    private FormListAdapter formListAdapter;
    private EditText et_wenzi;
    private GridView gridView;
    private LinearLayout rl_commit;
    private String prompt, label;
    private ArrayList<String> mPicList = new ArrayList<>(); //上传的图片凭证的数据源
    private GridViewAdapter mGridViewAddImgAdapter; //展示上传的图片的适配器
    private SelectPicPopupWindow menuWindow;
    private String wenzi = "", banshiId = "";
    private String backUrl = "";
    private int tag = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mixture_two);
        mContext = this;
        back = findViewById(R.id.back);
        back.setOnClickListener(this);

        addview = findViewById(R.id.addview);

        rl_commit = findViewById(R.id.rl_commit);
        rl_commit.setOnClickListener(this);

        tv_menu = findViewById(R.id.tv_menu);
        tv_menu.setOnClickListener(this);

        tv_cm = findViewById(R.id.tv_cm);
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        String s = bundle.getString("data");
        Log.d("temple==>>", s);
        int t = 0;
        try {
            temple = new JSONObject(s);
            t = temple.getInt("t");
            Log.d("t===>>>", "" + t);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        switch (t) {
            case 1: //成就
                View v = View.inflate(mContext, R.layout.mix_tuwen_item, null);
                addview.addView(v);
                et_wenzi = v.findViewById(R.id.et_wenzi);
                tv_lable = v.findViewById(R.id.tv_lable);
                tv_text_count = v.findViewById(R.id.tv_text_count);
                gridView = v.findViewById(R.id.gridView);
                et_wenzi.addTextChangedListener(mTextWatcher);
                try {
                    prompt = temple.getString("prompt");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
//                try {
////                    label = temple.getString("label");
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
                et_wenzi.setHint(prompt);
//                Log.d("label==>>>", label);
//                tv_lable.setText(label);
                initGridView();
                break;
            case 2:  //表單
                View v1 = View.inflate(mContext, R.layout.mix_form_item, null);
                addview.addView(v1);
                listview = v1.findViewById(R.id.listview);
                JSONArray item = null;
                try {
                    item = temple.getJSONArray("item");

                    mdatas = new ArrayList<>();
                    for (int i = 0; i < item.length(); i++) {
                        JSONObject object2 = item.getJSONObject(i);
                        String id = object2.getString("id");
                        String label = object2.getString("label");
                        String prompt = object2.getString("prompt");
                        int type = object2.getInt("type");
                        String options = object2.getString("options");
                        int order = object2.getInt("order");
                        FormListItem formListItem = new FormListItem(id, label, prompt, type, options, order);
                        mdatas.add(formListItem);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                formListAdapter = new FormListAdapter(mContext, mdatas);
                listview.setAdapter(formListAdapter);
                setListViewHeightBasedOnChildren(listview);
                break;
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back:
                Utils.hideInput(mContext, v);
                finish();
                break;
            case R.id.tv_menu:
                showOrderMenu(v);
                break;

            case R.id.rl_commit:
                JSONObject object2 = null;
                int t2 = 0;
                try {
                    object2 = temple;
                    t2 = object2.getInt("t");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                switch (t2) {
                    case 2:
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
                            Toast.makeText(mContext, "请完善信息", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        if (template.length() == 1) {
//                     doCommitForm(object);
                        } else {
                            Intent intent = new Intent(mContext, MixtureTwoActivity.class);
                            JSONObject object3 = null;
                            Bundle bundle = new Bundle();
                            try {
                                object3 = template.getJSONObject(1);
                                bundle.putString("data", object3.toString());
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            intent.putExtras(bundle);
                            startActivity(intent);
                        }
                        break;
                    case 1:
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
                        if (template.length() == 1) {
                            try {
                                doUploadImge(mPicList);
                            } catch (FileNotFoundException e) {
                                e.printStackTrace();
                            }
                        } else {
                            Intent intent = new Intent(mContext, MixtureTwoActivity.class);
                            JSONObject object3 = null;
                            Bundle bundle = new Bundle();
                            try {
                                object3 = template.getJSONObject(1);
                                bundle.putString("data", object3.toString());
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            intent.putExtras(bundle);
                            startActivity(intent);
                        }
                        break;
                }


        }
    }

    //上传图片到图片服务器
    private void doUploadImge(final ArrayList<String> mPicList) throws FileNotFoundException {
        loadingDialog = new LoadingDialog(this, "提交中...", R.drawable.ic_dialog_loading);
        loadingDialog.show();
        if (!NetworkUtils.checkNetWork(mContext)) {
            Toast.makeText(mContext, R.string.no_network, Toast.LENGTH_LONG).show();
            return;
        }
        Log.d("imge==>>>", mPicList.toString());
        final JSONArray imgArray = new JSONArray();
        final JSONObject imgObject = new JSONObject();
        for (int i = 0; i < mPicList.size(); i++) {
            File file = new File(mPicList.get(i));
            String imgUrl = getResources().getString(R.string.uploadFile);
//            Bitmap bitmap = BitmapHelper.compressImage(BitmapFactory.decodeFile(file.getPath()));
//            File imgFile = BitmapHelper.saveBitmapFile(bitmap, mPicList.get(i).toString());
            Log.d("imgFile-byte:", String.valueOf(file.length()));
            AsyncHttpClient client = new AsyncHttpClient();
            RequestParams params = new RequestParams();
            params.put("file", file);
            client.post(imgUrl, params, new JsonHttpResponseHandler() {

                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    super.onSuccess(statusCode, headers, response);
                    Log.d("response==>>>", response.toString());
                    try {
                        int code = response.getInt("code");
                        if (code == 0) {
                            tag = tag + 1;
                            JSONObject object = response.getJSONObject("data");
                            String singUrl;
                            singUrl = getResources().getString(R.string.uploadUrl) + object.getString("path");
                            imgObject.put("image", singUrl);
                            imgArray.put(imgObject);
//                            if (backUrl.equals("")) {
//                                backUrl = singUrl;
//                            } else {
//                                backUrl = backUrl + "|" + singUrl;
//                            }
                            Log.d("backUrl[][][]==>>>", imgArray.toString());
                            if (tag == mPicList.size()) {
                                Log.d("backUrlend==>>>", imgArray.toString());
//                                doUploadTuWen(banshiId, wenzi, imgArray);
                            }
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
                    Log.d("responseString==>>", responseString);
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

    private void showOrderMenu(View v) {
        final PopupMenu popupMenu = new PopupMenu(this, v);
        popupMenu.getMenuInflater().inflate(R.menu.menu_order, popupMenu.getMenu());
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (item.getTitle().equals("拍照上传")) {
                    Log.d("item.getTitle()==>", "拍照上传");
                    Intent intent = new Intent(mContext, TuWenTakePhotoComActivity.class);
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
                        menuWindow = new SelectPicPopupWindow(MixtureTwoActivity.this, itemsOnClick);
                        //显示窗口
                        menuWindow.showAtLocation(MixtureTwoActivity.this.findViewById(R.id.ll_mix2), Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0); //设置layout在PopupWindow中显示的位置

                    }
                } else {
                    viewPluImg(position);
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
                    Intent intent = new Intent(mContext, CloudPhotosActivity.class);
                    startActivityForResult(intent, 1);
                    break;
                default:
                    break;
            }
        }
    };

    /**
     * 打开相册或者照相机选择凭证图片，最多9张
     *
     * @param maxTotal 最多选择的图片的数量
     */
    private void selectPic(int maxTotal) {
        PictureSelectorConfig.initMultiConfig(this, maxTotal);
    }


}

package com.mpl.GrowthStud.Student.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.view.PagerAdapter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.mpl.GrowthStud.R;
import com.mpl.GrowthStud.Student.Bean.AnswerBean;
import com.mpl.GrowthStud.Student.Bean.QuestionItem;
import com.mpl.GrowthStud.Student.Tools.NetworkUtils;
import com.mpl.GrowthStud.Student.Tools.Utils;
import com.mpl.GrowthStud.Student.View.NoScrollViewPager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


import cz.msebera.android.httpclient.Header;

/**
 * 问卷编辑
 */
public class QuestionnaireActivity extends AppCompatActivity implements View.OnClickListener {
    private String achieveId;
    private String headTitle;
    private TextView tv_title;
    private LinearLayout back;
    private int question_count;
    private int total_point;
    private QuestionItem questionItem;
    private List<QuestionItem> questionItemList;
    private NoScrollViewPager viewPager;
    private ArrayList<View> viewArrayList;
    private TextView tv_one_count;

    private View view;
    private TextView item_tv_name;
    private TextView item_tv_a_content;
    private TextView item_tv_b_content;
    private TextView item_tv_c_content;
    private TextView item_tv_d_content;

    private LinearLayout item_ll_a;
    private LinearLayout item_ll_b;
    private LinearLayout item_ll_c;
    private LinearLayout item_ll_d;

    private CheckBox item_tv_a;
    private CheckBox item_tv_b;
    private CheckBox item_tv_c;
    private CheckBox item_tv_d;

    private LinearLayout item_ll_first;
    private LinearLayout item_ll_two;

    private TextView item_one_net;
    private TextView item_tv_upq;
    private TextView item_tv_nextq;

    private LinearLayout item_ll_choose_item;
    private List<AnswerBean> answerList = new ArrayList<>();
    private AnswerBean answerBean;

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_questionnaire);
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        achieveId = extras.getString("achieveid");
        headTitle = extras.getString("headtitle");


        tv_title = findViewById(R.id.tv_title);
        tv_title.setText(headTitle);
        tv_one_count = findViewById(R.id.tv_one_count);
        back = findViewById(R.id.back);
        back.setOnClickListener(this);
        viewPager = findViewById(R.id.viewpager);
        getAnswers(achieveId);


    }

    /**
     * 获取题目
     *
     * @param achieveId
     */
    private void getAnswers(final String achieveId) {
        if (!NetworkUtils.checkNetWork(QuestionnaireActivity.this)) {
            Toast.makeText(QuestionnaireActivity.this, R.string.no_network, Toast.LENGTH_LONG).show();
            return;
        }
        String getUrl = getResources().getString(R.string.local_url) + "/v1/achievement/question/show/" + achieveId;
        SharedPreferences sharedPreferences = getSharedPreferences("myinfo", MODE_PRIVATE);
        String token = sharedPreferences.getString("token", "");
        Log.d("url==>>", getUrl);
        AsyncHttpClient client = new AsyncHttpClient();
        client.addHeader("X-Api-Token", token);
        client.get(getUrl, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                Log.d("response==>>", response.toString());
                try {
                    int code = response.getInt("code");
                    if (code == 0) {
                        JSONObject data = response.getJSONObject("data");
                        JSONObject question = data.getJSONObject("question");
                        question_count = question.getInt("item_number");
                        total_point = question.getInt("total_point");
                        JSONArray item = data.getJSONArray("item");
                        questionItemList = new ArrayList<QuestionItem>();

                        if (item.length() == 0) {
                            return;
                        }
                        for (int i = 0; i < item.length(); i++) {
                            JSONArray options = null;
                            JSONObject object = item.getJSONObject(i);
                            String id = object.getString("id");
                            String name = object.getString("name");
                            int point = object.getInt("point");
                            int type = object.getInt("type");
                            if (object.has("options")) {
                                options = object.getJSONArray("options");
                            }
                            questionItem = new QuestionItem(id, name, point, type, options);
                            questionItemList.add(questionItem);
                        }

                        Log.d("questionItemList==>>", "" + questionItemList.size());
                        LayoutInflater lf = getLayoutInflater().from(QuestionnaireActivity.this);
                        viewArrayList = new ArrayList<View>();
                        for (int j = 0; j < questionItemList.size(); j++) {
                            View view = lf.inflate(R.layout.do_exercise_item, null);
                            viewArrayList.add(view);
                        }
                        tv_one_count.setText(1 + "/" + questionItemList.size());
                        viewPager.setCurrentItem(0);
                        viewPager.setOffscreenPageLimit(viewArrayList.size());
                        viewPager.setAdapter(new PagerAdapter() {
                            @Override
                            public int getCount() {
                                return viewArrayList.size();
                            }

                            @Override
                            public boolean isViewFromObject(View view, Object object) {
                                return view == object;
                            }

                            @Override
                            public Object instantiateItem(ViewGroup container, final int position) {
                                view = viewArrayList.get(position);
                                container.addView(view);

                                TextView tv_type = view.findViewById(R.id.tv_type);
                                if (questionItemList.get(position).getType() == 1) {
                                    tv_type.setText("单选题");
                                } else if (questionItemList.get(position).getType() == 2) {
                                    tv_type.setText("多选题");
                                } else {
                                    tv_type.setText("说明");
                                }
                                item_tv_name = view.findViewById(R.id.tv_name);
                                item_tv_a_content = view.findViewById(R.id.tv_a_content);
                                item_tv_b_content = view.findViewById(R.id.tv_b_content);
                                item_tv_c_content = view.findViewById(R.id.tv_c_content);
                                item_tv_d_content = view.findViewById(R.id.tv_d_content);

                                item_ll_a = view.findViewById(R.id.ll_a);
                                item_ll_b = view.findViewById(R.id.ll_b);
                                item_ll_c = view.findViewById(R.id.ll_c);
                                item_ll_d = view.findViewById(R.id.ll_d);

                                item_tv_a = view.findViewById(R.id.tv_a);
                                item_tv_a.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                                    @Override
                                    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                                        if (questionItemList.get(position).getType() == 1) {
                                            if (b == true) {
                                                item_tv_a.setChecked(true);
                                                item_tv_b.setChecked(false);
                                                item_tv_c.setChecked(false);
                                                item_tv_d.setChecked(false);
                                            }
                                        } else if (questionItemList.get(position).getType() == 2) {
                                            if (b == true) {
                                                item_tv_a.setChecked(true);
                                            }
                                        }
                                    }

                                });
                                item_tv_b = view.findViewById(R.id.tv_b);
                                item_tv_b.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()

                                {
                                    @Override
                                    public void onCheckedChanged(CompoundButton compoundButton,
                                                                 boolean b) {
                                        if (questionItemList.get(position).getType() == 1) {
                                            if (b == true) {
                                                item_tv_a.setChecked(false);
                                                item_tv_b.setChecked(true);
                                                item_tv_c.setChecked(false);
                                                item_tv_d.setChecked(false);
                                            }
                                        } else if (questionItemList.get(position).getType() == 2) {
                                            if (b == true) {
                                                item_tv_b.setChecked(true);
                                            }

                                        }
                                    }
                                });
                                item_tv_c = view.findViewById(R.id.tv_c);
                                item_tv_c.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()

                                {
                                    @Override
                                    public void onCheckedChanged(CompoundButton compoundButton,
                                                                 boolean b) {
                                        if (questionItemList.get(position).getType() == 1) {
                                            if (b == true) {
                                                item_tv_a.setChecked(false);
                                                item_tv_b.setChecked(false);
                                                item_tv_c.setChecked(true);
                                                item_tv_d.setChecked(false);

                                            }
                                        } else if (questionItemList.get(position).getType() == 2) {

                                            if (b == true) {
                                                item_tv_c.setChecked(true);
                                            }
                                        }
                                    }
                                });
                                item_tv_d = view.findViewById(R.id.tv_d);
                                item_tv_d.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()

                                {
                                    @Override
                                    public void onCheckedChanged(CompoundButton compoundButton,
                                                                 boolean b) {
                                        if (questionItemList.get(position).getType() == 1) {
                                            if (b == true) {
                                                item_tv_a.setChecked(false);
                                                item_tv_b.setChecked(false);
                                                item_tv_c.setChecked(false);
                                                item_tv_d.setChecked(true);

                                            }
                                        } else if (questionItemList.get(position).getType() == 2) {

                                            if (b == true) {
                                                item_tv_d.setChecked(true);
                                            }
                                        }
                                    }
                                });

                                item_ll_first = view.findViewById(R.id.ll_first);
                                item_ll_two = view.findViewById(R.id.ll_two);

                                item_one_net = view.findViewById(R.id.one_net);
                                item_one_net.setOnClickListener(new View.OnClickListener()

                                {
                                    @Override
                                    public void onClick(View view) {
                                        if (questionItemList.get(position).getType() == 1) {//单选
                                            if (item_tv_a.isChecked()) {
                                                doGetOneChooseAnswer(position, 0);
                                            } else if (item_tv_b.isChecked()) {
                                                doGetOneChooseAnswer(position, 1);
                                            } else if (item_tv_c.isChecked()) {
                                                doGetOneChooseAnswer(position, 2);
                                            } else if (item_tv_d.isChecked()) {
                                                doGetOneChooseAnswer(position, 3);
                                            } else {
                                                if (answerList.size() == position + 1) {
                                                    answerList.remove(position);
                                                }
                                            }
                                            if (answerList.size() != position + 1) {
                                                Toast.makeText(QuestionnaireActivity.this, "请选择答案", Toast.LENGTH_SHORT).show();
                                                return;
                                            }
                                            Log.d("answerList==>>", answerList.get(position).getOptionsId());
                                            if (questionItemList.size() == 1) {
                                                doCommit(answerList);
                                            } else {
                                                tv_one_count.setText(position + 2 + "/" + questionItemList.size());
                                                viewPager.setCurrentItem(position + 1);
                                            }

                                        } else if (questionItemList.get(position).getType() == 2) {
                                            if (answerList.size() == position + 1) {
                                                answerList.remove(position);
                                            }
                                            if (item_tv_a.isChecked()) {
                                                doGetMoreChooseAnswer(position, 0);
                                            }

                                            if (item_tv_b.isChecked()) {
                                                doGetMoreChooseAnswer(position, 1);
                                            }
                                            if (item_tv_c.isChecked()) {
                                                doGetMoreChooseAnswer(position, 2);
                                            }
                                            if (item_tv_d.isChecked()) {
                                                doGetMoreChooseAnswer(position, 3);
                                            }
                                            if (!item_tv_a.isChecked() && !item_tv_b.isChecked() && !item_tv_c.isChecked() && !item_tv_d.isChecked()) {
                                                if (answerList.size() == position + 1) {
                                                    answerList.remove(position);
                                                }
                                            }
                                            if (answerList.size() != position + 1) {
                                                Toast.makeText(QuestionnaireActivity.this, "请选择答案", Toast.LENGTH_SHORT).show();
                                                return;
                                            }
                                            Log.d("answerList==>>", answerList.get(position).getOptionsId());
                                            if (questionItemList.size() == 1) {
                                                doCommit(answerList);
                                            } else {
                                                tv_one_count.setText(position + 2 + "/" + questionItemList.size());
                                                viewPager.setCurrentItem(position + 1);
                                            }

                                        } else {
                                            answerBean = new AnswerBean("", "");
                                            answerList.add(answerBean);
                                            if (questionItemList.size() == 1) {
                                            } else {
                                                tv_one_count.setText(position + 2 + "/" + questionItemList.size());
                                                viewPager.setCurrentItem(position + 1);
                                            }
                                        }

                                    }
                                });
                                item_tv_upq = view.findViewById(R.id.tv_upq);
                                item_tv_upq.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        tv_one_count.setText(position + "/" + questionItemList.size());
                                        viewPager.setCurrentItem(position - 1);
                                    }
                                });
                                item_tv_nextq = view.findViewById(R.id.tv_nextq);
                                item_tv_nextq.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        if (questionItemList.get(position).getType() == 1) {//单选
                                            if (item_tv_a.isChecked()) {
                                                doGetOneChooseAnswer(position, 0);
                                            } else if (item_tv_b.isChecked()) {
                                                doGetOneChooseAnswer(position, 1);
                                            } else if (item_tv_c.isChecked()) {
                                                doGetOneChooseAnswer(position, 2);
                                            } else if (item_tv_d.isChecked()) {
                                                doGetOneChooseAnswer(position, 3);
                                            } else {
                                                if (answerList.size() == position + 1) {
                                                    answerList.remove(position);
                                                }
                                            }

                                        } else if (questionItemList.get(position).getType() == 2) {
                                            if (answerList.size() == position + 1) {
                                                answerList.remove(position);
                                            }
                                            if (item_tv_a.isChecked()) {
                                                doGetMoreChooseAnswer(position, 0);
                                            }

                                            if (item_tv_b.isChecked()) {
                                                doGetMoreChooseAnswer(position, 1);
                                            }
                                            if (item_tv_c.isChecked()) {
                                                doGetMoreChooseAnswer(position, 2);
                                            }
                                            if (item_tv_d.isChecked()) {
                                                doGetMoreChooseAnswer(position, 3);
                                            }
                                            if (!item_tv_a.isChecked() && !item_tv_b.isChecked() && !item_tv_c.isChecked() && !item_tv_d.isChecked()) {
                                                if (answerList.size() == position + 1) {
                                                    answerList.remove(position);
                                                }
                                            }
                                        } else {
                                            answerBean = new AnswerBean("", "");
                                            answerList.add(answerBean);
                                        }
                                        Log.d("answerList==>>", answerList.get(position).getOptionsId());
                                        if (answerList.size() != position + 1) {
                                            Toast.makeText(QuestionnaireActivity.this, "请选择答案", Toast.LENGTH_SHORT).show();
                                            return;
                                        }
                                        if (position + 1 == questionItemList.size()) {
                                            doCommit(answerList);
                                        } else {
                                            tv_one_count.setText(position + 2 + "/" + questionItemList.size());
                                            viewPager.setCurrentItem(position + 1);
                                        }
                                    }


                                });

                                item_tv_name.setText((position + 1) + "、" + questionItemList.get(position).getName());
                                item_ll_choose_item = view.findViewById(R.id.ll_choose_item);
                                if (questionItemList.get(position).
                                        getOptions() == null) {
                                    item_ll_choose_item.setVisibility(View.INVISIBLE);
                                    item_ll_first.setVisibility(View.INVISIBLE);
                                    item_ll_two.setVisibility(View.VISIBLE);
                                    if (questionItemList.size() == 1) {
                                        item_ll_two.setVisibility(View.INVISIBLE);
                                        item_ll_first.setVisibility(View.VISIBLE);
                                        item_one_net.setText("提交");
                                    }
                                } else

                                {
                                    item_ll_choose_item.setVisibility(View.VISIBLE);
                                    JSONArray array = questionItemList.get(position).getOptions();
                                    try {
                                        switch (array.length()) {
                                            case 1:
                                                item_ll_a.setVisibility(View.VISIBLE);
                                                item_tv_a_content.setText(array.getJSONObject(0).getString("name"));
                                                break;
                                            case 2:
                                                item_ll_a.setVisibility(View.VISIBLE);
                                                item_ll_b.setVisibility(View.VISIBLE);
                                                item_tv_a_content.setText(array.getJSONObject(0).getString("name"));
                                                item_tv_b_content.setText(array.getJSONObject(1).getString("name"));
                                                break;
                                            case 3:
                                                item_ll_a.setVisibility(View.VISIBLE);
                                                item_ll_b.setVisibility(View.VISIBLE);
                                                item_ll_c.setVisibility(View.VISIBLE);
                                                item_tv_a_content.setText(array.getJSONObject(0).getString("name"));
                                                item_tv_b_content.setText(array.getJSONObject(1).getString("name"));
                                                item_tv_c_content.setText(array.getJSONObject(2).getString("name"));
                                                break;
                                            case 4:
                                                item_ll_a.setVisibility(View.VISIBLE);
                                                item_ll_b.setVisibility(View.VISIBLE);
                                                item_ll_c.setVisibility(View.VISIBLE);
                                                item_ll_d.setVisibility(View.VISIBLE);
                                                item_tv_a_content.setText(array.getJSONObject(0).getString("name"));
                                                item_tv_b_content.setText(array.getJSONObject(1).getString("name"));
                                                item_tv_c_content.setText(array.getJSONObject(2).getString("name"));
                                                item_tv_d_content.setText(array.getJSONObject(3).getString("name"));
                                                break;
                                        }

                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                    if (position == 0) {
                                        item_ll_first.setVisibility(View.VISIBLE);
                                        item_ll_two.setVisibility(View.INVISIBLE);
                                    } else if (position == questionItemList.size() - 1) {
                                        item_ll_first.setVisibility(View.INVISIBLE);
                                        item_ll_two.setVisibility(View.VISIBLE);
                                        item_tv_nextq.setText("提交");
                                    } else {
                                        item_ll_first.setVisibility(View.INVISIBLE);
                                        item_ll_two.setVisibility(View.VISIBLE);
                                    }
                                    if (questionItemList.size() == 1) {
                                        item_ll_first.setVisibility(View.VISIBLE);
                                        item_ll_two.setVisibility(View.INVISIBLE);
                                        item_one_net.setText("提交");
                                    }
                                }


                                return view;
                            }

                            @Override
                            public void destroyItem(ViewGroup container, int position, Object
                                    object) {
                                container.removeView((View) object);
                            }
                        });
                    } else {
                        Toast.makeText(QuestionnaireActivity.this, response.getString("message"), Toast.LENGTH_LONG).show();
                        return;
                    }
                } catch (
                        JSONException e)

                {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable
                    throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                Toast.makeText(QuestionnaireActivity.this, R.string.no_network, Toast.LENGTH_LONG).show();
                return;
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String
                    responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                Toast.makeText(QuestionnaireActivity.this, R.string.no_network, Toast.LENGTH_LONG).show();
                return;
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable
                    throwable, JSONArray errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                Toast.makeText(QuestionnaireActivity.this, R.string.no_network, Toast.LENGTH_LONG).show();
                return;
            }
        });
    }

    private void doGetMoreChooseAnswer(int position, int i) {
        JSONArray array = questionItemList.get(position).getOptions();
        try {
            JSONObject jsonObject = array.getJSONObject(i);
            if (answerList.size() != position + 1) {
                answerBean = new AnswerBean(questionItemList.get(position).getId(), jsonObject.getString("id"));
                answerList.add(answerBean);
            } else {
                String a = answerList.get(position).getOptionsId() + "|" + jsonObject.getString("id");
                answerBean = new AnswerBean(questionItemList.get(position).getId(), a);
                answerList.set(position, answerBean);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void doGetOneChooseAnswer(int position, int i) {
        JSONArray array = questionItemList.get(position).getOptions();
        try {
            JSONObject jsonObject = array.getJSONObject(i);
            if (answerList.size() != position + 1) {
                answerBean = new AnswerBean(questionItemList.get(position).getId(), jsonObject.getString("id"));
                answerList.add(answerBean);
            } else {
                answerBean = new AnswerBean(questionItemList.get(position).getId(), jsonObject.getString("id"));
                answerList.set(position, answerBean);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void doCommit(List<AnswerBean> answerList) {
        JSONArray array = Utils.ListToArray(answerList);
        if (!NetworkUtils.checkNetWork(QuestionnaireActivity.this)) {
            Toast.makeText(QuestionnaireActivity.this, R.string.no_network, Toast.LENGTH_LONG).show();
            return;
        }
        String getUrl = getResources().getString(R.string.local_url) + "/v1/achievement/question/update/" + achieveId;
        SharedPreferences sharedPreferences = getSharedPreferences("myinfo", MODE_PRIVATE);
        String token = sharedPreferences.getString("token", "");
        Log.d("url==>>", getUrl);
        AsyncHttpClient client = new AsyncHttpClient();
        client.addHeader("X-Api-Token", token);
        RequestParams params = new RequestParams();
        params.put("content", array.toString());
        client.put(getUrl, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                Log.d("response==>>>", response.toString());
                try {
                    int code = response.getInt("code");
                    if (code == 0) {
                        Toast.makeText(QuestionnaireActivity.this, "提交成功", Toast.LENGTH_LONG).show();
                        finish();
                    } else {
                        Toast.makeText(QuestionnaireActivity.this, response.getString("message"), Toast.LENGTH_LONG).show();
                        return;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable
                    throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                Toast.makeText(QuestionnaireActivity.this, R.string.no_network, Toast.LENGTH_LONG).show();
                return;
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String
                    responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                Toast.makeText(QuestionnaireActivity.this, R.string.no_network, Toast.LENGTH_LONG).show();
                return;
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable
                    throwable, JSONArray errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                Toast.makeText(QuestionnaireActivity.this, R.string.no_network, Toast.LENGTH_LONG).show();
                return;
            }
        });


    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
        }
    }
}

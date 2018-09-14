package com.mpl.GrowthStud.Parent.Activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.mpl.GrowthStud.R;
import com.mpl.GrowthStud.Student.Activity.MessageActivity;
import com.mpl.GrowthStud.Student.Activity.SettingActivity;
import com.mpl.GrowthStud.Student.Bean.StudentInfo;
import com.mpl.GrowthStud.Student.Tools.NetworkUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

import static android.content.Context.MODE_PRIVATE;


public class PMyFragment extends Fragment implements View.OnClickListener {
    private TextView tv_parent_name, tv_bundle_chlid, tv_grade, tv_class, tv_section, tv_teacher, tv_in_school;
    private LinearLayout ll_set;
    private ImageView head_img, iv_gender;
    private String parent_account, parent_username, parent_mobile;
    private int parent_gender;
    private String student_user_id;
    private String student_username;
    private String student_school_name;
    private String student_classroom_name;
    private String student_grade;
    private String student_teacher_name;
    private String student_birthday;
    private int student_gender;
    private int student_scope;
    private String student_account;
    private JSONArray student = null;
    private ImageView ib_message;

    public PMyFragment() {
        // Required empty public constructor
    }


    public static PMyFragment newInstance(String param1) {
        PMyFragment fragment = new PMyFragment();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_pmy, container, false);
        initView(root);
        getParentInfo();
//        initData();
        // Inflate the layout for this fragment
        return root;
    }

    private void getParentInfo() {
        if (NetworkUtils.checkNetWork(getActivity()) == false) {
            Toast.makeText(getActivity(), R.string.no_network, Toast.LENGTH_LONG).show();
            return;
        }
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("myinfo", MODE_PRIVATE);
        String token = sharedPreferences.getString("token", "");
        String url = getResources().getString(R.string.local_url) + "/user/parent-info";
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
                        parent_account = data.getString("account");
                        parent_username = data.getString("username");
                        parent_gender = data.getInt("gender");
                        parent_mobile = data.getString("mobile");
                        if (data.has("student")) {
                            student = data.getJSONArray("student");
                            if (student.length() > 0) {
                                JSONObject object = student.getJSONObject(0);
                                student_user_id = object.getString("user_id");
                                student_username = object.getString("username");
                                if (object.has("info")) {
                                    JSONObject sinfo = object.getJSONObject("info");
                                    student_school_name = sinfo.getString("school_name");
                                    student_classroom_name = sinfo.getString("classroom_name");
                                    student_grade = sinfo.getString("grade");
                                    student_teacher_name = sinfo.getString("teacher_name");
                                    student_birthday = sinfo.getString("birthday");
                                    student_gender = sinfo.getInt("gender");
                                    student_scope = sinfo.getInt("scope");
                                    student_account = sinfo.getString("account");
                                }
                            }
                        }
                        Message message = new Message();
                        message.what = 1;
                        handler.sendMessage(message);

                    } else {
                        Toast.makeText(getActivity(), response.getString("message"), Toast.LENGTH_LONG).show();
                        return;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                Toast.makeText(getActivity(), R.string.no_network, Toast.LENGTH_LONG).show();
                return;
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                Toast.makeText(getActivity(), R.string.no_network, Toast.LENGTH_LONG).show();
                return;
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                Toast.makeText(getActivity(), R.string.no_network, Toast.LENGTH_LONG).show();
                return;
            }
        });
    }

    private Handler handler = new Handler() {
        @SuppressLint("NewApi")
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    tv_parent_name.setText(parent_username);
                    if (parent_gender == 1) {
                        iv_gender.setBackground(getActivity().getResources().getDrawable(R.mipmap.small_boy_img));
                    } else {
                        iv_gender.setBackground(getActivity().getResources().getDrawable(R.mipmap.small_girl_img));
                    }
                    if (student.length() > 0) {
                        tv_bundle_chlid.setText(student_username);
                        tv_grade.setText(student_grade);
                        tv_class.setText(student_classroom_name);
                        if (student_scope == 2) {
                            tv_section.setText("小学");
                        } else if (student_scope == 3) {
                            tv_section.setText("初中");
                        } else if (student_scope == 4) {
                            tv_section.setText("高中");
                        }
                        tv_teacher.setText(student_teacher_name);
                        tv_in_school.setText(student_school_name);
                    }
                    break;
            }
        }
    };

    private void initView(View root) {
        ib_message = root.findViewById(R.id.ib_message);
        ib_message.setOnClickListener(this);
        head_img = root.findViewById(R.id.head_img);
        tv_parent_name = root.findViewById(R.id.tv_parent_name);
        iv_gender = root.findViewById(R.id.iv_gender);
        tv_bundle_chlid = root.findViewById(R.id.tv_bundle_chlid);
        tv_bundle_chlid.setOnClickListener(this);
        tv_grade = root.findViewById(R.id.tv_grade);
        tv_class = root.findViewById(R.id.tv_class);
        tv_section = root.findViewById(R.id.tv_section);
        tv_teacher = root.findViewById(R.id.tv_teacher);
        tv_in_school = root.findViewById(R.id.tv_in_school);
        ll_set = root.findViewById(R.id.ll_set);
        ll_set.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ib_message:
                Intent intent3 = new Intent(getActivity(), MessageActivity.class);
                startActivity(intent3);
                break;
            case R.id.tv_bundle_chlid:
                break;
            case R.id.ll_set:
                Intent intent = new Intent(getActivity(), PSettingActivity.class);
                startActivity(intent);
                break;
        }
    }
}

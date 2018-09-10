package com.mpl.GrowthStud.Student.Activity;

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
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.mpl.GrowthStud.R;
import com.mpl.GrowthStud.Student.Bean.StudentInfo;
import com.mpl.GrowthStud.Student.Tools.NetworkUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

import static android.content.Context.MODE_PRIVATE;

public class MyFragment extends Fragment implements View.OnClickListener {
    private TextView tv_student_name, tv_school, tv_grade, tv_brithday, tv_school_grade, tv_parent_id, tv_teacher, tv_in_school;
    private LinearLayout ll_set;
    private String userName, classroomName, grade, teacherName, birthday, gender, scope, schoolName;
    private StudentInfo studentInfo;

    public static MyFragment newInstance(String param1) {
        MyFragment fragment = new MyFragment();
        Bundle args = new Bundle();
        args.putString("name", param1);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_my, container, false);
        initView(root);
        initData();
        // Inflate the layout for this fragment
        return root;
    }

    private void initData() {
        if (NetworkUtils.checkNetWork(getActivity()) == false) {
            Toast.makeText(getActivity(), R.string.no_network, Toast.LENGTH_LONG).show();
            return;
        }
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("myinfo", MODE_PRIVATE);
        String token = sharedPreferences.getString("token", "");
        String url = getResources().getString(R.string.local_url) + "/user/student-info";
        AsyncHttpClient client = new AsyncHttpClient();
        client.addHeader("X-Api-Token", token);
        client.get(url, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                Log.d("response==>>", response.toString());
                try {
                    int code = response.getInt("code");
                    JSONObject data = response.getJSONObject("data");
                    if (code == 0) {
                        schoolName = data.getString("school_name");
                        classroomName = data.getString("classroom_name");
                        grade = data.getString("grade");
                        teacherName = data.getString("teacher_name");
                        birthday = data.getString("birthday");
                        gender = data.getString("gender");
                        scope = data.getString("scope");
                        userName = data.getString("username");
                        Message message = new Message();
                        message.what = 1;
                        handler.sendMessage(message);
                        studentInfo = new StudentInfo(schoolName, classroomName, grade, teacherName, birthday, gender, scope, userName);
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
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    tv_student_name.setText(userName);
                    tv_brithday.setText(birthday);
                    tv_school.setText(schoolName);
                    tv_grade.setText(grade);
                    if (scope.equals("2")) {
                        tv_school_grade.setText("小学");
                    } else if (scope.equals("1")) {
                        tv_school_grade.setText("幼儿园");
                    }
                    tv_teacher.setText(teacherName);
                    tv_in_school.setText(schoolName);
                    if (gender.equals("1")) {
                        tv_student_name.setCompoundDrawables(null, null, null, getActivity().getResources().getDrawable(R.mipmap.small_boy_img));
                    } else {
                        tv_student_name.setCompoundDrawables(null, null, null, getActivity().getResources().getDrawable(R.mipmap.small_girl_img));
                    }
                    break;
            }
        }
    };

    private void initView(View root) {
        tv_student_name = root.findViewById(R.id.tv_student_name);

        tv_school = root.findViewById(R.id.tv_school);

        tv_grade = root.findViewById(R.id.tv_grade);

        tv_brithday = root.findViewById(R.id.tv_brithday);
        tv_brithday.setOnClickListener(this);

        tv_school_grade = root.findViewById(R.id.tv_school_grade);

        tv_parent_id = root.findViewById(R.id.tv_parent_id);
        tv_parent_id.setOnClickListener(this);

        tv_teacher = root.findViewById(R.id.tv_teacher);
        tv_in_school = root.findViewById(R.id.tv_in_school);
        ll_set = root.findViewById(R.id.ll_set);
        ll_set.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_brithday:
                break;
            case R.id.tv_parent_id:
                break;
            case R.id.ll_set:
                Intent intent = new Intent(getActivity(), SettingActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("scope", scope);
                intent.putExtras(bundle);
                startActivity(intent);
                break;
        }
    }
}

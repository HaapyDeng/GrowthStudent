package com.mpl.GrowthStud.Student.Activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.mpl.GrowthStud.R;
import com.mpl.GrowthStud.Student.Bean.ParentApplyListItem;
import com.mpl.GrowthStud.Student.Bean.StudentInfo;
import com.mpl.GrowthStud.Student.Tools.NetworkUtils;
import com.mpl.GrowthStud.Student.View.CircleImageView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import cz.msebera.android.httpclient.Header;

import static android.content.Context.MODE_PRIVATE;

/**
 * 个人模块
 */
public class MyFragment extends Fragment implements View.OnClickListener {
    private TextView tv_student_name, tv_school, tv_grade, tv_brithday, tv_school_grade, tv_parent_id, tv_teacher, tv_in_school;
    private LinearLayout ll_set, ll_parent_id, ll_brithday;
    private String userName, classroomName, grade, teacherName, birthday, schoolName;
    private int gender, scope;
    private StudentInfo studentInfo;
    private List<ParentApplyListItem> listItems;
    private ImageView iv_gender, head_img;
    private int tag = 0;
    private ImageView ib_message;
    private CircleImageView iv_dot;
    int mYear, mMonth, mDay;
    Calendar c;

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
        Calendar ca = Calendar.getInstance();
        mYear = ca.get(Calendar.YEAR);
        mMonth = ca.get(Calendar.MONTH);
        mDay = ca.get(Calendar.DAY_OF_MONTH);
        initView(root);
        getParentList();


        // Inflate the layout for this fragment
        return root;
    }

    /*
     *获取家长列表
     */
    private void getParentList() {
        if (NetworkUtils.checkNetWork(getActivity()) == false) {
            Toast.makeText(getActivity(), R.string.no_network, Toast.LENGTH_LONG).show();
            return;
        }
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("myinfo", MODE_PRIVATE);
        String token = sharedPreferences.getString("token", "");
        String url = getResources().getString(R.string.local_url) + "/user/get-audit";
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
                        JSONArray array = data.getJSONArray("audit");
                        listItems = new ArrayList<ParentApplyListItem>();
                        for (int i = 0; i < array.length(); i++) {
                            JSONObject object = array.getJSONObject(i);
                            int id = object.getInt("id");
                            String user_id = object.getString("user_id");
                            String parent_id = object.getString("parent_id");
                            String parent_username = object.getString("parent_username");
                            int role = object.getInt("role");
                            int status = object.getInt("status");
                            String content = object.getString("content");
                            String mobile = object.getString("mobile");
                            int gender = object.getInt("gender");
                            ParentApplyListItem parentApplyListItem = new ParentApplyListItem(id, user_id, parent_id, parent_username, role, status, content, mobile, gender);
                            listItems.add(parentApplyListItem);
                        }
                        initData();
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

    /**
     * 获取个人详细
     */
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
                    if (code == 0) {
                        JSONObject data = response.getJSONObject("data");
                        schoolName = data.getString("school_name");
                        classroomName = data.getString("classroom_name");
                        grade = data.getString("grade");
                        teacherName = data.getString("teacher_name");
                        if (data.getString("birthday").equals("null")) {
                            birthday = "";
                        } else {
                            birthday = data.getString("birthday");
                        }

                        gender = data.getInt("gender");
                        scope = data.getInt("scope");
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
        @SuppressLint("NewApi")
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    tv_student_name.setText(userName);
                    if (birthday.equals("")) {

                    } else {
                        tv_brithday.setText(birthday.substring(0, birthday.indexOf(" ")));
                    }
                    tv_school.setText(schoolName);
                    tv_grade.setText(grade);
                    if (scope == 2) {
                        tv_school_grade.setText("小学");
                    } else if (scope == 1) {
                        tv_school_grade.setText("幼儿园");
                    } else if (scope == 3) {
                        tv_school_grade.setText("初中");
                    } else if (scope == 4) {
                        tv_school_grade.setText("高中");
                    }
                    for (int i = 0; i < listItems.size(); i++) {
                        int status = listItems.get(i).getStatus();
                        if (status == 1) {
                            tag = 1;
                        }
                    }
                    if (tag == 1) {
                        iv_dot.setVisibility(View.VISIBLE);
                        tv_parent_id.setText("新的请求");
                    }
                    tv_teacher.setText(teacherName);
                    tv_in_school.setText(schoolName);
                    if (gender == 1) {
                        head_img.setImageDrawable(getActivity().getResources().getDrawable(R.mipmap.head_student_boy));
                        iv_gender.setBackground(getActivity().getResources().getDrawable(R.mipmap.small_boy_img));
                    } else {
                        head_img.setImageDrawable(getActivity().getResources().getDrawable(R.mipmap.head_student_girl));
                        iv_gender.setBackground(getActivity().getResources().getDrawable(R.mipmap.small_girl_img));
                    }
                    break;
                case 2:
                    tv_brithday.setText(DateFormat.format("yyy-MM-dd", c));
                    break;
            }
        }
    };

    private void initView(View root) {
        ib_message = root.findViewById(R.id.ib_message);
        ib_message.setOnClickListener(this);

        head_img = root.findViewById(R.id.head_img);

        tv_student_name = root.findViewById(R.id.tv_student_name);

        iv_gender = root.findViewById(R.id.iv_gender);

        tv_school = root.findViewById(R.id.tv_school);

        tv_grade = root.findViewById(R.id.tv_grade);

        tv_brithday = root.findViewById(R.id.tv_brithday);

        ll_brithday = root.findViewById(R.id.ll_brithday);
        ll_brithday.setOnClickListener(this);

        tv_school_grade = root.findViewById(R.id.tv_school_grade);

        tv_parent_id = root.findViewById(R.id.tv_parent_id);

        iv_dot = root.findViewById(R.id.iv_dot);

        ll_parent_id = root.findViewById(R.id.ll_parent_id);
        ll_parent_id.setOnClickListener(this);

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
            case R.id.ll_brithday:
                c = Calendar.getInstance();
                DatePickerDialog dialog = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        c.set(year, monthOfYear, dayOfMonth);
                        Log.d("data>>>", "" + DateFormat.format("yyy-MM-dd", c));
                        doUpDateBirthday(DateFormat.format("yyy-MM-dd", c));
                    }
                }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH));
                dialog.show();
                break;
            case R.id.ll_parent_id:
                Intent intent2 = new Intent(getActivity(), ParentsAccountActivity.class);
                startActivity(intent2);
                break;
            case R.id.ll_set:
                Intent intent = new Intent(getActivity(), SettingActivity.class);
                Bundle bundle = new Bundle();
                bundle.putInt("scope", scope);
                intent.putExtras(bundle);
                startActivity(intent);
                break;
        }

    }

    /**
     * 更新生日
     *
     * @param format
     */
    private void doUpDateBirthday(CharSequence format) {
        if (NetworkUtils.checkNetWork(getActivity()) == false) {
            Toast.makeText(getActivity(), R.string.no_network, Toast.LENGTH_LONG).show();
            return;
        }
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("myinfo", MODE_PRIVATE);
        String token = sharedPreferences.getString("token", "");
        String url = getResources().getString(R.string.local_url) + "/user/update-day/" + format;
        AsyncHttpClient client = new AsyncHttpClient();
        client.addHeader("X-Api-Token", token);
        client.get(url, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                Log.d("respomse==>>", response.toString());
                try {
                    int code = response.getInt("code");
                    if (code == 0) {
                        Message message = new Message();
                        message.what = 2;
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

}

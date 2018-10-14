package com.mpl.GrowthStud.Student.Activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.mpl.GrowthStud.Student.Adapter.AchieveToDoListViewAdapter;
import com.mpl.GrowthStud.Student.Bean.AchieveToDoItem;
import com.mpl.GrowthStud.R;
import com.mpl.GrowthStud.Student.Tools.NetworkUtils;
import com.mpl.GrowthStud.Student.View.LoadMoreListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;

import static android.content.Context.MODE_PRIVATE;


public class AchieveTodoFragment extends Fragment implements AdapterView.OnItemClickListener, SwipeRefreshLayout.OnRefreshListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private LoadMoreListView listView;
    private List<AchieveToDoItem> mDatas = new ArrayList<AchieveToDoItem>();
    ;
    private AchieveToDoListViewAdapter achieveToDoListViewAdapter;
    private LinearLayout ll_empty;
    private SwipeRefreshLayout mSwipeLayout;
    private boolean isRefresh = false;//是否刷新中
    private String currentPage = "1";
    private int totalPage;

    public AchieveTodoFragment() {
        // Required empty public constructor
    }

    public static AchieveTodoFragment newInstance(String param1, String param2) {
        AchieveTodoFragment fragment = new AchieveTodoFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
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
        View root = inflater.inflate(R.layout.fragment_achieve_todo, container, false);
        listView = root.findViewById(R.id.listview);
        ll_empty = root.findViewById(R.id.ll_empty);
        listView.setOnItemClickListener(this);
        getTodoAchieve(currentPage);
        // Inflate the layout for this fragment

        //设置SwipeRefreshLayout
        mSwipeLayout = (SwipeRefreshLayout) root.findViewById(R.id.swipeLayout);
        //设置进度条的颜色主题，最多能设置四种 加载颜色是循环播放的，只要没有完成刷新就会一直循环
        mSwipeLayout.setColorSchemeColors(Color.RED,
                Color.RED,
                Color.RED,
                Color.RED);
        // 设置手指在屏幕下拉多少距离会触发下拉刷新
        mSwipeLayout.setDistanceToTriggerSync(300);
        // 设定下拉圆圈的背景
        mSwipeLayout.setProgressBackgroundColorSchemeColor(Color.WHITE);
        mSwipeLayout.setTag("下拉刷新");
        // 设置圆圈的大小
        mSwipeLayout.setSize(SwipeRefreshLayout.LARGE);

        //设置下拉刷新的监听
        mSwipeLayout.setOnRefreshListener(this);

        listView.setOnLoadMoreListener(new LoadMoreListView.OnLoadMoreListener() {
            @Override
            public void onloadMore() {
                int i = Integer.parseInt(currentPage);
                Log.d("i==>>", "" + i);
                if (i < totalPage) {
                    getTodoAchieve("" + (i + 1));
                } else {
                    listView.setLoadCompleted();
                }
            }
        });
        return root;
    }

    @Override
    public void onRefresh() {
//检查是否处于刷新状态
        if (!isRefresh) {
            isRefresh = true;
            //模拟加载网络数据，这里设置4秒，正好能看到4色进度条
            new Handler().postDelayed(new Runnable() {
                public void run() {

                    //显示或隐藏刷新进度条
                    mSwipeLayout.setRefreshing(false);
                    if (mDatas.size() > 0) {
                        mDatas.clear();
                    }
                    //修改adapter的数据
                    getTodoAchieve("1");
                    achieveToDoListViewAdapter.notifyDataSetChanged();
                    isRefresh = false;
                }
            }, 3000);
        }

    }

    private void getTodoAchieve(String page) {
        if (!NetworkUtils.checkNetWork(getActivity())) {
            Toast.makeText(getActivity(), R.string.no_network, Toast.LENGTH_LONG).show();
            return;
        }
        SharedPreferences sharedPreferences = this.getActivity().getSharedPreferences("myinfo", MODE_PRIVATE);
        String token = sharedPreferences.getString("token", "");
        String uid = sharedPreferences.getString("userid", "");
        String url = getResources().getString(R.string.local_url) + "/v1/achievement/" + "1/" + uid + "?page=" + page;
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
                        JSONObject data = response.getJSONObject("data");
                        totalPage = data.getInt("totalPage");
                        JSONArray list = data.getJSONArray("list");
                        if (list.length() == 0) {
                            Message message = new Message();
                            message.what = 1;
                            handler.sendMessage(message);
                        }

                        for (int i = 0; i < list.length(); i++) {
                            JSONObject object = list.getJSONObject(i);
                            String id = object.getString("id");
                            String name = object.getString("name");
                            String type = object.getString("type");
                            String image = object.getString("image");
                            String category_name = object.getString("category_name");
                            String label_name = object.getString("label_name");
                            String task_star = object.getString("task_star");
                            String status = object.getString("status");
                            String role = object.getString("role");
                            String star = object.getString("star");
                            AchieveToDoItem achieveToDoItem = new AchieveToDoItem(id, name, type, image, category_name, label_name, task_star, status, role, star);
                            mDatas.add(achieveToDoItem);
                        }
                        achieveToDoListViewAdapter = new AchieveToDoListViewAdapter(getActivity(), mDatas);
                        listView.setAdapter(achieveToDoListViewAdapter);
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
                    ll_empty.setVisibility(View.VISIBLE);
                    break;
            }
        }
    };

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (mDatas.get(position).getType().equals("1")) {
            Intent intent = new Intent(getActivity(), WenziActivity.class);
            Bundle bundle = new Bundle();
            bundle.putString("achieveid", mDatas.get(position).getId());
            bundle.putString("headtitle", mDatas.get(position).getName());
            intent.putExtras(bundle);
            startActivity(intent);
        } else if (mDatas.get(position).getType().equals("2")) {
            Intent intent = new Intent(getActivity(), TuWenActivity.class);
            Bundle bundle = new Bundle();
            bundle.putString("achieveid", mDatas.get(position).getId());
            bundle.putString("headtitle", mDatas.get(position).getName());
            intent.putExtras(bundle);
            startActivity(intent);
        } else if (mDatas.get(position).getType().equals("3")) {
            Intent intent = new Intent(getActivity(), VideoActivity.class);
            Bundle bundle = new Bundle();
            bundle.putString("achieveid", mDatas.get(position).getId());
            bundle.putString("headtitle", mDatas.get(position).getName());
            intent.putExtras(bundle);
            startActivity(intent);
        } else if (mDatas.get(position).getType().equals("4")) {
            Intent intent = new Intent(getActivity(), QuestionnaireActivity.class);
            Bundle bundle = new Bundle();
            bundle.putString("achieveid", mDatas.get(position).getId());
            bundle.putString("headtitle", mDatas.get(position).getName());
            intent.putExtras(bundle);
            startActivity(intent);
        } else if (mDatas.get(position).getType().equals("5")) {
//            Intent intent = new Intent(getActivity(), SyatemAchieveActivity.class);
//            Bundle bundle = new Bundle();
//            bundle.putString("achieveid", mDatas.get(position).getId());
//            bundle.putString("headtitle", mDatas.get(position).getName());
//            intent.putExtras(bundle);
//            startActivity(intent);
        }
    }
}

package com.mpl.GrowthStud.Student.Activity;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.mpl.GrowthStud.Student.Adapter.AchieveToDoListViewAdapter;
import com.mpl.GrowthStud.Student.Bean.AchieveToDoItem;
import com.mpl.GrowthStud.R;
import com.mpl.GrowthStud.Student.Tools.NetworkUtils;
import com.mpl.GrowthStud.Student.View.LoadMoreListView;
import com.mpl.GrowthStud.Student.View.LoadingDialog;
import com.mpl.GrowthStud.Student.View.XListView;
import com.mpl.GrowthStud.Student.View.XListViewFooter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;

import static android.content.Context.MODE_PRIVATE;

/**
 * 待完成成就fragment容器
 */
public class AchieveTodoFragment extends Fragment implements AdapterView.OnItemClickListener, XListView.IXListViewListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private XListView listView;
    private List<AchieveToDoItem> mDatas = new ArrayList<AchieveToDoItem>();
    ;
    private AchieveToDoListViewAdapter achieveToDoListViewAdapter;
    private LinearLayout ll_empty;
    private SwipeRefreshLayout mSwipeLayout;
    private boolean isRefresh = false;//是否刷新中
    private String currentPage = "1";
    private LoadingDialog loadingDialog;

    //定义一个页数
    private int totalPage = 0;
    private Handler mHandler;
    private int start = 0;
    private static int refreshCnt = 0;


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

    //fragment重新刷新的方法
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        LocalBroadcastManager broadcastManager = LocalBroadcastManager.getInstance(getActivity());
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.intent.action.CART_BROADCAST");
        BroadcastReceiver mItemViewListClickReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String msg = intent.getStringExtra("data");
                final int[] flag = {1};//返回刷新一下
                if ("refresh".equals(msg)) {
                    if (flag[0] == 1) {
                        Log.d("refresh==>>>", 111111 + "");
                        mDatas.clear();
                        getTodoAchieve("1");
                        flag[0] = flag[0] + 1;
                    }

                }
            }
        };
        broadcastManager.registerReceiver(mItemViewListClickReceiver, intentFilter);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_achieve_todo, container, false);

        listView = root.findViewById(R.id.listview);
        ll_empty = root.findViewById(R.id.ll_empty);
        listView.setOnItemClickListener(this);
        if (mDatas.size() > 0) {
            mDatas.clear();
        }
        getTodoAchieve(currentPage);
        listView.setPullLoadEnable(true);
        listView.setXListViewListener(this);

        mHandler = new Handler();
        return root;
    }

    private void onLoad() {
        listView.stopRefresh();
        listView.stopLoadMore();
        listView.setRefreshTime("刚刚");
    }

    @Override
    public void onRefresh() {
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                start = ++refreshCnt;
                mDatas.clear();
                getTodoAchieve("1");
                onLoad();
            }
        }, 2000);
    }

    @Override
    public void onLoadMore() {
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (Integer.parseInt(currentPage) < totalPage) {
                    int ye = 0;
                    ye = Integer.parseInt(currentPage) + 1;
                    getTodoAchieve("" + ye);
                    achieveToDoListViewAdapter.notifyDataSetChanged();
                    onLoad();
                } else {
                    onLoad();
                    XListViewFooter.setState(3);
                    Toast.makeText(getActivity(), "加载完成", Toast.LENGTH_SHORT).show();
                }
            }
        }, 2000);
    }


    private void getTodoAchieve(String page) {
        if (page.equals("1")) {
            mDatas.clear();
        }
        loadingDialog = new LoadingDialog(getContext(), "加载中...", R.drawable.ic_dialog_loading);
        loadingDialog.show();
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
                        loadingDialog.dismiss();
                        JSONObject data = response.getJSONObject("data");
                        totalPage = data.getInt("totalPage");
                        currentPage = data.getString("currentPage");
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
                        if (getActivity() != null) {
                            Log.d("mdata:", String.valueOf(mDatas.size()));
                            achieveToDoListViewAdapter = new AchieveToDoListViewAdapter(getActivity(), mDatas);
                            listView.setAdapter(achieveToDoListViewAdapter);
                        }

                    } else {
                        loadingDialog.dismiss();
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
                loadingDialog.dismiss();
                Toast.makeText(getActivity(), R.string.no_network, Toast.LENGTH_LONG).show();
                return;
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                loadingDialog.dismiss();
                Toast.makeText(getActivity(), R.string.no_network, Toast.LENGTH_LONG).show();
                return;
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                loadingDialog.dismiss();
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
        position = position - 1;
        if (mDatas.get(position).getType().equals("1")) {//文字
            Intent intent = new Intent(getActivity(), WenziActivity.class);
            Bundle bundle = new Bundle();
            bundle.putString("achieveid", mDatas.get(position).getId());
            bundle.putString("headtitle", mDatas.get(position).getName());
            intent.putExtras(bundle);
            startActivityForResult(intent, 1);
        } else if (mDatas.get(position).getType().equals("2")) {//图文
            Intent intent = new Intent(getActivity(), TuWenActivity.class);
            Bundle bundle = new Bundle();
            bundle.putString("achieveid", mDatas.get(position).getId());
            bundle.putString("headtitle", mDatas.get(position).getName());
            intent.putExtras(bundle);
            startActivity(intent);
        } else if (mDatas.get(position).getType().equals("3")) {//视频
            Intent intent = new Intent(getActivity(), VideoActivity.class);
            Bundle bundle = new Bundle();
            bundle.putString("achieveid", mDatas.get(position).getId());
            bundle.putString("headtitle", mDatas.get(position).getName());
            intent.putExtras(bundle);
            startActivity(intent);
        } else if (mDatas.get(position).getType().equals("4")) {//问卷
            Intent intent = new Intent(getActivity(), QuestionnaireActivity.class);
            Bundle bundle = new Bundle();
            bundle.putString("achieveid", mDatas.get(position).getId());
            bundle.putString("headtitle", mDatas.get(position).getName());
            intent.putExtras(bundle);
            startActivity(intent);
        } else if (mDatas.get(position).getType().equals("5")) {//系统
            SharedPreferences sp = getActivity().getSharedPreferences("myinfo", MODE_PRIVATE);
            String scope = sp.getString("scope", "");
            if (scope.equals("1")) {//幼儿园
//                Intent intent = new Intent(getActivity(), SyatemAchieveYouActivity.class);
                Intent intent = new Intent(getActivity(), SyatemAchieveYouActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("achieveid", mDatas.get(position).getId());
                bundle.putString("headtitle", mDatas.get(position).getName());
                intent.putExtras(bundle);
                startActivity(intent);
            } else if (scope.equals("2")) {//小学
                Intent intent = new Intent(getActivity(), SyatemAchieveXiaoActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("achieveid", mDatas.get(position).getId());
                bundle.putString("headtitle", mDatas.get(position).getName());
                intent.putExtras(bundle);
                startActivity(intent);
            } else if (scope.equals("3")) {//初中
                Intent intent = new Intent(getActivity(), SyatemAchieveChuActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("achieveid", mDatas.get(position).getId());
                bundle.putString("headtitle", mDatas.get(position).getName());
                intent.putExtras(bundle);
                startActivity(intent);
            } else if (scope.equals("4")) {//高中
//                Intent intent = new Intent(getActivity(), SyatemAchieveYouActivity.class);
//                Bundle bundle = new Bundle();
//                bundle.putString("achieveid", mDatas.get(position).getId());
//                bundle.putString("headtitle", mDatas.get(position).getName());
//                intent.putExtras(bundle);
//                startActivity(intent);
            }

        } else if (mDatas.get(position).getType().equals("6")) {//表单
            Intent intent = new Intent(getActivity(), FormActivity.class);
            Bundle bundle = new Bundle();
            bundle.putString("achieveid", mDatas.get(position).getId());
            bundle.putString("headtitle", mDatas.get(position).getName());
            intent.putExtras(bundle);
            startActivity(intent);
        } else if (mDatas.get(position).getType().equals("7")) {//混合
            Intent intent = new Intent(getActivity(), MixtureOneActivity.class);
            Bundle bundle = new Bundle();
            bundle.putString("achieveid", mDatas.get(position).getId());
            bundle.putString("headtitle", mDatas.get(position).getName());
            intent.putExtras(bundle);
            startActivity(intent);
        }
    }
}

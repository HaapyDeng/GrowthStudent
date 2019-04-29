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
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.mpl.GrowthStud.R;
import com.mpl.GrowthStud.Student.Activity.GetStarInfoInfoActivity;
import com.mpl.GrowthStud.Student.Adapter.GetStarInfoListViewAdapter;
import com.mpl.GrowthStud.Student.Bean.GetStarInfoItem;
import com.mpl.GrowthStud.Student.Tools.NetworkUtils;
import com.mpl.GrowthStud.Student.View.XListView;
import com.mpl.GrowthStud.Student.View.XListViewFooter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;

import static android.content.Context.MODE_PRIVATE;


public class PGetStarInfoFragment extends Fragment implements AdapterView.OnItemClickListener, XListView.IXListViewListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private XListView listView;
    private List<GetStarInfoItem> mDatas = new ArrayList<GetStarInfoItem>();
    ;
    private GetStarInfoListViewAdapter getStarInfoListViewAdapter;
    private TextView star_score;
    private LinearLayout ll_empty;
    private String cid;
    //定义一个页数
    private int totalPage = 0;
    private Handler mHandler;
    private int start = 0;
    private static int refreshCnt = 0;
    private String currentPage = "1";

    public PGetStarInfoFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment GetStarInfoFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static PGetStarInfoFragment newInstance(String param1, String param2) {
        PGetStarInfoFragment fragment = new PGetStarInfoFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_get_star_info, container, false);
        listView = root.findViewById(R.id.lv);
        listView.setOnItemClickListener(this);
        ll_empty = root.findViewById(R.id.ll_empty);
        getStarInfoData(currentPage);
        star_score = root.findViewById(R.id.star_score);
        SharedPreferences sharedPreferences = this.getActivity().getSharedPreferences("myinfo", MODE_PRIVATE);
        String one_star_point = sharedPreferences.getString("one_star_point", "");
        star_score.setText("= " + one_star_point + " 分");
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
                getStarInfoData("1");
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
                    getStarInfoData("" + ye);
                    getStarInfoListViewAdapter.notifyDataSetChanged();
                    onLoad();
                } else {
                    onLoad();
                    XListViewFooter.setState(3);
                    Toast.makeText(getActivity(), "加载完成", Toast.LENGTH_SHORT).show();
                }
            }
        }, 2000);
    }


    private void getStarInfoData(String s) {
        if (!NetworkUtils.checkNetWork(getActivity())) {
            Toast.makeText(getActivity(), R.string.no_network, Toast.LENGTH_LONG).show();
            return;
        }
        SharedPreferences sharedPreferences3 = getActivity().getSharedPreferences("userid", MODE_PRIVATE);
        if (!sharedPreferences3.getBoolean("have", false)) {
            Toast.makeText(getActivity(), "请先绑定你的孩子", Toast.LENGTH_LONG).show();
            return;
        } else {
            cid = sharedPreferences3.getString("id", "");
        }
        Log.d("cid==>>", cid);
        SharedPreferences sharedPreferences = this.getActivity().getSharedPreferences("myinfo", MODE_PRIVATE);
        String token = sharedPreferences.getString("token", "");
        String uid = sharedPreferences.getString("userid", "");
        String url = getResources().getString(R.string.local_url) + "/v1/achievement/statistical/category/" + "1/" + cid + "?page=" + s;
        Log.d("url==>>", url);
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
                            String classroom_id = object.getString("classroom_id");
                            String category_name = object.getString("category_name");
                            String category_id = object.getString("category_id");
                            String grade = object.getString("grade");
                            String star = object.getString("star");
                            String task_star = object.getString("task_star");
                            String point = object.getString("point");
                            String total_point = object.getString("total_point");
                            GetStarInfoItem getStarInfoItem = new GetStarInfoItem(classroom_id, category_name, category_id, grade, star, task_star, point, total_point);
                            mDatas.add(getStarInfoItem);
                        }
                        getStarInfoListViewAdapter = new GetStarInfoListViewAdapter(getActivity(), mDatas);
                        listView.setAdapter(getStarInfoListViewAdapter);
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
                    listView.setVisibility(View.GONE);
                    ll_empty.setVisibility(View.VISIBLE);
                    break;
            }
        }
    };

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        String categoryid = mDatas.get(i).getCategory_id();
        String categoryname = mDatas.get(i).getCategory_name();
        Intent intent = new Intent(getActivity(), PGetStarInfoInfoActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("categoryid", categoryid);
        bundle.putString("categoryname", categoryname);
        intent.putExtras(bundle);
        startActivity(intent);
    }
}

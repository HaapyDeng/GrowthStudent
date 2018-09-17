package com.mpl.GrowthStud.Student.Activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.mcxtzhang.commonadapter.lvgv.CommonAdapter;
import com.mcxtzhang.commonadapter.lvgv.ViewHolder;
import com.mcxtzhang.swipemenulib.SwipeMenuLayout;
import com.mpl.GrowthStud.R;
import com.mpl.GrowthStud.Student.Adapter.MessageListViewAdapter;
import com.mpl.GrowthStud.Student.Bean.MessageItem;
import com.mpl.GrowthStud.Student.Tools.NetworkUtils;
import com.mpl.GrowthStud.Student.View.CircleImageView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;

public class MessageActivity extends AppCompatActivity {
    private LinearLayout ll_empty;
    private ListView listView;
    private List<MessageItem> mDatas;
    private MessageListViewAdapter messageListViewAdapter;
    private LinearLayout back;
    private int refresh = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);
        ll_empty = findViewById(R.id.ll_empty);
        listView = findViewById(R.id.listview);
        back = findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        getMessage();
    }

    @Override
    protected void onResume() {
        super.onResume();
        getMessage();
    }

    private void getMessage() {
        if (NetworkUtils.checkNetWork(this) == false) {
            Toast.makeText(this, R.string.no_network, Toast.LENGTH_LONG).show();
            return;
        }
        SharedPreferences sharedPreferences = this.getSharedPreferences("myinfo", MODE_PRIVATE);
        String token = sharedPreferences.getString("token", "");
        String url = getResources().getString(R.string.local_url) + "/message/index";
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
                        JSONArray list = data.getJSONArray("list");
                        if (list.length() == 0) {
                            ll_empty.setVisibility(View.VISIBLE);
                            return;
                        }
                        mDatas = new ArrayList<MessageItem>();
                        for (int i = 0; i < list.length(); i++) {
                            JSONObject object = list.getJSONObject(i);
                            String id = object.getString("id");
                            String title = object.getString("title");
                            String content = object.getString("content");
                            int type = object.getInt("type");
                            int is_read = object.getInt("is_read");
                            String created_at = object.getString("created_at");
                            MessageItem messageItem = new MessageItem(id, title, content, created_at, type, is_read);
                            mDatas.add(messageItem);
                        }
//                        messageListViewAdapter = new MessageListViewAdapter(MessageActivity.this, mDatas);
//                        listView.setAdapter(messageListViewAdapter);
                        listView.setAdapter(new CommonAdapter<MessageItem>(MessageActivity.this, mDatas, R.layout.message_item) {

                            @SuppressLint("NewApi")
                            @Override
                            public void convert(final ViewHolder holder, MessageItem messageItem, final int i) {
                                if (mDatas.get(i).getType() == 1) {
                                    ((ImageView) holder.getView(R.id.iv_head)).setBackground(MessageActivity.this.getResources().getDrawable(R.mipmap.icon_system_info));
                                } else {
                                    ((ImageView) holder.getView(R.id.iv_head)).setBackground(MessageActivity.this.getResources().getDrawable(R.mipmap.icon_evaluation_info));
                                }
                                ((TextView) holder.getView(R.id.tv_name)).setText(mDatas.get(i).getTitle());
                                if (mDatas.get(i).getIs_read() == 0) {
                                    ((CircleImageView) holder.getView(R.id.iv_dot)).setVisibility(View.VISIBLE);
                                } else {
                                    ((CircleImageView) holder.getView(R.id.iv_dot)).setVisibility(View.INVISIBLE);
                                }
                                ((TextView) holder.getView(R.id.tv_time)).setText(mDatas.get(i).getCreated_at());
                                ((TextView) holder.getView(R.id.tv_content)).setText(mDatas.get(i).getContent());
                                ((LinearLayout) holder.getView(R.id.ll_content)).setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        if (mDatas.get(i).getIs_read() == 0) {
                                            doReadMessage(i, mDatas.get(i).getId());
                                        } else {
                                            Intent intent = new Intent(MessageActivity.this, MessageInfoActivity.class);
                                            Bundle bundle = new Bundle();
                                            bundle.putString("title", mDatas.get(i).getTitle());
                                            bundle.putString("time", mDatas.get(i).getCreated_at());
                                            bundle.putString("content", mDatas.get(i).getContent());
                                            intent.putExtras(bundle);
                                            startActivity(intent);
                                        }
                                    }
                                });
                                (((Button) holder.getView(R.id.btnDelete))).setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        ((SwipeMenuLayout) holder.getConvertView()).quickClose();
                                        doDeleteMessage(i, mDatas.get(i).getId());
                                        mDatas.remove(i);
                                        notifyDataSetChanged();
                                    }
                                });
                            }
                        });
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                Toast.makeText(MessageActivity.this, R.string.no_network, Toast.LENGTH_LONG).show();
                return;
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                Toast.makeText(MessageActivity.this, R.string.no_network, Toast.LENGTH_LONG).show();
                return;
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                Toast.makeText(MessageActivity.this, R.string.no_network, Toast.LENGTH_LONG).show();
                return;
            }
        });

    }

    private void doDeleteMessage(final int i, String id) {
        ///message/read/{id}
        if (NetworkUtils.checkNetWork(this) == false) {
            Toast.makeText(this, R.string.no_network, Toast.LENGTH_LONG).show();
            return;
        }
        SharedPreferences sharedPreferences = this.getSharedPreferences("myinfo", MODE_PRIVATE);
        String token = sharedPreferences.getString("token", "");
        String url = getResources().getString(R.string.local_url) + "/message/delete/" + id;
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
                    } else {
                        Toast.makeText(MessageActivity.this, response.getString("message"), Toast.LENGTH_LONG).show();
                        getMessage();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                Toast.makeText(MessageActivity.this, R.string.no_network, Toast.LENGTH_LONG).show();
                return;
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                Toast.makeText(MessageActivity.this, R.string.no_network, Toast.LENGTH_LONG).show();
                return;
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                Toast.makeText(MessageActivity.this, R.string.no_network, Toast.LENGTH_LONG).show();
                return;
            }
        });

    }

//    @Override
//    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//        if (mDatas.get(i).getIs_read() == 0) {
//            doReadMessage(i, mDatas.get(i).getId());
//        } else {
//            Intent intent = new Intent(this, MessageInfoActivity.class);
//            Bundle bundle = new Bundle();
//            bundle.putString("title", mDatas.get(i).getTitle());
//            bundle.putString("time", mDatas.get(i).getCreated_at());
//            bundle.putString("content", mDatas.get(i).getContent());
//            intent.putExtras(bundle);
//            startActivity(intent);
//        }

//    }

    private void doReadMessage(final int i, String id) {
        ///message/read/{id}
        if (NetworkUtils.checkNetWork(this) == false) {
            Toast.makeText(this, R.string.no_network, Toast.LENGTH_LONG).show();
            return;
        }
        SharedPreferences sharedPreferences = this.getSharedPreferences("myinfo", MODE_PRIVATE);
        String token = sharedPreferences.getString("token", "");
        String url = getResources().getString(R.string.local_url) + "/message/read/" + id;
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
                        Intent intent = new Intent(MessageActivity.this, MessageInfoActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putString("title", mDatas.get(i).getTitle());
                        bundle.putString("time", mDatas.get(i).getCreated_at());
                        bundle.putString("content", mDatas.get(i).getContent());
                        intent.putExtras(bundle);
                        startActivity(intent);
                    } else {
                        Toast.makeText(MessageActivity.this, response.getString("message"), Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                Toast.makeText(MessageActivity.this, R.string.no_network, Toast.LENGTH_LONG).show();
                return;
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                Toast.makeText(MessageActivity.this, R.string.no_network, Toast.LENGTH_LONG).show();
                return;
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                Toast.makeText(MessageActivity.this, R.string.no_network, Toast.LENGTH_LONG).show();
                return;
            }
        });
    }


}

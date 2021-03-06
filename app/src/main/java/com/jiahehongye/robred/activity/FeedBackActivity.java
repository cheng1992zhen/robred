package com.jiahehongye.robred.activity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.gson.Gson;
import com.jiahehongye.robred.BaseActivity;
import com.jiahehongye.robred.Constant;
import com.jiahehongye.robred.R;
import com.jiahehongye.robred.bean.SuggestBean;
import com.jiahehongye.robred.cook.CookieJarImpl;
import com.jiahehongye.robred.cook.PersistentCookieStore;
import com.jiahehongye.robred.view.MyProgressDialog;

import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class FeedBackActivity extends BaseActivity implements View.OnClickListener {

    private RelativeLayout feed_back;
    private EditText et;

    private static final int BACK = 1111;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case BACK:
                    animDialog.dismiss();
                    String result = (String) msg.obj;
                    Gson gson = new Gson();
                    SuggestBean suggestBean = gson.fromJson(result, SuggestBean.class);

                    if (result != null) {

                        if (suggestBean.getResult().equals("success")) {
                            Toast.makeText(FeedBackActivity.this, "提交成功", Toast.LENGTH_SHORT).show();
                            finish();
                        }else {
                            Toast.makeText(FeedBackActivity.this, "失败", Toast.LENGTH_SHORT).show();
                        }
                    }

                    break;
            }
        }
    };


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        applyKitKatTranslucency();
        mTintManager.setStatusBarTintResource(R.color.home_state_color);
        setContentView(R.layout.activity_feed_back);

        feed_back = (RelativeLayout) findViewById(R.id.feed_back);
        feed_back.setOnClickListener(this);
        Button set_suggest_ok = (Button) findViewById(R.id.set_suggest_ok);
//        feed_up = (Button) findViewById(R.id.set_suggest_ok);
        set_suggest_ok.setOnClickListener(this);
//        feed_up.setOnClickListener(this);
        et = (EditText) findViewById(R.id.set_suggest_et);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.feed_back:
                finish();
                break;
            case R.id.set_suggest_ok:
                String suggestion = et.getText().toString();
                if (suggestion != null && suggestion.length() > 0) {
                    initData(suggestion);
                } else {
                    Toast.makeText(this, "请输入内容", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    private PersistentCookieStore persistentCookieStore;
    private MyProgressDialog animDialog;
    private Call call;

    private void initData(String suggest) {

        showMyDialog();

        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        persistentCookieStore = new PersistentCookieStore(this);
        CookieJarImpl cookieJarImpl = new CookieJarImpl(persistentCookieStore);
        builder.cookieJar(cookieJarImpl);
        OkHttpClient client = builder.build();

        JSONObject json = new JSONObject();
        try {
            json.put("adviceInfo", suggest);
        } catch (Exception e) {
            e.printStackTrace();
        }

        RequestBody body = RequestBody.create(Constant.JSON, json.toString());
        Request request = new Request.Builder()
                .url(Constant.FEEDBACK)
                .post(body)
                .build();

        call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                FeedBackActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(FeedBackActivity.this, "网络请求失败", Toast.LENGTH_SHORT).show();
                        animDialog.dismiss();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String result = response.body().string();
                Log.e("提交建议返回：", result);

                Message msg = handler.obtainMessage();
                msg.what = BACK;
                msg.obj = result;
                handler.sendMessage(msg);
            }
        });

    }

    /**
     * 显示对话框
     */
    public void showMyDialog() {
        animDialog = new MyProgressDialog(this, "玩命加载中...", R.drawable.loading);
        animDialog.show();
        animDialog.setCancelable(true);
        animDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                if (call.isExecuted()) {
                    call.cancel();
                }
            }
        });
    }
}

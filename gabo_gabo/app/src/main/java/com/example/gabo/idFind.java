package com.example.gabo;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

public class idFind extends AppCompatActivity{
    private Button btn_Find_id, btn_Find_pw, btn_Find_account;
    private EditText edt_Find_name, edt_Find_phone;
    private TextView tv_Find_name, tv_Find_phone;
    private RequestQueue queue;
    private StringRequest stringRequest;
    private String info;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        setContentView(R.layout.idfind);
        btn_Find_id = findViewById(R.id.btn_Find_id);
        btn_Find_pw = findViewById(R.id.btn_Find_pw);
        btn_Find_account = findViewById(R.id.btn_Find_account);
        edt_Find_name = findViewById(R.id.edt_Find_name);
        edt_Find_phone = findViewById(R.id.edt_Find_phone);
        tv_Find_name = findViewById(R.id.tv_Find_name);
        tv_Find_phone = findViewById(R.id.tv_Find_phone);
        btn_Find_pw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), pwFind.class);
                startActivity(intent);
            }
        });
        btn_Find_account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendRequest();
            }
        });
    }
            private void sendRequest() {
                queue = Volley.newRequestQueue(this);
                String url = "http://192.168.21.8:5013/findID";
                StringRequest stringRequest2 = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                    // 응답데이터를 받아오는 곳
                    @Override
                    public void onResponse(String response) {
                        Log.v("resultValue", response);
                        String[] info  = response.split(",");
                        Intent intent = new Intent(getApplicationContext(), idReceive.class);
                        intent.putExtra("id",info[0]);
                        intent.putExtra("name",info[1]);
                        intent.putExtra("startDate",info[2]);
                        startActivity(intent);
                        finish();
                    }
                }, new Response.ErrorListener() {
                    // 서버와의 연동 에러시 출력
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                    }
                }) {
                    @Override //response를 UTF8로 변경해주는 소스코드
                    protected Response<String> parseNetworkResponse(NetworkResponse response) {
                        try {
                            String utf8String = new String(response.data, "UTF-8");
                            return Response.success(utf8String, HttpHeaderParser.parseCacheHeaders(response));
                        } catch (UnsupportedEncodingException e) {
                            // log error
                            return Response.error(new ParseError(e));
                        } catch (Exception e) {
                            // log error
                            return Response.error(new ParseError(e));
                        }
                    }

                    // 보낼 데이터를 저장하는 곳
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String, String> params = new HashMap<String, String>();
                        String name = edt_Find_name.getText().toString();
                        String phone = edt_Find_phone.getText().toString();
                        params.put("name", name);
                        params.put("phone", phone);
                        return params;
                    }
                };

        String TAG = "leejh";
        stringRequest2.setTag(TAG);
        queue.add(stringRequest2);
    }

}

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

public class pwFind extends AppCompatActivity {
    Button btn_find_Id, btn_find_Pw, btn_join_Account;
    EditText edt_find_Name, edt_find_Email;
    TextView tv_find_Name, tv_find_Email3;

    private RequestQueue queue;
    private StringRequest stringRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        setContentView(R.layout.pwfind);
        btn_find_Id = findViewById(R.id.btn_find_Id);
        btn_find_Pw = findViewById(R.id.btn_find_Pw);
        btn_join_Account = findViewById(R.id.btn_join_Account);
        edt_find_Name = findViewById(R.id.edt_find_Name);
        edt_find_Email = findViewById(R.id.edt_find_Email);
        tv_find_Name = findViewById(R.id.tv_find_Name);
        tv_find_Email3 = findViewById(R.id.tv_find_Email);
        btn_find_Id.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), idFind.class);
                startActivity(intent);
            }
        });
        btn_join_Account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {sendRequest();
                Intent intent = new Intent(getApplicationContext(), pwReceive.class);
                startActivity(intent);
            }
        });
    }


    public  void sendRequest(){
        //메소드는 클래스 아래에서 만듬
        // Volley Lib 새로운 요청객체 생성
        queue = Volley.newRequestQueue(this);
        // 서버에 요청할 주소
        String url = "http://192.168.21.252:5013/findPw";
        // 요청 문자열 저장
        stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            // 응답데이터를 받아오는 곳
            @Override
            public void onResponse(String response) {
                Log.v("resultValue",response);
                String [] info = response.split(",");
                Log.v("resultValue","id : " +info[0]);
                Log.v("resultValue","pw : " +info[1]);
                Log.v("resultValue","nick : " +info[2]);
                Log.v("resultValue","addr : " +info[3]);
                Log.v("resultValue", "pn : " + info[4]);
                Log.v("resultValue", "email : " + info[5]);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        }){
            @Override
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

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                String id = edt_find_Name.getText().toString();
                String email = edt_find_Email.getText().toString();
                params.put("id", id);
                params.put("email", email);
                return params;
            }
        };
        String TAG = "leejh";
        stringRequest.setTag(TAG);
        queue.add(stringRequest);
    }


}
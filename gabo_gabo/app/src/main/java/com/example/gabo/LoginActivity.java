package com.example.gabo;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
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

public class LoginActivity extends AppCompatActivity {
    private MainActivity main;
    private EditText edt_login_email, edt_login_passoword;
    private Button btn_login_signin, btn_login_signup, btn_id_Find, btn_pw_Find;
    private RequestQueue queue;
    private StringRequest stringRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        //상단바 삭제
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        edt_login_email = findViewById(R.id.edt_login_email);
        edt_login_passoword = findViewById(R.id.edt_login_password);
        btn_login_signin = findViewById(R.id.btn_login_signin);
        btn_login_signup = findViewById(R.id.btn_login_signup);
        btn_id_Find = findViewById(R.id.btn_id_Find);
        btn_pw_Find = findViewById(R.id.btn_pw_Find);

        //회원가입 페이지
        btn_login_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), JoinActivity.class);
                startActivity(intent);
            }
        });
        //아이디 찾기 기능
        btn_id_Find.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), idFind.class);
                startActivity(intent);
            }
        });
        // 비밀번호 찾기 기능
        btn_pw_Find.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), pwFind.class);
                startActivity(intent);
            }
        });

        //로그인 기능
        btn_login_signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                sendRequest();
            }
        });
    }

    public void sendRequest() {
        // Volley Lib 새로운 요청객체 생성
        queue = Volley.newRequestQueue(this);
        // 서버에 요청할 주소
        String url = main.mainHost+"login";
        // 요청 문자열 저장
        stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            // 응답데이터를 받아오는 곳
            @Override
            public void onResponse(String response) {
                Log.v("resultValue", response);
                String[] info = response.split(",");
                System.out.println(info[0]);
                System.out.println(edt_login_email.getText().toString());
                if (info[0].equals(edt_login_email.getText().toString())){
                    Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                    intent.putExtra("id",edt_login_email.getText().toString());
                    startActivity(intent);
                } else {
                    Toast.makeText(getApplicationContext(),"ID 또는 PW가 틀렸습니다.",Toast.LENGTH_LONG).show();
                }
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
                String id = edt_login_email.getText().toString();
                String pw = edt_login_passoword.getText().toString();

                params.put("id", id);
                params.put("pw", pw);

                return params;
            }
        };


        String Tag = "LJY";
        stringRequest.setTag(Tag);
        queue.add(stringRequest);


    }
}



package com.example.gabo;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

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

public class pwReceive extends AppCompatActivity {
    private Button btn_receive_Id, btn_receive_Pw, btn_change_pw;
    private EditText edt_change_pw1, edt_change_pw2;
    private TextView tv_receive_Restart, tv_receive_Name, tv_receive_Password_check;
    private RequestQueue queue;
    private StringRequest stringRequest;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        setContentView(R.layout.pwreceive);
        btn_receive_Id = findViewById(R.id.btn_receive_Id);
        btn_receive_Pw = findViewById(R.id.btn_receive_Pw);
        btn_change_pw = findViewById(R.id.btn_change_pw);
        edt_change_pw1 = findViewById(R.id.edt_change_pw1);
        edt_change_pw2 = findViewById(R.id.edt_change_pw2);
        tv_receive_Restart = findViewById(R.id.tv_receive_Restart);
        tv_receive_Name = findViewById(R.id.tv_receive_Name);
        tv_receive_Password_check = findViewById(R.id.tv_receive_Password_check);
        btn_receive_Id.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), idFind.class);
                startActivity(intent);
            }
        });


        // 비밀번호 변경 기능
        btn_change_pw.setOnClickListener(new View.OnClickListener() {
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

        String url = "http://192.168.21.112:5013/changePW";

        // 요청 문자열 저장
        stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            // 응답데이터를 받아오는 곳
            @Override
            public void onResponse(String response) {
                Intent intent = new Intent(getApplicationContext(),LoginActivity.class);
                Toast.makeText(getApplicationContext(),"비밀호를 변경 완료.",Toast.LENGTH_LONG).show();
                startActivity(intent);
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
                String pw1 = edt_change_pw1.getText().toString();
                String pw2 = edt_change_pw2.getText().toString();

                Intent intent = getIntent();
                String intentName = intent.getStringExtra("name");
                Log.v("name",intentName);
                String intentMail = intent.getStringExtra("mail");
                Log.v("name",intentMail);

                if (pw1.equals(pw2)){
                    params.put("name",intentName);
                    params.put("email",intentMail);
                    params.put("pw1", pw1);
                    params.put("pw2", pw2);
                }
//                else{
//                    Toast.makeText(getApplicationContext(),"비밀호를 다시 확인 부탁드립니다.",Toast.LENGTH_LONG).show();
//                }
                return params;
            }
        };


        String Tag = "LJH";
        stringRequest.setTag(Tag);
        queue.add(stringRequest);


    }
}
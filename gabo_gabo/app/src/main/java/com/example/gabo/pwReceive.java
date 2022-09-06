package com.example.gabo;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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
    private ImageView pwreciveimg1, pwreciveimg2;
    private Button btn_receive_Id, btn_receive_Pw, btn_change_pw;
    private EditText edt_change_pw1, edt_change_pw2;
    private TextView tv_receive_Restart, tv_new_Passoword, tv_receive_password_Check;
    private RequestQueue queue;
    private StringRequest stringRequest;
    private boolean pw1, pw2;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        setContentView(R.layout.pwreceive);
        pwreciveimg1 = findViewById(R.id.pwreciveimg1);
        pwreciveimg2 = findViewById(R.id.pwreciveimg2);
        btn_receive_Id = findViewById(R.id.btn_receive_Id);
        btn_receive_Pw = findViewById(R.id.btn_receive_Pw);
        btn_change_pw = findViewById(R.id.btn_change_pw);
        edt_change_pw1 = findViewById(R.id.edt_change_Pw1);
        edt_change_pw2 = findViewById(R.id.edt_change_Pw2);
        tv_receive_Restart = findViewById(R.id.tv_receive_Restart);
        tv_new_Passoword = findViewById(R.id.tv_new_Passoword);
        tv_receive_password_Check = findViewById(R.id.tv_receive_password_Check);
        btn_receive_Id.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), idFind.class);
                startActivity(intent);
            }
        });

        edt_change_pw1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (edt_change_pw1.getText().toString().equals(edt_change_pw1.getText().toString())) {
                    pwreciveimg1.setImageResource(R.drawable.findpw3);
                    pw1 = true;

                } else {
                    pwreciveimg1.setImageResource(R.drawable.findpw1);
                    pw1 = false;
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        edt_change_pw2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (edt_change_pw1.getText().toString().equals(edt_change_pw2.getText().toString())) {
                    pwreciveimg2.setImageResource(R.drawable.findpw4);
                    pw2 = true;

                } else {
                    pwreciveimg2.setImageResource(R.drawable.findpw2);
                    pw2 = false;
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (pw2==false){
                    edt_change_pw2.setText("입력하신 비밀번호와 다릅니다.");
                } else if(pw2==true){
                    edt_change_pw2.setText("");
                }
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

        String url = "http://192.168.21.8:5013/changePW";

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
package com.example.gabo;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
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

public class pwFind extends AppCompatActivity {
    private Button btn_find_Id, btn_find_Pw, btn_findPw_pw;
    private EditText edt_findPw_name, edt_findPw_mail;
    private TextView tv_find_name, tv_find_email;

    private RequestQueue queue;
    private StringRequest stringRequest;
    private Dialog pwfaildialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        setContentView(R.layout.pwfind);
        btn_find_Id = findViewById(R.id.btn_find_Id);
        btn_find_Pw = findViewById(R.id.btn_find_Pw);
        btn_findPw_pw = findViewById(R.id.btn_findPw_pw);
        edt_findPw_name = findViewById(R.id.edt_findPW_name);
        edt_findPw_mail = findViewById(R.id.edt_findPW_mail);
        tv_find_name = findViewById(R.id.tv_find_Name);
        tv_find_email = findViewById(R.id.tv_find_email);

        //아이디 찾기 페이지로 이동
        btn_find_Id.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), idFind.class);
                startActivity(intent);
            }
        });

        // 비밀번호 찾기 기능
        btn_findPw_pw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendRequest();

            }
        });

    }

    /*---------------------------------------비번불일치 다이얼로그 실행---------------------------------------*/
    private void openpwfailDialog(){
        pwfaildialog = new Dialog(getApplicationContext());
        pwfaildialog.setContentView(R.layout.dialog_findaccount);
        pwfaildialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        ImageView imageViewClose=pwfaildialog.findViewById(R.id.imageViewClose);
        TextView tv_findquiz1 = pwfaildialog.findViewById(R.id.findaccount_againbtn);

        imageViewClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pwfaildialog.dismiss();
                Toast.makeText(getApplicationContext(), "Dialog Close", Toast.LENGTH_SHORT).show();
            }
        });
        tv_findquiz1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pwfaildialog.dismiss();
            }
        });
        pwfaildialog.show();

    }

    public void sendRequest() {
        // Volley Lib 새로운 요청객체 생성
        queue = Volley.newRequestQueue(this);
        // 서버에 요청할 주소

        String url = "http://192.168.21.8:5013/findPW";

        // 요청 문자열 저장
        stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            // 응답데이터를 받아오는 곳
            @Override
            public void onResponse(String response) {
                Log.v("resultValue", response);
                String[] info = response.split(",");
                System.out.println("결과는"+ info[0]);
                String putName = edt_findPw_name.getText().toString();
                String putMail = edt_findPw_mail.getText().toString();
                if (info[0].equals(putName) && info[1].equals(putMail)){
                    Intent intent = new Intent(getApplicationContext(),pwReceive.class);
                    intent.putExtra("name",putName);
                    intent.putExtra("mail",putMail);
                    startActivity(intent);
                } else {
                    openpwfailDialog();
                    //Toast.makeText(getApplicationContext(),"입력하신 정보를 다시 한 번 확인해 주세요..",Toast.LENGTH_LONG).show();
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
                String name = edt_findPw_name.getText().toString();
                String email = edt_findPw_mail.getText().toString();

                params.put("name", name);
                params.put("email", email);

                return params;
            }
        };


        String Tag = "LJH";
        stringRequest.setTag(Tag);
        queue.add(stringRequest);


    }
}
package com.example.gabo;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

public class idReceive extends AppCompatActivity {
    Button btn_Receive_id, btn_Receive_pw, btn_Receive_signin;
    TextView tv_Receive_name, tv_Receive_email, tv_Receive_member;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        setContentView(R.layout.idreceive);


        btn_Receive_id = findViewById(R.id.btn_Receive_id);
        btn_Receive_pw = findViewById(R.id.btn_Receive_pw);
        btn_Receive_signin = findViewById(R.id.btn_Receive_signin);
        tv_Receive_name = findViewById(R.id.tv_Receive_name);
        tv_Receive_email = findViewById(R.id.tv_Receive_email);
        tv_Receive_member = findViewById(R.id.tv_Receive_member);

        Intent getstr = getIntent();
        String id = getstr.getStringExtra("id");
        String name = getstr.getStringExtra("name");
        String startDate =  getstr.getStringExtra("startDate");

        tv_Receive_name.setText(name + " 님의 계정은");
        tv_Receive_email.setText(id + " 입니다");
        tv_Receive_member.setText("(가입일 : " +startDate+")");


        btn_Receive_pw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), pwFind.class);
                startActivity(intent);
            }
        });
        btn_Receive_signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);
            }
        });

    }
}
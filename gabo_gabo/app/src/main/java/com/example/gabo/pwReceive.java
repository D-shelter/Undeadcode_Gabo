package com.example.gabo;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

public class pwReceive extends AppCompatActivity {
    Button btn_receive_Id, btn_receive_Pw, btn_receive_Change;
    EditText edt_receive_Password, edt_receive_Password_check;
    TextView tv_receive_Restart, tv_receive_Name, tv_receive_Password_check;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        setContentView(R.layout.pwreceive);
        btn_receive_Id = findViewById(R.id.btn_receive_Id);
        btn_receive_Pw = findViewById(R.id.btn_receive_Pw);
        btn_receive_Change = findViewById(R.id.btn_receive_Change);
        edt_receive_Password = findViewById(R.id.edt_receive_Password);
        edt_receive_Password_check = findViewById(R.id.edt_receive_Password_check);
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
        btn_receive_Change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (edt_receive_Password.getText().toString().equals(edt_receive_Password_check.getText().toString())){
                    Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                    startActivity(intent);
                }else {
                    tv_receive_Password_check.setText("일치하지 않습니다.");
                }
            }
        });
    }
}
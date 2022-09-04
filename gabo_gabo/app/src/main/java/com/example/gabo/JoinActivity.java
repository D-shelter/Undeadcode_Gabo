package com.example.gabo;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.example.gabo.VO.UsersVO;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.Volley;

public class JoinActivity extends AppCompatActivity {
    // 실제로 해당 Server에 요청을 하는 객체
    private RequestQueue queue;
    // 요청할 때 필요한 요청사항을 기록하는 객체
    private StringRequest stringRequest;

    private ImageView setImage;
    private EditText edt_id, edt_nick, edt_pw, edt_phone, edt_pwch, edt_mail, edt_name, edt_join_year, edt_join_day;
    private Button btn_join;
    private RadioButton radio_man, radio_women, gende;
    private boolean id,pw,pwch,email,name,nick,phone,birth_year,birth_day = false;
    private RadioGroup rGroup;
    private TextView tv_check_id,tv_check_pw,tv_check_pwch,tv_check_name,tv_check_nick,tv_check_email,tv_check_phone,tv_check_birth,tv_check_gender;
    private Spinner sp_month;
    private String gender=null;
    private String join_id,join_pw,join_nick,join_phone,join_mail,join_name,join_year,join_day,join_month,join_birth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        //스피너(month) 설정
        sp_month = findViewById(R.id.sp_month);
        sp_month.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                //텍스트 색 회색으로 바꿔주기
                ((TextView)adapterView.getChildAt(0)).setTextColor(Color.GRAY);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        //현재 시간 가져오기
        long now = System.currentTimeMillis();
        //Date 생성
        Date date = new Date(now);
        // 패턴 저장
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
        // 형식 저장
        Integer getTime = Integer.valueOf(sdf.format(date));
        // 년도 담을 배열 생성
        ArrayList<String> years = new ArrayList<>();
        // 1900년부터 현재 년도까지 담음
        for (int i = 1900; i<getTime; i++ ){
            years.add(String.valueOf(i));
        }
        // 날짜 담을 배열 생성
        ArrayList<String> days = new ArrayList<>();
        // 1~31까지 담음
        for (int i = 1; i<=31; i++){
            days.add(String.valueOf(i));
        }

        edt_pw = findViewById(R.id.edt_pw);
        edt_pwch = findViewById(R.id.edt_pwch);
        setImage = findViewById(R.id.setImage);

        edt_id = findViewById(R.id.edt_id);
        edt_pw = findViewById(R.id.edt_pw);
        edt_nick = findViewById(R.id.edt_nick);
        edt_phone = findViewById(R.id.edt_phone);
        edt_pwch = findViewById(R.id.edt_pwch);
        edt_mail = findViewById(R.id.edt_mail);
        edt_name = findViewById(R.id.edt_name);
        edt_join_year = findViewById(R.id.edt_join_year);
        edt_join_day = findViewById(R.id.edt_join_day);
        tv_check_id = findViewById(R.id.tv_check_id);
        tv_check_pw = findViewById(R.id.tv_check_pw);
        tv_check_pwch = findViewById(R.id.tv_check_pwch);
        tv_check_email = findViewById(R.id.tv_check_email);
        tv_check_phone = findViewById(R.id.tv_check_phone);
        tv_check_birth = findViewById(R.id.tv_check_birth);
        tv_check_gender = findViewById(R.id.tv_check_gender);
        tv_check_name = findViewById(R.id.tv_check_name);
        tv_check_nick = findViewById(R.id.tv_check_nick);
        Spinner sp_month = (Spinner)findViewById(R.id.sp_month);

        btn_join = findViewById(R.id.btn_join);

        rGroup = findViewById(R.id.rGroup);

        //회원가입 버튼 클릭
        btn_join.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println(id);
                System.out.println(pw);
                System.out.println(nick);
                System.out.println(phone);
                System.out.println(email);
                System.out.println(name);
                System.out.println(birth_year);
                System.out.println(birth_day);
                System.out.println(!sp_month.getSelectedItem().toString().equals("Month"));
                System.out.println(gender!=null);
                //모든 조건이 맞는다면
                if (id==true&&pw==true&&pwch==true&&email==true&&name==true&&nick==true&&phone==true&&birth_year==true&&!sp_month.getSelectedItem().toString().equals("Month")&&birth_day==true&&gender!=null){
                    join_id = edt_id.getText().toString();
                    join_pw = edt_pw.getText().toString();
                    join_nick = edt_nick.getText().toString();
                    join_phone = edt_phone.getText().toString();
                    join_mail = edt_mail.getText().toString();
                    join_name = edt_name.getText().toString();
                    join_year = edt_join_year.getText().toString();
                    join_day = edt_join_day.getText().toString();
                    join_month = sp_month.getSelectedItem().toString();
                    join_birth = join_year + join_month + join_day;
                    join_phone.replace("-", "");
                    //서버 통신 메소드 실행
                    sendRequest();
                    //액티비티로 옮길거 준비 및 실행
                    UsersVO vo = new UsersVO(join_id, join_pw, join_name, join_phone, join_birth, join_mail, join_nick, gender);
                    Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                    intent.putExtra("vo", vo);
                    startActivity(intent);
                } else if (id==true&&pw==true&&pwch==true&&email==true&&name==true&&nick==true&&phone==true&&birth_year==true&&sp_month.getSelectedItem().toString().equals("Month")&&birth_day==true&&gender!=null){
                    tv_check_birth.setText("태어난 월을 선택해주세요.");
                }else if (!sp_month.getSelectedItem().toString().equals("Month")){
                    tv_check_birth.setText("");
                } else  {
                    Toast.makeText(getApplicationContext(),"정보 입력이 제대로 되지 않았습니다.", Toast.LENGTH_LONG).show();
                }
                if (id==true&&pw==true&&pwch==true&&email==true&&name==true&&nick==true&&phone==true&&birth_year==true&&!sp_month.getSelectedItem().toString().equals("Month")&&birth_day==true&&gender==null){
                    tv_check_gender.setText("성별을 선택해주세요.");
                }else if (gender!=null){
                    tv_check_gender.setText("");
                }else  {
                    Toast.makeText(getApplicationContext(),"정보 입력이 제대로 되지 않았습니다.", Toast.LENGTH_LONG).show();
                }
            }
        });
        // 아이디 체크
        edt_id.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                // 아이디를 한글자씩 쪼게서 char 배열에 넣어줌
                char[] chars = edt_id.getText().toString().toCharArray();
                for (int j = 0; j<edt_id.getText().toString().length();j++){
                    // 조건에 맞는 식이면 id=true
                    if (j>=4){ // 5글자 이상인가
                        if (Character.isLowerCase(chars[j])){// 소문자 인가
                            id = true;
                        } else if (Character.isDigit(chars[j])){ // 숫자 인가
                            id = true;
                        } else if (chars[j]=='_'){ // '_' 인가
                            id = true;
                        } else if (chars[j]=='-'){ // '-' 인가
                            id = true;
                        } else { // 아니면 id = false 반복문 탈출
                            id = false;
                            break;
                        }
                    }else {
                        id = false;
                    }

                }
            }

            @Override
            // id = true 여부에 따라서 파란색 글자로 표시
            public void afterTextChanged(Editable editable) {
                if (id==false){
                    tv_check_id.setText("5~20자의 영문 소문자, 숫자와 특수기호(_),(-)만 사용 가능합니다.");
                } else if(id==true){
                    tv_check_id.setText("");
                }
            }
        });

        // 비번 체크
        edt_pw.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (edt_pw.getText().toString().length()>=7&&!tv_check_pw.getText().toString().equals("")){
                    pw = true;
                }else{
                    pw = false;
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
                tv_check_pw.setText(checkPassword(edt_pw.getText().toString(), edt_id.getText().toString()));
            }
        });

        // 비번 확인란 체크
        edt_pwch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (edt_pw.getText().toString().equals(edt_pwch.getText().toString())) {
                    setImage.setImageResource(R.drawable.join_check_on);
                    pwch = true;
                } else {
                    setImage.setImageResource(R.drawable.join_check_off);
                    pwch = false;
                }
            }
            @Override
            public void afterTextChanged(Editable editable) {
                if (pwch==false){
                    tv_check_pwch.setText("입력하신 비밀번호와 다릅니다.");
                } else if(pwch==true){
                    tv_check_pwch.setText("");
                }
            }
        });

        // 이메일 체크
        edt_mail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void afterTextChanged(Editable editable) {
                // 정규식 저장
                String regx = "^(.+)@(.+).(.+)$";
                // 정규식을 패턴으로 컴파일
                Pattern pattern = Pattern.compile(regx);
                // 패턴과 작성한 이메일을 매치해서 저장
                Matcher matcher = pattern.matcher(edt_mail.getText().toString());
                // 불리언타입으로 변환
                email = matcher.matches();
                if (email==true){
                    tv_check_email.setText("");
                }else if (email==false){
                    tv_check_email.setText("E-mail 형식에 맞게 작성해주세요");
                }
            }
        });

        // 이름 체크
        edt_name.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (edt_name.getText().toString().length()>=2){
                    name = true;
                } else{
                    name = false;
                }
            }
            @Override
            public void afterTextChanged(Editable editable) {
                if (name==false){
                    tv_check_name.setText("이름을 입력해주세요.");
                } else if (name==true){
                    tv_check_name.setText("");
                }
            }
        });

        // 닉넴 체크
        edt_nick.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (edt_nick.getText().toString().length()>=2){
                    nick = true;
                } else{
                    nick = false;
                }
            }
            @Override
            public void afterTextChanged(Editable editable) {
                if (nick==false){
                    tv_check_nick.setText("닉네임을 입력해주세요.");
                } else if (nick==true){
                    tv_check_nick.setText("");
                }
            }
        });
        // 연락처 체크
        edt_phone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (edt_phone.getText().toString().length()>=10){
                    phone = true;
                } else{
                    phone = false;
                }
            }
            @Override
            public void afterTextChanged(Editable editable) {
                if (phone==false){
                    tv_check_phone.setText("연락처를 제대로 입력해주세요.");
                } else if (phone==true){
                    tv_check_phone.setText("");
                }
            }
        });

        // 생일(년) 체크
        edt_join_year.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (years.contains(edt_join_year.getText().toString())){
                    birth_year = true;
                } else{
                    birth_year = false;
                }
            }
            @Override
            public void afterTextChanged(Editable editable) {
                if (birth_year==false){
                    tv_check_birth.setText("태어난 년도가 올바르지 않습니다.");
                } else {
                    tv_check_birth.setText("");
                }
            }
        });

        // 생일(일) 체크
        edt_join_day.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (days.contains(edt_join_day.getText().toString())){
                    birth_day = true;
                } else{
                    birth_day = false;
                }
            }
            @Override
            public void afterTextChanged(Editable editable) {
                if (birth_day==false) {
                    tv_check_birth.setText("태어난 일자가 올바르지 않습니다.");
                } else {
                    tv_check_birth.setText("");
                }
            }
        });

        //성별 체크
        rGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                StringBuilder mStr = new StringBuilder();
                if (i == R.id.radio_man) {
                    mStr.append("radio_men selected");
                    gender = "남";
                } else if (i == R.id.radio_women){
                    mStr.append("radio_women selected");
                    gender = "여";
                }

            }
        });
    }
    // 비밀번호 정규식 메소드
    private String checkPassword(String pwd, String id){

        // 비밀번호 포맷 확인(영문, 특수문자, 숫자 포함 8자 이상)
        Pattern passPattern1 = Pattern.compile("^(?=.*[a-zA-Z])(?=.*\\d)(?=.*\\W).{8,20}$");
        Matcher passMatcher1 = passPattern1.matcher(pwd);

        if(!passMatcher1.find()){
            return "비밀번호는 영문과 특수문자 숫자를 포함하며 8자 이상이어야 합니다.";
        }

        // 반복된 문자 확인
        Pattern passPattern2 = Pattern.compile("(\\w)\\1\\1\\1");
        Matcher passMatcher2 = passPattern2.matcher(pwd);

        if(passMatcher2.find()){
            return "비밀번호에 동일한 문자를 과도하게 연속해서 사용할 수 없습니다.";
        }

        // 아이디 포함 확인
        if(pwd.contains(id)){
            return "비밀번호에 ID를 포함할 수 없습니다.";
        }

        // 특수문자 확인
        Pattern passPattern3 = Pattern.compile("\\W");
        Pattern passPattern4 = Pattern.compile("[!@#$%^*+=-]");

        for(int i = 0; i < pwd.length(); i++){
            String s = String.valueOf(pwd.charAt(i));
            Matcher passMatcher3 = passPattern3.matcher(s);

            if(passMatcher3.find()){
                Matcher passMatcher4 = passPattern4.matcher(s);
                if(!passMatcher4.find()){
                    return "비밀번호에 특수문자는 !@#$^*+=-만 사용 가능합니다.";
                }
            }
        }

        //연속된 문자 확인
        int ascSeqCharCnt = 0; // 오름차순 연속 문자 카운트
        int descSeqCharCnt = 0; // 내림차순 연속 문자 카운트

        char char_0;
        char char_1;
        char char_2;

        int diff_0_1;
        int diff_1_2;

        for(int i = 0; i < pwd.length()-2; i++){
            char_0 = pwd.charAt(i);
            char_1 = pwd.charAt(i+1);
            char_2 = pwd.charAt(i+2);

            diff_0_1 = char_0 - char_1;
            diff_1_2 = char_1 - char_2;

            if(diff_0_1 == 1 && diff_1_2 == 1){
                ascSeqCharCnt += 1;
            }

            if(diff_0_1 == -1 && diff_1_2 == -1){
                descSeqCharCnt -= 1;
            }
        }

        if(ascSeqCharCnt > 1 || descSeqCharCnt > 1){
            return "비밀번호에 연속된 문자열을 사용할 수 없습니다.";
        }

        return "";
    }

    // 서버로 전송
    public void sendRequest(){
        // Volley Lib 새로운 요청객체 생성
        queue = Volley.newRequestQueue(this);
        // 서버에 요청할 주소
        String url = "http://192.168.45.230:5013/join";
        // 요청 문자열 저장
        stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            // 응답데이터를 받아오는 곳
            @Override
            public void onResponse(String response) {
                Log.v("resultValue",response);

            }
        }, new Response.ErrorListener() {
            // 서버와의 연동 에러시 출력
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        }){
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
                String id= join_id;
                String pw= join_pw;
                String nick= join_nick;
                String email= join_mail;
                String name = join_name;
                String phone = join_phone;
                String birth = join_year+"-"+join_month+"-"+join_day;
                phone = phone.replace("-","");
                params.put("id",id);
                params.put("pw",pw);
                params.put("nick",nick);
                params.put("email",email);
                params.put("name",name);
                params.put("phone",phone);
                params.put("birth",birth);
                params.put("gender",gender);

                return params;
            }
        };

        String Tag = "parky";
        stringRequest.setTag(Tag);
        queue.add(stringRequest);
    }



}

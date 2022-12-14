package com.example.gabo;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

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
import com.naver.maps.geometry.LatLng;
import com.naver.maps.map.overlay.Marker;
import com.naver.maps.map.overlay.OverlayImage;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

public class trsListview extends Fragment {

    private RequestQueue queue;
    private StringRequest stringRequest;

    private ListView tl_list;
    private TextView tl_T_num;
    private trsListviewAdapter adapter;
    private String user_id , user_location, user_lati, user_longi;
    private String mainhost;
    private String list_name, list_key1, list_key2, list_key3, list_hidedate,list_like;
    private View view;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {


        view = inflater.inflate(R.layout.trs_listview,container,false);

        mainhost = getArguments().getString("mainhost");
        user_location = getArguments().getString("user_location");
        user_id = getArguments().getString("user_id");
        String[] location = user_location.split(",");
        user_lati = location[0];
        user_longi = location[1];

        tl_list = view.findViewById(R.id.tl_list);

        sendRequest1();

//        adapter.listaddItems(ContextCompat.getDrawable(getActivity(), R.drawable.e1),ContextCompat.getDrawable(getActivity(),R.drawable.like_full),"parky","1km","#????????????","#????????????","#?????????","1????????? ??????","123");
//        adapter.listaddItems(ContextCompat.getDrawable(getActivity(), R.drawable.e2),ContextCompat.getDrawable(getActivity(),R.drawable.like_full),"PTSD","2km","#?????????","#??????","#??????","08/11/22 06:10 PM??? ??????","23");
//        adapter.listaddItems(ContextCompat.getDrawable(getActivity(), R.drawable.e3),ContextCompat.getDrawable(getActivity(),R.drawable.like_full),"Bugs","6km","#?????????","#?????????","#?????????","?????? ??????","13");
//        adapter.listaddItems(ContextCompat.getDrawable(getActivity(), R.drawable.e4),ContextCompat.getDrawable(getActivity(),R.drawable.like_full),"Undaed","7km","hint1","hint2","hin3","?????? ??????","12");
//        adapter.listaddItems(ContextCompat.getDrawable(getActivity(), R.drawable.e1),ContextCompat.getDrawable(getActivity(),R.drawable.like_full),"Coliny","10km","????????????","????????????","?????????","?????? ??????","1323");
//        adapter.listaddItems(ContextCompat.getDrawable(getActivity(), R.drawable.e3),ContextCompat.getDrawable(getActivity(),R.drawable.like_full),"Bugs","6km","hint1","hint2","hin3","?????? ??????","13");
//        adapter.listaddItems(ContextCompat.getDrawable(getActivity(), R.drawable.e4),ContextCompat.getDrawable(getActivity(),R.drawable.like_full),"Undaed","7km","hint1","hint2","hin3","?????? ??????","12");
//        adapter.listaddItems(ContextCompat.getDrawable(getActivity(), R.drawable.e1),ContextCompat.getDrawable(getActivity(),R.drawable.like_full),"Coliny","10km","????????????","????????????","?????????","?????? ??????","1323");
//        tl_list.setAdapter(adapter);


        return view;


    }
    // ?????? ?????? ??????
    public void sendRequest1() {
        // Volley Lib ????????? ???????????? ??????
        queue = Volley.newRequestQueue(getContext().getApplicationContext());
        // ????????? ????????? ??????
        String url = mainhost +"nearlist";
        // ?????? ????????? ??????
        stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            // ?????????????????? ???????????? ???
            @Override
            public void onResponse(String response) {
                adapter = new trsListviewAdapter();
                tl_T_num = view.findViewById(R.id.tl_T_num);
                tl_list = view.findViewById(R.id.tl_list);

                Log.v("resultValue", response);
                String[] info = response.split("/");
                for (int i = 0; i < info.length; i++) {
//                    System.out.println(info[i]);
                    String [] info2 = info[i].split(",");
                    for (int j = 0; j <info[i].length();j++) {
                            list_name = info2[2];
                            list_key1 = info2[5];
                            list_key2 = info2[6];
                            list_key3 = info2[7];
                            list_hidedate = info2[10];
                            list_like = info2[11];
                    }
                    adapter.listaddItems(ContextCompat.getDrawable(getActivity(), R.drawable.e1),ContextCompat.getDrawable(getActivity(),R.drawable.like_full),list_name,"1km",list_key1,list_key2,list_key3,list_hidedate,list_like);
                }
                System.out.println("aaaaa"+list_name);
                tl_list.setAdapter(adapter);
                tl_T_num.setText((info.length)+"");

            }
        }, new Response.ErrorListener() {
            // ???????????? ?????? ????????? ??????
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        }) {
            @Override //response??? UTF8??? ??????????????? ????????????
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

            // ?????? ???????????? ???????????? ???
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("user_id",user_id);
                params.put("user_lati",user_lati);
                params.put("user_longi",user_longi);
                return params;
            }
        };

        String Tag = "LJY";
        stringRequest.setTag(Tag);
        queue.add(stringRequest);


    }
}

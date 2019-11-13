//로그인 요청 java 파일
package com.hfad.myplanner;


import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class LoginRequst extends StringRequest {

    final static private String URL = "http://imhost.iptime.org/Login.php";
    private Map<String, String> parameters;

    public LoginRequst(String ID, String PW, Response.Listener<String> listener){
        super(Method.POST, URL, listener, null);
        parameters = new HashMap<>();
        parameters.put("ID",ID);
        parameters.put("PW",PW);
    }

    @Override
    public Map<String, String> getParams() { return parameters; }
}

package com.hfad.myplanner;


import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class RegisterRequest extends StringRequest {

    final static private String URL = "http://imhost.iptime.org/Register.php";
    private Map<String, String> parameters;

    public RegisterRequest(String ID, String PW,String U_NAME, Response.Listener<String> listener){
        super(Method.POST, URL, listener, null);
        parameters = new HashMap<>();
        parameters.put("ID",ID);
        parameters.put("PW",PW);
        parameters.put("U_NAME",U_NAME);
    }

    @Override
    public Map<String, String> getParams() { return parameters; }
}

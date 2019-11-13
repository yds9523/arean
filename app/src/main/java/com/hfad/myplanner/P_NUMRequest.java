package com.hfad.myplanner;


import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class P_NUMRequest extends StringRequest {

    final static private String URL = "http://imhost.iptime.org/P_NUM.php";
    private Map<String, String> parameters;

    public P_NUMRequest(int U_NUM, Response.Listener<String> listener){
        super(Method.POST, URL, listener, null);
        parameters = new HashMap<>();
        parameters.put("U_NUM",U_NUM + "");
    }

    @Override
    public Map<String, String> getParams() { return parameters; }
}
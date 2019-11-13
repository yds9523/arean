package com.hfad.myplanner;


import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class C_numContest extends StringRequest {

    final static private String URL = "http://imhost.iptime.org/C_NUMcontest.php";
    private Map<String, String> parameters;

    public C_numContest(String C_NUM, Response.Listener<String> listener){
        super(Method.POST, URL, listener, null);
        parameters = new HashMap<>();
        parameters.put("C_NUM",C_NUM + "");
    }

    @Override
    public Map<String, String> getParams() { return parameters; }
}

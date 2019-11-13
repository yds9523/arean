package com.hfad.myplanner;


import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class SelectMatchRequest extends StringRequest {

    final static private String URL = "http://imhost.iptime.org/AddMatch.php";
    private Map<String, String> parameters;

    public SelectMatchRequest(String C_NUM,String date,String title,String t1, String t2, Response.Listener<String> listener){
        super(Method.POST, URL, listener, null);
        parameters = new HashMap<>();
        parameters.put("C_NUM",C_NUM + "");
        parameters.put("DATE",date);
        parameters.put("TITLE",title);
        parameters.put("T1",t1);
        parameters.put("T2",t2);


    }

    @Override
    public Map<String, String> getParams() { return parameters; }
}

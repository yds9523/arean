package com.hfad.myplanner;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class SearchRequest extends StringRequest {

    final static private String URL = "http://imhost.iptime.org/SearchContest.php";
    private Map<String, String> parameters;

    public SearchRequest(String cname, String category,  Response.Listener<String> listener){
        super(Method.POST, URL, listener, null);
        parameters = new HashMap<>();
        parameters.put("C_NAME",cname);
        parameters.put("CATEGORY",category);
    }


    @Override
    public Map<String, String> getParams() { return parameters; }
}

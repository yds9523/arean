package com.hfad.myplanner;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class ContestListRequest extends StringRequest {

    final static private String URL = "http://imhost.iptime.org/ListContesst.php";
    private Map<String, String> parameters;


    public ContestListRequest(Response.Listener<String> listener){
        super(Method.POST, URL, listener, null);
        parameters = new HashMap<>();
    }


    @Override
    public Map<String, String> getParams() { return parameters; }
}

package com.hfad.myplanner;


import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class CommentRequest extends StringRequest {

    final static private String URL = "http://imhost.iptime.org/ListComment.php";
    private Map<String, String> parameters;

    public CommentRequest(int B_NUM, Response.Listener<String> listener){
        super(Method.POST, URL, listener, null);
        parameters = new HashMap<>();
        parameters.put("B_NUM",B_NUM + "");
    }

    @Override
    public Map<String, String> getParams() { return parameters; }
}

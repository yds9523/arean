package com.hfad.myplanner;


import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class DeleteCommentRequest extends StringRequest {

    final static private String URL = "http://imhost.iptime.org/DeleteComment.php";
    private Map<String, String> parameters;

    public DeleteCommentRequest(int CM_NUM ,Response.Listener<String> listener){
        super(Method.POST, URL, listener, null);
        parameters = new HashMap<>();
        parameters.put("CM_NUM",CM_NUM +"");
    }

    @Override
    public Map<String, String> getParams() { return parameters; }
}

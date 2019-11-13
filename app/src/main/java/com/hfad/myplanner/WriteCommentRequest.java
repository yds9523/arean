package com.hfad.myplanner;


import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class WriteCommentRequest extends StringRequest {

    final static private String URL = "http://imhost.iptime.org/CreateComment.php";
    private Map<String, String> parameters;

    public WriteCommentRequest(String CM_TEXT,int B_NUM,int U_NUN,String date ,Response.Listener<String> listener){
        super(Method.POST, URL, listener, null);
        parameters = new HashMap<>();
        parameters.put("CM_TEXT",CM_TEXT);
        parameters.put("B_NUM",B_NUM + "");
        parameters.put("U_NUN",U_NUN + "");
        parameters.put("CM_DATE",date + "");
    }

    @Override
    public Map<String, String> getParams() { return parameters; }
}

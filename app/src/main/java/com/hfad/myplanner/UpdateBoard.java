package com.hfad.myplanner;


import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class UpdateBoard extends StringRequest {

    final static private String URL = "http://imhost.iptime.org/UpdateBoard.php";
    private Map<String, String> parameters;

    public UpdateBoard(int B_NUM, String title, String text, Response.Listener<String> listener){
        super(Method.POST, URL, listener, null);
        parameters = new HashMap<>();
        parameters.put("B_NUM",B_NUM + "");
        parameters.put("title",title);
        parameters.put("text",text);
    }

    @Override
    public Map<String, String> getParams() { return parameters; }
}
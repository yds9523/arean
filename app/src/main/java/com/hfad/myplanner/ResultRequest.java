package com.hfad.myplanner;


import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class ResultRequest extends StringRequest {

    final static private String URL = "http://imhost.iptime.org/ResultMatch.php";
    private Map<String, String> parameters;

    public ResultRequest(int G_NUM,int r1, int r2, String result,int to,String C_NUM,String winner, Response.Listener<String> listener){
        super(Method.POST, URL, listener, null);
        parameters = new HashMap<>();
        parameters.put("G_NUM",G_NUM + "");
        parameters.put("R1",r1+ "");
        parameters.put("R2",r2 + "");
        parameters.put("RESULT",result);
        parameters.put("to",to + "");
        parameters.put("C_NUM",C_NUM + "");
        parameters.put("winner",winner);
    }

    @Override
    public Map<String, String> getParams() { return parameters; }
}

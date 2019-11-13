
package com.hfad.myplanner;


import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class UpdateMatchRequest extends StringRequest {

    final static private String URL = "http://imhost.iptime.org/UpdateMatch.php";
    private Map<String, String> parameters;

    public UpdateMatchRequest(int G_NUM, String title, String date, String t1, String t2,Response.Listener<String> listener){
        super(Method.POST, URL, listener, null);
        parameters = new HashMap<>();
        parameters.put("G_NUM", G_NUM + "");
        parameters.put("TITLE",title);
        parameters.put("DATE",date);
        parameters.put("T1",t1);
        parameters.put("T2",t2);
    }

    @Override
    public Map<String, String> getParams() { return parameters; }
}

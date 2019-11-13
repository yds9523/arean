
package com.hfad.myplanner;


import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class TeamRequest extends StringRequest {

    final static private String URL = "http://imhost.iptime.org/ListTeam.php";
    private Map<String, String> parameters;

    public TeamRequest(String C_NUM, Response.Listener<String> listener){
        super(Method.POST, URL, listener, null);
        parameters = new HashMap<>();
        parameters.put("C_NUM", C_NUM + "");
    }

    @Override
    public Map<String, String> getParams() { return parameters; }
}

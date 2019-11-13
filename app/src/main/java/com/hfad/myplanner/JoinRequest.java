
package com.hfad.myplanner;


import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class JoinRequest extends StringRequest {

    final static private String URL = "http://imhost.iptime.org/JoinContest.php";
    private Map<String, String> parameters;

    public JoinRequest(String P_NAME,int U_NUM, String C_NUM, String T_NUM, Response.Listener<String> listener){
        super(Method.POST, URL, listener, null);
        parameters = new HashMap<>();
        parameters.put("P_NAME", P_NAME);
        parameters.put("U_NUM", U_NUM + "");
        parameters.put("C_NUM", C_NUM + "");
        parameters.put("T_NUM", T_NUM + "");
    }

    @Override
    public Map<String, String> getParams() { return parameters; }
}
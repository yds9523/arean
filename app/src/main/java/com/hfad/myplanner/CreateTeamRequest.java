
package com.hfad.myplanner;


import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class CreateTeamRequest extends StringRequest {

    final static private String URL = "http://imhost.iptime.org/CreateTeam.php";
    private Map<String, String> parameters;

    public CreateTeamRequest(String C_NUM,String T_NAME, Response.Listener<String> listener){
        super(Method.POST, URL, listener, null);
        parameters = new HashMap<>();
        parameters.put("C_NUM", C_NUM + "");
        parameters.put("T_NAME",T_NAME);
    }

    @Override
    public Map<String, String> getParams() { return parameters; }
}

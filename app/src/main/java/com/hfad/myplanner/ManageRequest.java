
package com.hfad.myplanner;


import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class ManageRequest extends StringRequest {

    final static private String URL = "http://imhost.iptime.org/ManageContest.php";
    private Map<String, String> parameters;

    public ManageRequest(int U_NUM, Response.Listener<String> listener){
        super(Method.POST, URL, listener, null);
        parameters = new HashMap<>();
        parameters.put("U_NUM", U_NUM + "");
    }

    @Override
    public Map<String, String> getParams() { return parameters; }
}

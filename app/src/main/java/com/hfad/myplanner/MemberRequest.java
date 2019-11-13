
package com.hfad.myplanner;


import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class MemberRequest extends StringRequest {

    final static private String URL = "http://imhost.iptime.org/ListMember.php";
    private Map<String, String> parameters;

    public MemberRequest(String C_NUM,int c, Response.Listener<String> listener){
        super(Method.POST, URL, listener, null);
        parameters = new HashMap<>();
        parameters.put("C_NUM", C_NUM + "");
        parameters.put("c", c + "");

    }

    @Override
    public Map<String, String> getParams() { return parameters; }
}

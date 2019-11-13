
package com.hfad.myplanner;


import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class ContestRequest extends StringRequest {

    final static private String URL = "http://imhost.iptime.org/CreateContest.php";
    private Map<String, String> parameters;

    public ContestRequest(int U_NUM, String C_NAME, String CATEGORY,String CSDATE, String JSDATE, String JEDATE, String C_TEXT,String FORM, String MEMBER,String ADDRESS, Response.Listener<String> listener){
        super(Method.POST, URL, listener, null);
        parameters = new HashMap<>();
        parameters.put("U_NUM", U_NUM + "");
        parameters.put("C_NAME",C_NAME);
        parameters.put("CATEGORY",CATEGORY);
        parameters.put("CSDATE",CSDATE);
        parameters.put("JSDATE",JSDATE);
        parameters.put("JEDATE",JEDATE);
        parameters.put("C_TEXT",C_TEXT);
        parameters.put("FORM",FORM);
        parameters.put("MEMBER",MEMBER);
        parameters.put("ADDRESS",ADDRESS);

    }

    @Override
    public Map<String, String> getParams() { return parameters; }
}

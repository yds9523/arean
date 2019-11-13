
package com.hfad.myplanner;


import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class UpdateContestRequest extends StringRequest {

    final static private String URL = "http://imhost.iptime.org/UpdateContest.php";
    private Map<String, String> parameters;

    public UpdateContestRequest(String C_NUM, String C_NAME, String CATEGORY,String CSDATE, String JSDATE, String JEDATE, String C_TEXT,String FORM, String MEMBER, String ADRRESS,Response.Listener<String> listener){
        super(Method.POST, URL, listener, null);
        parameters = new HashMap<>();
        parameters.put("C_NUM", C_NUM + "");
        parameters.put("C_NAME",C_NAME);
        parameters.put("CATEGORY",CATEGORY);
        parameters.put("CSDATE",CSDATE);
        parameters.put("JSDATE",JSDATE);
        parameters.put("JEDATE",JEDATE);
        parameters.put("C_TEXT",C_TEXT);
        parameters.put("FORM",FORM);
        parameters.put("MEMBER",MEMBER);
        parameters.put("ADDRESS",ADRRESS);

    }

    @Override
    public Map<String, String> getParams() { return parameters; }
}

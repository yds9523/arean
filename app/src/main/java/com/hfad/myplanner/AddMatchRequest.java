package com.hfad.myplanner;


        import com.android.volley.Response;
        import com.android.volley.toolbox.StringRequest;

        import java.util.HashMap;
        import java.util.Map;

public class AddMatchRequest extends StringRequest {

    final static private String URL = "http://imhost.iptime.org/SelectMatch.php";
    private Map<String, String> parameters;

    public AddMatchRequest(String C_NUM,int member, Response.Listener<String> listener){
        super(Method.POST, URL, listener, null);
        parameters = new HashMap<>();
        parameters.put("C_NUM",C_NUM + "");
        parameters.put("MEMBER",member + "");

    }

    @Override
    public Map<String, String> getParams() { return parameters; }
}

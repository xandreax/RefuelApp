package dia.units.refuelapp.volley;

import androidx.annotation.Nullable;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;
import java.util.HashMap;
import java.util.Map;

public class InputStreamVolleyRequest extends Request<byte[]> {
    private final Response.Listener<byte[]> listener;
    private Map<String, String> params;
    public Map<String, String> responseHeaders ;

    public InputStreamVolleyRequest(int method, String url, HashMap<String, String> params, Response.Listener<byte[]> listener, @Nullable Response.ErrorListener errorListener) {
        super(method, url, errorListener);
        setShouldCache(false);
        this.listener = listener;
        this.params=params;
    }

    @Override
    protected Map<String, String> getParams() {
        return params;
    };

    @Override
    protected Response<byte[]> parseNetworkResponse(NetworkResponse response) {
        responseHeaders = response.headers;
        return Response.success(response.data, HttpHeaderParser.parseCacheHeaders(response));
    }

    @Override
    protected void deliverResponse(byte[] response) {
        listener.onResponse(response);
    }
}

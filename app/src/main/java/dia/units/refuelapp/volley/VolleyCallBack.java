package dia.units.refuelapp.volley;

import java.util.List;

public interface VolleyCallBack<T> {
    void onSuccess(List<T> list);
    void onFailure(Throwable t);
}

package dia.units.refuelapp.data;

public interface RepositoryCallback<T> {
    void onComplete(T result);
    void onFailure(Throwable t);
}

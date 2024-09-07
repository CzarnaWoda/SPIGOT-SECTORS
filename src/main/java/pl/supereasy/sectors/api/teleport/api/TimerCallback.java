package pl.supereasy.sectors.api.teleport.api;

public interface TimerCallback<E> {
    void success();

    void error();
}
package pl.supereasy.sectors.threads.api;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

public class ThreadExecutorAPI {

    public final static ScheduledExecutorService SCHEDULED = Executors.newScheduledThreadPool(4);
    public final static ScheduledExecutorService EXECUTOR = Executors.newScheduledThreadPool(2);


}

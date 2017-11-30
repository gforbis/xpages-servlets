package adamantium.servlets;

import java.io.Serializable;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

public class TaskManager implements Serializable {
    private static final long serialVersionUID = 1L;
    private static final int MAX_THREADS = 10;
    private final ExecutorService service = Executors.newFixedThreadPool(MAX_THREADS);

    public void execute(final Runnable job, final long maxRuntime, final TimeUnit units) {
        Future<?> f = this.service.submit(job);
        Timer timer = new Timer();
        timer.schedule(new Timeout(f), units.toMillis(maxRuntime));
    }

    protected void finalise() throws Throwable {
        if ((null != this.service) && !this.service.isTerminated()) {
            this.service.shutdown();
        }
        super.finalize();
    }

    private class Timeout extends TimerTask {
        private final Future<?> future;

        public Timeout(final Future<?> future) {
            this.future = future;
        }

        @Override
        public void run() {
            if (!this.future.isDone()) {
                this.future.cancel(true);
            }
        }
    }

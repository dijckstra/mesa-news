package inc.mesa.mesanews.executors;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class DiskThreadExecutor implements Executor {

    private Executor diskExecutor;

    public DiskThreadExecutor() {
        diskExecutor = Executors.newSingleThreadExecutor();
    }

    @Override
    public void execute(final Runnable command) {
        diskExecutor.execute(command);
    }
}

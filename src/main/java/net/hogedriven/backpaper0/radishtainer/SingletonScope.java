package net.hogedriven.backpaper0.radishtainer;

import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

public class SingletonScope implements Scope {

    private ConcurrentMap<Class<?>, FutureTask<Object>> tasks = new ConcurrentHashMap<>();

    @Override
    public Object getInstance(final Container container, final Class<?> impl) {
        Callable<Object> callable = new Callable<Object>() {
            @Override
            public Object call() throws Exception {
                Object instance = container.newInstance(impl);
                container.inject(instance);
                return instance;
            }
        };
        FutureTask<Object> newTask = new FutureTask<>(callable);
        FutureTask<Object> task = tasks.putIfAbsent(impl, newTask);
        if (task == null) {
            task = newTask;
            task.run();
        }
        try {
            return task.get();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException(e);
        } catch (ExecutionException e) {
            throw new RuntimeException(e.getCause());
        }
    }
}

package net.hogedriven.backpaper0.radishtainer;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Stage;
import java.util.HashSet;
import java.util.Set;

public class GuiceTest extends ContainerTest {

    @Override
    protected Container newContainer() {
        return new Container() {
            private Set<Class<?>> types = new HashSet<>();

            private Injector injector;

            @Override
            public <T> void add(Class<T> type) {
                types.add(type);
            }

            @Override
            public <T> T getInstance(Class<T> type) {
                return getInjector().getInstance(type);
            }

            @Override
            public void inject(Object target) {
                getInjector().injectMembers(target);
            }

            private Injector getInjector() {
                if (injector == null) {
                    injector = Guice.createInjector(Stage.PRODUCTION, new AbstractModule() {
                        @Override
                        protected void configure() {
                            for (Class<?> type : types) {
                                bind(type);
                            }
                        }
                    });
                }
                return injector;
            }
        };
    }
}

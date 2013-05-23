package net.hogedriven.backpaper0.radishtainer;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Stage;
import java.util.ArrayList;
import java.util.List;

public class GuiceTest extends ContainerTest {

    @Override
    protected Container newContainer() {
        return new Container() {
            class TypeAndImpl<T> {

                Class<T> type;

                Class<? extends T> impl;

            }
            private List<TypeAndImpl<?>> types = new ArrayList<>();

            private Injector injector;

            @Override
            public <T> void add(Class<T> type, Class<? extends T> impl) {
                TypeAndImpl<T> tai = new TypeAndImpl();
                tai.type = type;
                tai.impl = impl;
                types.add(tai);
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
                            for (TypeAndImpl<?> tai : types) {
                                bind(tai);
                            }
                        }

                        private <T> void bind(TypeAndImpl<T> tai) {
                            if (tai.impl != null) {
                                bind(tai.type).to(tai.impl);
                            } else {
                                bind(tai.type);
                            }
                        }
                    });
                }
                return injector;
            }
        };
    }
}

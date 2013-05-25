package net.hogedriven.backpaper0.radishtainer;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Key;
import com.google.inject.Stage;
import com.google.inject.binder.LinkedBindingBuilder;
import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.List;
import org.junit.Ignore;

public class GuiceTest extends ContainerTest {

    @Ignore
    @Override
    public void test_addInstance() throws Exception {
    }

    @Override
    protected Container newContainer() {
        return new Container() {
            class TypeAndImpl<T> {

                Class<T> type;

                Annotation qualifier;

                Class<? extends T> impl;

            }
            private List<TypeAndImpl<?>> types = new ArrayList<>();

            private Injector injector;

            @Override
            public <T> void add(Class<T> type, Annotation qualifier, Class<? extends T> impl) {
                TypeAndImpl<T> tai = new TypeAndImpl();
                tai.type = type;
                tai.qualifier = qualifier;
                tai.impl = impl;
                types.add(tai);
            }

            @Override
            public <T> T getInstance(Class<T> type, Annotation qualifier) {
                Key<T> key;
                if (qualifier != null) {
                    key = Key.get(type, qualifier);
                } else {
                    key = Key.get(type);
                }
                return getInjector().getInstance(key);
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
                            LinkedBindingBuilder<T> builder;
                            if (tai.qualifier != null) {
                                builder = bind(Key.get(tai.type, tai.qualifier));
                            } else {
                                builder = bind(Key.get(tai.type));
                            }
                            if (tai.impl != null) {
                                builder.to(tai.impl);
                            }
                        }
                    });
                }
                return injector;
            }
        };
    }
}

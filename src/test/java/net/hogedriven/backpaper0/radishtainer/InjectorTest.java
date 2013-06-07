package net.hogedriven.backpaper0.radishtainer;

import java.lang.annotation.Annotation;
import javax.inject.Named;
import net.hogedriven.backpaper0.radishtainer.test.Aaa;
import net.hogedriven.backpaper0.radishtainer.test.Iii1;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class InjectorTest {

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Iii1
    @Named
    Object withQualifiers;

    @Test
    public void test_no_more_than_one_qualifier() throws Exception {
        Injector injector = new Injector() {
            @Override
            public Object inject(Container container, Object target) {
                return null;
            }
        };
        Container container = null;
        Annotation[] annotations = InjectorTest.class.getDeclaredField("withQualifiers").getAnnotations();
        expectedException.expect(IllegalArgumentException.class);
        injector.getDependency(container, Aaa.class, Aaa.class, annotations);
    }
}

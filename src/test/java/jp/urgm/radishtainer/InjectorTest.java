package jp.urgm.radishtainer;

import java.lang.annotation.Annotation;
import javax.inject.Named;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import jp.urgm.radishtainer.Container;
import jp.urgm.radishtainer.Injector;
import jp.urgm.radishtainer.test.Aaa;
import jp.urgm.radishtainer.test.Iii1;

public class InjectorTest {

    @Rule
    public ExpectedException expectedException = ExpectedException.none();
    @Iii1
    @Named
    Object withQualifiers;

    @Test
    public void test_no_more_than_one_qualifier() throws Exception {
        Container container = null;
        Injector injector = new Injector(container);
        Annotation[] annotations = InjectorTest.class.getDeclaredField("withQualifiers").getAnnotations();
        expectedException.expect(IllegalArgumentException.class);
        injector.getDependency(Aaa.class, Aaa.class, annotations);
    }
}

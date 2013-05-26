package net.hogedriven.backpaper0.radishtainer;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.reflect.Method;
import net.hogedriven.backpaper0.radishtainer.test.Eee1;
import net.hogedriven.backpaper0.radishtainer.test.Eee2;
import net.hogedriven.backpaper0.radishtainer.test.sub.Eee3;
import org.junit.Test;
import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;

@RunWith(Enclosed.class)
public class MethodInjectorTest {

    @Data(superClass = Eee1.class,
            subClass = Eee2.class,
            packagePrivateMethodExpected = true,
            protectedMethodExpected = true,
            publicMethodExpected = true)
    public static class SamePackage extends Base {
    }

    @Data(superClass = Eee1.class,
            subClass = Eee3.class,
            protectedMethodExpected = true,
            publicMethodExpected = true)
    public static class DiffPackage extends Base {
    }

    @Data(superClass = Eee2.class,
            subClass = Eee3.class)
    public static class NoInheritance extends Base {
    }

    @Data(superClass = Eee1.class,
            subClass = Eee1.class)
    public static class SameClass extends Base {
    }

    @Ignore
    @Retention(RetentionPolicy.RUNTIME)
    public @interface Data {

        Class<?> superClass();

        Class<?> subClass();

        boolean privateMethodExpected() default false;

        boolean packagePrivateMethodExpected() default false;

        boolean protectedMethodExpected() default false;

        boolean publicMethodExpected() default false;
    }

    @Ignore
    public static abstract class Base {

        private Class<?> superClass;

        private Class<?> subClass;

        private boolean privateMethodExpected;

        private boolean packagePrivateMethodExpected;

        private boolean protectedMethodExpected;

        private boolean publicMethodExpected;

        @Before
        public void setUp() throws Exception {
            Data data = getClass().getAnnotation(Data.class);
            superClass = data.superClass();
            subClass = data.subClass();
            privateMethodExpected = data.privateMethodExpected();
            packagePrivateMethodExpected = data.packagePrivateMethodExpected();
            protectedMethodExpected = data.protectedMethodExpected();
            publicMethodExpected = data.publicMethodExpected();
        }

        @Test
        public void test_privateMethod() throws Exception {
            Method superMethod = superClass.getDeclaredMethod("privateMethod");
            Method subMethod = subClass.getDeclaredMethod("privateMethod");
            assertThat(Container.isOverrideForm(subMethod, superMethod), is(privateMethodExpected));
        }

        @Test
        public void test_packagePrivateMethod() throws Exception {
            Method superMethod = superClass.getDeclaredMethod("packagePrivateMethod");
            Method subMethod = subClass.getDeclaredMethod("packagePrivateMethod");
            assertThat(Container.isOverrideForm(subMethod, superMethod), is(packagePrivateMethodExpected));
        }

        @Test
        public void test_protectedMethod() throws Exception {
            Method superMethod = superClass.getDeclaredMethod("protectedMethod");
            Method subMethod = subClass.getDeclaredMethod("protectedMethod");
            assertThat(Container.isOverrideForm(subMethod, superMethod), is(protectedMethodExpected));
        }

        @Test
        public void test_publicMethod() throws Exception {
            Method superMethod = superClass.getDeclaredMethod("publicMethod");
            Method subMethod = subClass.getDeclaredMethod("publicMethod");
            assertThat(Container.isOverrideForm(subMethod, superMethod), is(publicMethodExpected));
        }
    }
}
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
            MethodInjector superInjector = newInjector(superClass, "privateMethod");
            MethodInjector subInjector = newInjector(subClass, "privateMethod");
            assertThat(subInjector.isOverrideForm(superInjector), is(privateMethodExpected));
        }

        @Test
        public void test_packagePrivateMethod() throws Exception {
            MethodInjector superInjector = newInjector(superClass, "packagePrivateMethod");
            MethodInjector subInjector = newInjector(subClass, "packagePrivateMethod");
            assertThat(subInjector.isOverrideForm(superInjector), is(packagePrivateMethodExpected));
        }

        @Test
        public void test_protectedMethod() throws Exception {
            MethodInjector superInjector = newInjector(superClass, "protectedMethod");
            MethodInjector subInjector = newInjector(subClass, "protectedMethod");
            assertThat(subInjector.isOverrideForm(superInjector), is(protectedMethodExpected));
        }

        @Test
        public void test_publicMethod() throws Exception {
            MethodInjector superInjector = newInjector(superClass, "publicMethod");
            MethodInjector subInjector = newInjector(subClass, "publicMethod");
            assertThat(subInjector.isOverrideForm(superInjector), is(publicMethodExpected));
        }

        private MethodInjector newInjector(Class<?> clazz, String methodName) throws Exception {
            Method method = clazz.getDeclaredMethod(methodName);
            return new MethodInjector(method);
        }
    }
}
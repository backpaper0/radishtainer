package jp.urgm.radishtainer;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

import org.junit.Test;
import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;

import jp.urgm.radishtainer.ClassInfo;
import jp.urgm.radishtainer.test.Eee1;
import jp.urgm.radishtainer.test.Eee2;
import jp.urgm.radishtainer.test.Vvv;
import jp.urgm.radishtainer.test.sub.Eee3;

@RunWith(Enclosed.class)
public class ClassInfoTest {

    public static class isInjectable_Test {

        @Test
        public void test_injectableField() throws Exception {
            Field field = Vvv.class.getDeclaredField("injectableField");
            assertThat(ClassInfo.isInjectable(field), is(true));
        }

        @Test
        public void test_noAtInjectField() throws Exception {
            Field field = Vvv.class.getDeclaredField("noAtInjectField");
            assertThat(ClassInfo.isInjectable(field), is(false));
        }

        @Test
        public void test_finalField() throws Exception {
            Field field = Vvv.class.getDeclaredField("finalField");
            assertThat(ClassInfo.isInjectable(field), is(false));
        }

        @Test
        public void test_injectableMethod() throws Exception {
            Method method = Vvv.class.getDeclaredMethod("injectableMethod");
            assertThat(ClassInfo.isInjectable(method), is(true));
        }

        @Test
        public void test_noAtInjectMethod() throws Exception {
            Method method = Vvv.class.getDeclaredMethod("noAtInjectMethod");
            assertThat(ClassInfo.isInjectable(method), is(false));
        }

        @Test
        public void test_abstractMethod() throws Exception {
            Method method = Vvv.class.getDeclaredMethod("abstractMethod");
            assertThat(ClassInfo.isInjectable(method), is(false));
        }
    }

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
            assertThat(ClassInfo.isOverridden(subMethod, superMethod), is(privateMethodExpected));
        }

        @Test
        public void test_packagePrivateMethod() throws Exception {
            Method superMethod = superClass.getDeclaredMethod("packagePrivateMethod");
            Method subMethod = subClass.getDeclaredMethod("packagePrivateMethod");
            assertThat(ClassInfo.isOverridden(subMethod, superMethod), is(packagePrivateMethodExpected));
        }

        @Test
        public void test_protectedMethod() throws Exception {
            Method superMethod = superClass.getDeclaredMethod("protectedMethod");
            Method subMethod = subClass.getDeclaredMethod("protectedMethod");
            assertThat(ClassInfo.isOverridden(subMethod, superMethod), is(protectedMethodExpected));
        }

        @Test
        public void test_publicMethod() throws Exception {
            Method superMethod = superClass.getDeclaredMethod("publicMethod");
            Method subMethod = subClass.getDeclaredMethod("publicMethod");
            assertThat(ClassInfo.isOverridden(subMethod, superMethod), is(publicMethodExpected));
        }
    }
}
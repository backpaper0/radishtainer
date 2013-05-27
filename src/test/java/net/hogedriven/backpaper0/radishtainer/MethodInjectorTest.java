package net.hogedriven.backpaper0.radishtainer;

import java.lang.reflect.Method;
import net.hogedriven.backpaper0.radishtainer.test.Vvv;
import static org.hamcrest.CoreMatchers.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class MethodInjectorTest {

    @Test
    public void test_injectableMethod() throws Exception {
        Method method = Vvv.class.getDeclaredMethod("injectableMethod");
        MethodInjector injector = new MethodInjector(method);
        assertThat(injector.isInjectable(), is(true));
    }

    @Test
    public void test_noAtInjectMethod() throws Exception {
        Method method = Vvv.class.getDeclaredMethod("noAtInjectMethod");
        MethodInjector injector = new MethodInjector(method);
        assertThat(injector.isInjectable(), is(false));
    }

    @Test
    public void test_abstractMethod() throws Exception {
        Method method = Vvv.class.getDeclaredMethod("abstractMethod");
        MethodInjector injector = new MethodInjector(method);
        assertThat(injector.isInjectable(), is(false));
    }
}

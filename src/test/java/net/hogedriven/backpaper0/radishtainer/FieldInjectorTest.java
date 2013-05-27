package net.hogedriven.backpaper0.radishtainer;

import java.lang.reflect.Field;
import net.hogedriven.backpaper0.radishtainer.test.Vvv;
import static org.hamcrest.CoreMatchers.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class FieldInjectorTest {

    @Test
    public void test_injectableField() throws Exception {
        Field field = Vvv.class.getDeclaredField("injectableField");
        FieldInjector injector = new FieldInjector(field);
        assertThat(injector.isInjectable(), is(true));
    }

    @Test
    public void test_noAtInjectField() throws Exception {
        Field field = Vvv.class.getDeclaredField("noAtInjectField");
        FieldInjector injector = new FieldInjector(field);
        assertThat(injector.isInjectable(), is(false));
    }

    @Test
    public void test_finalField() throws Exception {
        Field field = Vvv.class.getDeclaredField("finalField");
        FieldInjector injector = new FieldInjector(field);
        assertThat(injector.isInjectable(), is(false));
    }
}

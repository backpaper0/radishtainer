package net.hogedriven.backpaper0.radishtainer;

import java.util.List;
import net.hogedriven.backpaper0.radishtainer.test.Www;
import net.hogedriven.backpaper0.radishtainer.test.WwwProvider;
import org.junit.Test;
import static org.junit.Assert.*;

public class BindingTest {

    @Test
    public void test_provider_binding() throws Exception {
        WwwProvider provider = new WwwProvider();
        Binding binding = Binding.newProviderBinding(provider);
        ClassInfo classInfo = binding.getClassInfo();
        List<Class<?>> classes = classInfo.getClasses();
        Class<?> clazz = classes.get(classes.size() - 1);
        assertTrue(clazz == Www.class);
    }
}

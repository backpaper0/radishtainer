package jp.urgm.radishtainer;

import java.util.List;

import org.junit.Test;

import jp.urgm.radishtainer.Binding;
import jp.urgm.radishtainer.ClassInfo;
import jp.urgm.radishtainer.test.Www;
import jp.urgm.radishtainer.test.WwwProvider;

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

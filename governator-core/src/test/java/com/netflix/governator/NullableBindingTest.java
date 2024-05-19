package com.netflix.governator;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import jakarta.annotation.Nullable;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;

import org.junit.Before;
import org.junit.Test;

import com.google.inject.AbstractModule;
import com.google.inject.Injector;
import com.google.inject.util.Providers;
import com.netflix.governator.event.guava.GuavaApplicationEventModule;

public class NullableBindingTest {

    private Injector injector;
    
    @Before
    public void setup() {
        injector = InjectorBuilder.fromModules(new GuavaApplicationEventModule(), new AbstractModule() {
            @Override
            protected void configure() {
                bind(InnerDependency.class).toProvider(Providers.<InnerDependency>of(null));               
            }
        }).createInjector();
    }
    
    @Test
    public void test() {
        OuterDependency instance = injector.getInstance(OuterDependency.class);
        assertNotNull(instance);
        assertNull(instance.innerDependency);
    }


    private static class OuterDependency {

        InnerDependency innerDependency;
        
        @Inject
        public OuterDependency(@Nullable InnerDependency innerDependency) {
            this.innerDependency = innerDependency;
        }
    }

    @Singleton
    private static class InnerDependency {

    }
}



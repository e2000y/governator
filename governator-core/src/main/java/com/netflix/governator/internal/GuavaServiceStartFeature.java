package com.netflix.governator.internal;

import com.google.common.util.concurrent.Service;

/**
 * Special LifecycleFeature to support Guava service lifecycle - start
 * 
 * @author e2000y
 */
public final class GuavaServiceStartFeature extends GuavaServiceLifecycleFeature {

    @Override
    public String toString() {
        return super.toString() + "start";
    }

    @Override
    protected void action(Service svc)
        throws Exception {
      svc.startAsync();
    }
}


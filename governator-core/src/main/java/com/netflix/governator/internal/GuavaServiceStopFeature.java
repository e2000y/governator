package com.netflix.governator.internal;

import com.google.common.util.concurrent.Service;

/**
 * Special LifecycleFeature to support Guava service lifecycle - stop
 * 
 * @author e2000y
 */
public final class GuavaServiceStopFeature extends GuavaServiceLifecycleFeature {

    @Override
    public String toString() {
        return super.toString() + "stop";
    }

    @Override
    protected void action(Service svc)
        throws Exception {
      svc.stopAsync();
    }
}


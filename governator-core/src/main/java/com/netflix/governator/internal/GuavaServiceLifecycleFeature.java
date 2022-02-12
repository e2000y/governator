package com.netflix.governator.internal;

import java.util.List;
import java.util.ArrayList;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.google.common.util.concurrent.Service;

import com.netflix.governator.LifecycleAction;
import com.netflix.governator.LifecycleFeature;

import org.slf4j.*;

/**
 * Special LifecycleFeature to support Guava service lifecycle
 * 
 * @author e2000y
 */
public abstract class GuavaServiceLifecycleFeature implements LifecycleFeature {

    private final static Logger logger = LoggerFactory.getLogger(GuavaServiceLifecycleFeature.class);
    private final static ExecutorService executor = Executors.newSingleThreadExecutor();

    //  log the service state
    static class GuavaServiceLogger extends Service.Listener {

        private final String svc;

        GuavaServiceLogger(Service svc) {
            this.svc = svc.toString();
        }

        @Override
        public void starting() {
            logger.info("Starting " + svc);
        }

        @Override
        public void running() {
            logger.info(svc + " is running");
        }

        @Override
        public void terminated(Service.State from) {
            logger.info(svc + " is terminated from " + from);
        }

        @Override
        public void stopping(Service.State from) {
            logger.info(svc + " is stopping from " + from);
        }

        @Override
        public void failed(Service.State from, Throwable failure) {
            logger.info(svc + " is failed from " + from, failure);
        }
    }

    @Override
    public List<LifecycleAction> getActionsForType(final Class<?> type) {
        List<LifecycleAction> ls = new ArrayList<LifecycleAction>();

        if (Service.class.isAssignableFrom(type)) {
            ls.add(new LifecycleAction() {
                @Override
                public void call(Object obj)
                    throws Exception {
                    Service svc = (Service) obj;

                    svc.addListener(new GuavaServiceLogger(svc), executor);

                    action(svc);
                }
            });
        }

        return ls;
    }

    @Override
    public String toString() {
        return "GuavaService-";
    }

    protected abstract void action(Service svc) throws Exception;
}


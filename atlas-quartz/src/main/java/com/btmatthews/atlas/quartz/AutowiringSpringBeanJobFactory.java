/*
 * Copyright 2011-2013 Brian Matthews
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.btmatthews.atlas.quartz;

import org.quartz.spi.TriggerFiredBundle;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.scheduling.quartz.SpringBeanJobFactory;

/**
 * Overrides {@link SpringBeanJobFactory} to auto-wire dependencies from the application context when creating job beans.
 *
 * @author <a href="mailto:brian@btmatthews.com">Brian Matthews</a>
 * @since 1.0.0
 */
public final class AutowiringSpringBeanJobFactory
        extends SpringBeanJobFactory
        implements ApplicationContextAware {

    /**
     * The bean factory.
     */
    private transient AutowireCapableBeanFactory beanFactory;

    /**
     * Used to inject the application context which we use to get the bean factory.
     *
     * @param context The application context.
     */
    public void setApplicationContext(
            final ApplicationContext context) {
        beanFactory = context.getAutowireCapableBeanFactory();
    }

    /**
     * Creates instances of the Quartz job and then uses the {@link #beanFactory} to auto-wire its dependencies.
     *
     * @param bundle The job configuration.
     * @return The auto-wired job bean.
     * @throws Exception If there was a problem instantiating the job bean.
     */
    @Override
    protected Object createJobInstance(
            final TriggerFiredBundle bundle)
            throws Exception {
        final Object job = super.createJobInstance(bundle);
        beanFactory.autowireBean(job);
        return job;
    }
}
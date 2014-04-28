/*
 * JBoss, Home of Professional Open Source
 * Copyright 2014, Red Hat Middleware LLC, and individual contributors
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.arquillian.spacelift.execution.impl;

import java.io.File;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import org.arquillian.spacelift.execution.Execution;
import org.arquillian.spacelift.execution.ExecutionService;
import org.arquillian.spacelift.execution.TimeoutExecutionException;

/**
 * Executor service which is able to execute external process as well as callables
 *
 * @author <a href="mailto:kpiwko@redhat.com">Karel Piwko</a>
 *
 */
public class ExecutionServiceImpl implements ExecutionService {

    private Map<String, String> environment;
    private File workingDirectory;
    private final ExecutorService service;
    private final ScheduledExecutorService scheduledService;

    public ExecutionServiceImpl() {
        this.service = Executors.newCachedThreadPool();
        this.scheduledService = Executors.newScheduledThreadPool(1);
        this.environment = new HashMap<String, String>();
    }

    @Override
    public ExecutionService setEnvironment(Map<String, String> environment) throws IllegalStateException {
        if (environment == null) {
            throw new IllegalStateException(
                "Environment properies map must not be null!");
        }
        this.environment = environment;
        return this;
    }

    @Override
    public ExecutionService setWorkingDirectory(String workingDirectory) throws IllegalArgumentException {
        if (workingDirectory == null) {
            this.workingDirectory = null;
            return this;
        }
        return setWorkingDirectory(new File(workingDirectory));
    }

    @Override
    public ExecutionService setWorkingDirectory(File workingDirectory) throws IllegalArgumentException {
        if (workingDirectory == null) {
            this.workingDirectory = null;
            return this;
        }
        if (!workingDirectory.exists()) {
            throw new IllegalArgumentException("Specified path " + workingDirectory.getAbsolutePath() + " does not exist!");
        }
        if (!workingDirectory.isDirectory()) {
            throw new IllegalArgumentException("Specified path " + workingDirectory.getAbsolutePath() + " is not a directory!");
        }

        this.workingDirectory = workingDirectory;
        return this;
    }

    @Override
    public Map<String, String> getEnvironment() {
        return Collections.unmodifiableMap(environment);
    }

    @Override
    public File getWorkingDirectory() {
        return workingDirectory;
    }

    @Override
    public <T> Execution<T> execute(Callable<T> task) throws org.arquillian.spacelift.execution.ExecutionException {
        Future<T> future = service.submit(task);
        return new FutureBasedExecution<T>(this, task, future);
    }

    @Override
    public <T> Execution<T> schedule(Callable<T> task, long delay, TimeUnit unit) throws TimeoutExecutionException,
        org.arquillian.spacelift.execution.ExecutionException {

        ScheduledFuture<T> future = scheduledService.schedule(task, delay, unit);
        return new FutureBasedExecution<T>(this, task, future);
    }

}

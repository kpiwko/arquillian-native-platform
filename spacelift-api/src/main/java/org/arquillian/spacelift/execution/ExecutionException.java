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
package org.arquillian.spacelift.execution;

import java.text.MessageFormat;

/**
 *
 * @author <a href="kpiwko@redhat.com">Karel Piwko</a>
 *
 */
public class ExecutionException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public ExecutionException(String messageFormat, Object... parameters) {
        super(MessageFormat.format(messageFormat, parameters));
    }

    public ExecutionException(Throwable cause, String messageFormat, Object... parameters) {
        super(MessageFormat.format(messageFormat, parameters), cause);
    }

    public ExecutionException prependMessage(String messageFormat, Object... parameters) {
        return new ExecutionException(this.getCause(), messageFormat + ". " + this.getMessage(), parameters);
    }

    public ExecutionException(Throwable cause) {
        super(cause);
    }

}

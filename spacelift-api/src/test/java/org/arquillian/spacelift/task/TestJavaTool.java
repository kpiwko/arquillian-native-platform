package org.arquillian.spacelift.task;

import java.util.List;

import org.apache.commons.lang3.SystemUtils;
import org.arquillian.spacelift.execution.ExecutionException;
import org.arquillian.spacelift.process.Command;
import org.arquillian.spacelift.process.CommandBuilder;
import org.arquillian.spacelift.task.Task;

class TestJavaTool extends Task<Object, Object> {

    private CommandBuilder builder;

    public TestJavaTool() {
        if (SystemUtils.IS_OS_LINUX || SystemUtils.IS_OS_MAC_OSX) {
            this.builder = new CommandBuilder("java");
        }
        else if (SystemUtils.IS_OS_WINDOWS) {
            this.builder = new CommandBuilder("java.exe");
        }
        else {
            throw new UnsupportedOperationException("Java tool is not supported for " + SystemUtils.OS_NAME);
        }
    }

    @Override
    protected Object process(Object input) throws ExecutionException {
        return new Object();
    }

    public TestJavaTool parameters(List<? extends CharSequence> parameters) {
        builder.parameters(parameters);
        return this;
    }

    public TestJavaTool parameters(CharSequence... parameters) {
        builder.parameters(parameters);
        return this;
    }

    public TestJavaTool parameter(CharSequence parameter) {
        builder.parameter(parameter);
        return this;
    }

    public TestJavaTool splitToParameters(CharSequence sequenceToBeParsed) {
        builder.splitToParameters(sequenceToBeParsed);
        return this;
    }

    public Command getCommand() {
        return builder.build();
    }
}
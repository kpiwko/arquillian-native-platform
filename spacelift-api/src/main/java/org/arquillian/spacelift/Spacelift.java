package org.arquillian.spacelift;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.arquillian.spacelift.Invokable.InvocationException;
import org.arquillian.spacelift.execution.ExecutionServiceFactory;
import org.arquillian.spacelift.task.InjectTask;
import org.arquillian.spacelift.task.Task;
import org.arquillian.spacelift.task.TaskRegistry;

public class Spacelift {

    public static <IN, OUT, TASK extends Task<? super IN, OUT>> TASK task(Class<TASK> taskDef) {
        return new SpaceliftInstance().registry().find(taskDef);
    }

    public static <IN, OUT, TASK extends Task<? super IN, OUT>> TASK task(IN input, Class<TASK> taskDef) {
        @SuppressWarnings("unchecked")
        InjectTask<IN> task = new SpaceliftInstance().registry().find(InjectTask.class);
        return task.passToNext(input).then(taskDef);
    }

    @SuppressWarnings({ "unchecked" })
    public static class SpaceliftInstance {
        private static final Logger log = Logger.getLogger(Spacelift.class.getName());

        private static ExecutionServiceFactory lastFactory;
        private static TaskRegistry registry;
        static {
            try {
                lastFactory = ImplementationLoader.implementationOf(ExecutionServiceFactory.class);
            } catch (InvocationException e) {
                log.log(Level.SEVERE,
                    "Unable to find default implemenation of {0} on classpath, make sure that you set one programatically.",
                    ExecutionServiceFactory.class.getName());
            }
            try {
                registry = ImplementationLoader.implementationOf(TaskRegistry.class);
                // register inject task for chaining
                registry.register(InjectTask.class);
            } catch (InvocationException e) {
                log.log(Level.SEVERE,
                    "Unable to find default implemenation of {0} on classpath, make sure that you set one programatically.",
                    TaskRegistry.class.getName());
            }
        }

        public TaskRegistry registry() {
            return registry;
        }

        public ExecutionServiceFactory service() {
            return lastFactory;
        }
    }

}
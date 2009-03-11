package org.exigencecorp.bd;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class BuildRunner {

    public static void main(String[] args) {
        try {
            new BuildRunner(Class.forName("Build").newInstance(), args).run();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static final List<Method> objectMethods = Arrays.asList(Object.class.getMethods());
    private final Object build;
    private final String[] args;

    private BuildRunner(Object build, String[] args) {
        this.build = build;
        this.args = args;
    }

    private void run() {
        if (this.shouldShowHelp()) {
            this.showHelp();
        } else {
            this.build();
        }
    }

    private void build() {
        System.out.println("Building...");
        for (String arg : this.args) {
            this.build(arg);
        }
        System.out.println("...done");
        System.exit(0);
    }

    private void build(String arg) {
        System.out.println("  " + arg);
        try {
            if (arg.indexOf('.') == -1) {
                Method method = this.build.getClass().getMethod(arg);
                method.invoke(this.build);
            } else {
                String[] parts = arg.split("\\.");
                Field field = this.build.getClass().getField(parts[0]);
                Object fieldValue = field.get(this.build);
                Method method = fieldValue.getClass().getMethod(parts[1]);
                method.invoke(fieldValue);
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    private boolean shouldShowHelp() {
        return this.args.length == 0 || (this.args.length == 1 && ("-h".equals(this.args[0]) || "--help".equals(this.args[0])));
    }

    private void showHelp() {
        List<String> methodNames = new ArrayList<String>();
        for (Field field : this.build.getClass().getFields()) {
            this.addMethodNames(methodNames, field.getType(), field.getName() + ".");
        }
        this.addMethodNames(methodNames, this.build.getClass(), "");
        Collections.sort(methodNames);
        System.out.println("Available targets:");
        for (String methodName : methodNames) {
            System.out.println("  " + methodName);
        }
    }

    private void addMethodNames(List<String> methodNames, Class<?> type, String prefix) {
        for (Method method : type.getMethods()) {
            if (method.getParameterTypes().length == 0
                && !BuildRunner.objectMethods.contains(method)
                && !method.getName().startsWith("get")
                && method.getReturnType().equals(void.class)) {
                methodNames.add(prefix + method.getName());
            }
        }
    }

}

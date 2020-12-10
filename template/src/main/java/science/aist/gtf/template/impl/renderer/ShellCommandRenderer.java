/*
 * Copyright (c) 2020 the original author or authors.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package science.aist.gtf.template.impl.renderer;

import lombok.CustomLog;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.PropertyPlaceholderHelper;
import science.aist.gtf.template.GeneratorTemplate;
import science.aist.gtf.template.TemplateTask;
import science.aist.gtf.template.TemplateTaskResult;
import science.aist.gtf.template.TemplateTaskResultTypeEnum;
import science.aist.gtf.transformation.renderer.condition.RendererCondition;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * <p>Executes a command on the command line</p>
 *
 * @author Andreas Pointner
 * @see Runtime#exec(String[], String[], File)
 * @since 1.0
 */
@CustomLog
public class ShellCommandRenderer extends AbstractGeneratorTemplateRenderer<TemplateTaskResult, TemplateTask> {

    private final Object lock = new Object();
    private final PropertyPlaceholderHelper propertyPlaceholderHelper;

    @SuppressWarnings("FieldMayBeFinal" /*We ignore that, because Spring can have problems with @Value when field is finale*/)
    @Value("${renderer.shell.command.key:command}")
    private String commandKey = "command";

    @SuppressWarnings("FieldMayBeFinal" /*We ignore that, because Spring can have problems with @Value when field is finale*/)
    @Value("${renderer.shell.commands.key:commands}")
    private String commandsKey = "commands";

    @SuppressWarnings("FieldMayBeFinal" /*We ignore that, because Spring can have problems with @Value when field is finale*/)
    @Value("${renderer.shell.environment.key:environment}")
    private String environmentKey = "environment";

    @SuppressWarnings("FieldMayBeFinal" /*We ignore that, because Spring can have problems with @Value when field is finale*/)
    @Value("${renderer.shell.workdir.key:workdir}")
    private String workdirKey = "workdir";

    @SuppressWarnings("FieldMayBeFinal" /*We ignore that, because Spring can have problems with @Value when field is finale*/)
    @Value("${renderer.shell.timeout.key:timeout}")
    private String timeoutKey = "timeout";


    public ShellCommandRenderer(RendererCondition<TemplateTask> rendererCondition, PropertyPlaceholderHelper propertyPlaceholderHelper) {
        super(rendererCondition);
        this.propertyPlaceholderHelper = propertyPlaceholderHelper;
    }

    @SneakyThrows
    private static String readLine(BufferedReader br) {
        return br.readLine();
    }

    @Override
    public TemplateTaskResult createElement() {
        return new TemplateTaskResult();
    }

    @Override
    public TemplateTaskResult executeTask(TemplateTaskResult taskResult, GeneratorTemplate container, TemplateTask task) {
        Function<String, List<String>> extractPropertyAndReturnAsStringList = key -> {
            Object values = task.getProperties().get(key);
            if (values instanceof Collection) {
                return ((Collection<?>) values).stream()
                        .map(String.class::cast)
                        .map(s -> propertyPlaceholderHelper.replacePlaceholders(s, container.getProperties()))
                        .collect(Collectors.toList());
            }
            return null;
        };

        try {
            String workDir = null;
            if (task.getProperties().containsKey(workdirKey)) {
                workDir = propertyPlaceholderHelper.replacePlaceholders(task.getProperties().getProperty(workdirKey), container.getProperties());
            }

            List<String> environment = null;
            if (task.getProperties().containsKey(environmentKey)) {
                environment = extractPropertyAndReturnAsStringList.apply(environmentKey);
            }

            List<String> commands = null;
            if (task.getProperties().containsKey(commandKey)) {
                commands = List.of(propertyPlaceholderHelper.replacePlaceholders(task.getProperties().getProperty(commandKey), container.getProperties()));
            }
            if (task.getProperties().containsKey(commandsKey)) {
                if (commands != null) {
                    throw new IllegalStateException("Both " + commandKey + " and " + commandsKey + " are set, only supports on of either");
                }
                commands = extractPropertyAndReturnAsStringList.apply(commandsKey);
            }

            if (commands == null) {
                throw new IllegalStateException("No commands where provided");
            }


            String[] environmentArray = null;
            if (environment != null) {
                environmentArray = environment.toArray(new String[0]);
            }

            File workDirFile = null;
            if (workDir != null) {
                workDirFile = new File(workDir);
            }

            for (String command : commands) {
                Process exec = Runtime.getRuntime().exec(command, environmentArray, workDirFile);

                CompletableFuture<Void> normalOutputFuture = log(exec.getInputStream(), log::info);
                CompletableFuture<Void> errorOutputFuture = log(exec.getErrorStream(), log::error);

                if (task.getProperties().containsKey(timeoutKey)) {
                    log.debug("Waiting for Process to finish or timeout");

                    if (!exec.waitFor((int) task.getProperties().get(timeoutKey), TimeUnit.MILLISECONDS)) {
                        normalOutputFuture.cancel(true);
                        errorOutputFuture.cancel(true);
                        exec.destroyForcibly();
                        log.warn("Shell execution was terminated because timeout exceeded");
                    }
                } else {
                    log.debug("Waiting for Process to finish");
                    exec.waitFor();
                }
            }

            taskResult.setTemplateTaskResultTypeEnum(TemplateTaskResultTypeEnum.SUCCESS);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            taskResult.setTemplateTaskResultTypeEnum(TemplateTaskResultTypeEnum.FAILED);
        }
        return taskResult;
    }

    @SuppressWarnings("java:S1943" /* Ignore use of InputStreamReader, because it uses default system encoding. As this
    is just for logging it does not matter. */)
    private CompletableFuture<Void> log(InputStream stream, Consumer<String> logMethod) {
        return CompletableFuture.runAsync(() -> {
            BufferedReader stdError = new BufferedReader(new InputStreamReader(stream));

            String s;
            while ((s = readLine(stdError)) != null) {
                synchronized (lock) {
                    logMethod.accept(s);
                }
            }
        });
    }
}

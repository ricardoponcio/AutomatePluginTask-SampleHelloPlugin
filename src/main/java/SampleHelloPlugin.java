import dev.poncio.AutomatePluginTask.PluginSdk.v1.constants.ParameterTypeEnum;
import dev.poncio.AutomatePluginTask.PluginSdk.v1.constants.PluginExecutionPlanEnum;
import dev.poncio.AutomatePluginTask.PluginSdk.v1.domain.*;
import dev.poncio.AutomatePluginTask.PluginSdk.v1.implementation.AbstractPluginTask;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.Callable;
import java.util.function.Consumer;

public class SampleHelloPlugin extends AbstractPluginTask {

    @Override
    public String getPluginName() {
        return "Simple Hello Plugin";
    }

    @Override
    public String getPluginDescription() {
        return "Default implementation for test engine plugin system";
    }

    @Override
    public List<PluginExecutionPlanEnum> getAvailableExecutionPlans() {
        return Collections.singletonList(PluginExecutionPlanEnum.ASYNC_WITH_PROGRESS);
    }

    @Override
    public List<PluginTaskInputParameterPrototype> getInputParametersPrototype() {
        return List.of(
                PluginTaskInputParameterPrototype.builder()
                        .name("inputTest")
                        .description("test input for plugin")
                        .type(ParameterTypeEnum.STRING)
                        .secret(false)
                        .required(true)
                        .build(),
                PluginTaskInputParameterPrototype.builder()
                        .name("inputSecretTest")
                        .description("test secret input for plugin")
                        .secret(true)
                        .required(false)
                        .type(ParameterTypeEnum.STRING)
                        .build());
    }

    @Override
    public List<PluginTaskBaseParameterPrototype> getBaseParametersPrototype() {
        return List.of(
                PluginTaskBaseParameterPrototype.builder()
                        .name("baseInputParam")
                        .description("any base param to every executions")
                        .secret(false)
                        .required(true)
                        .type(ParameterTypeEnum.STRING)
                        .build());
    }

    @Override
    public Callable<PluginTaskOutput> runAsync(List<PluginTaskBaseParameter> baseInputParameters, List<PluginTaskInputParameter> inputParameters, Consumer<PluginTaskProgress> progressConsumer) {
        return new Callable<PluginTaskOutput>() {
            @Override
            public PluginTaskOutput call() throws Exception {
                progressConsumer.accept(PluginTaskProgress.builder().progress(0.5).executionLog("Starting yet...").build());
                String parameter = (String) Objects.requireNonNull(inputParameters.stream().filter(p -> "inputTest".equals(p.getName())).findFirst().orElse(null)).getValue();
                if (parameter == null) {
                    return PluginTaskOutput.builder().code(500).success(false).message("You dont provide any parameter :/").build();
                } else {
                    return PluginTaskOutput.builder().code(200).success(true).message("Hello with param: " + parameter).build();
                }
            }
        };
    }

}



import dev.poncio.AutomatePluginTask.PluginSdk.constants.ParameterTypeEnum;
import dev.poncio.AutomatePluginTask.PluginSdk.domain.PluginTaskInputParameter;
import dev.poncio.AutomatePluginTask.PluginSdk.domain.PluginTaskInputParameterPrototype;
import dev.poncio.AutomatePluginTask.PluginSdk.domain.PluginTaskOutput;
import dev.poncio.AutomatePluginTask.PluginSdk.domain.PluginTaskProgress;
import dev.poncio.AutomatePluginTask.PluginSdk.interfaces.IPluginTask;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.Callable;
import java.util.function.Consumer;

public class SampleHelloPlugin implements IPluginTask {
    @Override
    public String getPluginName() {
        return "Simple Hello Plugin";
    }

    @Override
    public String getPluginDescription() {
        return "Default implementation for test engine plugin system";
    }

    @Override
    public List<PluginTaskInputParameterPrototype> getInputParametersPrototype() {
        return List.of(
                PluginTaskInputParameterPrototype.builder()
                        .name("inputTest")
                        .description("test input for plugin")
                        .type(ParameterTypeEnum.STRING)
                        .build(),
                PluginTaskInputParameterPrototype.builder()
                        .name("inputSecretTest")
                        .description("test secret input for plugin")
                        .secret(true)
                        .type(ParameterTypeEnum.STRING)
                        .build());
    }

    @Override
    public Callable<PluginTaskOutput> runAsync(List<PluginTaskInputParameter> list) {
        return new Callable<PluginTaskOutput>() {
            @Override
            public PluginTaskOutput call() throws Exception {
                return run(list);
            }
        };
    }

    @Override
    public Callable<PluginTaskOutput> runAsync(List<PluginTaskInputParameter> list, Consumer<PluginTaskProgress> progressConsumer) {
        return new Callable<PluginTaskOutput>() {
            @Override
            public PluginTaskOutput call() throws Exception {
                progressConsumer.accept(PluginTaskProgress.builder().progress(0.5).executionLog("Starting yet...").build());
                return run(list);
            }
        };
    }

    @Override
    public PluginTaskOutput run(List<PluginTaskInputParameter> list) {
        String parameter = (String) Objects.requireNonNull(list.stream().filter(p -> "inputTest".equals(p.getName())).findFirst().orElse(null)).getValue();
        if (parameter == null) {
            return PluginTaskOutput.builder().code(500).success(false).message("You dont provide any parameter :/").build();
        } else {
            return PluginTaskOutput.builder().code(200).success(true).message("Hello with param: " + parameter).build();
        }
    }
}

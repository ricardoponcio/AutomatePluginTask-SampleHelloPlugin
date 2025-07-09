# SampleHelloPlugin

## Example Plugin for Automate Plugin Task

This project is an example implementation of a plugin using the [PluginSDK](https://github.com/ricardoponcio/AutomatePluginTask-PluginSdk) for the Automate Plugin Task Engine.

---

## Overview

SampleHelloPlugin demonstrates how to implement a plugin that can be dynamically loaded and executed by the engine. It shows how to define input and base parameters, handle secrets, and provide asynchronous execution with progress reporting.

---

## How It Works

- **Input Parameters:**
  - `inputTest` (required, string): Used to produce a HelloWorld message in the output.
  - `inputSecretTest` (optional, string, secret): Declared to test secret parameter handling in the engine.
- **Base Parameter:**
  - `baseInputParam` (required, string): Required by the engine, not used in plugin logic (for demonstration purposes).

When executed, the plugin:
- Reports progress at 50%.
- Reads the `inputTest` parameter.
- Returns a success message including the provided value, or an error if not provided.

---

## Example Implementation

See [`SampleHelloPlugin.java`](src/main/java/SampleHelloPlugin.java):

```java
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
                .build()
        );
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
                .build()
        );
    }

    @Override
    public Callable<PluginTaskOutput> runAsync(
        List<PluginTaskBaseParameter> baseInputParameters,
        List<PluginTaskInputParameter> inputParameters,
        Consumer<PluginTaskProgress> progressConsumer
    ) {
        return () -> {
            progressConsumer.accept(
                PluginTaskProgress.builder().progress(0.5).executionLog("Starting yet...").build()
            );
            String parameter = (String) Objects.requireNonNull(
                inputParameters.stream()
                    .filter(p -> "inputTest".equals(p.getName()))
                    .findFirst()
                    .orElse(null)
            ).getValue();
            if (parameter == null) {
                return PluginTaskOutput.builder()
                    .code(500)
                    .success(false)
                    .message("You dont provide any parameter :/")
                    .build();
            } else {
                return PluginTaskOutput.builder()
                    .code(200)
                    .success(true)
                    .message("Hello with param: " + parameter)
                    .build();
            }
        };
    }
}
```

---

## Usage

1. **Build the plugin:**
   ```sh
   mvn clean package
   ```
   The JAR will be available in `target/SampleHelloPlugin-0.0.1-SNAPSHOT.jar`.

2. **Load the plugin JAR** into the Automate Plugin Task Engine.

3. **Configure parameters** as described above.

---

## References

- [PluginSDK README](https://github.com/ricardoponcio/AutomatePluginTask-PluginSdk)
- [Main Repository](https://github.com/ricardoponcio/AutomatePluginTask)

---

## License

MIT

---

## Author

Ricardo Poncio  
E-mail: **ricardo@poncio.dev**  
Telegram: **@rponcio**

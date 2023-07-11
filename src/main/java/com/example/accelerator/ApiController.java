package com.example.accelerator;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Map;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.yaml.snakeyaml.Yaml;
import reactor.core.publisher.Mono;

@RestController
public class ApiController {
  private  final AcceleratorLocations acceleratorLocations;

  public ApiController(AcceleratorLocations acceleratorLocations) {
    this.acceleratorLocations = acceleratorLocations;
  }

  @GetMapping("api/proxy/accelerators")
  Mono<Map<String,Object>> getAccelerators() {
    ArrayList accelerators = new ArrayList();
    for (int i = 0; i < this.acceleratorLocations.getLocations().length ; i++) {
      Map<String,Object> accelerator = this.getAccelerator(acceleratorLocations.getLocations()[i]);
      accelerator.remove("options");
      accelerator.put("name",String.valueOf(i));
      accelerators.add(accelerator);
    }

    return Mono.just(Map.of("_embedded", Map.of("accelerators", accelerators)));
  }
  @GetMapping("api/proxy/accelerators/options")
  Mono<Map<String,Object>> getOptions(@RequestParam("name") Integer index) {
    Map<String,Object> accelerator = getAccelerator(acceleratorLocations.getLocations()[index]);
    return Mono.just(Map.of("options", accelerator.get("options")));
  }

  @GetMapping("api/git-providers")
  Mono<String> getGitProviders() {
    return Mono.just("");
  }

  private Map<String,Object> loadYaml(String filename) {
    Yaml yaml = new Yaml();
    try (InputStream inputStream = new FileInputStream(filename)) {
      return yaml.load(inputStream);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  private Map<String, Object> getAccelerator(String path) {
    Map<String,Object>  yaml = this.loadYaml(path);
    Map<String,Object> accelerator = (Map<String, Object>) yaml.get("accelerator");
    return accelerator;
  }
}

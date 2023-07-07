package com.example.accelerator;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.yaml.snakeyaml.Yaml;

@RestController
public class UiApi {

  private  final AcceleratorLocations acceleratorLocations;

  public UiApi(AcceleratorLocations acceleratorLocations) {
    this.acceleratorLocations = acceleratorLocations;
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



  private Map<String, Object> loadOptions(String path) {
    Map<String,Object> accelerator = getAccelerator(path);
    return Map.of("options", accelerator.get("options"));
  }

  @GetMapping("api/proxy/accelerators/options")
  public Map<String,Object> getOptions(@RequestParam("name") Integer index) {
    return loadOptions(acceleratorLocations.getLocations()[index]);
  }


  @GetMapping("api/proxy/accelerators")
  public Map<String,Object> get() {

    var accelerators = new ArrayList();
    for (int i = 0; i < this.acceleratorLocations.getLocations().length ; i++) {
      Map<String,Object> accelerator = this.getAccelerator(acceleratorLocations.getLocations()[i]);
      accelerator.remove("options");
      accelerator.put("name",String.valueOf(i));
      accelerators.add(accelerator);
    }

    Map<String,Object> result =  Map.of("_embedded", Map.of("accelerators", accelerators));
    return result;
  }
}

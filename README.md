# local-accelerator

Local Development Server for Application Accelerators. 

## Run the app 

1. Make sure you have Java 17 installed.
2. Run `./mvnw spring-boot:run` 
3. install the application accelerator plugin in your IDE.
4. configure the accelerator plugin to point at `http://localhost:7788`
5. Open the accelerator plugin in VSCode and you will see two accelerators

## EConfigure which accelerators are available 

1. Edit the `src/main/resources/application.yaml` to point at your accelerator file locations

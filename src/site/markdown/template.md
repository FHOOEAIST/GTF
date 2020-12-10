# Template / Automation Engine

The template/automation engine is a concept that allows executing different tasks by just providing a simple yaml-based
configuration file.

## Background

The template engine is based on the transformation's renderer approach and uses different renderers and condition to 
perform the different tasks. For this, the default available tasks are configured in the `template-config.xml` spring
configuration file. 

## How to use the template engine

Basically the template engine uses the `GeneratorTemplateRenderer` class to do the rendering. For this, by default the
bean `generatorTemplateRenderer` can be used. This class can be executed using a `GeneratorTemplate`
which has to be instantiated using the `GeneratorTemplateFactory` and a `TemplateResource`. To add custom data to the 
process the template can be enhanced by a `Properties` element. Thus, the base process that is required to execute a task 
are the following:

```java
GeneratorTemplateFactory generatorTemplateFactory = ...;
GeneratorTemplateRenderer<GeneratorTemplate, Void> generatorTemplateRenderer = ...;
TemplateResource templateResource = new TemplateResource("pathToTemplateFile.yaml");

Properties myProperties = new Properties();
// add custom properties

GeneratorTemplate template = generatorTemplateFactory.loadGeneratorTemplate(templateResource);
generatorTemplateRenderer.renderElement(myProperties, null); // the second value is ignore for this type
```

With that base definition a given template is rendered. Of course this requires to configure a template resource yaml file.
An example file is provided here (this of course requires configuration of the properties, etc. and should only give 
an idea of how the syntax looks and how the different task types are used):

```yaml
templateName: generateDocumentation
templateTasks:
  - name: createGraphWizDir
    type: createDirectory
    properties:
      path: ${project}/src/site/resources/js/fhir2bpmn

  - name: createMarkdownDir
    type: createDirectory
    properties:
      path: ${project}/src/site/markdown/fhir2bpmn/

  - name: createMarkdown
    type: instantiateTemplate
    properties:
      template: classpath:/template/fhir2bpmndoc.ftl
      dest: ${project}/src/site/markdown/fhir2bpmn/${filename}.md

  - name: unzipAutoLayout
    type: unzip
    properties:
      src: classpath:/bpmn-auto-layout.zip
      dest: ${project}/target/

  - name: createBpmn
    type: instantiateTemplate
    properties:
      template: classpath:/template/bpmn.ftl
      dest: ${project}/target/bpmn-auto-layout/test/fixtures/transform.bpmn

  - name: processNpm
    type: shell
    properties:
      commands:
        - cmd /c npm install
        - cmd /c npm run all
      workdir: ${project}/target/bpmn-auto-layout/
      timeout: 60000 # ms --> 1 min (for each command)

  - name: deleteBpmnJs
    type: deleteFile
    properties:
      path: ${project}/src/site/resources/js/fhir2bpmn/${filename}.js

  - name: copyBpmnJs
    type: copyFile
    properties:
      src: ${project}/target/bpmn-auto-layout/test/generated/transform.bpmn
      dest: ${project}/src/site/resources/js/fhir2bpmn/${filename}.js
```  

Each template task inside the template needs to have an unique name over the file. The type of the template task
specifies what the task is doing, therefore different types are available which are described in the section blow. The
properties which are required differ from type to type. For each property placeholders can be defined. These are using
the `${placeholder_name}` syntax. These placeholders are replaced with the Properties defined in the java code. Meaning
if we define a property: `myProperties.setProperty("placeholder_name", "myCustomValue");` that each time
`${placeholder_name}` is used inside the yaml file it gets replaced by `myCustomValue`. 

## Template tasks and configuration

The following Tasks are currently supported:

 - Delete File
 - Copy Directory
 - Create Directory
 - Copy File
 - GraphViz
 - Template 
 - Unzip
 - Shell

As we already defined, each task needs a name and type as well as a list of properties, that is described in this
section.

### Delete File

| Property     | Description                                                                                                              |
|--------------|--------------------------------------------------------------------------------------------------------------------------|
| path         | The path to the file that should be deleted. (Required)                                                                  |
| errorNoExist | If an error should be thrown if the file does not exist, else the error is ignored but logged. (Optional. Default: true) | 

### Copy Directory

| Property | Description                                  |
|----------|----------------------------------------------|
| src      | The path of the source directory (Required.) |
| dest     | The path of the target directory (Required.) |

### Create Directory

| Property | Description                                                  |
|----------|--------------------------------------------------------------|
| path     | The path of the directory that should be created (Required.) |

### Copy File

| Property | Description                             |
|----------|-----------------------------------------|
| src      | The path of the source file (Required.) |
| dest     | The path of the target file (Required.) |

### GraphViz

| Property | Description                                      |
|----------|--------------------------------------------------|
| src      | The path of the source graphwiz file (Required.) |
| dest     | The path of the target png image (Required.)     |

### Template

| Property | Description                                                                                                                                                                                                                                                         |
|----------|---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| template | The path to the template file that should be used for the template engine. (Required.) Per Default the FreeMarker template engine is used. If there is no other bean configured. Thus you have to make sure, that the template file is a valid freemarker template. |
| dest     | The path of the resulting file (Required.)                                                                                                                                                                                                                          |
| append   | Boolean Flag if the content should be appended to the file or overwriting the file, if it already exists. (Optional. Default: false)                                                                                                                                |

### Unzip

| Property | Description                                    |
|----------|------------------------------------------------|
| src      | The path of the zip file (Required.)           |
| dest     | The path of the destination folder (Required.) |

### Shell

| Property    | Description                                                                                                                                          |
|-------------|------------------------------------------------------------------------------------------------------------------------------------------------------|
| command     | The command that should be executed (Required. Not allowed when commands is used)                                                                    |
| commands    | A list of commands that should be executed (Required. Not allowed when command is used)                                                              |
| environment | A list of environment strings. This value is used for the envp parameter of the Runtime.exec command. (Optional. Default: not set)                   |
| workdir     | The workdir where the command should be executed. This value is used for the dir parameter of the Runtime.exec command. (Optional. Default: not set) |
| timeout     | A timeout in ms, after which the method returns if the process has not yet finished. (Optional. Default: no timeout)                                 |

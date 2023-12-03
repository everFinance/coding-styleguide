# java Code Guidelines
## Project Structure
```
/project-root
  /src
    /main
      /java
        /com
          /example
            /demo
              DemoApplication.java      // spring boot entry point
              /config                   // Configuration information
                DemoConfig.java
              /controller               // Store controller class
                StudentController.java
              /service                  // Storage service class
                StudentService.java
              /mapper                   // Stores data access interfaces
                StudentMapper.java
              /entity                   // Stores table data objects
                StudentDO.java
              /model                    // Stores the data model classes
                Student.java
              /vo                       // Store view objects
                StudentVO.java
              /utils                    // Storage utility class
                DemoUtil.java
              /enums                    // Store enumeration class
                DemoEnum.java
      /resources
        application.properties

  /test                                 // Store test code
    /java
      /com
        /example
          /controller
            SampleControllerTest.java
          /service
            SampleServiceTest.java
  /target                               // Build output directory
  README.md                             // Documentation of the project
  .gitignore                            // Git ignores file configuration
  pom.xml                               // Maven project configuration file
```
[demo](./demo)

If it is a small entrepreneurial project, we recommend using the single application in the demo or even serverless, lambda. For large java projects, we recommend the cola architecture shown in the following figure

![img.png](img.png)

### Benchmarks
- Hierarchical architecture, as shown in the figure above, the upper layer depends on the lower layer, and the lower layer shields the processing details of the upper layer, and each layer performs its own duties and separates the concerns
- Don't add entities if you don't have to. Domain model has high requirements for design ability, and if it is not used well, a wrong abstraction is better than no abstraction
- Do not rely on SNAPSHOT versions for online applications
- When relying on a two-square library, a uniform version variable must be defined to avoid inconsistent version numbers
- Keep code styles consistent within the same project
- During system design, the system should rely on abstract classes and interfaces as much as possible according to the dependency inversion principle, which is conducive to expansion and maintenance

## Common Third-party Libraries
- Application development framework: spring boot
- Scheduled task: quartz、elastic job、spring task scheduling
- log: log4j、logback、log4j2
- test: junit、testng、mockito、spring test
- arweave sdk: arseedingsdk4j-sdk
- ethereum sdk: web3j
- json: jackson、gson、fastjson
- data source: druid、c3p0
- http: OkHttp、Apache HttpClient
- redis: jedis、redisson
- config: zookeeper、nacos、apollo
- MQ: rocketMQ、kafka
- RPC: dubbo、spring cloud

## log
The application cannot directly use the API in the log system (Log4j, Logback), but should rely on the API in the log framework SLF4J, and use the log framework in the facade mode, which is conducive to maintenance and the unity of log processing methods of each class

```agsl
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
private static final Logger logger = LoggerFactory.getLogger(Test.class); 
```

### 使用
```agsl
private static final Logger logger = LoggerFactory.getLogger(Test.class); 

// debug level
logger.debug("Processing trade with id: {} and symbol: {}", id, symbol);

// info level
logger.info("Processing trade with id: {} and symbol: {}", id, symbol);

// warn level
logger.warn("Processing trade with id: {} and symbol: {}", id, symbol);

// error level
logger.error(msg + "_" + e.getMessage(), e);
```

### Usage specification
* All log files are saved for at least 15 days, because some exceptions have a "weekly" frequency
* For log output at the trace/debug level, you must enable and disable the log level
```agsl
if (logger.isDebugEnabled()) { 
    logger.debug("Current ID is: {} and name is: {}", id, getName());
}
```
* Avoid wasting disk space by printing logs repeatedly
* Exception information should include two types of information: crime scene information and exception stack information
* debug level logs cannot be output in the production environment. Output info level logs selectively
* The warn level log can be used to record user input parameter errors to avoid user complaints
* Try to describe log error messages in English

#### Use log levels correctly
In java, common log levels are from low to high: trace、debug、info、warn、error、fatal
1. trace: The lowest level log is used to track the execution of the program. In general, it is only used in the debugging phase to output some detailed debugging information.

2. debug: Output debugging information to help developers locate problems. In production environments, the debug level is generally not recommended because of the large amount of log output.

3. info: info logs are used to output important running information, such as program startup information and the results of critical operations. Info logs are used in production environments to monitor the running status of programs.

4. warn: Warning information indicates that there may be a potential problem in the program, but it does not affect the normal running of the program. For example, if the parameters of a method do not meet expectations, the program can continue to execute.

5. error: Output an error message indicating that a recoverable error occurred in the program. When an error occurs in the program. Generally, logs of the error level are recorded and handled accordingly.

6. fatal: Logs of the highest level are used to output fatal error messages. When an unrecoverable error occurs in the program, a fatal level log is logged and the execution of the program is terminated.

In actual applications, you can set the log level as required. Normally, the development environment can be set to debug level and the production environment can be set to info level to avoid excessive log output

## Coding Guidelines

Refer to [alibaba java coding guidelines](https://github.com/alibaba/Alibaba-Java-Coding-Guidelines)

alibaba java coding guidelines written very detailed, here to write some daily development often encounter the situation

### Naming style

- None of the names in the code can start with an underscore or dollar sign, nor can they end with an underscore or dollar sign
- The naming in the code is strictly forbidden to use the combination of pinyin and English, and it is not allowed to directly use the Chinese way
- The class name uses the UpperCamelCase style except for the following cases: DO/BO/DTO/VO/AO/PO, and so on
- Method names, parameter names, member variables, and local variables use the lowerCamelCase style and must follow the hump form
- Constant names are all uppercase, words are separated by underscores, and the semantic expression is complete and clear, and the name is not too long
- The name of an Abstract class starts with abstract or Base; Exception class names end with exception; A Test class name starts with the name of the class it is testing and ends with test
- Package names are all lowercase, with one and only one natural English word between dot separators. Package names are singular, but class names can be plural if they have plural meanings


### Security protocol
- The pages or functions that belong to the user must be checked for permission control
- Sensitive user data is not allowed to be displayed directly, and display data must be desensitized
- SQL parameters entered by users are strictly limited by parameter binding or METADATA field values, preventing SQL injection
- The user requests that any parameters passed in must be validated

### Database design
- Table name Field names must use lowercase characters or numbers; Do not start with a digit or have only a digit between two underscores
- Disable reserved words, such as desc, range, match, and delayed
- Primary key index name pk_ field name; Unique index name uk_ field name; The common index name is the idx_ field name
- The decimal type is decimal. float and double are prohibited
- varchar is a variable length character string with a maximum length of 5000. If the stored length exceeds this value, use text and list it independently
- Three required fields in the table are id, create_time, update_time
- Do not use count(column name) or count(constant) instead of count(*), count(*) is the syntax for the standard count of rows defined in SQL92, independent of the database, NULL and non-null
- If all the values in a column are NULL, count(col) returns 0, but sum(col) returns NULL. Therefore, pay attention to NPE problems when using sum()
  If the -in operation can be avoided, avoid it. If it cannot be avoided, carefully evaluate the number of set elements behind the in operation and control it within 1000
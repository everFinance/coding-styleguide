# Golang Code Guidelines
## Project Structure
```
- cmd
    - demo
        - main.go   // Entry point for the demo project
- demo
    - schema        // Type declarations
        - db.go     // Type declarations related to the database
        - api.go    // Type declarations for API responses
		- error.go  // Error messages
        - other.go  // Type declarations for other modules
    - api.go        // Implementation of RESTful interfaces
    - demo.go       // Entry function
    - jobs.go       // Implementation of scheduled tasks
    - wdb.go        // Implementation of relational database read/write methods
    - cache.go      // Cache
    - other.go      // Independent processing methods
- example           // Examples
    - xxx.go
- .drone.yml        // CI/CD configuration file
- Dockerfile 
- README.md         // Readme for the demo project
- mod.go
- Makefile          // make all, make test
- test.sh

```
[project-example](./project)
### Benchmarks
- Each package should be independently usable, with externally facing interfaces kept concise and ideally reusable.
- Package folder names should match the names of their entry files.
- Entry files should not be overly complex.
- External functions should be placed in entry files, with business logic implemented from top to bottom, showcasing the core functionality of the package.
- Entry files generally include three external functions: New(), Run(), and Close().
- Before committing code, run make all and make test, ensuring they pass before submission.
- Follow the naming style established in previous projects for consistency.
- In cmd/main.go, after listening for signals, it's mandatory to call Close() for a safe exit.

## Common Third-party Libraries
- cli: github.com/urfave/cli
- api: github.com/gin-gonic/gin
- job: github.com/go-co-op/gocron
- log: github.com/inconshreveable/log15
- test: github.com/stretchr/testify
- json: github.com/tidwall/gjson
- http-cli: gopkg.in/h2non/gentleman.v2
- decimal library: github.com/shopspring/decimal
- ethereumRPC: github.com/everFinance/ethrpc
- ethereumSDK: github.com/everFinance/goether
- arweaveSDK: github.com/everFinance/goar
- web-framework: github.com/gin-gonic/gin
- redis-cli: github.com/go-redis/redis
- mysql-cli: gorm.io/gorm

## Logging
### Packing
```
func NewLog(serverName string) log15.Logger {
	lg := log15.New("module", serverName)

	// Default logger handle
	h := lg.GetHandler()
	// Sentry integrated logger handle
	sentryHandle := log15.FuncHandler(func(r *log15.Record) error {
		if r.Lvl == log15.LvlError {
			msg := string(log15.JsonFormat().Format(r))
			go func(m string) {
				sentry.CaptureMessage(m)
			}(msg)
		}
		return nil
	})

	lg.SetHandler(log15.MultiHandler(h, sentryHandle))

	return lg
}
```
### Usage
```
var log = NewLog("demo")

log.Info(msg, "var1",var1,"var2",var2)
log.Debug(msg, "var1",var1,"var2",var2)
log.Warn(msg, "var1",var1,"var2",var2)
log.Error(msg, "err",err,"var1",var1)
```
### Usage Guidelines
#### Keep Content Concise
Log messages must be concise and clear, avoiding irrelevant data that can significantly interfere with log analysis. For example, when logging transaction details, only print the necessary fields.
#### Avoid Duplicate Output
It's common to encounter duplicate log prints. For example:
```
func A() {
    err := b()
    if err != nil {
        log.Error("b() failed","err",err) 
      
    }
    
}

func b() error {
   _, err :=  strconv.Atoi("111")
   if err != nil {
       log.Error("strconv.Atoi("111")","err",err)
       return err
   }
   return nil
}

```
In this case, the error message is printed both in function A and function b, leading to unnecessary duplication. It's preferable to only log at the outermost function.
#### Use the Correct Log Levels
In log15, use Debug, Info, Warn, and Error for logging.
1. Debug: Code debugging, outputting key information for program state judgment.   
2. Info: Print initialization data information when the program starts, and output information after key state changes.
3. Warn: For errors that do not significantly impact the overall system operation but cannot be ignored, output through the warn level. For example, a periodic task reading from an external service fails for a specific iteration.
4. Error: Any error affecting service operation must be output at the Error level.

#### Declare Error Information
Error information should be declared uniformly in error.go, using snake_case naming convention:
```
var ERR_INVALID_SIGNATURE = "err_invalid_signature"
```

## Coding Guidelines
Refer to [Uber-go](https://github.com/xxjwxc/uber_go_guide_cn#uber-go-%E8%AF%AD%E8%A8%80%E7%BC%96%E7%A0%81%E8%A7%84%E8%8C%83)

### Additional Points
- In demo.go, implement only three methods: New(), Run(), and Close() as a principle.
- In .go files, only methods that need to be called by other files should have an uppercase first character.
- Public methods in files should be written before private methods.
  ```go
  AA(){}
  BB(){}
  aa(){}
  bb(){}
  ```
- Method names should follow camelCase and be concise.
   ```go
  bad:
    UpdateBlockChainStableBlock(){}
  Good:
    UpdateStableBlock(){}
  ```
- Group parameters with the same properties together, and return values accordingly.
   ```go
   AA(ethRpc, moonRpc, cfxRpc string, db *Db) {}
   ```
- If an error is returned, place the error at the end of the return values.
   ```go
   AA(ethRpc, moonRpc, cfxRpc string, db *Db) (string,int,error) {}
   ```
- Avoid using pointer types for function parameters. If it's a slice or map, consider copying before passing.
- Avoid using nested if-else structures; replace them with switch statements.
- Avoid using closures since you cannot control how external variables called in closures change.
    ``` 
    bad:
        for i:=0; i <10; i ++ {
            go func() {
                t.Log(i) // The 'i' in the closure changes with the for loop, and the final print is always 10.
            }()
        }
    good:
        for i:=0; i <10; i ++ {
            go func(i int) { // Use deterministic parameter passing
                t.Log(i)
            }(i)
        }
    ```
- When using `locker.Lock()`, immediately `defer locker.Unlock()`
    ```go
    bad: 
  func AA() {
  	locker.Lock()
      ... 
  	locker.UnLock()
     }

  good:
  func AA() {
  	locker.Lock()
  	defer locker.UnLock()
      ...
  }
    ```
- Avoid using init() functions, as the loading order of init() functions is uncontrollable, and the logic in init() is not explicitly invoked. Place the logic in demo.go/New() or Run() instead.
- When using slices or maps, specify the capacity during make to make append more efficient and save memory space during runtime.
  ```go
  tokenList := make([]severSchema.TokenInfo, 0, 50)
  ```
- Normally, avoid using buffered channels. If a buffered channel is necessary, provide a comment - explaining why the capacity is set to that value.
When renaming imported packages, follow the principle of proximity. No need to rename within the same module, but other modules with naming conflicts should include the module name.
    ```go
    import (
        confSchema "github.com/everFinance/everpay/config/schema" // config package
        paySchema "github.com/everFinance/everpay/pay/schema" // pay package
        "github.com/everFinance/everpay/server/schema" // current package
    )
    ```
- API response results must use the struct form.
    ```go
  bad:
  c.JSON(http.StatusOK, gin.H{
		"total": total,
		"txs": txs,
	})
  
  good:
    c.JSON(http.StatusOK, schema.RespTxs{
            Total: total,
            Txs:   txs,
        })
    ```
- Declare integer types in code using int64.


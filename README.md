## 项目结构
```
- cmd
    - demo
        - main.go // demo 项目启动入口
- demo
    - schema  // type 声明
        - db.go // db 相关的 type 声明
        - api.go // api response type 声明
        - other.go // 其他模块对应的 type 声明
    - api.go    // restful 接口实现
    - error.go   // error 信息
    - demo.go   // 入口函数
    - jobs.go   // 定时任务实现
    - wdb.go   // 关系型数据库读写方法实现
    - cache.go // 缓存
    - other.go // 独立的处理方法
- .drone.yml // ci/cd 配置文件
- Dockerfile 
- README.md // demo 项目 readme
- mod.go
```
### 基准
- 每个 package 独立可用，对外接口尽量简洁，最好具备复用性
- package 文件夹名称和里面的入口文件命名一致
- 入口文件不宜有太多编码
- 应将外部函数放在入口文件，业务自顶向下实现，入口能表现 package 的核心功能
- 入口文件一般包含三个外部函数，分别是：New(), Run(), Close()

## 通用三方库
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

## 日志
### 封装
```
func NewLog(serverName string) log15.Logger {
	lg := log15.New("module", serverName)

	// 默认的 logger handle
	h := lg.GetHandler()
	// 集成 sentry 的 logger handle
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
### 使用
```
var log = NewLog("demo")

log.Info(msg, "var1",var1,"var2",var2)
log.Debug(msg, "var1",var1,"var2",var2)
log.Warn(msg, "var1",var1,"var2",var2)
log.Error(msg, "err",err,"var1",var1)
```
### 使用规范
#### 内容简洁
日志 msg 必须简洁明确，不相干数据严重干扰日志分析。比如在打印 tx 日志中，不要输出 tx 所有字段，只需要打印需要的字段。
#### 重复输出
在开发过程中，经常出现日志打印重复的情况。比如如下:
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
b() 中打印过 err msg，但是 A() 中又会打印一次 b() 返回的error, 所以出现了重复打印的情况。这种情况严重干扰对日志的分析。
正确的做法是在 b() 中不打印，只要在最外层调用函数中打印。
#### 正确使用日志级别
在 log5 中主要使用到 Debug, Info, Warn, Error 这四个级别来打印日志。
1. Debug 使用场景    
   代码调试、输出关键信息用于判断程序状态。
2. Info 使用场景    
   程序启动打印初始化数据信息、关键状态正常改变之后的信息输出。
3. Warn 使用场景    
   对系统整体运行影响不大但是不可忽略的错误，需要通过 warn 级别输出。比如读取外部服务的定时任务某次读取失败的情况。
4. Error 使用场景    
   服务出现 Error 并影响服务正常运行都必须使用 Error 级别输出。

#### error 信息声明
error 信息统一在 error.go 中声明，命名方式为蛇形命名法，如：
```
var ERR_INVALID_SIGNATURE = "err_invalid_signature"
```

## 编码规范
参考 [Uber-go 规范](https://github.com/xxjwxc/uber_go_guide_cn#uber-go-%E8%AF%AD%E8%A8%80%E7%BC%96%E7%A0%81%E8%A7%84%E8%8C%83)


---

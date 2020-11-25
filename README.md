# coding-styleguide

## Golang

### 基准

- 每个 package 独立可用，对外接口尽量简洁，最好具备复用性
- package 文件夹名称和里面的入口文件命名一致
- 入口文件不宜有太多编码
- 应将外部函数放在入口文件，业务自顶向下实现，入口能表现 package 的核心功能
- 入口文件一般包含三个外部函数，分别是：New(), Run(), Close()

说明：

package 可能指向某一项业务，package 目录内是通用的处理框架。

在一些场景下，如果框架内的文件具备通用性，被多个其他 package 引用，要考虑把它们作为独立的 package, 比如一些特定的 db、redis 业务，作为独立的 package 更有优势，通常这些 package 会共享给其他 repo 使用。

### 引用 mod 标准

- cli: github.com/urfave/cli/v2
- api: github.com/gin-gonic/gin
- job: github.com/go-co-op/gocron
- redis: TODO
- mysql: github.com/jinzhu/gorm
- log: github.com/inconshreveable/log15
- test: github.com/stretchr/testify

### 示例

```
- demo
  - schemea         // 如果有很多复杂的 type 可以考虑统一放入 schema
    - const.go      // 常量
    - db.go         // 数据库 schema
    - redis.go      // redis schema
  - api.go          // restful API 接口
  - demo.go         // 入口文件
  - process.go      // 功能性处理
  - jobs.go         // 定时任务
  - cache.go        // 内存高速缓存
  - wdb.go          // 数据库相关的 wrap
  - wredis.go       // redis 相关的 wrap
  - other.go        // 其他有必要独立的架构程序
```
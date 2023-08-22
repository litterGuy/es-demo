# es-demo

- spring boot 2.7.11
- es对应版本为7.17.10

---

es的基本操作

---

## 使用x-pack-sql-jdbc操作es

问题记录：
-  druid-spring-boot-starter的版本号最高升级到了1.2.16，再高的版本一直空指针报错、应该是需要使用springboot3.X
-  x-pack-sql-jdbc使用权限需要有许可证书,白金级和企业级才可以使用、该功能需要付费。参见官网：[subscriptions](https://www.elastic.co/cn/subscriptions)

> 如有使用sql去查询es的需求，切换到插件[elasticsearch-sql](https://github.com/NLPchina/elasticsearch-sql)
> 不过该仓库已经弃用，不在更新。
> [试用api](https://www.elastic.co/guide/en/elasticsearch/reference/current/start-trial.html)

# spring-dubbo-example
![](./image.jpg)
## Build
```bash
mvn clean package -DskipTests
```

## Docker Build
```bash
docker build -t candyleer/spring-dubbo-provider:v1 -f spring-dubbo-provider/Dockerfile  ./spring-dubbo-provider/
docker build -t candyleer/spring-dubbo-consumer:v1 -f spring-dubbo-consumer/Dockerfile  ./spring-dubbo-consumer/

```

## docker Push
```bash
docker push candyleer/spring-dubbo-provider:v1
docker push candyleer/spring-dubbo-consumer:v1
```

## Deploy
```bash
# 部署 zookeeper 3个节点
kubectl apply -f deploy/zookeeper.yaml
# 部署 provider 5个节点
kubectl apply -f deploy/provider.yaml
# 部署 consumer 一个节点
kubectl apply -f deploy/consumer.yaml

访问 http://<consumer pod ip>:8080/hello 测试是否通
```
## Monitor
```bash
#路径
/actuator/prometheus

# 提供方的监控指标
# HELP dubbo_provider_seconds  
# TYPE dubbo_provider_seconds summary
dubbo_provider_seconds_count{method="hello",service="io.github.candyleer.springdubboapi.HelloService",} 21.0
dubbo_provider_seconds_sum{method="hello",service="io.github.candyleer.springdubboapi.HelloService",} 0.157680316
# HELP dubbo_provider_seconds_max  
# TYPE dubbo_provider_seconds_max gauge
dubbo_provider_seconds_max{method="hello",service="io.github.candyleer.springdubboapi.HelloService",} 0.077624181


#消费方的指标
# HELP dubbo_consumer_seconds_max  
# TYPE dubbo_consumer_seconds_max gauge
dubbo_consumer_seconds_max{method="hello",service="io.github.candyleer.springdubboapi.HelloService",} 0.334864448
# HELP dubbo_consumer_seconds  
# TYPE dubbo_consumer_seconds summary
dubbo_consumer_seconds_count{method="hello",service="io.github.candyleer.springdubboapi.HelloService",} 21.0
dubbo_consumer_seconds_sum{method="hello",service="io.github.candyleer.springdubboapi.HelloService",} 0.69077555

```


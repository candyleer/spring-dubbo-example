# spring-dubbo-example
simple example
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



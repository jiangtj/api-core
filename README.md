# api-core
就转换一些数据，顺便试试GitHub的容器注册

### Run

```shell
docker run -p 8066:8066 ghcr.io/jiangtj/api-core:latest
```

### Api

- `${host}:8066/base64d?url=x` Base64解密，解密url地址的内容（假设url获取到时base64的内容，需要解密）

# api-core
就转换一些数据，顺便试试GitHub的容器注册

### Run

```shell
docker run -p 8066:8066 ghcr.io/jiangtj/api-core:latest
```

```shell
export ADDR_BASE_URL = https://raw.githubusercontent.com/modood/Administrative-divisions-of-China/master/dist
docker run --name api-core -p 8066:8066 \
--restart=unless-stopped \
-e ADDR_PROVINCE=${ADDR_BASE_URL}/provinces.json \
-e ADDR_CITY=${ADDR_BASE_URL}/cities.json \
-e ADDR_AREA=h${ADDR_BASE_URL}/areas.json \
-e ADDR_STREET=${ADDR_BASE_URL}/streets.json \
-d ghcr.io/jiangtj/api-core:latest
```

### Api

- `${host}:8066/base64d?url=x` Base64解密，解密url地址的内容（假设url获取到时base64的内容，需要解密）

- `address/provinces` 省列表
- `address/cities` 市列表
- `address/cities/{code}` 使用省code查询市
- `address/areas` 区列表
- `address/areas/{code}` 使用市code查询区
- `address/streets` 街道列表
- `address/streets/{code}` 使用区code查询街道
- `address/update` 触发省市区数据更新，默认https://github.com/modood/Administrative-divisions-of-China

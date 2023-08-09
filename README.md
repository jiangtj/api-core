# api-core
一些通用的接口（自用）

### Run

```shell
docker run -p 8066:8066 ghcr.io/jiangtj/api-core:latest
```

### Api

- `base64d?url=x` Base64解密，解密url地址的内容（假设url获取到时base64的内容，需要解密）


#### 省市区

数据来源 https://github.com/modood/Administrative-divisions-of-China

- `address/provinces` 省列表
- `address/cities` 市列表
- `address/cities/{code}` 使用省code查询市
- `address/areas` 区列表
- `address/areas/{code}` 使用市code查询区
- `address/{code}` 依据指定code，查询详情

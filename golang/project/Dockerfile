FROM alpine:latest
MAINTAINER sandy <sandy@ever.finance>

ENV PATH /go/bin:/usr/local/go/bin:$PATH
ENV GOPATH /go

WORKDIR /demo

# 外挂bolt文件夹
VOLUME ["/demo/data"]

COPY cmd/demo/demo /demo/demo
EXPOSE 8080

ENTRYPOINT [ "/demo/demo" ]

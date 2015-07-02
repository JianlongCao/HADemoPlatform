package com.cisco.demo.comm;

public class HttpCmd {
    private String method;
    private String uri;
    private String entity;

    public HttpCmd(String method, String uri, String entity) {
        this.method = method;
        this.uri = uri;
        this.entity = entity;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public String getEntity() {
        return entity;
    }

    public void setEntity(String entity) {
        this.entity = entity;
    }

    @Override
    public String toString() {
        return "HttpCmd{" +
                "method='" + method + '\'' +
                ", uri='" + uri + '\'' +
                ", entity='" + entity + '\'' +
                '}';
    }
}

package br.com.rodrigo.nascimento.domain;

import java.util.HashMap;
import java.util.Map;

public class ResponseApi {
    private int statusCode;
    private Map<String,String> headers = new HashMap<>();
    private String body;

    public ResponseApi(int statusCode, Map<String, String> headers, String body) {
        this.statusCode = statusCode;
        this.headers = headers;
        this.body = body;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public String getBody() {
        return body;
    }

    @Override
    public String toString() {
        return "ResponseApi{" +
                "statusCode=" + statusCode +
                ", headers=" + headers +
                ", body='" + body + '\'' +
                '}';
    }
}

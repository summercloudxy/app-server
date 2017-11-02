package com.zgiot.app.server.service.impl;

import org.springframework.http.ResponseEntity;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class DataEngineTemplate extends RestTemplate {
    public static final String URI_CMD = "/cmd/send";
    public static final String URI_DATA_ALL = "/data";
    public static final String URI_DATA_SYNC = "/data/sync";
    private final String host;

    public DataEngineTemplate(String host) {
        Objects.requireNonNull(host, "host can not be null");
        this.host = host;
    }

    public DataEngineTemplate(String host, ClientHttpRequestFactory requestFactory) {
        super(requestFactory);
        this.host = host;
    }

    public DataEngineTemplate(String host, List<HttpMessageConverter<?>> messageConverters) {
        super(messageConverters);
        this.host = host;
    }


    @Override
    public <T> T getForObject(String url, Class<T> responseType, Object... uriVariables) throws RestClientException {
        url = prependHost(url);
        return super.getForObject(url, responseType, uriVariables);
    }

    @Override
    public <T> T getForObject(String url, Class<T> responseType, Map<String, ?> uriVariables) throws RestClientException {
        url = prependHost(url);
        return super.getForObject(url, responseType, uriVariables);
    }

    @Override
    public <T> T getForObject(URI url, Class<T> responseType) throws RestClientException {
        return super.getForObject(url, responseType);
    }

    @Override
    public <T> ResponseEntity<T> getForEntity(String url, Class<T> responseType, Object... uriVariables) throws RestClientException {
        url = prependHost(url);
        return super.getForEntity(url, responseType, uriVariables);
    }

    @Override
    public <T> ResponseEntity<T> getForEntity(String url, Class<T> responseType, Map<String, ?> uriVariables) throws RestClientException {
        url = prependHost(url);
        return super.getForEntity(url, responseType, uriVariables);
    }

    @Override
    public <T> ResponseEntity<T> getForEntity(URI url, Class<T> responseType) throws RestClientException {
        return super.getForEntity(url, responseType);
    }

    @Override
    public <T> T postForObject(String url, Object request, Class<T> responseType, Object... uriVariables) throws RestClientException {
        url = prependHost(url);
        return super.postForObject(url, request, responseType, uriVariables);
    }

    @Override
    public <T> T postForObject(String url, Object request, Class<T> responseType, Map<String, ?> uriVariables) throws RestClientException {
        url = prependHost(url);
        return super.postForObject(url, request, responseType, uriVariables);
    }

    @Override
    public <T> T postForObject(URI url, Object request, Class<T> responseType) throws RestClientException {
        return super.postForObject(url, request, responseType);
    }

    @Override
    public <T> ResponseEntity<T> postForEntity(String url, Object request, Class<T> responseType, Object... uriVariables) throws RestClientException {
        url = prependHost(url);
        return super.postForEntity(url, request, responseType, uriVariables);
    }

    @Override
    public <T> ResponseEntity<T> postForEntity(String url, Object request, Class<T> responseType, Map<String, ?> uriVariables) throws RestClientException {
        url = prependHost(url);
        return super.postForEntity(url, request, responseType, uriVariables);
    }

    @Override
    public <T> ResponseEntity<T> postForEntity(URI url, Object request, Class<T> responseType) throws RestClientException {
        return super.postForEntity(url, request, responseType);
    }

    @Override
    public void put(String url, Object request, Object... uriVariables) throws RestClientException {
        url = prependHost(url);
        super.put(url, request, uriVariables);
    }

    @Override
    public void put(String url, Object request, Map<String, ?> uriVariables) throws RestClientException {
        url = prependHost(url);
        super.put(url, request, uriVariables);
    }

    @Override
    public void put(URI url, Object request) throws RestClientException {
        super.put(url, request);
    }

    @Override
    public <T> T patchForObject(String url, Object request, Class<T> responseType, Object... uriVariables) throws RestClientException {
        url = prependHost(url);
        return super.patchForObject(url, request, responseType, uriVariables);
    }

    @Override
    public <T> T patchForObject(String url, Object request, Class<T> responseType, Map<String, ?> uriVariables) throws RestClientException {
        url = prependHost(url);
        return super.patchForObject(url, request, responseType, uriVariables);
    }

    @Override
    public <T> T patchForObject(URI url, Object request, Class<T> responseType) throws RestClientException {
        return super.patchForObject(url, request, responseType);
    }

    @Override
    public void delete(String url, Object... uriVariables) throws RestClientException {
        url = prependHost(url);
        super.delete(url, uriVariables);
    }

    @Override
    public void delete(String url, Map<String, ?> uriVariables) throws RestClientException {
        url = prependHost(url);
        super.delete(url, uriVariables);
    }

    @Override
    public void delete(URI url) throws RestClientException {
        super.delete(url);
    }

    private String prependHost(String uri) {
        return host + uri;
    }
}

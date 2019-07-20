package com.fuli.util;

import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import okhttp3.MultipartBody.Builder;

import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author fuli
 */
@Slf4j
public class HttpUtil {
    private static volatile HttpUtil instance;
    private static OkHttpClient client;
    private static final int CONNECT_TIMEOUT = 10;
    private static final int READ_TIMEOUT = 20;
    private static final int MAX_IDLE_CONNECTIONS = 5;
    private static final int KEEP_ALIVE_DURATION = 5;
    private static final MediaType MEDIA_JSON = MediaType.parse("application/json; charset=utf-8");
    public static final String GET = "get";
    public static final String POST = "post";
    public static final String JSON = "json";
    public static final String FORM = "form";
    public static final String FILE = "file";
    public static final String WEBSERVICE = "webservice";

    private HttpUtil(OkHttpClient client) {
        if (client == null) {
            ConnectionPool pool = new ConnectionPool(MAX_IDLE_CONNECTIONS, KEEP_ALIVE_DURATION, TimeUnit.MINUTES);
            HttpUtil.client = new OkHttpClient.Builder()
                    .connectTimeout(CONNECT_TIMEOUT, TimeUnit.SECONDS)
                    .readTimeout(READ_TIMEOUT, TimeUnit.SECONDS)
                    .retryOnConnectionFailure(true)
                    .connectionPool(pool)
                    .build();
        } else {
            HttpUtil.client = client;
        }
    }

    private static HttpUtil init() {
        if (instance == null) {
            synchronized (HttpUtil.class) {
                if (instance == null) {
                    instance = new HttpUtil(null);
                }
            }
        }
        return instance;
    }

    public static HttpUtil getInstance() {
        return init();
    }

    public static OkHttpClient getOkHttpClient() {
        return HttpUtil.client;
    }

    public static HttpResult call(String method, String url, Map<String, ?> params, Map<String, String> headerMap) {
        switch (method) {
            case HttpUtil.GET:
                return get(url, params, headerMap);
            case HttpUtil.POST:
            case HttpUtil.FORM:
                return post(url, params, headerMap, method);
         /*   case HttpUtil.WEBSERVICE:
                //todo
                return null;*/
            default:
                throw new HttpException(HttpException.MISS_TYPE, method);
        }
    }

    public static HttpResult get(String url, Map<String, ?> params) {
        return doRequest(getRequest(url, params, null));
    }

    public static HttpResult get(String url, Map<String, ?> params, Map<String, String> headerMap) {
        return doRequest(getRequest(url, params, headerMap));
    }

    public static HttpResult post(String url, Map<String, ?> params, String method) {
        return doRequest(postRequest(url, params, method, null));
    }

    public static HttpResult post(String url, Map<String, ?> params, Map<String, String> headerMap, String method) {
        return doRequest(postRequest(url, params, method, headerMap));
    }

    /**
     * jsonString
     */
    public static HttpResult post(String url, String json) {
        return doRequest(postRequest(url, json, null));
    }

    /**
     * jsonString
     */
    public static HttpResult post(String url, String json, Map<String, String> headerMap) {
        return doRequest(postRequest(url, json, headerMap));
    }

    private static Request getRequest(String url, Map<String, ?> params, Map<String, String> headerMap) {
        url += "?"+ CommonUtil.splice(params,"&","=");
        if (log.isDebugEnabled()) {
            log.debug("请求参数为,url地址{},{}", params, url);
        }
        Request.Builder builder = new Request.Builder().url(url).get();
        if (headerMap != null && headerMap.size() > 0) {
            builder.headers(Headers.of(headerMap));
        }
        return builder.build();
    }

    private static Request postRequest(String url, Map<String, ?> params, String method, Map<String, String> headerMap) {
        RequestBody body;
        switch (method) {
            case POST:
            case FORM:
            case FILE:
                String json = JsonUtil.toJson(params);
                if (log.isDebugEnabled()) {
                    log.debug("请求参数为{}", json);
                }
                body = RequestBody.create(json, MEDIA_JSON);
                break;
            default:
                throw new HttpException(HttpException.MISS_TYPE, method);
        }
        return buildRequest(url, body, headerMap);
    }

    private static Request buildRequest(String url, RequestBody body, Map<String, String> headerMap) {
        Request.Builder builder = new Request.Builder().url(url);
        if (headerMap != null) {
            builder.headers(Headers.of(headerMap));
        }
        if (body != null) {
            builder.post(body);
        }
        return builder.build();
    }

    private static Request postRequest(String url, String json, Map<String, String> headerMap) {
        RequestBody body = RequestBody.create(json, MEDIA_JSON);
        return buildRequest(url, body, headerMap);
    }

    private static RequestBody bodyFile(Map<String, ?> params) {
        Builder builder = new Builder().setType(MultipartBody.FORM);
        params.forEach((k, v) -> {
            if (v instanceof File) {
                File file = (File) v;
                builder.addFormDataPart("file", file.getName(), RequestBody.create(file, MediaType.parse("multipart/form-data")));
            } else {
                builder.addFormDataPart(k, v.toString());
            }
        });
        return builder.build();
    }

    private static RequestBody bodyForm(Map<String, ?> params) {
        FormBody.Builder builder = new FormBody.Builder();
        params.forEach((k, v) -> builder.add(k, v.toString()));
        return builder.build();
    }

    private static RequestBody bodyJson(Map<String, ?> params) {
        String json = JsonUtil.toJson(params);
        if (log.isDebugEnabled()) {
            log.debug("请求参数为{}", json);
        }
        return RequestBody.create(json, MEDIA_JSON);
    }

    private static HttpResult doRequest(final Request request) {
        init();
        try {
            return responseResult(client.newCall(request).execute());
        } catch (IOException e) {
            throw new HttpException(e);
        }
    }

    private static HttpResult responseResult(Response response) {
        if (response.body() != null) {
            try (Reader reader = response.body().charStream()) {
                int capacity = (int) response.body().contentLength();
                if (capacity < 0) {
                    capacity = 4096;
                }
                StringBuilder sb = new StringBuilder(capacity);
                char[] tmp = new char[1024];
                int l;
                while ((l = reader.read(tmp)) != -1) {
                    sb.append(tmp, 0, l);
                }
                return new HttpResult(response.code(), sb.toString());
            } catch (IOException e) {
                throw new HttpException(e);
            }
        }
        return new HttpResult(response.code(), response.message());
    }
}
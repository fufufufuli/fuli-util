package com.fuli.pudding.util;


import okhttp3.*;


import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class HttpUtil {
    private static volatile HttpUtil instance;
    private static OkHttpClient client;
    private static final int CONNECT_TIMEOUT = 10;
    private static final int READ_TIMEOUT = 20;
    private static final int MAX_IDLE_CONNECTIONS = 5;
    private static final int KEEP_ALIVE_DURATION = 5;
    private static final MediaType MEDIA_JSON = MediaType.parse("application/json; charset=utf-8");
    private static final String GET = "get";
    private static final String POST = "post";
    private static final String FORM = "form";
    private static final String FILE = "file";
    private static final String JSON = "json";
    private static final String WEBSERVICE = "webservice";

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
                instance = new HttpUtil(null);
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

    public static HttpResult callWeb(String method, String url, Map<String, ?> params, Map<String, String> headerMap) throws IOException {
        switch (method) {
            case HttpUtil.GET:
                return get(url, params, headerMap);
            case HttpUtil.POST:
                return post(url, params, headerMap, JSON);
            case HttpUtil.FORM:
                return post(url, params, headerMap, FORM);
         /*   case HttpUtil.WEBSERVICE:
                //todo
                return null;*/
            default:
                throw new ChannelException(ChannelEnum.TYPE, method);
        }
    }

    public static HttpResult get(String url, Map<String, ?> params) throws IOException {
        return doRequest(getRequest(url, params, null));
    }

    public static HttpResult get(String url, Map<String, ?> params, Map<String, String> headerMap) throws IOException {
        return doRequest(getRequest(url, params, headerMap));
    }

    public static HttpResult post(String url, Map<String, ?> params, String type) throws IOException {
        return doRequest(postRequest(url, params, type, null));
    }

    public static HttpResult post(String url, Map<String, ?> params, Map<String, String> headerMap, String type) throws IOException {
        return doRequest(postRequest(url, params, type, headerMap));
    }

    /**
     * jsonString
     */
    public static HttpResult post(String url, String json) throws IOException {
        return doRequest(postRequest(url, json, null));
    }

    /**
     * jsonString
     */
    public static HttpResult post(String url, String json, Map<String, String> headerMap) throws IOException {
        return doRequest(postRequest(url, json, headerMap));
    }

    private static Request getRequest(String url, Map<String, ?> params, Map<String, String> headerMap) {
        StringBuilder sb = new StringBuilder(url);
        if (params != null && params.size() > 0) {
            sb.append("?");
            for (Map.Entry<?, ?> kv : params.entrySet()) {
                sb.append(kv.getKey());
                sb.append("=");
                sb.append(kv.getValue());
                sb.append("&");
            }
            sb.deleteCharAt(sb.length() - 1);
        }
        if (headerMap != null && headerMap.size() > 0) {
            return new Request.Builder().url(sb.toString()).get().headers(Headers.of(headerMap)).build();
        }
        return new Request.Builder().url(sb.toString()).get().build();
    }

    private static Request postRequest(String url, Map<String, ?> params, String type, Map<String, String> headerMap) {
        RequestBody body;
        switch (type) {
            case JSON:
                body = bodyJson(params);
                break;
            case FORM:
                body = bodyForm(params);
                break;
            case FILE:
                body = bodyFile(params);
                break;
            default:
                throw new ChannelException(ChannelEnum.TYPE, type);
        }
        return buildRequest(url, body, headerMap);
    }

    private static Request buildRequest(String url, RequestBody body, Map<String, String> headerMap) {
        if (body != null && headerMap != null) {
            return new Request.Builder().url(url).headers(Headers.of(headerMap)).post(body).build();
        } else {
            if (body != null) {
                return new Request.Builder().url(url).post(body).build();
            }
            if (headerMap != null) {
                return new Request.Builder().url(url).headers(Headers.of(headerMap)).build();
            }
        }
        return new Request.Builder().url(url).build();
    }

    private static Request postRequest(String url, String json, Map<String, String> headerMap) {
        RequestBody body = RequestBody.create(MEDIA_JSON, json);
        return buildRequest(url, body, headerMap);
    }

    private static RequestBody bodyFile(Map<String, ?> params) {
        for (Map.Entry<String, ?> stringEntry : params.entrySet()) {
            if (stringEntry.getValue() instanceof File) {
                File file = (File) stringEntry.getValue();
                return new MultipartBody.Builder()
                        .setType(MultipartBody.FORM)
                        .addFormDataPart("file", file.getName(), RequestBody.create(MediaType.parse("image/png"), file))
                        .build();
            }
        }
        return null;
    }

    private static RequestBody bodyForm(Map<String, ?> params) {
        FormBody.Builder builder = new FormBody.Builder();
        for (Map.Entry<String, ?> stringEntry : params.entrySet()) {
            builder.add(stringEntry.getKey(), stringEntry.getValue().toString());
        }
        return builder.build();
    }

    private static RequestBody bodyJson(Map<String, ?> params) {
        return RequestBody.create(MEDIA_JSON, JSONUtil.toJson(params));
    }

    private static HttpResult doRequest(final Request request) throws IOException {
        init();
        Response response = client.newCall(request).execute();
        return responseResult(response);
    }

    private static HttpResult responseResult(Response response) throws IOException {
        if (response.isSuccessful() && response.body() != null) {
            try (Reader reader = response.body().charStream()) {
                int capacity = (int) response.body().contentLength();
                if (capacity < 0) {
                    capacity = 4096;
                }
                StringBuilder buffer = new StringBuilder(capacity);
                char[] tmp = new char[1024];
                int l;
                while ((l = reader.read(tmp)) != -1) {
                    buffer.append(tmp, 0, l);
                }
                return new HttpResult(response.code(), buffer.toString());
            }
        } else return new HttpResult(response.code(), response.message());
    }


}
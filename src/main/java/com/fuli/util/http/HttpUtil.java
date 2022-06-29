package com.fuli.util.http;

import com.fuli.util.Commons;
import com.fuli.util.json.JsonUtil;
import com.google.common.base.Joiner;
import okhttp3.*;
import okhttp3.MultipartBody.Builder;

import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author fuli
 */
public class HttpUtil {

    private static OkHttpClient client;
    private static final int CONNECT_TIMEOUT = 10;
    private static final int READ_TIMEOUT = 20;
    private static final int MAX_IDLE_CONNECTIONS = 5;
    private static final int KEEP_ALIVE_DURATION = 5;
    private static final MediaType MEDIA_JSON = MediaType.parse("application/json; charset=utf-8");
    public static final String GET = "get";
    public static final String POST = "post";

    public static final String FORM = "form";
    public static final String FILE = "file";
    public static final String SOAP = "soap";

    private HttpUtil() {
    }

    static {
        ConnectionPool pool = new ConnectionPool(MAX_IDLE_CONNECTIONS, KEEP_ALIVE_DURATION, TimeUnit.MINUTES);
        HttpUtil.client = new OkHttpClient.Builder()
                .connectTimeout(CONNECT_TIMEOUT, TimeUnit.SECONDS)
                .readTimeout(READ_TIMEOUT, TimeUnit.SECONDS)
                .retryOnConnectionFailure(true)
                .connectionPool(pool)
                .build();
    }

    public static OkHttpClient getOkHttpClient() {
        return HttpUtil.client;
    }

    public static HttpResult call(String method, String url, Map<String, ?> params, Map<String, String> header) {
        return switch (method) {
            case HttpUtil.GET -> get(url, params, header);
            case HttpUtil.POST, HttpUtil.FORM -> post(url, params, header, method);
                    /*   case HttpUtil.SOAP:
                //todo
                return null;*/
            default -> throw new HttpException(HttpException.MISS_TYPE, method);
        };
    }

    public static HttpResult get(String url) {
        return doRequest(getRequest(url, null, null));
    }

    public static HttpResult get(String url, Map<String, ?> params) {
        return doRequest(getRequest(url, params, null));
    }

    public static HttpResult get(String url, Map<String, ?> params, Map<String, String> header) {
        return doRequest(getRequest(url, params, header));
    }

    /**
     * 支持单个数组
     */
    public static HttpResult get(String url, String key, List<String> args) {
        return doRequest(getRequestArray(url, null, null, key, args));
    }

    public static HttpResult get(String url, Map<String, ?> params, String key, List<String> args) {
        return doRequest(getRequestArray(url, params, null, key, args));
    }

    public static HttpResult get(String url, Map<String, ?> params, Map<String, String> header, String key, List<String> args) {
        return doRequest(getRequestArray(url, params, header, key, args));
    }

    /**
     * 支持多个数组
     */
    @SafeVarargs
    public static HttpResult get(String url, List<String> keys, List<String>... args) {
        return doRequest(getRequestArrays(url, null, null, keys, args));
    }

    @SafeVarargs
    public static HttpResult get(String url, Map<String, ?> params, List<String> keys, List<String>... args) {
        return doRequest(getRequestArrays(url, params, null, keys, args));
    }

    @SafeVarargs
    public static HttpResult get(String url, Map<String, ?> params, Map<String, String> header, List<String> keys, List<String>... args) {
        return doRequest(getRequestArrays(url, params, header, keys, args));
    }

    @SafeVarargs
    private static Request getRequestArrays(String url, Map<String, ?> params, Map<String, String> header, List<String> keys, List<String>... args) {
        url = addUrl(url, params);
        if (Commons.isEmpty(keys)) {
            throw new HttpException("数组key值为空");
        } if (args==null|| keys.size() == args.length){
            throw new HttpException("数组为空或数组key的个数与数组个数不符");
        }
        StringBuilder urlBuilder = new StringBuilder(url);
        for (String key : keys) {
            for (List<String> arg : args) {
                for (String s : arg) {
                    urlBuilder.append("&").append(key).append("=").append(s);
                }
            }
        }
        url = urlBuilder.toString();
        return buildGetRequest(url, header);
    }

    private static String addUrl(String url, Map<String, ?> params) {
        if (Commons.isNotEmpty(params)) {
            url += "?" + Joiner.on("&").withKeyValueSeparator("=").join(params);
        }
        return url;
    }

    private static Request getRequestArray(String url, Map<String, ?> params, Map<String, String> header, String key, List<String> args) {
        url = addUrl(url, params);
        if (Commons.isNotEmpty(key)) {
            StringBuilder urlBuilder = new StringBuilder(url);
            for (String arg : args) {
                urlBuilder.append("&").append(key).append("=").append(arg);
            }
            url = urlBuilder.toString();
        }
        return buildGetRequest(url, header);
    }

    private static Request getRequest(String url, Map<String, ?> params, Map<String, String> header) {
        url = addUrl(url, params);
        return buildGetRequest(url, header);
    }

    private static Request buildGetRequest(String url, Map<String, String> header) {
        Request.Builder builder = new Request.Builder().url(url).get();
        if (Commons.isNotEmpty(header)) {
            builder.headers(Headers.of(header));
        }
        return builder.build();
    }

    public static HttpResult post(String url, String json, Map<String, String> header) {
        return doRequest(postRequest(url, json, header));
    }

    public static HttpResult post(String url, Map<String, ?> params, String method) {
        return doRequest(postRequest(url, params, method, null));
    }

    public static HttpResult post(String url, Map<String, ?> params, Map<String, String> header, String method) {
        return doRequest(postRequest(url, params, method, header));
    }

    public static HttpResult post(String url, String json) {
        return doRequest(postRequest(url, json, null));
    }

    private static Request postRequest(String url, Map<String, ?> params, String method, Map<String, String> header) {
        RequestBody body = switch (method) {
            case POST -> bodyJson(params);
            case FORM -> bodyForm(params);
            case FILE -> bodyFile(params);
            default -> throw new HttpException(HttpException.MISS_TYPE, method);
        };
        return buildPostRequest(url, body, header);
    }

    private static Request buildPostRequest(String url, RequestBody body, Map<String, String> header) {
        Request.Builder builder = new Request.Builder().url(url);
        if (Commons.isNotEmpty(header)) {
            builder.headers(Headers.of(header));
        }
        if (body != null) {
            builder.post(body);
        }
        return builder.build();
    }

    private static Request postRequest(String url, String json, Map<String, String> header) {
        RequestBody body = RequestBody.create(json, MEDIA_JSON);
        return buildPostRequest(url, body, header);
    }

    private static RequestBody bodyFile(Map<String, ?> params) {
        Builder builder = new Builder().setType(MultipartBody.FORM);
        params.forEach((k, v) -> {
            if (v instanceof File file) {
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
        return RequestBody.create(JsonUtil.toJson(params), MEDIA_JSON);
    }

    private static HttpResult doRequest(final Request request) {
        try {
            return responseResult(client.newCall(request).execute());
        } catch (IOException e) {
            throw new HttpException(e);
        }
    }

    private static HttpResult responseResult(Response response) throws IOException {
        if (response.body() == null) {
            return new HttpResult(response.code(), response.message());
        }
        int capacity = (int) response.body().contentLength();
        StringBuilder sb = new StringBuilder(capacity < 0 ? 4096 : capacity);
        char[] tmp = new char[1024];
        int l;
        try (Reader reader = response.body().charStream()) {
            while ((l = reader.read(tmp)) != -1) {
                sb.append(tmp, 0, l);
            }
        }
        return new HttpResult(response.code(), sb.toString());
    }
}

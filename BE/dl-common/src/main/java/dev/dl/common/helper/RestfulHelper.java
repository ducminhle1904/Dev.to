package dev.dl.common.helper;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializer;
import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;

import static dev.dl.common.constant.Constant.RESPONSE_OK;

@SuppressWarnings("DuplicatedCode")
@Slf4j
public class RestfulHelper {

    private static volatile RestfulHelper INSTANCE;

    private RestfulHelper() {
    }

    public static synchronized RestfulHelper getInstance() {
        if (Optional.ofNullable(INSTANCE).isEmpty()) {
            INSTANCE = new RestfulHelper();
        }
        return INSTANCE;
    }
    private static final Gson GSON;

    static {
        GSON = new GsonBuilder()
                .registerTypeAdapter(
                        LocalDateTime.class,
                        (JsonDeserializer<LocalDateTime>) (json, type, jsonDeserializationContext) -> LocalDateTime.parse(json.getAsJsonPrimitive().getAsString())
                )
                .registerTypeAdapter(
                        LocalDateTime.class,
                        (JsonSerializer<LocalDateTime>) (localDateTime, type, jsonSerializationContext) -> new JsonPrimitive(localDateTime.toString())
                )
                .registerTypeAdapter(
                        LocalDate.class,
                        (JsonDeserializer<LocalDate>) (json, type, jsonDeserializationContext) -> LocalDate.parse(json.getAsJsonPrimitive().getAsString())
                )
                .registerTypeAdapter(
                        LocalDate.class,
                        (JsonSerializer<LocalDate>) (localDate, type, jsonSerializationContext) -> new JsonPrimitive(localDate.toString())
                )
                .create();
    }

    /**
     * Convert object to a JSON string
     *
     * @param object
     * @return
     * @throws JsonProcessingException
     */
    public static String convertObjectToJsonString(Object object) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper().findAndRegisterModules();
        return mapper.writer().withDefaultPrettyPrinter().writeValueAsString(object);
    }

    /**
     * Convert object to a JSON string by GSON
     *
     * @param object
     * @return
     * @throws JsonProcessingException
     */
    public static String convertObjectToJsonStringGson(Object object) {
        return GSON.toJson(object);
    }

    /**
     * Convert an JSON string to an Object
     *
     * @param jsonString
     * @param classType
     * @param <T>
     * @return
     * @throws JsonProcessingException
     */
    public static <T> T convertJsonStringToObject(String jsonString, Class<T> classType) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper().findAndRegisterModules();
        return mapper.readValue(jsonString, classType);
    }

    /**
     * Convert an JSON string to an Object by GSON
     *
     * @param jsonString
     * @param classType
     * @param <T>
     * @return
     * @throws JsonProcessingException
     */
    public static <T> T convertJsonStringToObjectGson(String jsonString, Class<T> classType) throws JsonProcessingException {
        return GSON.fromJson(jsonString, classType);
    }

    /**
     * Create connect to an HTTP(S) endpoint
     *
     * @param host    base URL of an endpoint
     * @param service service of that endpoint
     * @return
     * @throws Exception
     */
    public HttpURLConnection generateConnection(String host, String service) throws Exception {
        URL url = new URL(String.format("%1$s%2$s", host, service));
        return (HttpURLConnection) url.openConnection();
    }

    public <RQ, RS> RS proceedRequest(String host, String service, String method, RQ request, Class<RS> response, Map<String, String> requestHeader) throws Exception {
        try {
            String responseString = this.sendRequest(host, service, method, requestHeader, request);
            return convertJsonStringToObject(responseString, response);
        } catch (Exception e) {
            return response.getDeclaredConstructor().newInstance();
        }
    }

    public <RQ, RS> Object proceedRequestAndReturnObject(String host, String service, String method, RQ request, Class<RS> response, Map<String, String> requestHeader) throws Exception {
        try {
            String responseString = this.sendRequest(host, service, method, requestHeader, request);
            try {
                return convertJsonStringToObject(responseString, response);
            } catch (Exception e) {
                return responseString;
            }
        } catch (Exception e) {
            log.info("[EXCEPTION] {}", e.getMessage());
        }
        return null;
    }

    public <RS> String sendRequest(String host, String service, String method, Map<String, String> requestHeader, RS request) throws Exception {
        HttpURLConnection http = this.generateConnection(host, service);
        http.setRequestMethod(method.toUpperCase());
        http.setDoOutput(true);
        http.setRequestProperty("Content-Type", "application/json");
        if (requestHeader != null && !requestHeader.isEmpty()) {
            for (Map.Entry<String, String> entry : requestHeader.entrySet()) {
                http.setRequestProperty(entry.getKey(), entry.getValue());
            }
        }
        if (request != null) {
            String data = convertObjectToJsonString(request);
            byte[] out = data.getBytes(StandardCharsets.UTF_8);
            OutputStream stream = http.getOutputStream();
            stream.write(out);
        }
        log.info("[REQUEST STATUS] {}", http.getResponseCode());
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(http.getResponseCode() == RESPONSE_OK ? http.getInputStream() : http.getErrorStream()));
        StringBuilder stringBuilder = new StringBuilder();
        String output;
        while ((output = bufferedReader.readLine()) != null) {
            stringBuilder.append(output);
        }
        return stringBuilder.toString();
    }

    public <RQ, RS> RS sendRequest(String endpoint, Map<String, String> requestHeader, String method, RQ input, Class<RS> responseClass) throws Exception {
        HttpRequest.Builder builder = HttpRequest.newBuilder()
                .uri(URI.create(endpoint))
                .headers("Content-Type", "application/json");
        if (input == null) {
            builder.method(method, HttpRequest.BodyPublishers.noBody());
        } else {
            builder.method(method, HttpRequest.BodyPublishers.ofString(convertObjectToJsonString(input)));
        }
        if (requestHeader != null && !requestHeader.isEmpty()) {
            for (Map.Entry<String, String> entry : requestHeader.entrySet()) {
                builder.header(entry.getKey(), entry.getValue());
            }
        }
        HttpRequest request = builder.build();
        HttpResponse<String> response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
        log.info("[RESPONSE STATUS] {}", response.statusCode());
        return convertJsonStringToObject(response.body(), responseClass);
    }

    public <RQ, RS> RS consume(String endpoint, Map<String, String> requestHeader, String method, RQ input, Class<RS> responseClass, Map<String, String> params) throws Exception {
        String url;
        if (ObjectHelper.isNullOrEmpty(params)) {
            url = endpoint;
        } else {
            url = this.buildParam(params);
        }
        HttpRequest.Builder builder = HttpRequest.newBuilder()
                .uri(URI.create(endpoint))
                .headers("Content-Type", "application/json");
        if (input == null) {
            builder.method(method, HttpRequest.BodyPublishers.noBody());
        } else {
            builder.method(method, HttpRequest.BodyPublishers.ofString(convertObjectToJsonStringGson(input)));
        }
        if (requestHeader != null && !requestHeader.isEmpty()) {
            for (Map.Entry<String, String> entry : requestHeader.entrySet()) {
                builder.header(entry.getKey(), entry.getValue());
            }
        }
        HttpRequest request = builder.build();
        HttpResponse<String> response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
        log.info("[RESPONSE STATUS to ({})] {}", endpoint, response.statusCode());
        if (response.statusCode() != RESPONSE_OK) {
            return null;
        }
        RS responseResult = GSON.fromJson(response.body(), responseClass);
        if (responseResult.equals(responseClass.getDeclaredConstructor().newInstance())) {
            return null;
        }
        return responseResult;
    }

    private String buildParam(Map<String, String> params) {
        StringBuilder stringBuilder = (new StringBuilder()).append("?");
        params.keySet().stream().forEach(key -> {
            stringBuilder.append(
                    String.format(
                            "%1$s=%2$s&",
                            key,
                            params.get(key)
                    )
            );
        });
        stringBuilder.deleteCharAt(stringBuilder.length() - 1);
        return stringBuilder.toString();
    }
}

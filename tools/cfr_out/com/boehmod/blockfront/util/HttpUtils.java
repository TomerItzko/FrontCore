/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.jetbrains.annotations.NotNull
 */
package com.boehmod.blockfront.util;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import org.jetbrains.annotations.NotNull;

public class HttpUtils {
    @NotNull
    private static final HttpClient CLIENT = HttpClient.newHttpClient();

    @NotNull
    public static String get(@NotNull URI uri) throws IOException, InterruptedException {
        HttpRequest httpRequest = HttpRequest.newBuilder().uri(uri).GET().build();
        HttpResponse<String> httpResponse = CLIENT.send(httpRequest, HttpResponse.BodyHandlers.ofString());
        if (httpResponse.statusCode() != 200) {
            throw new IOException("HTTP response code: " + httpResponse.statusCode());
        }
        return httpResponse.body();
    }
}


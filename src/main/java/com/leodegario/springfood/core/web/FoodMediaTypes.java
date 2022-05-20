package com.leodegario.springfood.core.web;

import org.springframework.http.MediaType;

public class FoodMediaTypes {
    public static final String V1_APPLICATION_JSON_VALUE = "application/vnd.springfood.v1+json";
    public static final MediaType V1_APPLICATION_JSON = MediaType.valueOf("application/vnd.springfood.v1+json");

    public static final String V2_APPLICATION_JSON_VALUE = "application/vnd.springfood.v2+json";
    public static final MediaType V2_APPLICATION_JSON = MediaType.valueOf("application/vnd.springfood.v2+json");
}

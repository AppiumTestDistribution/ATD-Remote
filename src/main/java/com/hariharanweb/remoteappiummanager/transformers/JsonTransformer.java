package com.hariharanweb.remoteappiummanager.transformers;

import com.google.gson.Gson;
import spark.ResponseTransformer;

public class JsonTransformer implements ResponseTransformer {

    private Gson gson = new Gson();

    public String render(Object model) {
        return gson.toJson(model);
    }

}
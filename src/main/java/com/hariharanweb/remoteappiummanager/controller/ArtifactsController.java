package com.hariharanweb.remoteappiummanager.controller;

import org.json.JSONObject;
import spark.Route;

import javax.servlet.MultipartConfigElement;
import javax.servlet.http.Part;
import java.io.File;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

import static spark.Spark.notFound;

public class ArtifactsController {

    public Route upload = (request, response) -> {
        String artifactLocation = "upload";
        if (System.getProperty("ARTIFACT_LOCATION") != null) {
            artifactLocation = System.getProperty("ARTIFACT_LOCATION");
        }
        File uploadDir = new File(artifactLocation);
        if (!uploadDir.exists()) {
            uploadDir.mkdirs();
        }

        request.attribute("org.eclipse.jetty.multipartConfig", new MultipartConfigElement("/temp"));

        Part uploadedFile = request.raw().getPart("uploaded_file");
        File file = new File(uploadDir, uploadedFile.getSubmittedFileName());

        try (InputStream input = uploadedFile.getInputStream()) {
            Files.copy(input, file.toPath(), StandardCopyOption.REPLACE_EXISTING);
        }
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("filePath", file.getAbsoluteFile());

        return jsonObject;
    };
}

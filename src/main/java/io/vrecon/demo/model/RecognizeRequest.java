package io.vrecon.demo.model;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class RecognizeRequest {
    private String apiKey;
    private String image;
    private String format;

    public RecognizeRequest() {}

    public RecognizeRequest(String apiKey, String image, String format) {
        this.apiKey = apiKey;
        this.image = image;
        this.format = format;
    }

    public String getApiKey() {
        return apiKey;
    }

    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }
}

package com.example.utils.GptApi;

import com.example.model.external.gpt.GptRequest;
import com.example.model.external.gpt.GptResponse;

public interface GptApi {
    public GptResponse getAnswerFromGpt(GptRequest gptRequest);
}

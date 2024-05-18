package com.example.utils.GptApi;

import com.example.model.dto.external.gpt.GptRequest;
import com.example.model.dto.external.gpt.GptResponse;

public interface GptApi {
    public GptResponse getAnswerFromGpt(GptRequest gptRequest);
}

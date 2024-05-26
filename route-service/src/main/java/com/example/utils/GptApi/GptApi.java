package com.example.utils.GptApi;

import com.example.model.dto.gpt.GptRequest;
import com.example.model.dto.gpt.GptResponse;

public interface GptApi {
    public GptResponse getAnswerFromGpt(GptRequest gptRequest);
}

package com.example.model.dto.external.gpt;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class GptMessage {
    private GptRole role;
    private String text;
}

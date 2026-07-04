package com.naadi.ojas.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SendDemoLinkRequest {

    @NotBlank(message = "Live link is required")
    @Pattern(
            regexp = "^https?://.+",
            message = "Live link must start with http:// or https://"
    )
    private String liveLink;

    @Size(max = 500, message = "Note must be under 500 characters")
    private String note;
}
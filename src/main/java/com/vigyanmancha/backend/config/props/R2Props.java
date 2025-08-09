package com.vigyanmancha.backend.config.props;

import jakarta.validation.constraints.NotBlank;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

@Component
@ConfigurationProperties(prefix = "cloudflare.r2")
@Validated
@Getter
@Setter
@EqualsAndHashCode
public class R2Props {
    @NotBlank
    private String accessKey;
    @NotBlank
    private String secretKey;
    @NotBlank
    private String region;
    @NotBlank
    private String endpoint;
    @NotBlank
    private String bucket;
}

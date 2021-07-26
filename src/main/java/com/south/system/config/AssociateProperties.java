package com.south.system.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@ConfigurationProperties("com.south-system.associate-properties")
@Component
public class AssociateProperties {

    private String cpfValidatorUrl;
}

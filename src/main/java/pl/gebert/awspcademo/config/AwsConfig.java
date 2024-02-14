package pl.gebert.awspcademo.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "aws")
public class AwsConfig {

  private String certificateAuthorityArn;
  private String templateArn;

  public String getCertificateAuthorityArn() {
    return certificateAuthorityArn;
  }

  public void setCertificateAuthorityArn(String certificateAuthorityArn) {
    this.certificateAuthorityArn = certificateAuthorityArn;
  }

  public String getTemplateArn() {
    return templateArn;
  }

  public void setTemplateArn(String templateArn) {
    this.templateArn = templateArn;
  }
}

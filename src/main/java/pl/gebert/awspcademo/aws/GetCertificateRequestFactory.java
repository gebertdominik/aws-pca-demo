package pl.gebert.awspcademo.aws;

import com.amazonaws.services.acmpca.model.GetCertificateRequest;
import org.springframework.stereotype.Component;

@Component
public class GetCertificateRequestFactory {

  public GetCertificateRequest create(String certificateArn) {
    // Create a certificate request:
    GetCertificateRequest req = new GetCertificateRequest();
    req.withCertificateArn(certificateArn);
    req.withCertificateAuthorityArn("arn:aws:acm-pca:eu-west-2:040238858521:certificate-authority/56c4ce00-8c41-47b8-a5e4-3a9ecd4057b2"); //todo
    return req;
  }
}

package pl.gebert.awspcademo.aws;

import com.amazonaws.services.acmpca.model.IssueCertificateRequest;
import com.amazonaws.services.acmpca.model.SigningAlgorithm;
import com.amazonaws.services.acmpca.model.Validity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import pl.gebert.awspcademo.config.AwsConfig;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

@Component
public class IssueCertificateRequestFactory {

  private static final Logger LOG = LoggerFactory.getLogger(IssueCertificateRequestFactory.class);

  private final AwsConfig awsConfig;

  public IssueCertificateRequestFactory(AwsConfig awsConfig) {
    this.awsConfig = awsConfig;
  }

  public IssueCertificateRequest createIssueCertificateRequest(String csr) {
    // Create a certificate request:
    IssueCertificateRequest req = new IssueCertificateRequest();

    // Set the CA ARN.
    req.withCertificateAuthorityArn(awsConfig.getCertificateAuthorityArn());

    // Specify the certificate signing request (CSR) for the certificate to be signed and issued.
    String strCSR = csr;


    LOG.info("################");
    LOG.info(strCSR);
    LOG.info("################");


    ByteBuffer csrByteBuffer = stringToByteBuffer(strCSR);
    req.setCsr(csrByteBuffer);

    // Specify the template for the issued certificate.
    req.withTemplateArn(awsConfig.getTemplateArn());

    // Set the signing algorithm.
    req.withSigningAlgorithm(SigningAlgorithm.SHA256WITHRSA);

    // Set the validity period for the certificate to be issued.
    Validity validity = new Validity();
    validity.withValue(365L);
    validity.withType("DAYS");
    req.withValidity(validity);

    // Set the idempotency token.
    req.setIdempotencyToken("1234"); //TODO
    return req;
  }

  private static ByteBuffer stringToByteBuffer(final String string) {
    if (Objects.isNull(string)) {
      return null;
    }
    byte[] bytes = string.getBytes(StandardCharsets.UTF_8);
    return ByteBuffer.wrap(bytes);
  }
}

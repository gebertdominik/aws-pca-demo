package pl.gebert.awspcademo;

import com.amazonaws.services.acmpca.model.GetCertificateResult;
import com.amazonaws.services.acmpca.model.IssueCertificateResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import pl.gebert.awspcademo.aws.AwsCertificateService;

import java.security.KeyPair;

@SpringBootApplication
public class AwsPcaDemoApplication implements CommandLineRunner {

  private static final Logger LOG = LoggerFactory.getLogger(AwsPcaDemoApplication.class);

  private final AwsCertificateService awsCertificateService;
  private final RsaKeyPairFactory rsaKeyPairFactory;

  private final CertificateSigningRequestFactory certificateSigningRequestFactory;

  public AwsPcaDemoApplication(
      AwsCertificateService awsCertificateService,
      RsaKeyPairFactory rsaKeyPairService,
      CertificateSigningRequestFactory certificateSigningRequestFactory
  ) {
    this.awsCertificateService = awsCertificateService;
    this.rsaKeyPairFactory = rsaKeyPairService;
    this.certificateSigningRequestFactory = certificateSigningRequestFactory;
  }

  public static void main(String[] args) {
    LOG.info("STARTING THE APPLICATION");
    SpringApplication.run(AwsPcaDemoApplication.class, args);
    LOG.info("APPLICATION FINISHED");
  }

  @Override
  public void run(String... args) throws Exception {
    LOG.info("EXECUTING : command line runner");
    KeyPair keyPair = rsaKeyPairFactory.generateRSAKeyPair();
    String certificateSigningRequest = certificateSigningRequestFactory.create(keyPair);
    IssueCertificateResult issueCertificateResult = awsCertificateService.issueCertificate(certificateSigningRequest);
    LOG.info("Certificate ARN: " + issueCertificateResult.getCertificateArn());
    GetCertificateResult getCertificateResult = awsCertificateService.getCertificate(issueCertificateResult.getCertificateArn());
    LOG.info("Certificate: " + getCertificateResult.getCertificate());
  }
}

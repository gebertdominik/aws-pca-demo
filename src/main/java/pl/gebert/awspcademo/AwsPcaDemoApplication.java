package pl.gebert.awspcademo;

import com.amazonaws.AmazonClientException;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.acmpca.AWSACMPCA;
import com.amazonaws.services.acmpca.AWSACMPCAClientBuilder;
import com.amazonaws.services.acmpca.model.GetCertificateRequest;
import com.amazonaws.services.acmpca.model.IssueCertificateRequest;
import com.amazonaws.services.acmpca.model.IssueCertificateResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import pl.gebert.awspcademo.aws.AWSCredentialsFactory;
import pl.gebert.awspcademo.aws.IssueCertificateRequestFactory;

import java.security.KeyPair;

@SpringBootApplication
public class AwsPcaDemoApplication implements CommandLineRunner {

  private static final Logger LOG = LoggerFactory.getLogger(AwsPcaDemoApplication.class);

  private final IssueCertificateRequestFactory issueCertificateRequestFactory;

  private final AWSCredentialsFactory awsCredentialsFactory;

  private final RsaKeyPairFactory rsaKeyPairFactory;

  private final CertificateSigningRequestFactory certificateSigningRequestFactory;
  public AwsPcaDemoApplication(
      IssueCertificateRequestFactory issueCertificateRequestFactory,
      AWSCredentialsFactory awsCredentialsFactory,
      RsaKeyPairFactory rsaKeyPairService,
      CertificateSigningRequestFactory certificateSigningRequestFactory
  ) {
    this.issueCertificateRequestFactory = issueCertificateRequestFactory;
    this.awsCredentialsFactory = awsCredentialsFactory;
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
    AWSCredentials credentials = awsCredentialsFactory.createDefaultCredentials();
    AWSACMPCA awsAcmPcaClient = createAwsAcmPcaClient(credentials);
    KeyPair keyPair = rsaKeyPairFactory.generateRSAKeyPair();
    String csr = certificateSigningRequestFactory.create(keyPair);
    IssueCertificateRequest req = issueCertificateRequestFactory.createIssueCertificateRequest(csr);
    IssueCertificateResult issueCertificateResult = awsAcmPcaClient.issueCertificate(req);
    LOG.info("Certificate ARN: " + issueCertificateResult.getCertificateArn());

  }

  private static AWSACMPCA createAwsAcmPcaClient(AWSCredentials credentials) {
    // Define the endpoint for your sample.
    String endpointRegion = "eu-west-2";  // Substitute your region here, e.g. "us-west-2"
    String endpointProtocol = "https://acm-pca." + endpointRegion + ".amazonaws.com/";
    AwsClientBuilder.EndpointConfiguration endpoint =
        new AwsClientBuilder.EndpointConfiguration(endpointProtocol, endpointRegion);

    // Create a client that you can use to make requests.
    return AWSACMPCAClientBuilder.standard()
        .withEndpointConfiguration(endpoint)
        .withCredentials(new AWSStaticCredentialsProvider(credentials))
        .build();
  }


  private AWSCredentials getCredentials() {
    try {
      AWSCredentials credentials = new ProfileCredentialsProvider("default").getCredentials();
      LOG.info("Credentials loaded from file: " + credentials.getAWSAccessKeyId() + " " + credentials.getAWSSecretKey());
      return credentials;
    } catch (Exception e) {
      throw new AmazonClientException("Cannot load your credentials from disk", e);
    }
  }
}

package pl.gebert.awspcademo.aws;

import com.amazonaws.services.acmpca.AWSACMPCA;
import com.amazonaws.services.acmpca.model.*;
import org.springframework.stereotype.Component;
import pl.gebert.awspcademo.config.AwsConfig;

@Component
public class AwsCertificateService {

  private final AwsAcmPcaClientFactory awsAcmPcaClientFactory;
  private final IssueCertificateRequestFactory issueCertificateRequestFactory;

  private final GetCertificateRequestFactory getCertificateRequestFactory;

  private final AwsConfig awsConfig;

  public AwsCertificateService(AwsAcmPcaClientFactory awsAcmPcaClientFactory, IssueCertificateRequestFactory issueCertificateRequestFactory, GetCertificateRequestFactory getCertificateRequestFactory, AwsConfig awsConfig) {
    this.awsAcmPcaClientFactory = awsAcmPcaClientFactory; //TODO inject client, not the factory
    this.issueCertificateRequestFactory = issueCertificateRequestFactory;
    this.getCertificateRequestFactory = getCertificateRequestFactory;
    this.awsConfig = awsConfig;
  }

  public IssueCertificateResult issueCertificate(String certificateSigningRequest) {
    AWSACMPCA awsAcmPcaClient = awsAcmPcaClientFactory.create();
    IssueCertificateRequest issueCertificateRequest = issueCertificateRequestFactory.create(certificateSigningRequest);
    return awsAcmPcaClient.issueCertificate(issueCertificateRequest);
  }

  public GetCertificateResult getCertificate(String certificateArn) throws InterruptedException {
    AWSACMPCA awsAcmPcaClient = awsAcmPcaClientFactory.create();
    GetCertificateRequest getCertificateRequest = getCertificateRequestFactory.create(certificateArn);
    GetCertificateResult result = null;
    long totalTimeout = 120000L;
    long timeSlept = 0L;
    long sleepInterval = 10000L;
    while (result == null && timeSlept < totalTimeout) {
      try {
        result = awsAcmPcaClient.getCertificate(getCertificateRequest);
      } catch (RequestInProgressException ex) {
        Thread.sleep(sleepInterval);
      } catch (ResourceNotFoundException | InvalidArnException ex) {
        throw ex;
      }

      timeSlept += sleepInterval;
    }
    return result;
  }

}

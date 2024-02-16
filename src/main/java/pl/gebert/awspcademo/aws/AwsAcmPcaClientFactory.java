package pl.gebert.awspcademo.aws;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.acmpca.AWSACMPCA;
import com.amazonaws.services.acmpca.AWSACMPCAClientBuilder;
import org.springframework.stereotype.Component;

@Component
public class AwsAcmPcaClientFactory {

  private final AwsCredentialsFactory awsCredentialsFactory;

  public AwsAcmPcaClientFactory(AwsCredentialsFactory awsCredentialsFactory) {
    this.awsCredentialsFactory = awsCredentialsFactory;
  }

  public AWSACMPCA create() {
    // Define the endpoint for your sample.
    String endpointRegion = "eu-west-2";  // Substitute your region here, e.g. "us-west-2"
    String endpointProtocol = "https://acm-pca." + endpointRegion + ".amazonaws.com/";
    AWSCredentials credentials = awsCredentialsFactory.createDefaultCredentials();

    AwsClientBuilder.EndpointConfiguration endpoint =
        new AwsClientBuilder.EndpointConfiguration(endpointProtocol, endpointRegion);

    // Create a client that you can use to make requests.
    return AWSACMPCAClientBuilder.standard()
        .withEndpointConfiguration(endpoint)
        .withCredentials(new AWSStaticCredentialsProvider(credentials))
        .build();
  }
}

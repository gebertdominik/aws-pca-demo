package pl.gebert.awspcademo.aws;

import com.amazonaws.AmazonClientException;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class AwsCredentialsFactory {

  private static final Logger LOG = LoggerFactory.getLogger(AwsCredentialsFactory.class);

  public AWSCredentials createDefaultCredentials() {
    try {
      AWSCredentials credentials = new ProfileCredentialsProvider("default").getCredentials();
      LOG.info("Credentials loaded from file: " + credentials.getAWSAccessKeyId() + " " + credentials.getAWSSecretKey());
      return credentials;
    } catch (Exception e) {
      throw new AmazonClientException("Cannot load your credentials from disk", e);
    }
  }
}

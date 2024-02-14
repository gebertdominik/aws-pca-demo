package pl.gebert.awspcademo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;

@Component
public class RsaKeyPairFactory {
  private static final Logger LOG = LoggerFactory.getLogger(RsaKeyPairFactory.class);
  private static final String KEY_PAIR_ALGORITHM = "RSA";
  private static final int KEY_SIZE = 2048;

  public KeyPair generateRSAKeyPair() throws NoSuchAlgorithmException {
    try {
      KeyPairGenerator kpg = KeyPairGenerator.getInstance(KEY_PAIR_ALGORITHM);
      kpg.initialize(KEY_SIZE);
      return kpg.generateKeyPair();
    } catch (Exception e) {
      LOG.error("Error generating RSA key pair", e);
      throw e;
    }
  }
}
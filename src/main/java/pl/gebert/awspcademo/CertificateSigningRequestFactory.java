package pl.gebert.awspcademo;

import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.operator.ContentSigner;
import org.bouncycastle.operator.OperatorCreationException;
import org.bouncycastle.operator.jcajce.JcaContentSignerBuilder;
import org.bouncycastle.pkcs.PKCS10CertificationRequest;
import org.bouncycastle.pkcs.PKCS10CertificationRequestBuilder;
import org.bouncycastle.pkcs.jcajce.JcaPKCS10CertificationRequestBuilder;
import org.springframework.stereotype.Component;
import pl.gebert.awspcademo.config.CertificateConfig;

import java.io.IOException;
import java.security.KeyPair;
import java.util.Base64;

@Component
public class CertificateSigningRequestFactory {

  private final CertificateConfig certificateConfig;

  public CertificateSigningRequestFactory(CertificateConfig certificateConfig) {
    this.certificateConfig = certificateConfig;
  }
  public String create(KeyPair keyPair) throws IOException, OperatorCreationException {
    PKCS10CertificationRequestBuilder p10Builder = new JcaPKCS10CertificationRequestBuilder(
        new X500Name(certificateConfig.getCommonName()),
        keyPair.getPublic()
    );

    JcaContentSignerBuilder csBuilder = new JcaContentSignerBuilder(certificateConfig.getSignatureAlgorithm());
    ContentSigner signer = csBuilder.build(keyPair.getPrivate());
    PKCS10CertificationRequest csr = p10Builder.build(signer);
    String csrString = Base64.getEncoder().encodeToString(csr.getEncoded());
    return "-----BEGIN CERTIFICATE REQUEST-----\n" + csrString.replaceAll("(.{64})", "$1\n") + "\n-----END CERTIFICATE REQUEST-----\n";
  }
}

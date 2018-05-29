package application.sephirmobile.sephirinterface;

import java.net.URI;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.converter.FormHttpMessageConverter;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import application.sephirmobile.sephirinterface.entitys.Certification;
import application.sephirmobile.sephirinterface.entitys.Login;
import application.sephirmobile.sephirinterface.exceptions.LoginException;
import application.sephirmobile.sephirinterface.forms.LoginForm;
import application.sephirmobile.sephirinterface.getters.CertificationGetter;

public class SephirInterface {
  private static final Logger LOGGER = LoggerFactory.getLogger(SephirInterface.class);
  public static final String BASE_URL = "https://sephir.ch/ICT/user/lernendenportal/";
  private RestTemplate restTemplate;
  private Certification certification = new Certification("", "");

  public SephirInterface() {
    restTemplate = new RestTemplate();
    restTemplate.getMessageConverters().add(new FormHttpMessageConverter());
  }

  public void login(Login login) throws LoginException {
    certification = new CertificationGetter(this).get();
    String response = new LoginForm(this).submit(login);
    if (response == null) {
      throw new LoginException("Login failed.");
    }
    LOGGER.debug("Login successful");
  }

  public String get(String url, MultiValueMap<String, String> getMap) {
    return restTemplate.getForEntity(buildUri(url, getMap), String.class).getBody();
  }

  public String get(String url) {
    return get(url, new LinkedMultiValueMap<>());
  }

  public String post(String url, MultiValueMap<String, String> postMap, MultiValueMap<String, String> getMap) {
    return restTemplate.postForEntity(buildUri(url, getMap), new HttpEntity<>(postMap, new HttpHeaders()), String.class)
        .getBody();
  }

  public String post(String url, MultiValueMap<String, String> postMap) {
    return post(url, postMap, new LinkedMultiValueMap<>());
  }

  URI buildUri(String url, MultiValueMap<String, String> getMap) {
    return UriComponentsBuilder.fromHttpUrl(BASE_URL).path(url).queryParam("cfid", certification.getCfid())
        .queryParam("cftoken", certification.getCftoken()).queryParams(getMap).build().toUri();
  }

  public RestTemplate getRestTemplate() {
    return restTemplate;
  }

  public Certification getCertification() {
    return certification;
  }

  void setCertification(Certification certification) {
    this.certification = certification;
  }
}

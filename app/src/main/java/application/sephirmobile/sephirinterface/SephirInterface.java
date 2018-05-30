package application.sephirmobile.sephirinterface;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import application.sephirmobile.sephirinterface.entitys.Certification;
import application.sephirmobile.sephirinterface.entitys.Login;
import application.sephirmobile.sephirinterface.entitys.Semester;
import application.sephirmobile.sephirinterface.exceptions.LoginException;
import application.sephirmobile.sephirinterface.forms.LoginForm;
import application.sephirmobile.sephirinterface.forms.SemesterChangeForm;
import application.sephirmobile.sephirinterface.getters.CertificationGetter;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class SephirInterface {
  private static final Logger LOGGER = LoggerFactory.getLogger(SephirInterface.class);
  public static final String BASE_URL = "https://sephir.ch/ICT/user/lernendenportal/";
  private SephirInterfaceService sephirInterfaceService;
  private Certification certification = new Certification("", "");

  public SephirInterface() {
    OkHttpClient client = new OkHttpClient.Builder().addInterceptor(new Interceptor() {
      @Override
      public okhttp3.Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        Certification certification = getCertification();
        HttpUrl url = request.url().newBuilder().addQueryParameter("cfid", certification.getCfid()).addQueryParameter("cftoken", certification.getCftoken()).build();
        request = request.newBuilder().url(url).build();
        LOGGER.debug("Method: {}", request.method());
        LOGGER.debug("Url: {}", url);
        return chain.proceed(request);
      }
    }).followRedirects(false).build();
    sephirInterfaceService = new Retrofit.Builder().baseUrl(BASE_URL).client(client).addConverterFactory(ScalarsConverterFactory.create()).build().create(SephirInterfaceService.class);
  }

  public void login(Login login) throws LoginException, IOException {
    certification = new CertificationGetter(this).get();
    boolean response = new LoginForm(this).submit(login);
    if (response == false) {
      throw new LoginException("Login failed.");
    }
    LOGGER.debug("Login successful");
  }

    public void changeSemester(Semester semester) throws IOException {
        new SemesterChangeForm(this).changeSemester(semester);
    }

    public Response<String> getForResponse(String url, Map<String, String> getMap) throws IOException {
        Response<String> response = sephirInterfaceService.get(url, getMap).execute();
        LOGGER.debug("Response with Code: {}", response.code());
        return response;
    }

  public String get(String url, Map<String, String> getMap) throws IOException {
      return getForResponse(url, getMap).body();
  }

  public String get(String url) throws IOException {
    return get(url, new HashMap<>());
  }

    public Response<String> postForResponse(String url, Map<String, String> postMap, Map<String, String> getMap) throws IOException {
        Response<String> response = sephirInterfaceService.post(url,postMap, getMap).execute();
        LOGGER.debug("Response with Code: {}", response.code());
        return response;
    }

  public String post(String url, Map<String, String> postMap, Map<String, String> getMap) throws IOException {
    return postForResponse(url, postMap, getMap).body();
  }

  public String post(String url, Map<String, String> postMap) throws IOException {
    return post(url,postMap, new HashMap<>());
  }

  public SephirInterfaceService getSephirInterfaceService() {
    return sephirInterfaceService;
  }

  public Certification getCertification() {
    return certification;
  }

  void setCertification(Certification certification) {
    this.certification = certification;
  }
}

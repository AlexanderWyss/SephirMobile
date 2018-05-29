package application.sephirmobile.sephirinterface;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.util.List;

import org.junit.Ignore;
import org.junit.Test;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import application.sephirmobile.sephirinterface.entitys.Certification;
import application.sephirmobile.sephirinterface.entitys.Login;
import application.sephirmobile.sephirinterface.entitys.SchoolClass;
import application.sephirmobile.sephirinterface.exceptions.LoginException;
import application.sephirmobile.sephirinterface.getters.SchoolClassGetter;

public class SephirInterfaceTest {
  @Test
  @Ignore("Does real Requests to Sephir")
  public void sephirInterfaceTest() throws Exception {
    SephirInterface sephirInterface = new SephirInterface();
    sephirInterface.login(getLogin());

    //SchoolClassGetter schoolClassGetter = new SchoolClassGetter(sephirInterface);
    //List<SchoolClass> schoolClasses = schoolClassGetter.get();
    //System.out.println(schoolClasses);
  }

  private Login getLogin() throws IOException {
   /* BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
    System.out.print("Email: ");
    String email = reader.readLine().trim();
    email = "".equals(email) ? "alexander_wyss@sluz.ch" : email;
    System.out.print("Password (WARNING: Visible): ");
    String password = reader.readLine();
    Login login = new Login(email, password);*/
    return new Login("alexander_wyss@sluz.ch", "");
  }

  @Test(expected = LoginException.class)
  @Ignore("Does real Requests to Sephir")
  public void wrongLoginData_login_loginException() throws Exception {
    SephirInterface sephirInterface = new SephirInterface();
    sephirInterface.login(new Login("a", "a"));
  }

  @Test
  public void sephirInterface_buildUri_correctUri() throws Exception {
    SephirInterface sephirInterface = new SephirInterface();
    sephirInterface.setCertification(new Certification("id", "token"));
    MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
    map.add("de", "Baum");
    map.add("en", "tree");

    URI uri = sephirInterface.buildUri("test.php", map);

    assertThat(uri.toString(),
        is("https://sephir.ch/ICT/user/lernendenportal/test.php?cfid=id&cftoken=token&de=Baum&en=tree"));
  }
}

package application.sephirmobile.sephirinterface;

import org.junit.Ignore;
import org.junit.Test;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.net.URI;
import java.util.List;

import application.sephirmobile.sephirinterface.entitys.AnnouncedTest;
import application.sephirmobile.sephirinterface.entitys.Certification;
import application.sephirmobile.sephirinterface.entitys.Login;
import application.sephirmobile.sephirinterface.exceptions.LoginException;
import application.sephirmobile.sephirinterface.getters.AnnouncedTestGetter;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class SephirInterfaceTest {
    @Test
    @Ignore("Does real Requests to Sephir")
    public void sephirInterfaceTest() throws Exception {
        //Login
        SephirInterface sephirInterface = new SephirInterface();
        sephirInterface.login(new Login("email", "You'd like to know that, wouldn't you?"));

        /*
        //Get SchoolClasses
        SchoolClassGetter schoolClassGetter = new SchoolClassGetter(sephirInterface);
        List<SchoolClass> schoolClasses = schoolClassGetter.get();
        System.out.println(schoolClasses);

        //Get Tests
        for (SchoolClass schoolClass : schoolClasses) {
            SchoolTestGetter testGetter = new SchoolTestGetter(sephirInterface);
            List<SchoolTest> tests = testGetter.get(schoolClass);
            System.out.println(tests);
        }
        */

        //Get Announced Tests
        AnnouncedTestGetter announcedTestGetter = new AnnouncedTestGetter(sephirInterface);
        List<AnnouncedTest> announcedTests = announcedTestGetter.get();
        System.out.println(announcedTests);
    }

    @Test(expected = LoginException.class)
    @Ignore("Does real Requests to Sephir")
    public void wrongLoginData_login_loginException() throws Exception {
        SephirInterface sephirInterface = new SephirInterface();
        sephirInterface.login(new Login("a", "a"));
    }

    @Test
    public void sephirInterface_buildUri_correctUri() {
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

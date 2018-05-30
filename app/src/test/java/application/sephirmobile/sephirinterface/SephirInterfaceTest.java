package application.sephirmobile.sephirinterface;

import org.junit.Ignore;
import org.junit.Test;

import java.io.IOException;
import java.util.List;

import application.sephirmobile.sephirinterface.entitys.AnnouncedTest;
import application.sephirmobile.sephirinterface.entitys.Certification;
import application.sephirmobile.sephirinterface.entitys.Login;
import application.sephirmobile.sephirinterface.entitys.SchoolClass;
import application.sephirmobile.sephirinterface.entitys.SchoolTest;
import application.sephirmobile.sephirinterface.exceptions.LoginException;
import application.sephirmobile.sephirinterface.getters.AnnouncedTestGetter;
import application.sephirmobile.sephirinterface.getters.AverageTestMarkGetter;
import application.sephirmobile.sephirinterface.getters.SchoolClassGetter;
import application.sephirmobile.sephirinterface.getters.SchoolTestGetter;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.scalars.ScalarsConverterFactory;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class SephirInterfaceTest {
    @Test
    @Ignore("Does real Requests to Sephir")
    public void sephirInterfaceTest() throws Exception {
        //Login
        SephirInterface sephirInterface = new SephirInterface();
        sephirInterface.login(new Login("alexander_wyss@sluz.ch", "***REMOVED***"));

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

            tests.stream().filter(test -> test.getMark() != 0).findFirst().ifPresent(test-> {
                try {
                    double averageMark = test.getAverageMark(sephirInterface);
                    System.out.println(averageMark);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        }


        //Get Announced Tests
        AnnouncedTestGetter announcedTestGetter = new AnnouncedTestGetter(sephirInterface);
        List<AnnouncedTest> announcedTests = announcedTestGetter.get();
        System.out.println(announcedTests);


        //Get average Mark (or use method on SchoolTest
        AverageTestMarkGetter averageTestMarkGetter = new AverageTestMarkGetter(sephirInterface);
        double averageMark = averageTestMarkGetter.get(new SchoolTest(null, null, null, null, null, 0, 0, "216054"));
        System.out.println(averageMark);
        */

    }

    @Test(expected = LoginException.class)
    @Ignore("Does real Requests to Sephir")
    public void wrongLoginData_login_loginException() throws Exception {
        SephirInterface sephirInterface = new SephirInterface();
        sephirInterface.login(new Login("a", "a"));
    }
}

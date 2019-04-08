package application.sephirmobile.sephirinterface;

import android.app.job.JobScheduler;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;
import java.util.List;

import application.sephirmobile.login.Login;
import application.sephirmobile.sephirinterface.entitys.AnnouncedTest;
import application.sephirmobile.sephirinterface.entitys.AverageSubjectMarks;
import application.sephirmobile.sephirinterface.entitys.SchoolClass;
import application.sephirmobile.sephirinterface.entitys.SchoolTest;
import application.sephirmobile.sephirinterface.entitys.Semesters;
import application.sephirmobile.sephirinterface.forms.SemesterChangeForm;
import application.sephirmobile.sephirinterface.getters.AnnouncedTestGetter;
import application.sephirmobile.sephirinterface.getters.AverageSubjectMarkGetter;
import application.sephirmobile.sephirinterface.getters.AverageTestMarkGetter;
import application.sephirmobile.sephirinterface.getters.SchoolClassGetter;
import application.sephirmobile.sephirinterface.getters.SchoolTestGetter;
import application.sephirmobile.sephirinterface.getters.SemesterGetter;
import application.sephirmobile.sephirinterface.getters.TestChartGetter;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class SephirInterfaceTest {

    @Test
    public void sephirInterfaceTest() throws Exception {
        //Does not test testchartgetter

        //Login
        SephirInterface sephirInterface = new SephirInterface();
        sephirInterface.login(new Login(StringFromResourceReader.read("ignore/email.txt"), StringFromResourceReader.read("ignore/password.txt")));


        //Get SchoolClasses
        SchoolClassGetter schoolClassGetter = new SchoolClassGetter(sephirInterface);
        List<SchoolClass> schoolClasses = schoolClassGetter.get();
        System.out.println(schoolClasses);

        //Get Tests
        SchoolTestGetter testGetter = new SchoolTestGetter(sephirInterface);
        for (SchoolClass schoolClass : schoolClasses) {
            List<SchoolTest> tests = testGetter.get(schoolClass);
            SchoolTest test = tests.get(0);
            test.getAverageMark(sephirInterface);
            System.out.println(tests);
        }


        //Get Announced Tests
        AnnouncedTestGetter announcedTestGetter = new AnnouncedTestGetter(sephirInterface);
        List<AnnouncedTest> announcedTests = announcedTestGetter.get();
        System.out.println(announcedTests);


        //Get average Mark (or use method on SchoolTest
        AverageTestMarkGetter averageTestMarkGetter = new AverageTestMarkGetter(sephirInterface);
        double averageMark = averageTestMarkGetter.get(new SchoolTest(null, null, null, null, null, 0, 0, "216054"));
        System.out.println(averageMark);


        //Get and Set Semester
        SemesterGetter semesterGetter = new SemesterGetter(sephirInterface);
        Semesters semesters = semesterGetter.get();
        System.out.println(semesters);
        sephirInterface.changeSemester(semesters.getSemesters().get(10));
        //OR
        new SemesterChangeForm(sephirInterface).changeSemester(semesters.getSemesters().get(10));


        AverageSubjectMarkGetter averageSubjectMarkGetter = new AverageSubjectMarkGetter(sephirInterface);
        for (SchoolClass schoolClass : schoolClasses) {
            AverageSubjectMarks averageSubjectMarks = averageSubjectMarkGetter.get(schoolClass);
            System.out.println(averageSubjectMarks);
        }
    }

    @Test
    @Ignore("Does real Requests to Sephir")
    public void wrongLoginData_login_loginException() throws Exception {
        SephirInterface sephirInterface = new SephirInterface();
        boolean successful = sephirInterface.login(new Login("a", "a"));
        assertThat(successful, is(false));
    }
}

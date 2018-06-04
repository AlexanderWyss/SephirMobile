package application.sephirmobile.sephirinterface;

import org.junit.Ignore;
import org.junit.Test;

import java.io.IOException;
import java.util.List;

import application.sephirmobile.sephirinterface.entitys.Login;
import application.sephirmobile.sephirinterface.entitys.SchoolClass;
import application.sephirmobile.sephirinterface.entitys.SchoolTest;
import application.sephirmobile.sephirinterface.exceptions.LoginException;
import application.sephirmobile.sephirinterface.getters.SchoolClassGetter;
import application.sephirmobile.sephirinterface.getters.SchoolTestGetter;

public class SephirInterfaceTest {
    @Test
    @Ignore("Does real Requests to Sephir")
    public void sephirInterfaceTest() throws Exception {
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
            tests.get(0).getAverageMark(sephirInterface);
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

/*
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
*/
    }

    @Test(expected = LoginException.class)
    @Ignore("Does real Requests to Sephir")
    public void wrongLoginData_login_loginException() throws Exception {
        SephirInterface sephirInterface = new SephirInterface();
        sephirInterface.login(new Login("a", "a"));
    }
}

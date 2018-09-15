package application.sephirmobile.fragments;

import java.util.ArrayList;
import java.util.List;

import application.sephirmobile.table.adapters.SchoolTestAdapter;
import application.sephirmobile.table.adapters.TableAdapter;
import application.sephirmobile.sephirinterface.SephirInterface;
import application.sephirmobile.sephirinterface.entitys.SchoolClass;
import application.sephirmobile.sephirinterface.entitys.SchoolTest;
import application.sephirmobile.sephirinterface.getters.SchoolClassGetter;
import application.sephirmobile.sephirinterface.getters.SchoolTestGetter;

public class MarksFragment extends TableFragment {

    @Override
    protected TableAdapter<?> createAdapter() {
        List<SchoolTest> tests = new ArrayList<>();
        SephirInterface sephirInterface = SephirInterface.getSephirInterface();
        try {
            List<SchoolClass> schoolClasses = new SchoolClassGetter(sephirInterface).get();
            SchoolTestGetter testGetter = new SchoolTestGetter(sephirInterface);
            for (SchoolClass schoolClass : schoolClasses) {
                tests.addAll(testGetter.getPastTests(schoolClass));
            }
        } catch (Exception e) {
            e.printStackTrace();
            loginActivity();
        } finally {
            return new SchoolTestAdapter(getContext(), tests, sephirInterface);
        }
    }
}

package application.sephirmobile.fragments;

import java.util.ArrayList;
import java.util.List;

import application.sephirmobile.table.adapters.AnnouncedTestAdapter;
import application.sephirmobile.table.adapters.TableAdapter;
import application.sephirmobile.sephirinterface.SephirInterface;
import application.sephirmobile.sephirinterface.entitys.AnnouncedTest;
import application.sephirmobile.sephirinterface.getters.AnnouncedTestGetter;

public class AnnouncedTestFragment extends TableFragment {
    @Override
    protected TableAdapter<?> createAdapter() {
        List<AnnouncedTest> tests = new ArrayList<>();
        try {
            AnnouncedTestGetter testGetter = new AnnouncedTestGetter(SephirInterface.getSephirInterface());
            tests.addAll(testGetter.get());
        } catch (Exception e) {
            //TODO handle exception
            e.printStackTrace();
            loginActivity();
        } finally {
            return new AnnouncedTestAdapter(getContext(), tests);
        }
    }
}

package application.sephirmobile.fragments;

import android.view.View;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

import application.sephirmobile.R;
import application.sephirmobile.sephirinterface.SephirInterface;
import application.sephirmobile.sephirinterface.entitys.AverageSubjectMarks;
import application.sephirmobile.sephirinterface.entitys.SchoolClass;
import application.sephirmobile.sephirinterface.getters.AverageSubjectMarkGetter;
import application.sephirmobile.sephirinterface.getters.SchoolClassGetter;
import application.sephirmobile.table.adapters.AverageSubjectMarksAdapter;
import application.sephirmobile.table.adapters.TableAdapter;

public class AverageMarksFragment extends TableFragment {
    @Override
    protected TableAdapter<?> createAdapter() {
        List<AverageSubjectMarks> marks = new ArrayList<>();
        try {
            SephirInterface sephirInterface = SephirInterface.getSephirInterface();
            AverageSubjectMarkGetter getter = new AverageSubjectMarkGetter(sephirInterface);
            SchoolClassGetter schoolClassGetter = new SchoolClassGetter(sephirInterface);
            List<SchoolClass> schoolClasses = schoolClassGetter.get();
            for (SchoolClass schoolClass : schoolClasses) {
                marks.add(getter.get(schoolClass));
            }
        } catch (Exception e) {
            e.printStackTrace();
            loginActivity();
        } finally {
            return new AverageSubjectMarksAdapter(getContext(), marks);
        }
    }

    @Override
    protected void onGuiOpened(LinearLayout tableView) {
        tableView.findViewById(R.id.divider).setVisibility(View.GONE);
    }
}

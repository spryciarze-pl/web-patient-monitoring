package se.views.appointment.dialogs;

import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import org.springframework.beans.factory.annotation.Autowired;
import se.db.model.User;
import se.db.service.DbService;

public class MeetingSchedulerDialog extends Dialog {

    @Autowired
    DbService dbService;

    private VerticalLayout mainLayout = new VerticalLayout();

    public MeetingSchedulerDialog(DbService dbService, User currentUser) {

    }

    private void createViewForDoctor() {

    }

    private void createViewForPatient() {

    }

}

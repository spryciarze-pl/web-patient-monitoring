package se.views.panel.dialogs;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import org.springframework.beans.factory.annotation.Autowired;
import se.db.model.PatientsActivity;
import se.db.model.User;
import se.db.service.DbService;

public class ActivityCompletedDialog extends Dialog {

    @Autowired
    DbService dbService;

    public ActivityCompletedDialog(DbService dbService, int activityId) {
        this.dbService = dbService;

        PatientsActivity activity = dbService.getPatientActivityByDoctorsActivityId(activityId);
        User patient = dbService.getUserById(activity.getPatientId());

        H2 header = new H2("Information");

        TextField patientName = new TextField("Patient Name");
        patientName.setValue(patient.getFullName());
        patientName.setReadOnly(true);

        TextField requestType = new TextField("Type");
        requestType.setValue(activity.getType());
        requestType.setReadOnly(true);

        TextField result = new TextField("Result");
        result.setValue(activity.getResult());
        result.setReadOnly(true);

        Button closeButton = new Button("Close", event -> close());

        VerticalLayout mainLayout = new VerticalLayout(header, patientName, requestType, result, closeButton);
        mainLayout.setAlignItems(FlexComponent.Alignment.CENTER);

        add(mainLayout);

    }

}

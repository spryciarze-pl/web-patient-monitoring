package se.views.panel.dialogs;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import org.springframework.beans.factory.annotation.Autowired;
import se.db.model.DoctorsActivity;
import se.db.model.PatientsActivity;
import se.db.service.DbService;
import se.dto.DoctorsActivityDto;

import java.time.LocalDateTime;

public class ActivityActionDialog extends Dialog {

    @Autowired
    DbService dbService;
    private VerticalLayout mainLayout = new VerticalLayout();
    private HorizontalLayout elementLayout;
    public ActivityActionDialog(DbService dbService, DoctorsActivityDto activityDto) {
        this.dbService = dbService;

        DoctorsActivity doctorsActivity = dbService.getActivityById(activityDto.getActivityId());

        switch (doctorsActivity.getType()) {
            case "Take temperature":
                createDefaultModal(doctorsActivity, "C", "");
                break;
            case "Measure Pressure":
                createDefaultModal(doctorsActivity, "mmHg", "");
                break;
            case "Measure Sugar Level":
                createDefaultModal(doctorsActivity, "mg/dL", "");
                break;
            case "Measure oxygen-blood saturation":
                createDefaultModal(doctorsActivity, "%", "");
                break;
            case "Issue Prescription":
                break;
            case "Take medicine":
                break;
        }
    }

    private void createDefaultModal(DoctorsActivity activity, String unit, String helperText) {

        H2 title = new H2("Title");

        TextField textField = new TextField();
        textField.setLabel(activity.getType());
        textField.setSuffixComponent(new Span(unit));
        textField.setHelperText(helperText);
        textField.setAllowedCharPattern("[0-9.]");
        textField.setWidth("50%");

        Button button = new Button("Submit");
        button.addClickListener(event -> {
            PatientsActivity patientsActivity = new PatientsActivity();

            patientsActivity.setPatientId(activity.getPatientId());
            patientsActivity.setTime(LocalDateTime.now());
            patientsActivity.setDoctorsRequestId(activity.getId());
            patientsActivity.setResult(textField.getValue());
            patientsActivity.setType(activity.getType());

            close();

        });
        button.setWidth("50%");

        elementLayout = new HorizontalLayout(textField, button);
        mainLayout.add(title, elementLayout);
        mainLayout.setAlignItems(FlexComponent.Alignment.CENTER);
        add(mainLayout);
        setWidth("60%");
        setHeight("60%");
    }

    private void createPrescriptionModal() {

    }

    private void createConfirmMedicineModal() {

    }

}

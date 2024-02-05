package se.views.panel.dialogs;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import org.springframework.beans.factory.annotation.Autowired;
import se.db.model.DoctorsActivity;
import se.db.model.PatientsActivity;
import se.db.service.DbService;
import se.dto.DoctorsActivityDto;

import javax.print.Doc;
import java.time.LocalDateTime;

public class ActivityActionDialog extends Dialog {

    @Autowired
    DbService dbService;
    Grid<DoctorsActivityDto> activityGrid;
    private VerticalLayout mainLayout = new VerticalLayout();
    private HorizontalLayout elementLayout;

    public ActivityActionDialog(DbService dbService, DoctorsActivityDto activityDto, Grid<DoctorsActivityDto> activityGrid) {
        this.dbService = dbService;
        this.activityGrid = activityGrid;

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
                createPrescriptionModal();
                break;
            case "Take medicine":
                createConfirmMedicineModal(doctorsActivity);
                break;
            case "Request from doctor":
                createMiscellaneousModal(doctorsActivity);
                break;
        }
        prepareLayout();
    }

    private void prepareLayout() {
        mainLayout.setAlignItems(FlexComponent.Alignment.CENTER);
        add(mainLayout);
    }

    private void createDefaultModal(DoctorsActivity activity, String unit, String helperText) {

        H2 title = new H2("Activity");

        TextField textField = new TextField();
        textField.setLabel(activity.getType());
        textField.setSuffixComponent(new Span(unit));
        textField.setHelperText(helperText);
        textField.setAllowedCharPattern("[0-9.]");
        textField.setWidth("50%");

        TextField descriptionTextFiled = new TextField();
        descriptionTextFiled.setValue(activity.getDescription());
        descriptionTextFiled.setLabel("Doctors description");
        descriptionTextFiled.setReadOnly(true);
        descriptionTextFiled.setWidthFull();

        Button button = new Button("Submit");
        button.addClickListener(event -> {
            PatientsActivity patientsActivity = new PatientsActivity();

            patientsActivity.setPatientId(activity.getPatientId());
            patientsActivity.setTime(LocalDateTime.now());
            patientsActivity.setDoctorsRequestId(activity.getId());
            patientsActivity.setResult(textField.getValue());
            patientsActivity.setType(activity.getType());

            dbService.saveNewPatientActivity(patientsActivity);
            dbService.setDoctorsActivityAsComplete(patientsActivity.getDoctorsRequestId());

            activityGrid.setItems(dbService.getActivityDtoByPatientId(activity.getPatientId()));

            close();
            Notification.show("Reply saved");
        });
        button.setWidth("50%");
        button.getStyle().set("margin-top", "35px");

        elementLayout = new HorizontalLayout(textField, button);
        mainLayout.add(title);
        if (!activity.getDescription().isEmpty()) {
            mainLayout.add(descriptionTextFiled);
        }
        mainLayout.add(elementLayout);
    }

    private void createPrescriptionModal() {

    }

    private void createConfirmMedicineModal(DoctorsActivity activity) {

        VerticalLayout elementLayout = new VerticalLayout();
        elementLayout.setWidthFull();
        elementLayout.setHeight("0px");

        H2 title = new H2("Take medicine: " + activity.getDescription());

        TextField textField = new TextField();
        textField.setLabel("Reason for not taking the medicine:");
        textField.setWidthFull();

        Checkbox checkbox = new Checkbox("Could not take the medicine:");

        checkbox.addValueChangeListener(event -> {
            if (event.getValue()) {
                elementLayout.add(textField);
                elementLayout.setHeightFull();
            } else {
                elementLayout.remove(textField);
                elementLayout.setHeight("0px");
            }
        });

        Button button = new Button("Submit");
        button.addClickListener(event -> {
            PatientsActivity patientsActivity = new PatientsActivity();

            patientsActivity.setPatientId(activity.getPatientId());
            patientsActivity.setTime(LocalDateTime.now());
            patientsActivity.setDoctorsRequestId(activity.getId());
            patientsActivity.setResult(checkbox.getValue() ? "FALSE: " + textField.getValue() :
                    "Medicine taken");
            patientsActivity.setType(activity.getType());

            dbService.saveNewPatientActivity(patientsActivity);
            dbService.setDoctorsActivityAsComplete(patientsActivity.getDoctorsRequestId());

            activityGrid.setItems(dbService.getActivityDtoByPatientId(activity.getPatientId()));

            close();
            Notification.show("Reply saved");
        });

        button.setWidth("50%");

        mainLayout.add(title, checkbox, elementLayout, button);
    }

    private void createMiscellaneousModal(DoctorsActivity activity) {
        H2 title = new H2("Activity Description");
        title.setHeight("10%");

        TextArea replyArea = new TextArea();
        replyArea.setWidthFull();
        replyArea.setHeightFull();

        TextArea descriptionArea = new TextArea();
        descriptionArea.setWidthFull();
        descriptionArea.setHeightFull();
        descriptionArea.setReadOnly(true);

        Button button = new Button("Submit descpription", event -> {

            PatientsActivity patientsActivity = new PatientsActivity();

            patientsActivity.setPatientId(activity.getPatientId());
            patientsActivity.setTime(LocalDateTime.now());
            patientsActivity.setDoctorsRequestId(activity.getId());
            patientsActivity.setResult(replyArea.getValue());
            patientsActivity.setType(activity.getType());

            dbService.saveNewPatientActivity(patientsActivity);
            dbService.setDoctorsActivityAsComplete(patientsActivity.getDoctorsRequestId());

            activityGrid.setItems(dbService.getActivityDtoByPatientId(activity.getPatientId()));
            close();
            Notification.show("Reply saved");

        });
        button.setHeight("10%");

        HorizontalLayout descriptionAreaLayout = new HorizontalLayout(descriptionArea);
        descriptionAreaLayout.setWidthFull();
        descriptionAreaLayout.setHeight("40%");

        HorizontalLayout replyAreaLayout = new HorizontalLayout(replyArea);
        replyAreaLayout.setWidthFull();
        replyAreaLayout.setHeight("40%");

        VerticalLayout mainLayout = new VerticalLayout(title, descriptionAreaLayout, replyAreaLayout, button);
        mainLayout.setAlignItems(FlexComponent.Alignment.CENTER);
        mainLayout.setHeightFull();
        mainLayout.setWidthFull();

        add(mainLayout);
        setWidth("60%");
        setHeight("60%");

    }
}

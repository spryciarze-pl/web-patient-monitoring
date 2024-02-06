package se.views.appointment.dialogs;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import se.db.model.Appointment;
import se.db.model.User;
import se.db.service.DbService;

public class RejectAppointmentDialog extends Dialog {

    public RejectAppointmentDialog(Appointment appointment, DbService dbService, User currentUser, Grid<Appointment> gridToUpdate) {

        H2 title = new H2("Reject Appointment");
        title.setHeight("10%");

        TextArea textArea = new TextArea();
        textArea.setWidthFull();
        textArea.setHeightFull();

        Button button = new Button("Submit rejection reason", event -> {
            dbService.setAppointmentInvalidByIdAndAddReason(appointment.getId(), textArea.getValue());
            if(currentUser.getRoleId() == 2) {
                gridToUpdate.setItems(dbService.getAppointmentsByDoctorIdAndDrive(currentUser.getId(), false));
            } else {
                gridToUpdate.setItems(dbService.getAppointmentsByPatientIdAndDrive(currentUser.getId(), true));
            }
            close();
            Notification.show("Appointment rejected");
        });
        button.setHeight("10%");

        HorizontalLayout textAreaLayout = new HorizontalLayout(textArea);
        textAreaLayout.setWidthFull();
        textAreaLayout.setHeight("80%");

        VerticalLayout mainLayout = new VerticalLayout(title, textAreaLayout, button);
        mainLayout.setAlignItems(FlexComponent.Alignment.CENTER);
        mainLayout.setHeightFull();
        mainLayout.setWidthFull();

        add(mainLayout);
        setWidth("60%");
        setHeight("60%");
    }
}

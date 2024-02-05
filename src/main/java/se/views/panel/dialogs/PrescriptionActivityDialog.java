package se.views.panel.dialogs;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import se.db.model.Prescription;
import se.db.service.DbService;

import java.util.Random;

public class PrescriptionActivityDialog extends Dialog {

    public PrescriptionActivityDialog(DbService dbService, Prescription prescription) {

        H2 title = new H2("Issue prescription");

        Random rand = new Random();
        String id = String.format("%04d", rand.nextInt(10000));
        TextField textField = new TextField("Medicine name");
        textField.setWidth("70%");

        Button button = new Button("Submit", event -> {
            if(textField.getValue() != null) {
                prescription.setMedicine(textField.getValue() + " " + id);
                dbService.saveNewPrescription(prescription);
                close();
                Notification.show("Added new prescription").open();
            } else {
                Notification.show("Medicine name is empty").open();
            }
        });
        button.getStyle().set("margin-top", "35px");
        button.setWidth("30%");

        HorizontalLayout elementLayout = new HorizontalLayout(textField, button);
        elementLayout.setWidthFull();

        VerticalLayout mainLayout = new VerticalLayout(title, elementLayout);
        mainLayout.setAlignItems(FlexComponent.Alignment.CENTER);
        mainLayout.setWidthFull();
        mainLayout.setHeightFull();
        add(mainLayout);

        setWidth("60%");
        setHeight("30%");

    }


}

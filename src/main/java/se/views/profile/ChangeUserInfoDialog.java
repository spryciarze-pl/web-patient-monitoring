package se.views.profile;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import org.springframework.beans.factory.annotation.Autowired;
import se.db.model.Clinic;
import se.db.model.Specialization;
import se.db.model.User;
import se.db.service.DbService;
import se.enums.RoleEnum;

public class ChangeUserInfoDialog extends Dialog {
    @Autowired
    DbService dbService;
    private TextField nameField = new TextField("Name");
    private TextField surnameField = new TextField("Surname");
    private TextField peselField = new TextField("PESEL");
    private PasswordField passwordField = new PasswordField("New password");
    private PasswordField repeatPasswordField = new PasswordField("Repeat new password");
    private EmailField emailField = new EmailField("E-mail address");
    private TextField phoneField = new TextField("Phone number");
    private ComboBox<Clinic> clinicComboBox = new ComboBox<>("Clinic");
    private ComboBox<Specialization> specializationComboBox = new ComboBox<>("Specialization");

    public ChangeUserInfoDialog(DbService dbService, User currentUser){
        this.dbService = dbService;
        VerticalLayout dialogLayout = new VerticalLayout(this.nameField, this.surnameField, this.peselField, this.passwordField,
                                                    this.repeatPasswordField, this.emailField, this.phoneField, this.clinicComboBox);
        clinicComboBox.setItemLabelGenerator(Clinic::getName);
        clinicComboBox.setItems(dbService.getAllClinics());
        specializationComboBox.setItemLabelGenerator(Specialization::getName);
        specializationComboBox.setItems(dbService.getAllSpecializations());
        specializationComboBox.setWidthFull();
        if(currentUser.getRoleId().equals(RoleEnum.DOCTOR.getRoleDbVal())) {
            dialogLayout.add(this.specializationComboBox);
        }
        dialogLayout.setPadding(false);
        dialogLayout.setSpacing(false);
        dialogLayout.setAlignItems(FlexComponent.Alignment.STRETCH);
        dialogLayout.getStyle().set("width", "18rem").set("max-width", "100%");
        this.setHeaderTitle("Change your data");
        this.add(dialogLayout);
        Button saveButton = new Button("Save changes", e -> {
            if(!this.nameField.isEmpty())
                currentUser.setName(this.nameField.getValue());
            if(!this.surnameField.isEmpty())
                currentUser.setSurname(this.surnameField.getValue());
            if(!this.peselField.isEmpty())
                currentUser.setPin(this.peselField.getValue());
            if(!this.emailField.isEmpty())
                currentUser.setMail(this.emailField.getValue());
            if(!this.phoneField.isEmpty())
                currentUser.setPhone(this.phoneField.getValue());
            if(!this.clinicComboBox.isEmpty())
                currentUser.setClinic(dbService.getClinicById(this.clinicComboBox.getValue().getId()));
            if(!this.specializationComboBox.isEmpty())
                currentUser.setSpecialization(dbService.getSpecializationById(this.specializationComboBox.getValue().getId()));

            if(!this.passwordField.isEmpty()){
                if(!this.repeatPasswordField.isEmpty() && this.repeatPasswordField.getValue().equals(this.passwordField.getValue())) {
                    dbService.changePassword(currentUser.getPasswordId(), this.passwordField.getValue());
                    dbService.saveNewUser(currentUser);
                    close();
                    UI.getCurrent().getPage().reload();
                }
                else{
                    Notification notification = Notification.show("Password must be the same as repeated password");
                    notification.setPosition(Notification.Position.MIDDLE);
                    notification.setDuration(1000);
                }
            }
            else {
                dbService.saveNewUser(currentUser);
                close();
                UI.getCurrent().getPage().reload();
            }
        });
        Button cancelButton = new Button("Cancel", e -> close());
        this.getFooter().add(cancelButton);
        this.getFooter().add(saveButton);
    }
}

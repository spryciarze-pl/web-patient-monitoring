package se.views.profile;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import org.springframework.beans.factory.annotation.Autowired;
import se.db.model.Password;
import se.db.model.User;
//import se.db.service.DbService;

public class ChangeUserInfoDialog extends Dialog {
    //@Autowired
    //DbService dbService;
    private TextField nameField = new TextField("Name");
    private TextField surnameField = new TextField("Surname");
    private TextField peselField = new TextField("PESEL");
    private PasswordField passwordField = new PasswordField("New password");
    private PasswordField repeatPasswordField = new PasswordField("Repeat new password");
    private EmailField emailField = new EmailField("E-mail address");
    private TextField phoneField = new TextField("Phone number");

    public ChangeUserInfoDialog(User currentUser){
        VerticalLayout dialogLayout = new VerticalLayout(this.nameField, this.surnameField, this.peselField, this.passwordField, this.repeatPasswordField, this.emailField, this.phoneField);
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
            if(!this.passwordField.isEmpty()){
                if(!this.repeatPasswordField.isEmpty() && this.repeatPasswordField.getValue().equals(this.passwordField.getValue())) {
                    Password newPassword = new Password(this.passwordField.getValue());
                    //currentUser.setPasswordId(dbService.savePasswordAndGetNewId(newPassword));
                }
                else{
                    //todo message informing that the repeated password needs to be the same as the password
                }
            }
            if(!this.emailField.isEmpty())
                currentUser.setMail(this.emailField.getValue());
            if(!this.phoneField.isEmpty())
                currentUser.setPhone(this.phoneField.getValue());

            //todo dbService.changeUserInfo(currentUser);
            close();
            UI.getCurrent().getPage().reload();
        });
        Button cancelButton = new Button("Cancel", e -> close());
        this.getFooter().add(cancelButton);
        this.getFooter().add(saveButton);
    }
}

package se.views.profile;

import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.PermitAll;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.parameters.P;
import se.db.repository.UserRepository;
import se.secuirty.SecurityService;
import se.views.MainLayout;

import java.awt.*;

@PermitAll
@PageTitle("Profile")
@Route(value = "profile", layout = MainLayout.class)
public class ProfileView extends VerticalLayout {

    @Autowired
    UserRepository userRepository;
    public ProfileView(SecurityService securityService) {
        setWidth("min-content");
        H3 h3 = new H3("Name: " + securityService.getAuthenticatedUser().getUser().getName());
        H3 h32 = new H3("Surname: " + securityService.getAuthenticatedUser().getUser().getSurname());
        H3 h33 = new H3("PESEL: " + securityService.getAuthenticatedUser().getUser().getPin());
        H3 h34 = new H3("Password: ");
        H3 h35 = new H3("E-mail address: " + securityService.getAuthenticatedUser().getUser().getMail());
        H3 h36 = new H3("Phone number: " + securityService.getAuthenticatedUser().getUser().getPhone());
        Button button = new Button("Change");
        Dialog dialog = new Dialog();
        dialog.setHeaderTitle("Change your data");

        VerticalLayout dialogLayout = createDialogLayout();
        dialog.add(dialogLayout);

        Button saveButton = createSaveButton(dialog);
        Button cancelButton = new Button("Cancel", e -> dialog.close());
        dialog.getFooter().add(cancelButton);
        dialog.getFooter().add(saveButton);

        add(dialog);
        button.addClickListener(e -> dialog.open());

        setAlignSelf(FlexComponent.Alignment.CENTER, button);
        add(h3, h32, h33, h34, h35, h36);
        add(button);
    }
    private static VerticalLayout createDialogLayout() {

        TextField nameField = new TextField("Name");
        TextField surnameField = new TextField("Surname");
        TextField peselField = new TextField("PESEL");
        PasswordField passwordField = new PasswordField("New password");
        PasswordField repeatPasswordField = new PasswordField("Repeat new password");
        EmailField emailField = new EmailField("E-mail address");
        TextField phoneField = new TextField("Phone number");

        VerticalLayout dialogLayout = new VerticalLayout(nameField, surnameField, peselField, passwordField, repeatPasswordField, emailField, phoneField);
        dialogLayout.setPadding(false);
        dialogLayout.setSpacing(false);
        dialogLayout.setAlignItems(FlexComponent.Alignment.STRETCH);
        dialogLayout.getStyle().set("width", "18rem").set("max-width", "100%");

        return dialogLayout;
    }

    private static Button createSaveButton(Dialog dialog) {
        Button saveButton = new Button("Save changes", e -> dialog.close());

        return saveButton;
    }
}
package se.views.profile;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.PermitAll;
import se.db.model.User;
import se.security.SecurityService;
import se.views.MainLayout;

@PermitAll
@PageTitle("Profile")
@Route(value = "profile", layout = MainLayout.class)
public class ProfileView extends VerticalLayout {

    public ProfileView(SecurityService securityService) {

        User curentUser = securityService.getAuthenticatedUser().getUser();

        setWidth("min-content");
        H3 h3 = new H3("Name: " + curentUser.getName());
        H3 h32 = new H3("Surname: " + curentUser.getSurname());
        H3 h33 = new H3("PESEL: " + curentUser.getPin());
        H3 h34 = new H3("Password: " + curentUser.getPasswordId());
        H3 h35 = new H3("E-mail address: " + curentUser.getMail());
        H3 h36 = new H3("Phone number: " + curentUser.getPhone());
        Button button = new Button("Change");
        button.addClickListener(event -> System.out.println("TEST"));
        setAlignSelf(FlexComponent.Alignment.CENTER, button);
        add(h3, h32, h33, h34, h35, h36);
        add(button);
    }
}
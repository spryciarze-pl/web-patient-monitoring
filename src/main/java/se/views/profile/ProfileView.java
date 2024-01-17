package se.views.profile;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.PermitAll;
import se.secuirty.SecurityService;
import se.views.MainLayout;
@PermitAll
@PageTitle("Profile")
@Route(value = "profile", layout = MainLayout.class)
public class ProfileView extends VerticalLayout {
    public ProfileView(SecurityService securityService) {
        setWidth("min-content");
        H3 h3 = new H3("Name: ");
        H3 h32 = new H3("Surname: ");
        H3 h33 = new H3("PESEL: ");
        H3 h34 = new H3("Password: ");
        H3 h35 = new H3("E-mail address: " + securityService.getAuthenticatedUser().getUsername());
        H3 h36 = new H3("Phone number: ");
        Button button = new Button("Change");
        button.addClickListener(event -> System.out.println("TEST"));
        setAlignSelf(FlexComponent.Alignment.CENTER, button);
        add(h3, h32, h33, h34, h35, h36);
        add(button);
    }
}
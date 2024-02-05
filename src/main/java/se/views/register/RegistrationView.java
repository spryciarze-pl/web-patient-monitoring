package se.views.register;

import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import se.db.service.DbService;

@Route("register")
@AnonymousAllowed
@Component
public class RegistrationView extends VerticalLayout {

    @Autowired
    DbService dbService;
    private H1 title = new H1("MEDIO");
    private RegistrationForm registrationForm;

    public RegistrationView(DbService dbService) {

        title.getStyle().set("margin-top", "3%");
        title.getStyle().set("margin-bottom", "1%");

        setAlignItems(Alignment.CENTER);
        registrationForm = new RegistrationForm(dbService);
        add(title, registrationForm);
    }
}

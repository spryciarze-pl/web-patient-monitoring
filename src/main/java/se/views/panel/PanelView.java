package se.views.panel;


import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.PermitAll;
import org.springframework.beans.factory.annotation.Autowired;
import se.db.model.Specialization;
import se.db.repository.SpecializationRepository;
import se.secuirty.SecurityService;
import se.views.MainLayout;

import java.time.LocalDate;
import java.util.List;

@PermitAll
@PageTitle("Hello World")
@Route(value = "", layout = MainLayout.class)
public class PanelView extends VerticalLayout {

    private VerticalLayout leftSideLayout;
    private VerticalLayout rightSideLayout;

    @Autowired
    private SpecializationRepository specializationRepository;
    @Autowired
    private SecurityService securityService;

    public PanelView(SecurityService securityService) {

        this.securityService = securityService;
        H2 header = new H2("Hello " + securityService.getAuthenticatedUser().getUsername());
        header.addClassName("H1");
        H3 dateField = new H3 (LocalDate.now().toString());
        dateField.addClassName("H1");
        add(header, dateField);

        Button button = new Button("Test");
        button.addClickListener(event -> {
            List<Specialization> list = specializationRepository.findAll();

            System.out.println("Test");
        });

        add(button);



    }
}

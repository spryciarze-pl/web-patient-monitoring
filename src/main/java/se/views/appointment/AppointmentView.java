package se.views.appointment;

import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.PermitAll;
import se.views.MainLayout;

@PageTitle("Appointment")
@Route(value = "appointment", layout = MainLayout.class)
@PermitAll
public class AppointmentView extends VerticalLayout {
    public AppointmentView() {

    }

}

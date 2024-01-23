package se.views.appointments;

import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.dependency.Uses;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.RolesAllowed;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.core.Local;
import org.springframework.stereotype.Service;
import se.db.model.Appointment;
import se.db.model.User;
import se.db.repository.AppointmentRepository;
import se.secuirty.SecurityService;
import se.views.MainLayout;

@PageTitle("Appointments")
@Route(value = "appointments", layout = MainLayout.class)
@RolesAllowed("USER")
@Uses(Icon.class)
public class AppointmentsView extends Composite<VerticalLayout> {

    @Autowired
    AppointmentRepository appointmentRepository;

    @Autowired
    SecurityService securityService;

    public AppointmentsView(AppointmentRepository appointmentRepository, SecurityService securityService) {
        this.appointmentRepository = appointmentRepository;
        this.securityService = securityService;
        Grid basicGrid = new Grid( /* temporary */ AppointmentData.class);
        getContent().setWidth("100%");
        getContent().getStyle().set("flex-grow", "1");
        basicGrid.setWidth("100%");
        basicGrid.getStyle().set("flex-grow", "0");
        SetGridSampleData(basicGrid);
        getContent().add(basicGrid);

        /*temporary*/
        Integer id = securityService.getAuthenticatedUser().getUser().getId();
        List<Appointment> list = appointmentRepository.findByDoctorId(72);
        Integer test = 12;
    }

    /* temporary */
    private void SetGridSampleData(Grid grid) {
        AppointmentData neurologist = new AppointmentData("John", "Doe", "Neurologist", LocalDateTime.of(2024, 5, 23, 15,30));
        AppointmentData cardiologist = new AppointmentData("Jan", "Kowalski", "Cardiologist", LocalDateTime.of(2025, 1, 10, 18,0));
        AppointmentData ripperdoc = new AppointmentData("Viktor", "Vektor", "Ripperdoc", LocalDateTime.of(2077, 11, 30, 20,50));

        grid.setItems(neurologist, cardiologist, ripperdoc);
    }
}


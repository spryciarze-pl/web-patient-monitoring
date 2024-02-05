package se.views.prescription;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.PermitAll;
import org.springframework.beans.factory.annotation.Autowired;
import se.db.model.Prescription;
import se.db.model.User;
import se.db.service.DbService;
import se.security.SecurityService;
import se.utils.MedioUtils;
import se.views.MainLayout;

@PermitAll
@PageTitle("Prescriptions")
@Route(value = "prescription", layout = MainLayout.class)
public class PrescriptionView extends VerticalLayout {

    @Autowired
    SecurityService securityService;
    @Autowired
    DbService dbService;
    private User currentUser;
    private HorizontalLayout mainContainerLayout;

    public PrescriptionView(SecurityService securityService, DbService dbService) {
        this.securityService = securityService;
        this.dbService = dbService;
        this.currentUser = securityService.getAuthenticatedUser().getUser();

        prepareView();

        switch (currentUser.getRoleId()) {
            case 1:
                createAdminView();
                break;
            case 2:
                createDoctorView();
                break;
            case 3:
                createPatientView();
                break;
        }

        add(mainContainerLayout);

    }

    private void prepareView() {
        mainContainerLayout = new HorizontalLayout();
        mainContainerLayout.setWidthFull();
        mainContainerLayout.setHeightFull();
    }

    private void createAdminView() {

    }

    private void createDoctorView() {
        Grid<Prescription> grid =  new Grid<>(Prescription.class, false);
        grid.addColumn(column -> dbService.getUserById(column.getPatientId()).getFullName()).setHeader("Patient");
        grid.addColumn(Prescription::getMedicine).setHeader("Medicine");
        grid.addColumn(column -> MedioUtils.formatDateTimeWithFullMonthName(column.getPrescriptionTime())).setHeader("Date and time");
        grid.addComponentColumn(column -> {
            Button button = new Button("Remove prescription", event -> {
                dbService.removePrescriptionById(column.getId());
                Notification.show("Removed prescription");
                grid.setItems(dbService.getPrescriptionByDoctorId(currentUser.getId()));
            });
            return button;
        }).setHeader("Action");

        grid.setItems(dbService.getPrescriptionByDoctorId(currentUser.getId()));

        mainContainerLayout.add(grid);
    }

    private void createPatientView() {
        Grid<Prescription> grid =  new Grid<>(Prescription.class, false);
        grid.addColumn(Prescription::getMedicine).setHeader("Medicine");
        grid.addColumn(column -> dbService.getUserById(column.getDoctorId()).getFullName()).setHeader("Doctor");
        grid.addColumn(column -> MedioUtils.formatDateTimeWithFullMonthName(column.getPrescriptionTime())).setHeader("Date and time");

        grid.setItems(dbService.getPrescriptionByPatientId(currentUser.getId()));

        mainContainerLayout.add(grid);
    }

}

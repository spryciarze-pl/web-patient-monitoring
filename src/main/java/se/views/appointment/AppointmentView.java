package se.views.appointment;


import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.PermitAll;
import org.springframework.beans.factory.annotation.Autowired;
import se.db.model.Appointment;
import se.db.model.User;
import se.db.service.DbService;
import se.security.SecurityService;
import se.utils.MedioUtils;
import se.views.MainLayout;
import se.views.appointment.dialogs.MeetingSchedulerDialog;
import se.views.appointment.dialogs.RejectAppointmentDialog;
import se.views.appointment.dialogs.ShowAppointmentStringDialog;

import java.util.List;
import se.views.MainLayout;

@PageTitle("Appointment")
@Route(value = "appointment", layout = MainLayout.class)
@PermitAll
public class AppointmentView extends VerticalLayout {
  
    @Autowired
    DbService dbService;

    @Autowired
    SecurityService securityService;

    private User currentUser;

    VerticalLayout mainLayout = new VerticalLayout();

    public AppointmentView(DbService dbService, SecurityService securityService) {

        this.dbService = dbService;
        this.securityService = securityService;

        this.currentUser = securityService.getAuthenticatedUser().getUser();

        createView();

        add(mainLayout);

    }

    private void createView() {

        List<Appointment> inData = getInData();
        List<Appointment> outData = getOutData();

        Grid<Appointment> inAppointment = new Grid<>(Appointment.class, false);
        Grid<Appointment> outAppointment = new Grid<>(Appointment.class, false);

        if (currentUser.getRoleId() == 2) {
            inAppointment.addColumn(column -> dbService.getUserById(column.getPatientId()).getFullName()).setHeader("Patient");
            outAppointment.addColumn(column -> dbService.getUserById(column.getPatientId()).getFullName()).setHeader("Patient");

        } else {
            inAppointment.addColumn(column -> dbService.getUserById(column.getDoctorId()).getFullName()).setHeader("Doctor");
            outAppointment.addColumn(column -> dbService.getUserById(column.getDoctorId()).getFullName()).setHeader("Doctor");
        }

        inAppointment.addColumn(column -> MedioUtils.formatDateTimeDefault(column.getAppointmentTime())).setHeader("Date and start time");
        inAppointment.addColumn(MedioUtils::toStringDuration).setHeader("Duration");
        inAppointment.addColumn(Appointment::getLocation).setHeader("Address");
        inAppointment.addComponentColumn(column -> new Button("Show", event -> {
            ShowAppointmentStringDialog showAppointmentStringDialog = new ShowAppointmentStringDialog(column.getDescription());
            showAppointmentStringDialog.open();
        })).setHeader("Description");
        inAppointment.addColumn(column -> {
            if (column.isConfirmed()) {
                return "Approved";
            } else if (column.isInvalid()) {
                return "Rejected";
            } else {
                return "Awaiting confirmation";
            }
        }).setHeader("Status");

        inAppointment.addComponentColumn(column -> {
            Button button = new Button("Confirm", event -> {
                dbService.setAppointmentConfirmedById(column.getId());
                Notification.show("Appointment confirmed");
                inAppointment.setItems(getInData());
            });
            if (column.isInvalid()) {
                button.setEnabled(false);
            }
            if (column.isConfirmed()) {
                button.setEnabled(false);
            }
            return button;
        }).setHeader("Confirm appointment");

        inAppointment.addComponentColumn(column -> {
            Button button;
            if (!column.isInvalid()) {
                button = new Button("Reject", event -> {
                    RejectAppointmentDialog rejectAppointmentDialog = new RejectAppointmentDialog(column, dbService, currentUser, inAppointment);
                    rejectAppointmentDialog.open();
                    inAppointment.setItems(getInData());
                });
            } else {
                button = new Button("Reject");
                button.setEnabled(false);
            }
            if(column.isConfirmed()) {
                button.setEnabled(false);
            }
            return button;
        }).setHeader("Reject appointment");

        inAppointment.setItems(inData);

        outAppointment.addColumn(column -> MedioUtils.formatDateTimeDefault(column.getAppointmentTime())).setHeader("Date and start time");
        outAppointment.addColumn(MedioUtils::toStringDuration).setHeader("Duration");
        outAppointment.addColumn(Appointment::getLocation).setHeader("Address");
        outAppointment.addComponentColumn(column -> new Button("Show", event -> {
            ShowAppointmentStringDialog showAppointmentStringDialog = new ShowAppointmentStringDialog(column.getDescription());
            showAppointmentStringDialog.open();
        })).setHeader("Description");
        outAppointment.addComponentColumn(column -> getIcon(column.isConfirmed())).setHeader("Confirmed");
        outAppointment.addComponentColumn(column -> getIcon(!column.isInvalid())).setHeader("Valid");
        outAppointment.addComponentColumn(column -> {
            Button button = new Button("Show", event -> {
                ShowAppointmentStringDialog showAppointmentStringDialog = new ShowAppointmentStringDialog(column.getReason());
                showAppointmentStringDialog.open();
            });
            button.setEnabled(column.isInvalid());
            return button;
        }).setHeader("Rejection reason");
        outAppointment.addComponentColumn(column -> new Button("Remove", event -> {
            dbService.removeAppointmentById(column.getId());
            outAppointment.setItems(getOutData());
            Notification.show("Removed appointment");
        })).setHeader("Remove activity");

        outAppointment.setItems(outData);

        inAppointment.setWidthFull();
        outAppointment.setWidthFull();

        Button button = new Button("Schedule a new meeting", event -> {
            MeetingSchedulerDialog dialog = new MeetingSchedulerDialog(dbService, currentUser, outAppointment);
            dialog.open();
        });

        button.setWidth("50%");

        mainLayout.add(button, inAppointment, outAppointment);

    }


    private List<Appointment> getInData() {
        if (currentUser.getRoleId() == 2) {
            return dbService.getAppointmentsByDoctorIdAndDrive(currentUser.getId(), false);
        } else {
            return dbService.getAppointmentsByPatientIdAndDrive(currentUser.getId(), true);
        }
    }

    private List<Appointment> getOutData() {
        if (currentUser.getRoleId() == 2) {
            return dbService.getAppointmentsByDoctorIdAndDrive(currentUser.getId(), true);
        } else {
            return dbService.getAppointmentsByPatientIdAndDrive(currentUser.getId(), false);
        }
    }

    private Icon getIcon(boolean value) {
        if (value) {
            return VaadinIcon.CHECK_CIRCLE.create();
        }
        return VaadinIcon.CLOSE_CIRCLE.create();
    }
}

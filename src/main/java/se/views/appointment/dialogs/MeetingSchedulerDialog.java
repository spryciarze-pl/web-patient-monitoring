package se.views.appointment.dialogs;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.timepicker.TimePicker;
import org.springframework.beans.factory.annotation.Autowired;
import se.db.model.Appointment;
import se.db.model.Clinic;
import se.db.model.User;
import se.db.service.DbService;
import se.enums.AppointmentType;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class MeetingSchedulerDialog extends Dialog {

    @Autowired
    DbService dbService;

    private User currentUser;
    private Grid<Appointment> gridToUpdate;

    private VerticalLayout mainLayout = new VerticalLayout();
    private HorizontalLayout elementLayout = new HorizontalLayout();

    public MeetingSchedulerDialog(DbService dbService, User currentUser, Grid<Appointment> gridToUpdate) {
        this.dbService = dbService;
        this.currentUser = currentUser;
        this.gridToUpdate = gridToUpdate;

        if (currentUser.getRoleId() == 2) {
            createViewForDoctor();
        } else {
            createViewForPatient();
        }

        elementLayout.setWidthFull();

        mainLayout.setAlignItems(FlexComponent.Alignment.CENTER);
        setWidth("70%");
        setHeight("70%");
        add(mainLayout);

    }

    private void createViewForDoctor() {

        H2 title = new H2("Appointment Description");
        title.setHeight("10%");

        ComboBox<User> patientsCombobox = new ComboBox<>("Patients ");
        patientsCombobox.setItems(dbService.getPatientsAssignedToDoctor(currentUser.getId()));
        patientsCombobox.setItemLabelGenerator(User::getFullName);

        ComboBox<AppointmentType> appointmentTypeCombobox = new ComboBox<>("Type");
        appointmentTypeCombobox.setItems(AppointmentType.values());
        appointmentTypeCombobox.setItemLabelGenerator(AppointmentType::getValue);

        ComboBox<Clinic> clinicComboBox = new ComboBox<>("Location");
        clinicComboBox.setItems(dbService.getAllClinics());
        clinicComboBox.setItemLabelGenerator(clinic -> clinic.getName() + " " + clinic.getAddress());

        appointmentTypeCombobox.addValueChangeListener(value -> {
            if (value.getValue() == AppointmentType.ONLINE) {
                elementLayout.remove(clinicComboBox);
            } else {
                elementLayout.add(clinicComboBox);
            }
        });

        TimePicker startTime = new TimePicker();
        startTime.setLabel("Start time");

        TimePicker endTime = new TimePicker();
        endTime.setLabel("End time");

        endTime.addValueChangeListener(event -> {
            startTime.setMax(event.getValue());
        });

        startTime.addValueChangeListener(event -> {
            endTime.setMin(event.getValue());
        });

        DatePicker datePicker = new DatePicker();
        datePicker.setMin(LocalDate.now());
        datePicker.setLabel("Select date");

        TextArea textArea = new TextArea();
        textArea.setWidthFull();
        textArea.setHeightFull();

        HorizontalLayout textAreaLayout = new HorizontalLayout(textArea);
        textAreaLayout.setWidthFull();
        textAreaLayout.setHeight("80%");

        elementLayout.add(patientsCombobox, appointmentTypeCombobox, startTime, endTime, datePicker);

        Button button = new Button("Save new Appointment", event -> {

            if (patientsCombobox.isEmpty() || datePicker.isEmpty() || appointmentTypeCombobox.isEmpty() || endTime.isEmpty() ||
                    (appointmentTypeCombobox.getValue() != AppointmentType.ONLINE && clinicComboBox.isEmpty())) {
                Notification.show("Please fill all of the fields");
            } else {
                LocalDateTime dateTime = LocalDateTime.of(datePicker.getValue(), startTime.getValue());
                LocalDateTime dateTimeEnd = LocalDateTime.of(datePicker.getValue(), endTime.getValue());

                Appointment appointment = new Appointment();
                appointment.setDoctorId(currentUser.getId());
                appointment.setPatientId(patientsCombobox.getValue().getId());
                appointment.setType(appointmentTypeCombobox.getValue().getValue());
                appointment.setDescription(textArea.getValue());
                appointment.setAppointmentTime(dateTime);
                appointment.setLocation(clinicComboBox.getValue() != null ? clinicComboBox.getValue().getAddress() : "online");
                appointment.setAppointmentEnd(dateTimeEnd);
                appointment.setConfirmed(false);
                appointment.setInvalid(false);
                appointment.setDoctorDriven(true);

                dbService.saveNewAppointment(appointment);

                gridToUpdate.setItems(dbService.getAppointmentsByDoctorIdAndDrive(currentUser.getId(), true));
                Notification.show("Created new Appointment");
                close();
            }
        });

        mainLayout.add(title, elementLayout, textAreaLayout, button);
    }

    private void createViewForPatient() {

        H2 title = new H2("Appointment Description");
        title.setHeight("10%");

        ComboBox<AppointmentType> appointmentTypeCombobox = new ComboBox<>("Type");
        appointmentTypeCombobox.setItems(AppointmentType.values());
        appointmentTypeCombobox.setItemLabelGenerator(AppointmentType::getValue);

        ComboBox<Clinic> clinicComboBox = new ComboBox<>("Location");
        clinicComboBox.setItems(dbService.getAllClinics());
        clinicComboBox.setItemLabelGenerator(clinic -> clinic.getName() + " " + clinic.getAddress());

        appointmentTypeCombobox.addValueChangeListener(value -> {
            if (value.getValue() == AppointmentType.ONLINE) {
                elementLayout.remove(clinicComboBox);
            } else {
                elementLayout.add(clinicComboBox);
            }
        });

        TimePicker startTime = new TimePicker();
        startTime.setLabel("Start time");

        TimePicker endTime = new TimePicker();
        endTime.setLabel("End time");

        endTime.addValueChangeListener(event -> {
            startTime.setMax(event.getValue());
        });

        startTime.addValueChangeListener(event -> {
            endTime.setMin(event.getValue());
        });

        DatePicker datePicker = new DatePicker();
        datePicker.setMin(LocalDate.now());
        datePicker.setLabel("Select date");

        TextArea textArea = new TextArea();
        textArea.setWidthFull();
        textArea.setHeightFull();

        HorizontalLayout textAreaLayout = new HorizontalLayout(textArea);
        textAreaLayout.setWidthFull();
        textAreaLayout.setHeight("80%");

        elementLayout.add(appointmentTypeCombobox, startTime, endTime, datePicker);

        Button button = new Button("Save new Appointment", event -> {

            if (datePicker.isEmpty() || appointmentTypeCombobox.isEmpty() || endTime.isEmpty() ||
                    (appointmentTypeCombobox.getValue() != AppointmentType.ONLINE && clinicComboBox.isEmpty())) {
                Notification.show("Please fill all of the fields");
            } else {
                LocalDateTime dateTime = LocalDateTime.of(datePicker.getValue(), startTime.getValue());
                LocalDateTime dateTimeEnd = LocalDateTime.of(datePicker.getValue(), endTime.getValue());

                Appointment appointment = new Appointment();
                appointment.setDoctorId(dbService.getPatientsDoctor(currentUser.getId()).getId());
                appointment.setPatientId(currentUser.getId());
                appointment.setType(appointmentTypeCombobox.getValue().getValue());
                appointment.setDescription(textArea.getValue() != null ? textArea.getValue() : "");
                appointment.setAppointmentTime(dateTime);
                appointment.setLocation(clinicComboBox.getValue() != null ? clinicComboBox.getValue().getAddress() : "online");
                appointment.setAppointmentEnd(dateTimeEnd);
                appointment.setConfirmed(false);
                appointment.setInvalid(false);
                appointment.setDoctorDriven(false);

                dbService.saveNewAppointment(appointment);

                gridToUpdate.setItems(dbService.getAppointmentsByPatientIdAndDrive(currentUser.getId(), false));
                Notification.show("Created new Appointment");
                close();
            }
        });

        mainLayout.add(title, elementLayout, textAreaLayout, button);

    }

}

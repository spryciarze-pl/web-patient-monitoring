package se.views.panel;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datetimepicker.DateTimePicker;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.PermitAll;
import org.springframework.beans.factory.annotation.Autowired;
import se.db.model.DoctorsActivity;
import se.db.model.PatientsActivity;
import se.db.model.Prescription;
import se.db.model.User;
import se.db.service.DbService;
import se.dto.DoctorsActivityDto;
import se.enums.DoctorsActivityEnum;
import se.enums.PatientsActivityEnum;
import se.security.SecurityService;
import se.utils.MedioUtils;
import se.views.MainLayout;
import com.vaadin.flow.component.grid.Grid;

import java.time.LocalDate;
import java.time.LocalDateTime;

import se.views.panel.dialogs.*;


import java.util.List;

@PermitAll
@PageTitle("Main Pannel")
@Route(value = "", layout = MainLayout.class)
public class PanelView extends VerticalLayout {
    @Autowired
    SecurityService securityService;

    @Autowired
    DbService dbService;

    private User currentUser;

    private HorizontalLayout mainContainerLayout;
    private VerticalLayout leftSideLayout;
    private VerticalLayout detailsLayout;
    private VerticalLayout activitySelectorLayout;
    private HorizontalLayout activityDetailsLayout;
    private VerticalLayout rightSideLayout;


    public PanelView(SecurityService securityService, DbService dbService) {

        this.dbService = dbService;
        this.currentUser = securityService.getAuthenticatedUser().getUser();
        prepareView();

        add(mainContainerLayout);

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
    }

    private void prepareView() {
        leftSideLayout = new VerticalLayout();
        leftSideLayout.setWidth("30%");
        leftSideLayout.setHeightFull();

        rightSideLayout = new VerticalLayout();
        rightSideLayout.setWidth("70%");
        rightSideLayout.setHeightFull();

        mainContainerLayout = new HorizontalLayout(leftSideLayout, rightSideLayout);
        mainContainerLayout.setWidthFull();
        mainContainerLayout.setHeightFull();
    }

    private void createDoctorView() {

        createWelcomeMessage();

        List<User> selectedPatients = dbService.getPatientsAssignedToDoctor(currentUser.getId());

        Grid<DoctorsActivityDto> doctorsActivityDtoGrid = createDoctorsActivityDtoGrid();
        Grid<User> patientsGrid = createPatientsGrid(doctorsActivityDtoGrid);

        rightSideLayout.add(patientsGrid);

        activitySelectorLayout = new VerticalLayout();
        activityDetailsLayout = new HorizontalLayout();

        ComboBox<DoctorsActivityEnum> activityCombobox = new ComboBox<>("Doctors Activity");
        activityCombobox.setItems(DoctorsActivityEnum.values());
        activityCombobox.setItemLabelGenerator(DoctorsActivityEnum::getValue);

        ComboBox<User> patientsCombobox = new ComboBox<>("Patients ");
        patientsCombobox.setItems(selectedPatients);
        patientsCombobox.setItemLabelGenerator(user -> user.getName() + " " + user.getSurname());

        DateTimePicker dateTimePicker = new DateTimePicker();
        dateTimePicker.getStyle().set("margin-top", "33px");
        dateTimePicker.setMin(LocalDateTime.now());

        ActivityDescriptionDialog activityDescriptionDialog = new ActivityDescriptionDialog();
        Button addDescriptionButton = new Button("Add description", event -> {
            activityDescriptionDialog.open();
        });

        Button selectActivity = new Button("Select activity", event -> {

            if (activityCombobox.getValue() == DoctorsActivityEnum.PRESCRIPTION) {
                if (patientsCombobox.isEmpty()) {
                    Notification.show("Please select all the boxes to create activity!");
                } else {
                    Prescription prescription = new Prescription();
                    prescription.setDoctorId(currentUser.getId());
                    prescription.setPatientId(patientsCombobox.getValue().getId());
                    prescription.setPrescriptionTime(LocalDateTime.now());

                    PrescriptionActivityDialog prescriptionActivityDialog = new PrescriptionActivityDialog(dbService, prescription);
                    prescriptionActivityDialog.open();
                }
            } else {
                if (activityCombobox.isEmpty() || patientsCombobox.isEmpty() || dateTimePicker.isEmpty()) {
                    Notification.show("Please select all the boxes to create activity");
                } else {
                    DoctorsActivity doctorsActivity = new DoctorsActivity(currentUser.getId(), patientsCombobox.getValue().getId(),
                            activityCombobox.getValue().getValue(), activityDescriptionDialog.getDescription(), LocalDateTime.now(), dateTimePicker.getValue(), false);
                    activityDescriptionDialog.clear();
                    dbService.saveNewDoctorsActivity(doctorsActivity);
                    Notification.show("Created new Activity");
                    activityCombobox.clear();
                    patientsCombobox.clear();
                    dateTimePicker.clear();
                    doctorsActivityDtoGrid.setItems(dbService.getActivityDtoByDoctorId(currentUser.getId()));
                }
            }

        });

        activityCombobox.addValueChangeListener(value -> {
            if (value.getValue() == DoctorsActivityEnum.PRESCRIPTION) {
                activityDetailsLayout.remove(dateTimePicker);
                activityDetailsLayout.remove(addDescriptionButton);
            } else {
                activityDetailsLayout.remove(selectActivity);
                activityDetailsLayout.add(dateTimePicker);
                activityDetailsLayout.add(addDescriptionButton);
                activityDetailsLayout.add(selectActivity);
            }
        });

        addDescriptionButton.getStyle().set("margin-top", "35px");
        selectActivity.getStyle().set("margin-top", "35px");

        activityDetailsLayout.add(activityCombobox, patientsCombobox, dateTimePicker, addDescriptionButton, selectActivity);

        activitySelectorLayout.add(activityDetailsLayout);

        add(doctorsActivityDtoGrid);
        add(activitySelectorLayout);

        //leftSideLayout.add(activitySelectorLayout);
    }

    private Grid<User> createPatientsGrid(Grid<DoctorsActivityDto> doctorsActivityDtoGrid) {
        Grid<User> patientsGrid = new Grid<>(User.class, false);
        patientsGrid.addColumn(User::getName).setHeader("First Name");
        patientsGrid.addColumn(User::getSurname).setHeader("Surname");
        patientsGrid.addColumn(User::getMail).setHeader("Mail");
        patientsGrid.addColumn(column -> column.getClinic() == null ? "" : column.getClinic().getName()).setHeader("Clinic");
        patientsGrid.addComponentColumn(patient -> {
            Button button = new Button("End treatment");
            button.addClickListener(event -> {
                dbService.removeAllPatientsDoctorsActivitiesByPatientId(patient.getId());
                dbService.removeAllPatientsActivitiesForPatientId(patient.getId());
                dbService.removeAssignedPatient(patient.getId());
                Notification.show("Treatment finalised for patient: " + patient.getName() + " " + patient.getSurname());
                doctorsActivityDtoGrid.setItems(dbService.getActivityDtoByDoctorId(currentUser.getId()));
                patientsGrid.setItems(dbService.getPatientsAssignedToDoctor(currentUser.getId()));

            });
            return button;
        }).setHeader("Action");

        patientsGrid.setItems(dbService.getPatientsAssignedToDoctor(currentUser.getId()));

        return patientsGrid;
    }

    private Grid<DoctorsActivityDto> createDoctorsActivityDtoGrid() {

        Grid<DoctorsActivityDto> activityGrid = new Grid<>(DoctorsActivityDto.class, false);
        activityGrid.addColumn(DoctorsActivityDto::getFullName).setHeader("Name");
        activityGrid.addColumn(DoctorsActivityDto::getActivityType).setHeader("Type");
        activityGrid.addColumn(column -> MedioUtils.formatDateTimeDefault(column.getDeadline())).setHeader("Deadline");
        activityGrid.addComponentColumn(activity -> {

            Button button = new Button("Show reply");
            if (!activity.isCompleted()) {
                button.setEnabled(false);
                button.setTooltipText("Activity is not yet completed");
            } else {
                button.addClickListener(event -> {
                    ActivityCompletedDialog activityCompletedDialog = new ActivityCompletedDialog(dbService, activity.getActivityId());
                    activityCompletedDialog.open();
                });
            }

            return button;
        }).setHeader("Show reply");
        activityGrid.addComponentColumn(activity -> {
            Button button = new Button("Remove activity");
            button.addClickListener(event -> {
                dbService.removeDoctorsActivity(activity.getActivityId());
                dbService.removePatientActivityByDoctorsRequestId(activity.getActivityId());

                Notification.show("Removed activity: " + activity.getActivityType() + " for " + activity.getFullName());
                activityGrid.setItems(dbService.getActivityDtoByDoctorId(currentUser.getId()));
            });
            return button;
        }).setHeader("Remove Action");

        activityGrid.setItems(dbService.getActivityDtoByDoctorId(currentUser.getId()));

        return activityGrid;
    }

    private Grid<DoctorsActivityDto> createDoctorsActivityDtoGridForPatient() {
        Grid<DoctorsActivityDto> activityGrid = new Grid<>(DoctorsActivityDto.class, false);

        activityGrid.addColumn(DoctorsActivityDto::getActivityType).setHeader("Type");
        activityGrid.addColumn(column -> MedioUtils.formatDateTimeDefault(column.getDeadline())).setHeader("Deadline");
        activityGrid.addColumn(column -> MedioUtils.getReaminingDuration(column.getDeadline())).setHeader("Remaining");
        activityGrid.addComponentColumn(component -> {
            Button button = new Button("Perform activity", event -> {
                ActivityActionDialog modal = new ActivityActionDialog(dbService, component, activityGrid);
                modal.open();
            });
            return button;
        }).setHeader("Action");

        activityGrid.setItems(dbService.getActivityDtoByPatientId(currentUser.getId()));

        return activityGrid;
    }

    private void createPatientView() {

        createWelcomeMessage();

        if (dbService.patientHasDoctor(currentUser.getId())) {
            User patientsDoctor = dbService.getPatientsDoctor(currentUser.getId());
            createDoctorDetails(patientsDoctor);
        } else {
            Button selectDoctorButton = new Button("Select your Doctor");
            selectDoctorButton.addClickListener(event -> {
                DoctorSelectionDialog dialog = new DoctorSelectionDialog(this.currentUser, dbService.getAvailableDoctors(), dbService);
                dialog.open();
            });
            leftSideLayout.add(selectDoctorButton);
        }

        Grid<DoctorsActivityDto> activityGrid = createDoctorsActivityDtoGridForPatient();

        activityDetailsLayout = new HorizontalLayout();
        activityDetailsLayout.setWidthFull();

        ComboBox<PatientsActivityEnum> activityCombobox = new ComboBox<>("Patients Activity");
        activityCombobox.setItems(PatientsActivityEnum.values());
        activityCombobox.setItemLabelGenerator(PatientsActivityEnum::getValue);

        ActivityDescriptionDialog activityDescriptionDialog = new ActivityDescriptionDialog();
        Button addDescriptionButton = new Button("Add description", event -> {
            activityDescriptionDialog.open();
        });

        Button selectActivity = new Button("Select Activity", event -> {
            if(activityCombobox.isEmpty()) {
                Notification.show("Please select all the boxes to create activity!");
            } else if (activityDescriptionDialog.getDescription().isEmpty()) {
                Notification.show("Please enter a description for the action");
            } else {

                DoctorsActivity doctorsActivity = new DoctorsActivity();
                doctorsActivity.setActivityTime(LocalDateTime.now());
                doctorsActivity.setPatientId(currentUser.getId());
                doctorsActivity.setDoctorId(dbService.getPatientsDoctor(currentUser.getId()).getId());
                doctorsActivity.setType(activityCombobox.getValue().getValue());
                doctorsActivity.setDeadlineTime(LocalDateTime.now());
                doctorsActivity.setCompleted(true);
                doctorsActivity.setDescription("Description");

                int idx = dbService.saveNewDoctorsActivity(doctorsActivity).getId();

                PatientsActivity patientsActivity = new PatientsActivity();
                patientsActivity.setPatientId(currentUser.getId());
                patientsActivity.setTime(LocalDateTime.now());
                patientsActivity.setDoctorsRequestId(idx);
                patientsActivity.setResult(activityDescriptionDialog.getDescription());
                patientsActivity.setType(activityCombobox.getValue().getValue());

                dbService.saveNewPatientActivity(patientsActivity);
                Notification.show("Activity saved");
            }
        });

        addDescriptionButton.getStyle().set("margin-top", "35px");
        selectActivity.getStyle().set("margin-top", "35px");

        activityDetailsLayout.add(activityCombobox, addDescriptionButton, selectActivity);

        if(dbService.patientHasDoctor(currentUser.getId())) {
            add(activityDetailsLayout);
        }

        rightSideLayout.add(activityGrid);

    }

    private void createWelcomeMessage() {
        H2 welcomeMessage = new H2("Hello " + currentUser.getName());
        H3 currentDate = new H3("Today is: " + LocalDate.now());
        currentDate.getStyle().set("margin", "15px");

        detailsLayout = new VerticalLayout(welcomeMessage, currentDate);
        detailsLayout.setAlignItems(Alignment.CENTER);

        detailsLayout.setHeight("50%");
        leftSideLayout.add(detailsLayout);
    }

    private void createDoctorDetails(User user) {
        H2 doctorName = new H2("Your Doctor: " + user.getName() + " " + user.getSurname());
        H3 specializationLabel = new H3("Specialization " + user.getSpecialization());
        if (user.getSpecialization() != null) {
            detailsLayout.add(doctorName, specializationLabel);
        } else {
            detailsLayout.add(doctorName);
        }
    }

    private void createAdminView() {

    }
}

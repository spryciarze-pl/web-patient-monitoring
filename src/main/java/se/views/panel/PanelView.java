package se.views.panel;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datetimepicker.DateTimePicker;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.PermitAll;
import org.springframework.beans.factory.annotation.Autowired;
import se.db.model.Assignment;
import se.db.model.DoctorsActivity;
import se.db.model.PatientsActivity;
import se.db.model.User;
import se.db.repository.*;
import se.dto.DoctorsActivityDto;
import se.enums.DoctorsActivityEnum;
import se.security.SecurityService;
import se.views.MainLayout;
import com.vaadin.flow.component.grid.Grid;

import java.time.LocalDateTime;
import java.util.Arrays; //to delete later
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.textfield.TextField;
import se.views.panel.dialogs.DoctorSelectionDialog;


import java.util.List;
import java.util.stream.Collectors;

@PermitAll
@PageTitle("Hello World")
@Route(value = "", layout = MainLayout.class)
public class PanelView extends VerticalLayout {

    @Autowired
    private SpecializationRepository specializationRepository;

    @Autowired
    AssignmentRepository assignmentRepository;
    @Autowired
    PatientsActivityRepository patientsActivityRepository;
    @Autowired
    DoctorsActivityRepository doctorsActivityRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    SecurityService securityService;

    private User currentUser;


    private HorizontalLayout mainContainerLayout;
    private VerticalLayout leftSideLayout;
    private VerticalLayout detailsLayout;
    private VerticalLayout activitySelectorLayout;
    private HorizontalLayout activityDetailsLayout;
    private VerticalLayout rightSideLayout;



    public PanelView(SecurityService securityService, AssignmentRepository assignmentRepository,
                     PatientsActivityRepository patientsActivityRepository, DoctorsActivityRepository doctorsActivityRepository,
                     SpecializationRepository specializationRepository, UserRepository userRepository) {
        this.securityService = securityService;
        this.assignmentRepository = assignmentRepository;
        this.patientsActivityRepository = patientsActivityRepository;
        this.doctorsActivityRepository = doctorsActivityRepository;
        this.specializationRepository = specializationRepository;
        this.userRepository = userRepository;

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

        leftSideLayout = new VerticalLayout();
        leftSideLayout.setWidth("50%");
        leftSideLayout.setHeightFull();

        rightSideLayout = new VerticalLayout();
        rightSideLayout.setWidth("50%");
        rightSideLayout.setHeightFull();

        mainContainerLayout = new HorizontalLayout(leftSideLayout, rightSideLayout);
        mainContainerLayout.setWidthFull();
        mainContainerLayout.setHeightFull();

    }

    private void createDoctorView() {
        List<User> selectedPatients = assignmentRepository.findByDoctorId(this.currentUser.getId()).stream()
                .map(id -> userRepository.findById(id.getPatientId())).toList();

        Grid<User> patientsGrid = new Grid<>(User.class, false);
        patientsGrid.addColumn(User::getName).setHeader("First Name");
        patientsGrid.addColumn(User::getSurname).setHeader("Surname");
        patientsGrid.addColumn(User::getMail).setHeader("Mail");
        patientsGrid.addColumn(User::getClinic).setHeader("Clinic");

        patientsGrid.setItems(selectedPatients);

        List<DoctorsActivityDto> doctorsActivityDtos;

        doctorsActivityDtos = doctorsActivityRepository.findByDoctorId(currentUser.getId()).stream()
                .map(activity -> new DoctorsActivityDto(userRepository.findById(activity.getPatientId()), activity))
                .toList();

        Grid<DoctorsActivityDto> activityGrid = new Grid<>(DoctorsActivityDto.class, false);
        activityGrid.addColumn(DoctorsActivityDto::getFullName).setHeader("Name");
        activityGrid.addColumn(DoctorsActivityDto::getLocalDateTime).setHeader("Deadline");
        activityGrid.addColumn(DoctorsActivityDto::getActivityType).setHeader("Type");

        activityGrid.setItems(doctorsActivityDtos);

        rightSideLayout.add(activityGrid);

        leftSideLayout.add(patientsGrid);

        activitySelectorLayout = new VerticalLayout();
        activityDetailsLayout = new HorizontalLayout();

        ComboBox<DoctorsActivityEnum> activityCombobox = new ComboBox<>("Doctors Activity");
        activityCombobox.setItems(DoctorsActivityEnum.values());
        activityCombobox.setItemLabelGenerator(DoctorsActivityEnum::getValue);

        ComboBox<User> patientsCombobox = new ComboBox<>("Patients ");
        patientsCombobox.setItems(selectedPatients);
        patientsCombobox.setItemLabelGenerator(User::getName);

        DateTimePicker dateTimePicker = new DateTimePicker();
        dateTimePicker.getStyle().set("margin-top", "33px");


        Button selectActivity = new Button("Select activity", event -> {

            if(activityCombobox.isEmpty() || patientsCombobox.isEmpty() || dateTimePicker.isEmpty()) {
                Notification.show("Please select all the boxes to create activity!").open();
            }

            DoctorsActivity doctorsActivity = new DoctorsActivity(currentUser.getId(), patientsCombobox.getValue().getId(),
                    activityCombobox.getValue().getValue(), "null", LocalDateTime.now(), dateTimePicker.getValue());
            doctorsActivityRepository.save(doctorsActivity);
            activityCombobox.clear();
            patientsCombobox.clear();
            dateTimePicker.clear();
            activityGrid.setItems(doctorsActivityRepository.findByDoctorId(currentUser.getId()).stream()
                    .map(activity -> new DoctorsActivityDto(userRepository.findById(activity.getPatientId()), activity))
                    .toList());
        });

        selectActivity.getStyle().set("margin-top", "35px");

        activityDetailsLayout.add(activityCombobox, patientsCombobox, dateTimePicker, selectActivity);

        activitySelectorLayout.add(activityDetailsLayout);

        leftSideLayout.add(activitySelectorLayout);

    }

    private void createPatientView() {

        if (assignmentRepository.findByPatientId(this.currentUser.getId()) != null) {
            User patientsDoctor = userRepository.findById(assignmentRepository.findByPatientId(this.currentUser.getId()).getDoctorId());
            createDoctorDetails(patientsDoctor);
        } else {
            Button selectDoctorButton = new Button("Select your Doctor");
            selectDoctorButton.addClickListener(event -> {
                List<Integer> availableDoctorsIds = assignmentRepository.findDoctorsWithMoreThan5Assignments();
                List<User> avaibleDoctors = userRepository.findByRoleId(2);

                avaibleDoctors = avaibleDoctors.stream().filter(user -> !availableDoctorsIds.contains(user.getId())).collect(Collectors.toList());

                DoctorSelectionDialog dialog = new DoctorSelectionDialog(this.currentUser, avaibleDoctors, assignmentRepository);
                dialog.open();

            });
            leftSideLayout.add(selectDoctorButton);
        }

        List<DoctorsActivity> patientsActivities = doctorsActivityRepository.findByPatientId(currentUser.getId());
        List<DoctorsActivityDto> patientsActivitiesDto = patientsActivities.stream().map(DoctorsActivityDto::new).toList();

        Grid<DoctorsActivityDto> activityGrid = new Grid<>(DoctorsActivityDto.class, false);
        activityGrid.addColumn(DoctorsActivityDto::getLocalDateTime).setHeader("Deadline");
        activityGrid.addColumn(DoctorsActivityDto::getActivityType).setHeader("Type");

        activityGrid.setItems(patientsActivitiesDto);

        rightSideLayout.add(activityGrid);

    }

    private void createDoctorDetails(User user) {

        detailsLayout = new VerticalLayout();

        H2 doctorName = new H2("Main Doctor: " + user.getName() + " " + user.getSurname());
        H3 specializationLabel = new H3("Specialization " + user.getSpecialization());

        detailsLayout.add(doctorName, specializationLabel);
        detailsLayout.setHeight("50%");
        leftSideLayout.add(detailsLayout);
    }

    private void createAdminView() {

    }

    private void test() {
        this.securityService = securityService;

        // Left Side Layout
        leftSideLayout = new VerticalLayout();
        leftSideLayout.setDefaultHorizontalComponentAlignment(FlexComponent.Alignment.CENTER);

        H2 mainDoctorHeading = new H2("Main Doctor");
        mainDoctorHeading.addClassName("H2");

        H3 nameSurnameLabel = new H3("Name and Surname");
        H3 specializationLabel = new H3("Specialization");

        H2 selectActivityHeading = new H2("Select Activity");
        selectActivityHeading.addClassName("H2");

        ComboBox<String> activityComboBox = new ComboBox<>();
        activityComboBox.setItems("Measure temperature", "Measure pressure");
        // Set up the ComboBox content here
        // Dialog setup
        Dialog activityDialog = new Dialog();
        activityDialog.setWidth("30em");
        activityDialog.setHeight("20em");

        // Add a value change listener to the ComboBox
        activityComboBox.addValueChangeListener(event -> {
            // Clear existing content
            activityDialog.removeAll();

            // Check the selected item and add content accordingly
            if ("Measure temperature".equals(event.getValue())) {
                // Measure temperature content
                H2 measureTemperatureHeading = new H2("Measure temperature");
                measureTemperatureHeading.addClassName("H2");

                TextField temperatureInput = new TextField();
                temperatureInput.setWidth("5em"); // Set the width to 10 em
                temperatureInput.setLabel(null); // Remove the label
                temperatureInput.setPlaceholder("Input"); // Add a placeholder text if needed
                temperatureInput.getStyle().set("font-size", "1.5em"); // Adjust the font size as needed

                H2 celsiusSymbol = new H2("°C");
                celsiusSymbol.getStyle().set("font-size", "2.3em"); // Adjust the font size as needed

                Button cancelButton = new Button("Cancel", e -> activityDialog.close());
                Button sendButton = new Button("Send", e -> System.out.println("Send"));

                cancelButton.getStyle().set("background-color", "#C33F23");
                cancelButton.getStyle().set("color", "white");
                cancelButton.getStyle().set("font-size", "1.5em"); // Adjust the font size as needed
                cancelButton.setWidth("6em"); // Adjust the width as needed

                sendButton.getStyle().set("font-size", "1.5em"); // Adjust the font size as needed
                sendButton.getElement().getStyle().set("margin-left", "auto");
                sendButton.setWidth("6em"); // Adjust the width as needed

                HorizontalLayout buttonsLayout = new HorizontalLayout(cancelButton, sendButton);
                buttonsLayout.getStyle().set("padding-top", "10em"); // Adjust padding as needed
                // Align "Send" button to the right within the buttonsLayout
                // Add components to the dialog
                activityDialog.add(measureTemperatureHeading, new HorizontalLayout(temperatureInput, celsiusSymbol), buttonsLayout);
            } else if ("Measure pressure".equals(event.getValue())) {
                // Measure pressure content
                H2 measurePressureHeading = new H2("Measure pressure");
                measurePressureHeading.addClassName("H2");

                TextField sysPressureInput = new TextField();
                sysPressureInput.setWidth("10em"); // Set the width to 10 em
                sysPressureInput.setPlaceholder("SYS Input"); // Add a placeholder text if needed
                sysPressureInput.getStyle().set("font-size", "1.5em"); // Adjust the font size as needed

                H2 mmHgSymbolSys = new H2("mmHg");
                mmHgSymbolSys.getStyle().set("font-size", "2.3em"); // Adjust the font size as needed

                TextField diaPressureInput = new TextField();
                diaPressureInput.setWidth("10em"); // Set the width to 10 em
                diaPressureInput.setPlaceholder("DIA Input"); // Add a placeholder text if needed
                diaPressureInput.getStyle().set("font-size", "1.5em"); // Adjust the font size as needed

                H2 mmHgSymbolDia = new H2("mmHg");
                mmHgSymbolDia.getStyle().set("font-size", "2.3em"); // Adjust the font size as needed

                Button cancelButton = new Button("Cancel", e -> activityDialog.close());
                Button sendButton = new Button("Send", e -> System.out.println("Send"));

                cancelButton.getStyle().set("background-color", "#C33F23");
                cancelButton.getStyle().set("font-size", "1.5em"); // Adjust the font size as needed
                cancelButton.getStyle().set("color", "white");
                cancelButton.setWidth("6em"); // Adjust the width as needed

                sendButton.getStyle().set("font-size", "1.5em"); // Adjust the font size as needed
                sendButton.getElement().getStyle().set("margin-left", "auto");
                sendButton.setWidth("6em"); // Adjust the width as needed

                HorizontalLayout buttonsLayout = new HorizontalLayout(cancelButton, sendButton);
                buttonsLayout.getStyle().set("padding-top", "7em"); // Adjust padding as needed
                buttonsLayout.setJustifyContentMode(FlexComponent.JustifyContentMode.END); // Align buttons to the end

                // Add components to the dialog
                activityDialog.add(measurePressureHeading,
                        new HorizontalLayout(sysPressureInput, mmHgSymbolSys),
                        new HorizontalLayout(diaPressureInput, mmHgSymbolDia),
                        buttonsLayout);
            }

            // Open the dialog when an item is selected
            activityDialog.open();
        });

        leftSideLayout.add(mainDoctorHeading, nameSurnameLabel, specializationLabel,
                selectActivityHeading, activityComboBox);

        // Right Side Layout
        // Right Side Layout
        rightSideLayout = new VerticalLayout();
        rightSideLayout.setDefaultHorizontalComponentAlignment(FlexComponent.Alignment.CENTER);

        // Header
        H2 activityGridHeading = new H2("Upcoming activities");
        activityGridHeading.addClassName("H2");

        // Sample grid, you can replace it with your actual data
        List<String[]> activitiesData = Arrays.asList(
                new String[]{"Activity 1", "10:00 AM", "2024-01-18"},
                new String[]{"Activity 2", "02:30 PM", "2024-01-19"}
                // Add more data as needed
        );

        Grid<String[]> activityGrid = new Grid<>();
        activityGrid.addColumn(data -> data[0]).setHeader("Name of Activity");
        activityGrid.addColumn(data -> data[1]).setHeader("Time");
        activityGrid.addColumn(data -> data[2]).setHeader("Date");

        activityGrid.setItems(activitiesData);

        rightSideLayout.add(activityGridHeading, activityGrid);

        // Main Layout
        H2 header = new H2("Hello " + securityService.getAuthenticatedUser().getUser().getName());
        header.addClassName("H1");

        // Use a HorizontalLayout to organize the left and right side layouts
        HorizontalLayout mainContentLayout = new HorizontalLayout(leftSideLayout, rightSideLayout);
        mainContentLayout.expand(leftSideLayout);
        mainContentLayout.setSizeFull();

        add(header, mainContentLayout);
    }


}

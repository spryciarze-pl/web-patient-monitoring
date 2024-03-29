package se.views.panel;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.PermitAll;
import org.springframework.beans.factory.annotation.Autowired;
import se.db.model.Specialization;
import se.db.repository.AssignmentRepository;
import se.db.repository.DoctorsActivityRepository;
import se.db.repository.PatientsActivityRepository;
import se.db.repository.SpecializationRepository;
import se.secuirty.SecurityService;
import se.views.MainLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import java.util.Arrays; //to delete later
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.textfield.TextField;



import java.time.LocalDate;
import java.util.List;

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
    private SecurityService securityService;


    private VerticalLayout leftSideLayout;
    private VerticalLayout rightSideLayout;

    private VerticalLayout leftSide;
    private VerticalLayout doctorDetailsLayout;

    private VerticalLayout activitySelectorLayout;
    private VerticalLayout rightSide;



    public PanelView(SecurityService securityService) {
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

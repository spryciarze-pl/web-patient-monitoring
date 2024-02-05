package se.views.register;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import org.springframework.beans.factory.annotation.Autowired;
import se.db.model.Clinic;
import se.db.model.Password;
import se.db.model.Specialization;
import se.db.model.User;
import se.db.service.DbService;
import se.enums.RoleEnum;

import java.time.LocalDateTime;

public class RegistrationForm extends VerticalLayout {

    @Autowired
    DbService dbService;
    private H3 title = new H3("Register");
    private TextField name = new TextField("Name");
    private TextField surname = new TextField("Surname");
    private TextField pin = new TextField("PIN");
    private TextField phone = new TextField("Phone");
    private TextField mail = new TextField("Email");
    private PasswordField password = new PasswordField("Password");
    private PasswordField confirmPassword = new PasswordField("Confirm Password");
    private ComboBox<RoleEnum> roleComboBox = new ComboBox<>("Role");
    private ComboBox<Clinic> clinicComboBox = new ComboBox<>("Clinic");
    private ComboBox<Specialization> specializationComboBox = new ComboBox<>("Specialization");
    private Binder<User> binder = new Binder<>(User.class);
    private Button submitButton;

    public RegistrationForm(DbService dbService) {
        this.dbService = dbService;

        HorizontalLayout nameAndSurnameLayout = new HorizontalLayout(name, surname);
        nameAndSurnameLayout.setJustifyContentMode(JustifyContentMode.CENTER);
        nameAndSurnameLayout.setWidthFull();
        HorizontalLayout pinAndPhoneLayout = new HorizontalLayout(pin, phone);
        pinAndPhoneLayout.setJustifyContentMode(JustifyContentMode.CENTER);
        pinAndPhoneLayout.setWidthFull();
        HorizontalLayout roleAndClinicLayout = new HorizontalLayout(roleComboBox, clinicComboBox);
        roleAndClinicLayout.setJustifyContentMode(JustifyContentMode.CENTER);
        roleAndClinicLayout.setWidthFull();
        HorizontalLayout passwordsLayout = new HorizontalLayout(password, confirmPassword);
        passwordsLayout.setWidthFull();
        passwordsLayout.setJustifyContentMode(JustifyContentMode.CENTER);

        VerticalLayout boundFieldsLayout = new VerticalLayout(nameAndSurnameLayout, pinAndPhoneLayout, mail, roleAndClinicLayout, passwordsLayout);
        boundFieldsLayout.setAlignItems(Alignment.CENTER);
        boundFieldsLayout.setWidthFull();

        createDefaultBinder();

        roleComboBox.setItemLabelGenerator(RoleEnum::getDescription);
        roleComboBox.setItems(RoleEnum.values());

        roleComboBox.addValueChangeListener(event -> {
            if (event.getValue() == RoleEnum.DOCTOR) {
                boundFieldsLayout.add(specializationComboBox);
                binder.forField(specializationComboBox)
                        .asRequired("Field cannot be empty")
                        .bind(User::getSpecialization, User::setSpecialization);
            } else {
                binder.removeBinding(specializationComboBox);
                boundFieldsLayout.remove(specializationComboBox);
            }
        });

        clinicComboBox.setItemLabelGenerator(Clinic::getName);
        clinicComboBox.setItems(dbService.getAllClinics());

        specializationComboBox.setItemLabelGenerator(Specialization::getName);
        specializationComboBox.setItems(dbService.getAllSpecializations());
        specializationComboBox.setWidthFull();

        submitButton = new Button("Submit");

        submitButton.addClickListener(event -> {
            if (binder.validate().isOk()) {
                Notification.show("Registration successful");
                saveNewUser();
                getUI().ifPresent(ui -> ui.navigate("login"));
            } else {
                Notification.show("Please fill in all the required fields correctly.");
            }
        });

        submitButton.setWidthFull();
        submitButton.getStyle().set("margin-top", "15px");

        mail.setWidthFull();
        setAlignItems(Alignment.CENTER);
        setWidth("50%");
        getStyle().set("background", "#303c54");

        add(title, boundFieldsLayout, submitButton);
    }

    private void saveNewUser() {
        User newUser = new User();
        newUser.setName(this.name.getValue());
        newUser.setSurname(this.surname.getValue());
        newUser.setPin(this.pin.getValue());
        newUser.setPhone(this.phone.getValue());
        newUser.setMail(this.mail.getValue());
        newUser.setRoleId(this.roleComboBox.getValue().getRoleDbVal());
        newUser.setClinic(this.clinicComboBox.getValue());
        newUser.setRegistrationTime(LocalDateTime.now());
        newUser.setConfirmed(false);

        if (newUser.getRoleId() == 2) {
            newUser.setSpecialization(this.specializationComboBox.getValue());
        }

        Password newPassword = new Password(this.password.getValue());
        newUser.setPasswordId(dbService.savePasswordAndGetNewId(newPassword));

        dbService.saveNewUser(newUser);
    }

    private void createDefaultBinder() {
        binder.bindInstanceFields(this);

        binder.forField(name)
                .asRequired("Field cannot be empty")
                .bind(User::getName, User::setName);
        name.setWidth("50%");

        binder.forField(surname)
                .asRequired("Field cannot be empty")
                .bind(User::getSurname, User::setSurname);
        surname.setWidth("50%");

        binder.forField(pin)
                .asRequired("Field cannot be empty")
                .bind(User::getPin, User::setPin);
        pin.setWidth("50%");

        binder.forField(phone)
                .asRequired("Field cannot be empty")
                .bind(User::getPhone, User::setPhone);
        phone.setWidth("50%");

        binder.forField(mail)
                .asRequired("Field cannot be empty")
                .bind(User::getMail, User::setMail);
        mail.setWidth("50%");

        binder.forField(roleComboBox)
                .asRequired("Field cannot be empty")
                .bind(role -> RoleEnum.valueOf(""), (user, role) -> {
                });
        roleComboBox.setWidth("50%");

        binder.forField(clinicComboBox)
                .asRequired("Field cannot be empty")
                .bind(User::getClinic, User::setClinic);
        clinicComboBox.setWidth("50%");

        binder.forField(password)
                .asRequired("Field cannot be empty")
                .bind(password -> "", (user, password) -> {
                });
        password.setWidth("50%");

        binder.forField(confirmPassword)
                .asRequired("Field cannot be empty")
                .withValidator(confirmPassword -> confirmPassword.equals(password.getValue()), "Passwords do not match")
                .bind(confirmPassword -> "", (user, password) -> {
                });
        confirmPassword.setWidth("50%");
    }

}
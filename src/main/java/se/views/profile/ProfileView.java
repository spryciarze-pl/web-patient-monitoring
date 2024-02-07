package se.views.profile;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.PermitAll;
import org.springframework.beans.factory.annotation.Autowired;
import se.db.model.User;
import se.db.service.DbService;
import se.enums.RoleEnum;
import se.security.SecurityService;
import se.views.MainLayout;
@PermitAll
@PageTitle("Profile")
@Route(value = "profile", layout = MainLayout.class)
public class ProfileView extends VerticalLayout {

    @Autowired
    DbService dbService;
    public ProfileView(SecurityService securityService, DbService dbService) {
        this.dbService = dbService;
        User currentUser = securityService.getAuthenticatedUser().getUser();

        setWidth("min-content");
        H3 h3 = new H3("Name: " + currentUser.getName());
        H3 h32 = new H3("Surname: " + currentUser.getSurname());
        H3 h33 = new H3("PESEL: " + currentUser.getPin());
        H3 h34 = new H3("Password: ********");
        H3 h35 = new H3("E-mail address: " + currentUser.getMail());
        H3 h36 = new H3("Phone number: " + currentUser.getPhone());
        H3 h37 = new H3("Clinic: " + (currentUser.getClinic() != null ? currentUser.getClinic().getName() : "Not selected"));

        Button button = new Button("Change");
        ChangeUserInfoDialog dialog = new ChangeUserInfoDialog(dbService, currentUser);
        button.addClickListener(e -> dialog.open());

        setAlignSelf(FlexComponent.Alignment.CENTER, button);
        add(h3, h32, h33, h34, h35, h36, h37);
        if(currentUser.getRoleId().equals(RoleEnum.DOCTOR.getRoleDbVal())) {
            H3 h38 = new H3("Specialization: " + (currentUser.getSpecialization() != null ?
                    currentUser.getSpecialization().getName() : "Not selected"));
            H3 h39 = new H3("Patients limit: " + dbService.getDoctorPatientsLimitByDoctorId(currentUser.getId()).getPLimit());
            add(h38, h39);
        }
        add(button);
    }
}
package se.views.panel.dialogs;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import org.springframework.beans.factory.annotation.Autowired;
import se.db.model.Assignment;
import se.db.model.User;
import se.db.service.DbService;

import java.util.List;

public class DoctorSelectionDialog extends Dialog {

    @Autowired
    DbService dbService;

    public DoctorSelectionDialog(User currentUser, DbService dbService) {

        this.dbService = dbService;
        Grid<User> grid = new Grid<>(User.class, false);

        grid.setItems(dbService.getAvailableDoctors());
        grid.addColumn(User::getFullName).setHeader("Name");
        grid.addColumn(column -> column.getSpecialization() != null ? column.getSpecialization().getName() : "").setHeader("Specialisation");
        grid.addColumn(column -> column.getClinic() != null ? column.getClinic().getName() : "No clinic selected").setHeader("Clinic");
        grid.addComponentColumn(user -> {
            Button button = new Button("Select this Doctor");
            button.addClickListener(event -> {
                Assignment assignment = new Assignment(currentUser.getId(), user.getId());
                dbService.assignPatientToDoctor(assignment);
                close();
                UI.getCurrent().getPage().reload();
            });
            return button;
        }).setHeader("Action");

        VerticalLayout verticalLayout = new VerticalLayout(grid);
        add(verticalLayout);

        setWidth("80%");
    }
}

package se.views.panel.dialogs;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import org.springframework.beans.factory.annotation.Autowired;
import se.db.model.Assignment;
import se.db.model.User;
import se.db.repository.AssignmentRepository;

import java.util.List;

public class DoctorSelectionDialog extends Dialog {


    @Autowired
    private AssignmentRepository assignmentRepository;

    public DoctorSelectionDialog(User currentUser, List<User> doctorList, AssignmentRepository assignmentRepository) {
        this.assignmentRepository = assignmentRepository;
        Grid<User> grid = new Grid<>(User.class, false);

        grid.setItems(doctorList);

        grid.addColumn(User::getName).setHeader("First Name");
        grid.addColumn(User::getSurname).setHeader("Surname");
        grid.addColumn(User::getClinic).setHeader("Clinic");
        grid.addComponentColumn(user -> {
            Button button = new Button("Select this Doctor");
            button.addClickListener(event -> {
                Assignment assignment = new Assignment(currentUser.getId(), user.getId());
                assignmentRepository.save(assignment);
                close();

            });
            return button;
        }).setHeader("Action");

        VerticalLayout verticalLayout = new VerticalLayout(grid);
        add(verticalLayout);

        setWidth("80%");
        setHeight("80%");
    }
}

package se.views.activity;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.PermitAll;
import org.springframework.beans.factory.annotation.Autowired;
import se.db.model.PatientsActivity;
import se.db.model.User;
import se.db.service.DbService;
import se.dto.DoctorsActivityDto;
import se.security.SecurityService;
import se.utils.MedioUtils;
import se.views.MainLayout;
import com.vaadin.flow.component.grid.Grid;
import se.views.activity.dialogs.ShowDescriptionDialog;
import se.views.panel.dialogs.ActivityCompletedDialog;

@PageTitle("Activity")
@Route(value = "activity", layout = MainLayout.class)
@PermitAll
public class ActivityView extends VerticalLayout {

    @Autowired
    SecurityService securityService;
    @Autowired
    DbService dbService;

    private User currentUser;
    private VerticalLayout mainLayout = new VerticalLayout();

    public ActivityView(DbService dbService, SecurityService securityService) {
        this.dbService = dbService;
        this.securityService = securityService;
        this.currentUser = securityService.getAuthenticatedUser().getUser();

        switch (currentUser.getRoleId()) {
            case 2:
                createDoctorsView();
                break;
            case 3:
                createPatientsView();
                break;
        }
        add(mainLayout);
    }

    private void createDoctorsView() {
        Grid<DoctorsActivityDto> activityGrid = new Grid<>(DoctorsActivityDto.class, false);

        activityGrid.setItems(dbService.getActivityDtoByDoctorId(currentUser.getId()));

        activityGrid.addColumn(DoctorsActivityDto::getFullName).setHeader("Name");
        activityGrid.addColumn(DoctorsActivityDto::getActivityType).setHeader("Type");
        activityGrid.addColumn(column -> MedioUtils.formatDateTimeDefault(column.getLocalDateTime())).setHeader("Creation Date");
        activityGrid.addColumn(column -> MedioUtils.formatDateTimeDefault(column.getDeadline())).setHeader("Deadline");
        activityGrid.addComponentColumn(activity -> {
            Button button = new Button("Show description", event -> {
                ShowDescriptionDialog showDescriptionDialog = new ShowDescriptionDialog(activity.getDescription());
                showDescriptionDialog.open();
            });
            return button;
        }).setHeader("Description");
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

        mainLayout.add(activityGrid);
    }

    private void createPatientsView() {
        Grid<PatientsActivity> activityGrid = new Grid<>(PatientsActivity.class, false);

        activityGrid.setItems(dbService.getPatientActivityByPatientId(currentUser.getId()));

        activityGrid.addColumn(PatientsActivity::getType).setHeader("Activity Type");
        activityGrid.addColumn(column -> MedioUtils.formatDateTimeDefault(column.getTime())).setHeader("Date completed");
        activityGrid.addComponentColumn(activity -> {
            Button button = new Button("Show description", event -> {
                ShowDescriptionDialog showDescriptionDialog = new ShowDescriptionDialog(activity.getResult());
                showDescriptionDialog.open();
            });
            return button;
        }).setHeader("Description");

        mainLayout.add(activityGrid);
    }

}

package se.views.activity;

import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.dependency.Uses;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.RolesAllowed;
import se.db.model.User;
import se.views.MainLayout;

@PageTitle("Activity")
@Route(value = "activity", layout = MainLayout.class)
@RolesAllowed("USER")
@Uses(Icon.class)
public class ActivityView extends Composite<VerticalLayout> {

    public ActivityView() {
        Grid basicGrid = new Grid(/*temporary*/User.class);
        getContent().setWidth("100%");
        getContent().getStyle().set("flex-grow", "1");
        basicGrid.setWidth("100%");
        basicGrid.getStyle().set("flex-grow", "0");
        getContent().add(basicGrid);
    }
}

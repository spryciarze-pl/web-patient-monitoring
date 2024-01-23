package se.views.activity;

import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.dependency.Uses;
import com.vaadin.flow.component.html.Hr;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.FlexComponent.Alignment;
import com.vaadin.flow.component.orderedlayout.FlexComponent.JustifyContentMode;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.theme.lumo.LumoUtility.Gap;
import com.vaadin.flow.theme.lumo.LumoUtility.Padding;
import jakarta.annotation.security.RolesAllowed;
import se.db.model.User;
import se.views.MainLayout;

@PageTitle("Activity")
@Route(value = "activity", layout = MainLayout.class)
@RolesAllowed("USER")
@Uses(Icon.class)
public class ActivityView extends Composite<VerticalLayout> {

    public ActivityView() {
        int numberOfActivityBoxes = 2;

        for (int i = 0; i < numberOfActivityBoxes; i++) {
            VerticalLayout layoutColumn2 = new VerticalLayout();
            HorizontalLayout layoutRow = new HorizontalLayout();
            TextField textField = new TextField();
            TextField textField2 = new TextField();
            Span badge = new Span();
            Hr hr = new Hr();

            getContent().setWidth("100%");
            getContent().getStyle().set("flex-grow", "1");
            getContent().setJustifyContentMode(JustifyContentMode.START);
            getContent().setAlignItems(Alignment.CENTER);
            layoutColumn2.setWidthFull();
            getContent().setFlexGrow(1.0, layoutColumn2);
            layoutColumn2.addClassName(Gap.SMALL);
            layoutColumn2.addClassName(Padding.SMALL);
            layoutColumn2.setWidth("768px");
            layoutColumn2.setHeight("min-content");
            layoutColumn2.setJustifyContentMode(JustifyContentMode.CENTER);
            layoutColumn2.setAlignItems(Alignment.CENTER);
            layoutRow.setWidthFull();
            layoutColumn2.setFlexGrow(1.0, layoutRow);
            layoutRow.addClassName(Gap.MEDIUM);
            layoutRow.setWidth("100%");
            layoutRow.setHeight("min-content");
            textField.setLabel("Name of activity");
            textField.getStyle().set("flex-grow", "1");
            textField2.setLabel("Date and time");
            layoutRow.setAlignSelf(FlexComponent.Alignment.START, textField2);
            textField2.setWidth("min-content");
            badge.setText("Not Completed");
            layoutColumn2.setAlignSelf(FlexComponent.Alignment.END, badge);
            badge.setWidth("140px");
            badge.getElement().getThemeList().add("badge");
            getContent().add(layoutColumn2);
            layoutColumn2.add(layoutRow);
            layoutRow.add(textField);
            layoutRow.add(textField2);
            layoutColumn2.add(badge);
            layoutColumn2.add(hr);

            textField.setValue("Sample Activity " + (i + 1));
            textField.setReadOnly(true);

            //now
            textField2.setValue("2021-05-20 12:00");
            textField2.setReadOnly(true);
        }
    }
}

package se.views.panel.dialogs;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import lombok.Getter;

public class ActivityDescriptionDialog extends Dialog {

    private TextArea textArea;

    public ActivityDescriptionDialog() {

        H2 title = new H2("Activity Description");
        title.setHeight("10%");

        textArea = new TextArea();
        textArea.setWidthFull();
        textArea.setHeightFull();

        Button button = new Button("Submit description", event -> {
            close();
        });
        button.setHeight("10%");

        HorizontalLayout textAreaLayout = new HorizontalLayout(textArea);
        textAreaLayout.setWidthFull();
        textAreaLayout.setHeight("80%");

        VerticalLayout mainLayout = new VerticalLayout(title, textAreaLayout, button);
        mainLayout.setAlignItems(FlexComponent.Alignment.CENTER);
        mainLayout.setHeightFull();
        mainLayout.setWidthFull();

        add(mainLayout);
        setWidth("60%");
        setHeight("60%");
    }

    public String getDescription() {
        return textArea.getValue();
    }

    public void clear() {
        textArea.clear();
    }
}

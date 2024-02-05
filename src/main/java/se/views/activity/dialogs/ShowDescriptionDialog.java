package se.views.activity.dialogs;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;

public class ShowDescriptionDialog extends Dialog {
    private final TextArea textArea;
    public ShowDescriptionDialog(String description) {
        H2 title = new H2("Activity Details");
        title.setHeight("10%");

        textArea = new TextArea();
        textArea.setValue(description);
        textArea.setWidthFull();
        textArea.setHeightFull();
        textArea.setReadOnly(true);

        Button button = new Button("Close", event -> {
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
}

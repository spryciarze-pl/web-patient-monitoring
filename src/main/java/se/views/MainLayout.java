package se.views;

import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Footer;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Header;
import com.vaadin.flow.component.orderedlayout.Scroller;
import com.vaadin.flow.component.sidenav.SideNav;
import com.vaadin.flow.component.sidenav.SideNavItem;
import com.vaadin.flow.theme.lumo.LumoUtility;
import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.lineawesome.LineAwesomeIcon;
import se.secuirty.SecurityService;
import se.views.about.AboutView;
import se.views.panel.PanelView;


public class MainLayout extends AppLayout {

    @Autowired
    private SecurityService securityService;
    private H2 viewTitle;

    private Button logoutButton;


    public MainLayout(SecurityService securityService) {
        this.securityService = securityService;
        setPrimarySection(Section.DRAWER);
        addDrawerContent();
        addHeaderContent();
    }

    private void addHeaderContent() {
        DrawerToggle toggle = new DrawerToggle();
        toggle.setAriaLabel("Menu toggle");

        viewTitle = new H2();
        viewTitle.addClassNames(LumoUtility.FontSize.LARGE, LumoUtility.Margin.NONE);

        addToNavbar(true, toggle, viewTitle);
    }

    private void addDrawerContent() {
        H1 appName = new H1("MEDIO");
        appName.addClassNames(LumoUtility.FontSize.LARGE, LumoUtility.Margin.NONE);
        Header header = new Header(appName);
        Scroller scroller = new Scroller(createNavigation());
        addToDrawer(header, scroller, createFooter());
    }

    private SideNav createNavigation() {
        SideNav nav = new SideNav();

        nav.addItem(new SideNavItem("Main panel", PanelView.class, LineAwesomeIcon.HOSPITAL.create()));
        nav.addItem(new SideNavItem("Profile", AboutView.class, LineAwesomeIcon.ADDRESS_BOOK.create()));
        nav.addItem(new SideNavItem("Activity", AboutView.class, LineAwesomeIcon.FEATHER_ALT_SOLID.create()));
        nav.addItem(new SideNavItem("Appointments", AboutView.class, LineAwesomeIcon.CALENDAR.create()));
        nav.addItem(new SideNavItem("Prescriptions", AboutView.class, LineAwesomeIcon.BELL.create()));
        nav.addItem(new SideNavItem("Notifications", AboutView.class, LineAwesomeIcon.ENVELOPE.create()));
        nav.addItem(new SideNavItem("Chat", AboutView.class, LineAwesomeIcon.COMMENT.create()));

        return nav;
    }

    private Footer createFooter() {
        Footer layout = new Footer();

        String u = securityService.getAuthenticatedUser().getUsername();
        logoutButton = new Button("Log out " + u, e -> securityService.logout());
        logoutButton.getStyle().set("width", "100%");
        layout.add(logoutButton);

        return layout;
    }
}

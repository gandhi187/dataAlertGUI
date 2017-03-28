package de.hsrt.sp.dataAlert.demo.gui;

/**
 * @Author David Sharma
 */
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map.Entry;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;

import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.annotations.Theme;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.navigator.Navigator;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.ClassResource;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Page;
import com.vaadin.server.Responsive;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.spring.navigator.SpringViewProvider;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Component;
import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Image;
import com.vaadin.ui.Label;
import com.vaadin.ui.MenuBar;
import com.vaadin.ui.MenuBar.MenuItem;
import com.vaadin.ui.UI;
import com.vaadin.ui.themes.ValoTheme;

@SpringUI(path = "/ui")
@Theme("valo")

public class ProjektAzureAlertUI extends UI {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1824546562366373527L;

	private boolean testMode = false;

	@Autowired
	private SpringViewProvider viewProvider;

	private Button menuButtons;
	private Button mainButton;

	@WebServlet(value = "/*", asyncSupported = true)
	@VaadinServletConfiguration(productionMode = false, ui = ProjektAzureAlertUI.class)
	public static class Servlet extends VaadinServlet {

		/**
		 * 
		 */
		private static final long serialVersionUID = 8043669236973184803L;

		@Override
		protected void servletInitialized() throws ServletException {
			super.servletInitialized();
			getService().addSessionInitListener(new ProjektAzureUISessionInitListener());
		}
	}

	ProjektAzureUILayout root = new ProjektAzureUILayout();
	ComponentContainer viewDisplay = root.getContentContainer();
	CssLayout menu = new CssLayout();
	CssLayout menuItemsLayout = new CssLayout();
	{
		menu.setId("testMenu");
	}
	private Navigator navigator;
	private final LinkedHashMap<String, String> menuItems = new LinkedHashMap<String, String>();

	@Override
	protected void init(final VaadinRequest request) {
		if (request.getParameter("test") != null) {
			testMode = true;

			if (browserCantRenderFontsConsistently()) {
				getPage().getStyles().add(".v-app.v-app.v-app {font-family: Sans-Serif;}");
			}
		}

		if (getPage().getWebBrowser().isIE() && getPage().getWebBrowser().getBrowserMajorVersion() == 9) {
			menu.setWidth("320px");
		}

		if (!testMode) {
			Responsive.makeResponsive(this);
		}

		getPage().setTitle("Projekt Azure");
		setContent(root);
		root.setWidth("100%");

		root.addMenu(buildMenu());
		addStyleName(ValoTheme.UI_WITH_MENU);

		navigator = new Navigator(this, viewDisplay);
		navigator.addProvider(viewProvider);

		CommonParts commonParts = (CommonParts) viewProvider.getView("commonParts");
		Settings settings = (Settings) viewProvider.getView("Settings");

		navigator.addView("common", commonParts);
		navigator.addView("settings", settings);

		final String f = Page.getCurrent().getUriFragment();
		if (f == null || f.equals("")) {
			navigator.navigateTo("common");
			navigator.navigateTo("settings");

		}

		navigator.setErrorView(commonParts);

		navigator.addViewChangeListener(new ViewChangeListener() {

			@Override
			public boolean beforeViewChange(final ViewChangeEvent event) {
				return true;
			}

			@Override
			public void afterViewChange(final ViewChangeEvent event) {
				for (final Iterator<Component> it = menuItemsLayout.iterator(); it.hasNext();) {
					it.next().removeStyleName("selected");
				}
				for (final Entry<String, String> item : menuItems.entrySet()) {
					if (event.getViewName().equals(item.getKey())) {
						for (final Iterator<Component> it = menuItemsLayout.iterator(); it.hasNext();) {
							final Component c = it.next();
							if (c.getCaption() != null && c.getCaption().startsWith(item.getValue())) {
								c.addStyleName("selected");
								break;
							}
						}
						break;
					}
				}
				menu.removeStyleName("valo-menu-visible");
			}

		});

		// new FeederThread().start();

	}

	private boolean browserCantRenderFontsConsistently() {

		return getPage().getWebBrowser().getBrowserApplication().contains("PhantomJS")
				|| (getPage().getWebBrowser().isIE() && getPage().getWebBrowser().getBrowserMajorVersion() <= 9);
	}

	static boolean isTestMode() {
		return ((ProjektAzureAlertUI) getCurrent()).testMode;
	}

	CssLayout buildMenu() {

		// Add items
		menuItems.put("common", "Startseite");
		menuItems.put("settings", "Einstellungen");

		Image image = new Image(null, new ClassResource("/Unbenannt-1.png"));

		image.setWidth("80%");

		image.addStyleName("image-va");

		final HorizontalLayout top = new HorizontalLayout();
		top.setWidth("100%");
		top.setDefaultComponentAlignment(Alignment.MIDDLE_LEFT);
		top.addStyleName("valo-menu-title");
		menu.addComponent(top);
		// menu.addComponent(createThemeSelect());

		final Button showMenu = new Button("Menu", new ClickListener() {
			@Override
			public void buttonClick(final ClickEvent event) {
				if (menu.getStyleName().contains("valo-menu-visible")) {
					menu.removeStyleName("valo-menu-visible");
				} else {
					menu.addStyleName("valo-menu-visible");
				}
			}
		});
		showMenu.addStyleName(ValoTheme.BUTTON_PRIMARY);
		showMenu.addStyleName(ValoTheme.BUTTON_SMALL);
		showMenu.addStyleName("valo-menu-toggle");
		showMenu.setIcon(FontAwesome.LIST);
		menu.addComponent(showMenu);

		final Label title = new Label("<h3>Projekt <strong>Azure Alert</strong></h3>", ContentMode.HTML);
		title.setSizeUndefined();
		top.addComponent(title);
		top.setExpandRatio(title, 1);

		final MenuBar settings = new MenuBar();
		settings.addStyleName("user-menu");

		final MenuItem settingsItem = settings.addItem("David" + " " + "Sharma",
				new ClassResource("/profile-pic-300px.jpg"), null);
		settingsItem.addItem("Profil ändern", null);
		settingsItem.addItem("Einstellungen", null);
		settingsItem.addSeparator();
		settingsItem.addItem("Ausloggen", null);
		menu.addComponent(settings);

		menuItemsLayout.setPrimaryStyleName("valo-menuitems");
		menu.addComponent(menuItemsLayout);

		Label label = null;

		for (final Entry<String, String> item : menuItems.entrySet()) {
			if (item.getKey().equals("settings")) {
				label = new Label("Menü", ContentMode.HTML);
				label.setPrimaryStyleName("valo-menu-subtitle");
				label.addStyleName("h4");
				label.setSizeUndefined();
				menuItemsLayout.addComponent(label);
			}
			if (item.getKey().equals("panels")) {
				System.out.println(label.getValue());

				label = new Label("Containers", ContentMode.HTML);
				label.setPrimaryStyleName("valo-menu-subtitle");
				label.addStyleName("h4");
				label.setSizeUndefined();
				menuItemsLayout.addComponent(label);
			}

			menuButtons = new Button(item.getValue(), new ClickListener() {
				@Override
				public void buttonClick(final ClickEvent event) {
					navigator.navigateTo(item.getKey());
				}
			});

			menuButtons.setHtmlContentAllowed(true);
			menuButtons.setPrimaryStyleName("valo-menu-item");

			if (item.getValue().equals("Einstellungen")) {
				menuButtons.setIcon(FontAwesome.FOLDER);
			}

			if (item.getValue().equals("Startseite")) {
				menuButtons.setIcon(FontAwesome.DASHBOARD);
			}
			if (item.getValue().equals("Startseite")) {

				mainButton = menuButtons;
			}
			menuItemsLayout.addComponent(menuButtons);
			// count++;
		}

		// label.setValue(label.getValue() + " <span class=\"valo-menu-badge\">"
		// + count + "</span>");

		menu.addComponent(image);

		return menu;

	}

}

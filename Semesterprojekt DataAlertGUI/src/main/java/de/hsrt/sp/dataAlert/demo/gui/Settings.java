package de.hsrt.sp.dataAlert.demo.gui;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;

@SpringView(name = "Settings")
@Component
public class Settings extends VerticalLayout implements View {
	private final static Logger LOG = LoggerFactory.getLogger(Settings.class);

	private VerticalLayout row, row1, row3, row4;
	private GridLayout comboGrid = new GridLayout(2, 1);

	public Settings() {
		setMargin(true);
		addStyleName("content-common");
		comboGrid.setSpacing(true);
		Label h1 = new Label(" <strong>Einstellungen</strong>", ContentMode.HTML);
		h1.addStyleName("del-headline");
		addComponent(h1);
		setSpacing(true);

		row = new VerticalLayout();
		Label h2 = new Label("<strong>Server eingeben </strong> :", ContentMode.HTML);
		h2.setStyleName("h3");

		row.setSpacing(true);
		row.addComponent(h2);
		row.setWidth("100%");
		row.addComponent(comboGrid);

		row.addComponent(new Label("<hr />", ContentMode.HTML));

		row1 = new VerticalLayout();
		Label h3 = new Label("<strong>Test </strong> :", ContentMode.HTML);
		h3.setStyleName("h3");
		row1.addComponent(h3);
		row1.setWidth("100%");

		VerticalLayout row2 = new VerticalLayout();

		row2.setSpacing(true);
		// row2.addComponent(initSaveButton());
		// row2.addComponent(new Label(""));
		row2.addComponent(new Label("<hr />", ContentMode.HTML));

		row3 = new VerticalLayout();

		GridLayout gridSmoothing = new GridLayout(2, 1);
		row3.setWidth("100%");

		gridSmoothing.addComponent(new Label());

		gridSmoothing.setSpacing(true);

		row3.addComponent(new Label("<hr />", ContentMode.HTML));

		row4 = new VerticalLayout();

		addComponent(row);
		addComponent(row1);
		addComponent(row2);
		addComponent(row3);
		addComponent(row4);

	}

	@Override
	public void enter(final ViewChangeEvent event) {
		// TODO Auto-generated method stub
	}



}

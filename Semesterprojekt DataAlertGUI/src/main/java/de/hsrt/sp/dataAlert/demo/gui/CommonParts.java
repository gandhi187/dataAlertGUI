
package de.hsrt.sp.dataAlert.demo.gui;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.hibernate.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.JsonObject;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.vaadin.annotations.Push;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Sizeable;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Grid;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Slider;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.themes.ValoTheme;

import de.hsrt.sp.dataAlert.demo.configs.Transact;
import de.hsrt.sp.dataAlert.demo.model.SensorValues;
import de.hsrt.sp.dataAlert.demo.model.SensorValues.SensorId;
import de.steinwedel.messagebox.MessageBox;

@Push
@SpringView(name = "commonParts")
@Component

public class CommonParts extends VerticalLayout implements View {

	private Grid gridDeliveries = new Grid("");
	private Grid gridFault = new Grid("");

	@PersistenceContext
	EntityManager entityManager;

	protected Session getCurrentSession() {
		return entityManager.unwrap(Session.class);
	}

	Window notificationsWindow;
	private final static Logger LOG = LoggerFactory.getLogger(CommonParts.class);
	static boolean alreadyExecuted = false;



	private TextField txt;

	private VerticalLayout row, row1;
	private Transact trans = new Transact();
	private BeanItemContainer<SensorValues> container;

	private List<SensorValues> sensorValues = new ArrayList<SensorValues>();
	private Grid grid;

	public CommonParts() {

		container = new BeanItemContainer<SensorValues>(SensorValues.class, sensorValues);

		gridDeliveries.setIcon(FontAwesome.CALENDAR);
		gridFault.setIcon(FontAwesome.WARNING);
		gridFault.setColumnReorderingAllowed(true);

		grid = new Grid(container);
		setMargin(true);
		addStyleName("content-common");

		Label h1 = new Label(" <font size=\"20\"> <strong>Übersicht</strong></font>", ContentMode.HTML);
		Label h2 = new Label(
				" <font size=\"12\"> Bitte Maximalwert für den <strong>Temperatur-Sensor</strong> eingeben</font>",
				ContentMode.HTML);
		// txt = new TextField ();

		Button bt = new Button("Abschicken", FontAwesome.SEND);
		bt.addStyleName(ValoTheme.BUTTON_FRIENDLY);

		Button bt2 = new Button("Refresh", FontAwesome.REFRESH);

		Button discoMode = new Button("Disco-Time", FontAwesome.STAR);

		discoMode.addClickListener(new ClickListener() {

			@Override
			public void buttonClick(ClickEvent event) {
				// TODO Auto-generated method stub
				try {
					discoMode();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});

		Slider slider = new Slider();
		slider.setImmediate(true);
		slider.setMin(10);
		slider.setMax(80);
		slider.setWidth(500, Sizeable.UNITS_PIXELS);
		// slider.setHeight(300,Sizeable.UNITS_PIXELS);

		bt.addClickListener(new ClickListener() {

			@Override
			public void buttonClick(ClickEvent event) {
				JsonObject j = new JsonObject();
				// TODO Auto-generated method stub
				ObjectMapper mapper = new ObjectMapper();
				String jsonInString = "";
				try {
					jsonInString = mapper.writeValueAsString(slider.getValue());
				} catch (JsonProcessingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				String output = null;
				Client client = Client.create();
				WebResource webResource = client.resource("http://localhost:8888/maxValue");

				ClientResponse response = webResource.type("application/json").post(ClientResponse.class, jsonInString);

				MessageBox.createInfo().withCaption("Wert geändert")
						.withMessage("Der Maximalwert wurde auf " + slider.getValue() + " geändert").withOkButton()
						.open();
				addToList(slider.getValue());

				// }
			}
		});

		bt2.addClickListener(new ClickListener() {

			@Override
			public void buttonClick(ClickEvent event) {
				refreshGrid();

			}
		});

		h1.addStyleName("headline");
		addComponent(h1);
		addComponent(h2);
		addComponent(slider);
		addComponent(bt);
		addComponent(bt2);
		addComponent(discoMode);
		setSpacing(true);

		row = new VerticalLayout();
		row.setWidth("100%");
		row.setSpacing(true);
		addComponent(row);
		addComponent(grid);
		grid.setSizeFull();
		GridLayout buttonGrid = new GridLayout(5, 1);
		addComponent(buttonGrid);

		// initFooterRowDel(gridDeliveries, deliveryBtC);

	}

	@Override
	public void enter(final ViewChangeEvent event) {
		// TODO Auto-generated method stub
	}

	public void addToList(Double value) {

		SensorValues sv = new SensorValues();
		SensorId sI = new SensorId();

		sI.setMeasureUnitId("01");
		sI.setSensorId("01");
		sI.setSensorTypeId("01");
		sv.setDate(new Date());

		sv.setValue(value);
		sv.setSensorId(sI);
		sensorValues.add(sv);

		container = new BeanItemContainer<SensorValues>(SensorValues.class, sensorValues);
	}

	public void refreshGrid() {
		container = new BeanItemContainer<SensorValues>(SensorValues.class, sensorValues);
		grid.removeAllColumns();
		grid.setContainerDataSource(container);
		grid.markAsDirty();
		grid.clearSortOrder();

		grid.refreshRows(grid.getContainerDataSource().getItemIds());

	}

	public void discoMode() throws InterruptedException {

		while (true) {
			String json = "{\"color\":\"" + "green" + "\" }";
			Client client = Client.create();
			WebResource webResource = client.resource("http://134.103.109.177:5000/api/v1.0/colors");

			ClientResponse response = webResource.type("application/json").post(ClientResponse.class, json);
			Thread.sleep(100);

			json = "{\"color\":\"" + "red" + "\" }";
			webResource.type("application/json").post(ClientResponse.class, json);

			Thread.sleep(100);
			json = "{\"color\":\"" + "blue" + "\" }";

			webResource.type("application/json").post(ClientResponse.class, json);
		}
	}

}

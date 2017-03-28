package de.hsrt.sp.dataAlert.demo.restControllers;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.messaging.Source;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import de.hsrt.sp.dataAlert.demo.model.SensorValues;
import de.hsrt.sp.dataAlert.demo.model.SensorValues.SensorId;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@Api(value = "maxValue", description = "SemesterProjektMaxValue API")
@RequestMapping(value = "/maxValue")
@EnableBinding(Source.class)
@Component
public class ProjektAzureRestController {
	private final static Logger LOG = LoggerFactory.getLogger(ProjektAzureRestController.class);
	@PersistenceContext
	EntityManager entityManager;

	protected Session getCurrentSession() {
		return entityManager.unwrap(Session.class);
	}

	@ApiOperation(value = "", notes = "create new max value")
	@RequestMapping(value = "", method = RequestMethod.POST, consumes = { "application/json" })
	public List setMaxValue(@RequestBody double value) {
		Session session = getCurrentSession();
		List oldest = null;
		Transaction tx = null;
		try {
			session.beginTransaction();

			SensorValues sv = new SensorValues();
			SensorId sI = new SensorId();

			sI.setMeasureUnitId("01");
			sI.setSensorId("01");
			sI.setSensorTypeId("01");

			sv.setValue(value);
			sv.setSensorId(sI);

			session.update(sv);
			session.getTransaction().commit();

			session.disconnect();

		} catch (HibernateException e) {
			if (tx != null)
				tx.rollback();
			e.printStackTrace();
		} finally {
			session.close();
		}
		return oldest;
	}

}

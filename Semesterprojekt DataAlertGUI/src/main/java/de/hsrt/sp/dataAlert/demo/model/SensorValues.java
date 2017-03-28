package de.hsrt.sp.dataAlert.demo.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name = "MaxReferenceSensorValue")
public class SensorValues implements Serializable {

	@Transient
	private Date date;

	@EmbeddedId
	private SensorId sensorId;

	@Column
	private double Value;

	public SensorValues(Date dateTime, String measureUnitId, String sensorId, String sensorTypeId, double value) {
		super();
		// MeasureUnitId = measureUnitId;
		// SensorId = sensorId;
		// SensorTypeId = sensorTypeId;
		Value = value;
	}

	public SensorValues() {

	}

	public double getValue() {
		return Value;
	}

	public void setValue(double value) {
		Value = value;
	}

	public SensorId getSensorId() {
		return sensorId;
	}

	public void setSensorId(SensorId sensorId) {
		this.sensorId = sensorId;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	@Embeddable
	public static class SensorId implements Serializable {
		protected String MeasureUnitId;
		protected String SensorId;
		protected String SensorTypeId;

		public SensorId() {

		}

		@Override
		public String toString() {
			return MeasureUnitId + SensorId + SensorTypeId;
		}

		public SensorId(String measureUnitId, String sensorId, String sensorTypeId) {
			MeasureUnitId = measureUnitId;
			SensorId = sensorId;
			SensorTypeId = sensorTypeId;
		}

		public String getMeasureUnitId() {
			return MeasureUnitId;
		}

		public void setMeasureUnitId(String measureUnitId) {
			MeasureUnitId = measureUnitId;
		}

		public String getSensorId() {
			return SensorId;
		}

		public void setSensorId(String sensorId) {
			SensorId = sensorId;
		}

		public String getSensorTypeId() {
			return SensorTypeId;
		}

		public void setSensorTypeId(String sensorTypeId) {
			SensorTypeId = sensorTypeId;
		}

	}
}

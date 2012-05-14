package com.vgs.dashboard.model.bo;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import eu.hansolo.steelseries.gauges.AbstractGauge;

public class Instrument<T> implements Runnable {

	protected T currentValue;
	protected Task<T> task;
	// time interval between task executions (in milliseconds)
	protected Long frequency = 5000L; // default frequency is 5 seconds
	protected final PropertyChangeSupport pcs = new PropertyChangeSupport(this);
	protected final ScheduledExecutorService executor = Executors
			.newScheduledThreadPool(1);
	protected ScheduledFuture<?> executorHandler = null;
	protected AbstractGauge gauge;

	public Instrument(Task<T> t, Long freq, final AbstractGauge ag) {
		this.task = t;
		this.frequency = freq;
		this.gauge = ag;
		this.executorHandler = executor.scheduleAtFixedRate(this, 0, freq,
				TimeUnit.MILLISECONDS);
		this.addPropertyChangeListener(new PropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				if ("currentValue".equals(evt.getPropertyName())) {
					ag.setValue((Double) evt.getNewValue());
				}
			}
		});
	}

	public void addPropertyChangeListener(PropertyChangeListener listener) {
		this.pcs.addPropertyChangeListener(listener);
	}

	public void removePropertyChangeListener(PropertyChangeListener listener) {
		this.pcs.removePropertyChangeListener(listener);
	}

	public Long getFrequency() {
		return frequency;
	}

	public void setFrequency(Long frequency) {
		this.frequency = frequency;
	}

	public void run() {
		T oldValue = this.currentValue;
		this.currentValue = this.task.performTask();
		this.pcs.firePropertyChange("currentValue", oldValue,
				this.task.performTask());
	}

	public AbstractGauge getGauge() {
		return gauge;
	}

	public void setGauge(AbstractGauge gauge) {
		this.gauge = gauge;
	}

}

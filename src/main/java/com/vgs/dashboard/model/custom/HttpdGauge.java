package com.vgs.dashboard.model.custom;

import java.awt.Dimension;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import net.schmizz.sshj.SSHClient;
import net.schmizz.sshj.common.IOUtils;
import net.schmizz.sshj.connection.ConnectionException;
import net.schmizz.sshj.connection.channel.direct.Session;
import net.schmizz.sshj.connection.channel.direct.Session.Command;
import net.schmizz.sshj.transport.TransportException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.vgs.dashboard.model.bo.Instrument;
import com.vgs.dashboard.model.bo.Task;
import com.vgs.dashboard.model.exception.RuntimeModelException;

import eu.hansolo.steelseries.gauges.Radial1Vertical;
import eu.hansolo.steelseries.tools.BackgroundColor;
import eu.hansolo.steelseries.tools.ColorDef;
import eu.hansolo.steelseries.tools.ForegroundType;
import eu.hansolo.steelseries.tools.FrameDesign;
import eu.hansolo.steelseries.tools.FrameEffect;
import eu.hansolo.steelseries.tools.KnobStyle;
import eu.hansolo.steelseries.tools.KnobType;

public class HttpdGauge {

	private static final Log logger = LogFactory.getLog(HttpdGauge.class);
	private Instrument<Double> instrument;

	public HttpdGauge() {
		initialize();
	}

	public void initialize() {
		Task<Double> t1 = new Task<Double>() {
			public Double performTask() {
				String result = "";
				SSHClient ssh = new SSHClient();
				Session session = null;
				try {
					ssh.loadKnownHosts();
					ssh.connect("srvdmz2", 22);
					ssh.authPassword("jboss", "nolosabe");
					session = ssh.startSession();
					final Command cmd = session
							.exec("ps -ef | grep httpd | wc -l");
					result = IOUtils.readFully(cmd.getInputStream()).toString();
					cmd.join(3, TimeUnit.SECONDS);
					return new Double(result);
				} catch (Exception e) {
					logger.fatal("error", e);
					throw new RuntimeModelException(e);
				} finally {
					try {
						session.close();
						ssh.disconnect();
					} catch (TransportException te) {
						logger.fatal("Exception closing session", te);
					} catch (ConnectionException ce) {
						logger.fatal("Exception closing session", ce);
					} catch (IOException ioe) {
						logger.fatal("Exception closing session", ioe);
					}
				}

			}
		};

		final Radial1Vertical pg = new Radial1Vertical();
		pg.setPreferredSize(new Dimension(280, 280));
		pg.setHighlightArea(true);
		pg.setMinValue(0);
		pg.setMaxValue(40);
		pg.setThreshold(30d);
		pg.setTitle("apache");
		pg.setUnitString("procs");
		pg.setBackgroundColor(BackgroundColor.BEIGE);
		pg.setPointerColor(ColorDef.BLACK);
		pg.setFrameDesign(FrameDesign.ANTHRACITE);
		pg.setFrameEffect(FrameEffect.EFFECT_INNER_FRAME);
		pg.setForegroundType(ForegroundType.FG_TYPE4);
		pg.setKnobStyle(KnobStyle.BRASS);
		pg.setKnobType(KnobType.METAL_KNOB);

		this.instrument = new Instrument<Double>(t1, 1000L, pg);
	}

	public Instrument<Double> getInstrument() {
		return instrument;
	}

	public void setInstrument(Instrument<Double> instrument) {
		this.instrument = instrument;
	}

}

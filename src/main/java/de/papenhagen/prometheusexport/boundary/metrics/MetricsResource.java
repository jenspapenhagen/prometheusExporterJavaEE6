package de.papenhagen.prometheusexport.boundary.metrics;

import de.papenhagen.prometheusexport.service.CounterBean;
import de.papenhagen.prometheusexport.service.ServerWatch;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.enterprise.context.ApplicationScoped;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

/**
 * Prometheus Endpoint with some simple metrics
 *
 * this is a testrun for not use io.prometheus dependency For easyer and
 * complexer metics better use it. but for this very basic and small metrics its
 * fine
 *
 * @author Jens Papenhagen
 */
@Path("metrics")
@ApplicationScoped
@Produces(MediaType.TEXT_PLAIN)
public class MetricsResource {

    @Inject
    private ServerWatch watch;

    @Inject
    private CounterBean counterBean;

    private List<String> metricParts;

    private String value;

    /**
     * This is a basic metic
     *
     * @return Metric
     */
    @GET
    public String metric() {
        metricParts = new ArrayList<String>();

        List<Metric> metricList = new ArrayList<Metric>();
        metricList.add(usedmemory());
        metricList.add(availablememory());
        metricList.add(bootTime());
        metricList.add(systemLoadAverage());
        metricList.add(availableProcessors());
        metricList.add(osName());
        metricList.add(osArchitecture());
        metricList.add(osVersion());
        metricList.add(treadCount());
        metricList.add(preakTreadCount());
        metricList.add(classLoadedCount());

        //add new metics here        
        metricList.add(getHitCounter());

        List<String> output = new ArrayList<String>();
        for (Metric m : metricList) {
            fillMetric(m);
            output.add(toMetric());
        }

        //output without brackets and commas
        StringBuilder stringBuilder = new StringBuilder();
        for (String outputString : output) {
            stringBuilder.append(outputString);
        }

        return stringBuilder.toString();
    }

    private void fillMetric(Metric input) {
        String application = input.getApplication();
        String component = input.getComponent();
        String units = input.getUnits();
        String suffix = input.getSuffix();

        this.value = input.getValue();

        this.metricParts = Arrays.asList(application, component, units, suffix);
    }

    /**
     * Sample Counter of a REST CALL Count only as Example, very basic
     *
     * @return Metric
     */
    private Metric getHitCounter() {
        int hitCount = counterBean.getHits();
        Metric m = new Metric("prometheusexport", "metricscalls", "count", "total");
        m.setValue(String.valueOf(hitCount));
        return m;
    }

    /**
     * This is a basic metic for memory usage of the heap that is used for
     * object allocation.
     *
     * @return Metric
     */
    private Metric usedmemory() {
        Metric m = new Metric("prometheusexport", "jvmusedmemory", "bytes", "size");
        m.setValue(String.valueOf(this.watch.usedMemoryInMb()));
        return m;
    }

    /**
     * This is a basic metic for memory available of the heap
     *
     * @return Metric
     */
    private Metric availablememory() {
        Metric m = new Metric("prometheusexport", "jvmavailablememory", "bytes", "size");
        m.setValue(String.valueOf(this.watch.availableMemoryInMB()));
        return m;
    }

    /**
     * This methode give backt the starting time of this service
     *
     * @return Metric
     */
    private Metric bootTime() {
        Metric m = new Metric("prometheusexport", "bootup", "seconds", "time");
        m.setValue(String.valueOf(watch.getDateTime().toEpochSecond()));
        return m;
    }

    /**
     * this methode give back the System Load Average
     *
     * @return Metric
     */
    private Metric systemLoadAverage() {
        Metric m = new Metric("prometheusexport", "systemLoad", null, "systemLoad");
        m.setValue(this.watch.systemLoadAverage());
        return m;
    }

    /**
     * this methode give back the available Processors/cores
     *
     * @return Metric
     */
    private Metric availableProcessors() {
        Metric m = new Metric("prometheusexport", "availableProcessors", "count", "total");
        m.setValue(String.valueOf(this.watch.availableProcessors()));
        return m;
    }

    /**
     * this methode give back the Name of the underlaying Operation System
     *
     * @return Metric
     */
    private Metric osName() {
        Metric m = new Metric("prometheusexport", "osName", null, null);
        m.setValue(this.watch.osName());
        return m;
    }

    /**
     * this methode give back the Architecture of the underlaying Operation
     * System
     *
     * @return Metric
     */
    private Metric osArchitecture() {
        Metric m = new Metric("prometheusexport", "osArchitecture", null, null);
        m.setValue(this.watch.osArchitecture());
        return m;
    }

    /**
     * this methode give back the Verison of the underlaying Operation System
     *
     * @return Metric
     */
    private Metric osVersion() {
        Metric m = new Metric("prometheusexport", "osVersion", null, null);
        m.setValue(this.watch.osVersion());
        return m;
    }

    /**
     * this methode give back the Thread count of live threads including both
     * daemon and non-daemon threads.
     *
     * @return Metric
     */
    private Metric treadCount() {
        Metric m = new Metric("prometheusexport", "tread", "count", "total");
        m.setValue(String.valueOf(this.watch.treadCount()));
        return m;
    }

    /**
     * this methode give back the Thread peak count since the Java virtual
     * machine started or peak was reset.
     *
     * @return Metric
     */
    private Metric preakTreadCount() {
        Metric m = new Metric("prometheusexport", "tread", "count", "peak");
        m.setValue(String.valueOf(this.watch.peakTradCount()));
        return m;
    }

    /**
     * this methode give back the Class number of classes that are currently
     * loaded in the Java virtual machine.
     *
     * @return Metric
     */
    private Metric classLoadedCount() {
        Metric m = new Metric("prometheusexport", "classLoaded", "count", "total");
        m.setValue(String.valueOf(this.watch.classLoadedCount()));
        return m;
    }

    /**
     * this methode convert a strict Metric to a "Prometheus format" String
     *
     * @return a "Prometheus format" String
     */
    private String toMetric() {
        String separator = "_";
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < metricParts.size(); i++) {
            metricParts.get(i);
            if (metricParts.get(i) == null) {
                continue;
            }
            sb.append(metricParts.get(i));

            // if not the last item
            if (i != metricParts.size() - 1) {
                sb.append(separator);
            }
        }

        //reset value to null if not forund
        if (value == null) {
            value = "";
        }

        return sb.toString() + " " + value + "\n";
    }

}

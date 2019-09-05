package de.papenhagen.prometheusexport;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

/**
 * Configures a JAX-RS endpoint. Delete this class, if you are not exposing
 * JAX-RS resources in your application.
 * @author adam-bien.com
 */
@ApplicationPath("/")
public class RESTConfiguration extends Application {
}

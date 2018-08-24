package org.viessmann.datapoint.LinkController.config;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

import org.viessmann.datapoint.LinkController.rest.StatusResource;

@ApplicationPath("/resources")
public class RestConfig extends Application {
    public Set<Class<?>> getClasses() {
    	return new HashSet<Class<?>>(Arrays.asList(StatusResource.class));
    }
}
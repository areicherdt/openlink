package org.viessmann.datapoint.LinkController.config;
import static spark.Spark.*;

import org.viessmann.datapoint.LinkController.rest.StandardResponse;
import org.viessmann.datapoint.LinkController.rest.StatusResponse;

import com.google.gson.Gson;

public class SparkConfig {
	
	public static void load() {
	    exception(Exception.class, (e, req, res) -> {
	    	res.status(500);
	    	res.body(new Gson().toJson(new StandardResponse(StatusResponse.ERROR, 
	    			String.format("%s - %s", e.getClass(), e.getMessage()))));
	    });
		port(8090);
	}

	public static void enableCORS(final String origin, final String methods, final String headers) {

		options("/*", (request, response) -> {

			String accessControlRequestHeaders = request.headers("Access-Control-Request-Headers");
			if (accessControlRequestHeaders != null) {
				response.header("Access-Control-Allow-Headers", accessControlRequestHeaders);
			}

			String accessControlRequestMethod = request.headers("Access-Control-Request-Method");
			if (accessControlRequestMethod != null) {
				response.header("Access-Control-Allow-Methods", accessControlRequestMethod);
			}

			return "OK";
		});

		before((request, response) -> {
			response.header("Access-Control-Allow-Origin", origin);
			response.header("Access-Control-Request-Method", methods);
			response.header("Access-Control-Allow-Headers", headers);
			// Note: this may or may not be necessary in your particular application
			response.type("application/json");
		});
	}

}

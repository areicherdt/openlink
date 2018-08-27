package org.viessmann.datapoint.LinkController.rest;

import static spark.Spark.get;

import org.viessmann.datapoint.LinkController.connect.InterfaceController;
import org.viessmann.datapoint.LinkController.connect.InterfaceFactory;

import com.google.gson.Gson;


public class InterfaceService {
	
	private static final String RETURN_TYPE = "application/json";
	
	private InterfaceController interfaceController;
	
	public InterfaceService(InterfaceController interfaceController) {
		this.interfaceController = interfaceController;
		init();
	}

	
	private void init() {	
		
		get("/status/ports",(req, res)-> {
			res.type(RETURN_TYPE);
			return new Gson().toJson(
					new StandardResponse(StatusResponse.SUCCESS, new Gson()
							.toJsonTree(InterfaceFactory.listCommPorts())));
		});
		
		get("/status/:port",(req, res) -> {
			res.type(RETURN_TYPE);
			String port = req.params(":port");
			return new Gson().toJson(
					new StandardResponse(StatusResponse.SUCCESS, new Gson()
							.toJsonTree(interfaceController.interfaceStatus(port))));
		});
		
		get("/interface/create/:port", (req, res) -> {
			res.type(RETURN_TYPE);
			String port = req.params(":port");
			try {
				interfaceController.createInterface(port);
				return new Gson().toJson(
						new StandardResponse(StatusResponse.SUCCESS));
			}
			catch(Exception e) {
				return new Gson().toJson(
						new StandardResponse(StatusResponse.ERROR, e.getMessage()));	
			}
		});
		
		get("/interface/close/:port", (req, res) -> {
			res.type(RETURN_TYPE);
			String port = req.params(":port");
			try {
				interfaceController.closeInterface(port);
				return new Gson().toJson(
						new StandardResponse(StatusResponse.SUCCESS));
			}
			catch(Exception e) {
				return new Gson().toJson(
						new StandardResponse(StatusResponse.ERROR, e.getMessage()));	
			}
		});
		
	}
}

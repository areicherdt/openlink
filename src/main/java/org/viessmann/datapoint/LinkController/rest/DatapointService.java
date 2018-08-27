package org.viessmann.datapoint.LinkController.rest;

import static spark.Spark.get;

import org.viessmann.datapoint.LinkController.connect.InterfaceController;
import org.viessmann.datapoint.LinkController.connect.SerialInterface;
import org.viessmann.datapoint.LinkController.format.ValueFormatter;
import org.viessmann.datapoint.LinkController.protocol.Protocol;
import org.viessmann.datapoint.LinkController.protocol.Type;
import org.viessmann.datapoint.LinkController.protocol.ViessmannKWProtocol;

import com.google.gson.Gson;

public class DatapointService {

	private static final String RETURN_TYPE = "application/json";

	private InterfaceController interfaceController;
	
	private Protocol protocolService;
	
	public DatapointService(InterfaceController interfaceController) {
		this.interfaceController = interfaceController;
		this.protocolService = new ViessmannKWProtocol();
		init();
	}
	
	private void init() {
		
		get("/read/:port/:datapoint",(req, res)-> {
			res.type(RETURN_TYPE);
			
			String port = req.params(":port");
			//String protocol = req.params(":protocol");
			String datapoint = req.params(":datapoint");
			
			SerialInterface serialInterface = interfaceController.getOrCreateInterface(port);
			byte[] result = protocolService.readData(serialInterface, Integer.parseInt(datapoint, 16), Type.SHORT);

			return new Gson().toJson(
					new StandardResponse(StatusResponse.SUCCESS, new Gson()
							.toJsonTree(ValueFormatter.formatByteValues(result, Type.SHORT, 10))));
		});
		
		get("/read/:datapoint",(req,res) -> {
			res.type(RETURN_TYPE);
			
			String datapoint = req.params(":datapoint");		
			SerialInterface serialInterface = interfaceController.getFirstInterface();
			
			if(serialInterface!=null) {
				byte[] result = protocolService.readData(serialInterface, Integer.parseInt(datapoint, 16), Type.SHORT);
				return new Gson().toJson(
						new StandardResponse(StatusResponse.SUCCESS, new Gson()
								.toJsonTree(ValueFormatter.formatByteValues(result, Type.SHORT, 10))));
			} else {
				return new Gson().toJson(
						new StandardResponse(StatusResponse.ERROR, "no default interface defined."));
			}			
		});
		
		
	}
}

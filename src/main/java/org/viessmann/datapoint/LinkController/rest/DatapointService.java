package org.viessmann.datapoint.LinkController.rest;

import static spark.Spark.get;

import java.util.List;

import org.viessmann.datapoint.LinkController.controller.CacheController;
import org.viessmann.datapoint.LinkController.controller.ProtocolController;
import org.viessmann.datapoint.LinkController.model.Datapoint;

import com.google.gson.Gson;

public class DatapointService {

	private static final String RETURN_TYPE = "application/json";
	private ProtocolController controller;
	private CacheController cache;
	
	public DatapointService(ProtocolController controller, List<Datapoint> datapoints, CacheController cache) {
		this.controller = controller;
		this.cache = cache; 
		init(datapoints);
	}
	
	private void init(List<Datapoint> datapoints) {
			
		datapoints.stream().forEach(dp -> {
			String ch = dp.getChannel();
			if(dp.isCache()) {
				cache.put(ch, "");
			}	
			get("/read/"+dp.getChannel(), (req,res) -> {
				String result = controller.readAddress(dp.getAddress(), dp.getType());
				if(dp.isCache()) {
					cache.put(ch, result);
				}		
				return new Gson().toJson(
						new StandardResponse(StatusResponse.SUCCESS, new Gson()
						.toJsonTree(result)));
			});
		});
		
		get("/read/:address/:type",(req,res) -> {
			res.type(RETURN_TYPE);
			
			String address = req.params(":address");
			String type = req.params(":type");
			String result = controller.readAddressWithStringType(address, type);

			return new Gson().toJson(
					new StandardResponse(StatusResponse.SUCCESS, new Gson()
					.toJsonTree(result)));
		});
		
		get("/read/cache",(req,res) -> {
			res.type(RETURN_TYPE);
			return new Gson().toJson(
					new StandardResponse(StatusResponse.SUCCESS, new Gson()
					.toJsonTree(cache.readAll())));
		});
		
		
	}
}

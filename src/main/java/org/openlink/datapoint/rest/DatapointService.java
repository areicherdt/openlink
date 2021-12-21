package org.openlink.datapoint.rest;

import static spark.Spark.get;

import java.util.List;

import org.openlink.datapoint.controller.CacheController;
import org.openlink.datapoint.controller.ProtocolController;
import org.openlink.datapoint.model.Datapoint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.gson.Gson;

@Component
public class DatapointService {

	private static final String RETURN_TYPE = "application/json";

	@Autowired
	public DatapointService(ProtocolController controller, CacheController cache, List<Datapoint> datapoints) {
			
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

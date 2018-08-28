package org.viessmann.datapoint.LinkController.db;

import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.influxdb.InfluxDB;
import org.influxdb.InfluxDBFactory;
import org.influxdb.dto.Point;
import org.influxdb.dto.Pong;
import org.influxdb.dto.Query;
import org.influxdb.dto.QueryResult;
import org.influxdb.InfluxDB.LogLevel;
import org.slf4j.LoggerFactory;
import org.viessmann.datapoint.LinkController.config.model.Database;

import ch.qos.logback.classic.Logger;

public class InfluxService {

	static Logger logger = (Logger) LoggerFactory.getLogger(InfluxService.class);
	
	private InfluxDB db;
	private String dbname;
	
	@SuppressWarnings("deprecation")
	public InfluxService(Database dbconfig) {
		this.dbname = dbconfig.getDbname();
		
		if(!dbconfig.getUser().isEmpty() && !dbconfig.getPassword().isEmpty()) {
			db = InfluxDBFactory.connect(dbconfig.getUrl(), 
					dbconfig.getUser(), dbconfig.getPassword());
		} else {
			db = InfluxDBFactory.connect(dbconfig.getUrl());
		}
		
		Pong response = db.ping();
		if (response.getVersion().equalsIgnoreCase("unknown")) {
			String msg = "error pinging db server.";
			logger.error(msg);
			throw new RuntimeException(msg);
		}
		
		if(!db.databaseExists(dbname)) {
			db.createDatabase(dbname);
			db.createRetentionPolicy("defaultPolicy", 
					dbname, 
					dbconfig.getRetention(), 
					1, true);
		}
		
		db.setLogLevel(LogLevel.NONE);
		db.setRetentionPolicy("defaultPolicy");
		db.setDatabase(dbname);
		
		logger.info("database ready. version {}", db.version());
	}
	
	public void writeDataPoint(String table, Map<String, Object> fieldMap) {
		
		Point point = Point.measurement(table)
				.time(System.currentTimeMillis(), TimeUnit.MILLISECONDS)
				.fields(fieldMap)
				.build();
		
		db.write(point);
	}
	
	public QueryResult queryDatabase(String query) {
		Query queryObject  = new Query(query, dbname);
		return db.query(queryObject);
	}
	
	
}

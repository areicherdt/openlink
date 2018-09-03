package org.viessmann.datapoint.LinkController.database;

import java.util.concurrent.TimeUnit;

import org.influxdb.InfluxDB;
import org.influxdb.InfluxDB.LogLevel;
import org.influxdb.InfluxDBFactory;
import org.influxdb.dto.Point;
import org.influxdb.dto.Pong;
import org.influxdb.dto.Query;
import org.influxdb.dto.QueryResult;
import org.junit.Before;
import org.junit.Test;

public class InfluxTest {

	private static final String dbName = "unitTest";
	
	private InfluxDB db;
	
	@SuppressWarnings("deprecation")
	@Before
	public void init() {
		db = InfluxDBFactory.connect("http://localhost:8086");
		if(db.databaseExists(dbName)) {
			db.deleteDatabase(dbName);
		}
		db.createDatabase(dbName);
		db.createRetentionPolicy("defaultPolicy", dbName, "52w", 1, true);
		db.setLogLevel(LogLevel.NONE);
	}
	
	//@Test
	public void testPing() {
		Pong response = db.ping();
		if (response.getVersion().equalsIgnoreCase("unknown")) {
		    System.out.println("Error pinging server.");
		} 
	}
	
	//@Test 
	public void writeData() throws InterruptedException {
		
		db.setDatabase(dbName);
		db.setRetentionPolicy("defaultPolicy");
		
		for (int i = 0; i < 10; i++) {
			Point point = Point.measurement("temperatures")
					.time(System.currentTimeMillis(),TimeUnit.MILLISECONDS)
					.addField("outside", 20.0 + Math.random())
					.build();
			
			db.write(point);
			Thread.sleep(2);
		}
		
		Thread.sleep(1000);
	}
	
	//@Test
	public void queryData() throws InterruptedException {
		writeData();
		Query queryObject = new Query("select * from temperatures order by time desc", dbName);
		QueryResult result = db.query(queryObject);
		
		result.getResults().forEach(System.out::println);
		
	}
}



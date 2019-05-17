package com.test.logic;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
//import java.text.SimpleDateFormat;
//import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.scheduling.annotation.Scheduled;

import com.apollographql.apollo.ApolloCall;
import com.apollographql.apollo.ApolloClient;
import com.apollographql.apollo.api.Response;
import com.apollographql.apollo.exception.ApolloException;
import com.test.SaveSensorAndSnapshotValueByIdentifierMutation;
import com.test.client.Connection;
import com.test.data.model.Setting;
import com.test.data.repository.SettingRepository;
import com.test.j2mod.J2modLibrary;

public class SensorReading extends JdbcDaoSupport {
	@Autowired
	private J2modLibrary j2modLibrary;

    @Autowired
    private ApolloClient apolloClient;

    @Autowired
    private SettingRepository settingRepository;

    @Autowired
    private Connection connection;

    ApolloCall<SaveSensorAndSnapshotValueByIdentifierMutation.Data> sensorMutationCall;
    
//	/**
//	 * Sensor con identifier = 6 (pH for verifier with name 'tanque 8')
//	 */
//	private static final String SELECT_SENSOR =
//	    " SELECT s.* "
//		+ " FROM Sensor s "
//		+ "WHERE s.identifier = ? ";

    /**
     * Updates the values of a sensor.
     */
    private static final String UPDATE_SENSOR_VALUES = 
    	" UPDATE Sensor s "
		+ "  SET lastReading = ?, "
        + "      lastReadingStamp = ? "
        + "WHERE s.identifier = ?";

    /**
     * Updates the values of a snapshot.
     */
    private static final String UPDATE_SNAPSHOT_VALUES = 
    	" UPDATE VerifierAttributeSnapshot vs "
        + "  SET lastReading = ?, "
    	+ "      lastReadingStamp = ? "
        + " FROM Sensor s "
    	+ "WHERE s.verifierId = vs.verifierId "
        + "  AND s.attributeId = vs.attributeId "
    	+ "  AND s.identifier = ?";

//	@Autowired
//	private SensorRepository sensorRepository;
//
//	@Autowired
//	private VerifierRepository verifierRepository;
//
//	@Autowired
//	private VerifierAttributeSnapshotRepository verifierAttributeSnapshotRepository;

	// PH & TEMP ADDRESS 6
    // Every minute on second 0
    @Scheduled(cron = "0 * * ? * *")//(fixedDelay = 1000 * 60)
	private Boolean readAndSaveAddress6() {

    	Integer sensorAddress = 6;
		Boolean result = false;

		if (readSensor(sensorAddress)) {
	    	Float[] phRegistersForAddress6 = j2modLibrary.readMultipleRegister(sensorAddress, 0, 4);
	//		for (Float phRegister : phRegistersForAddress6) {
	
	    	logger.info("Address " + sensorAddress + "[0]: " + phRegistersForAddress6.length + " " + phRegistersForAddress6[0]);
			result = saving(phRegistersForAddress6[0], "6");
	
			logger.info("Address " + sensorAddress + "[1]: " + phRegistersForAddress6.length + " " + phRegistersForAddress6[1]);
			result = saving(phRegistersForAddress6[1], "7");
	//		VerifierAttributeSnapshot vas = verifierAttributeSnapshotRepository.findByVerifierAndAttribute(v, a).orElseGet(() -> {
	//			VerifierAttributeSnapshot vas2 = new VerifierAttributeSnapshot();
	//			vas2.setAttribute(a);
	//			vas2.setVerifier(v);
	//			return vas2;
	//		});
	//		vas.setLastReading(BigDecimal.valueOf(phRegistersForAddress6[0]));
	//		vas.setLastReadingSt(LocalDateTime.now().toInstant(ZoneOffset.ofHours(-5)));
	//		verifierAttributeSnapshotRepository.save(vas);
			
			
	//		}
		} else {
			logger.error("SENSOR " + sensorAddress + " NOT ABLE TO READ" );
		}

		return result;
	}

	// PH & TEMP ADDRESS 1
    // Every minute on second 5
    @Scheduled(cron = "5 * * ? * *")
	private Boolean readAndSaveAddress1() {

    	Integer sensorAddress = 1;
		Boolean result = false;

		if (readSensor(sensorAddress)) {
	    	Float[] phRegistersForAddress1 = j2modLibrary.readMultipleRegister(sensorAddress, 0, 4);
	
	    	logger.info("Address " + sensorAddress + "[0]: " + phRegistersForAddress1.length + " " + phRegistersForAddress1[0]);
			result = saving(phRegistersForAddress1[0], "0");
	
			logger.info("Address " + sensorAddress + "[1]: " + phRegistersForAddress1.length + " " + phRegistersForAddress1[1]);
			result = saving(phRegistersForAddress1[1], "1");
		} else {
			logger.error("SENSOR " + sensorAddress + " NOT ABLE TO READ" );
		}

		return result;
	}

	// OXYGEN ADDRESS 2
    // Every minute on second 10
    @Scheduled(cron = "10 * * ? * *")
	private Boolean readAndSaveAddress2() {

    	Integer sensorAddress = 2;
		Boolean result = false;

		if (readSensor(sensorAddress)) {
	    	Float[] oxyRegistersForAddress2 = j2modLibrary.readInputRegister(sensorAddress, 0, 4);
	
	    	logger.info("Address " + sensorAddress + "[0]: " + oxyRegistersForAddress2.length + " " + oxyRegistersForAddress2[0]);
			result = saving(oxyRegistersForAddress2[0], "2");
	
			logger.info("Address " + sensorAddress + "[1]: " + oxyRegistersForAddress2.length + " " + oxyRegistersForAddress2[1]);
			result = saving(oxyRegistersForAddress2[1], "3");
		} else {
			logger.error("SENSOR " + sensorAddress + " NOT ABLE TO READ" );
		}

		return result;
	}

	// OXYGEN ADDRESS 7
    // Every minute on second 15
    @Scheduled(cron = "15 * * ? * *")
	private Boolean readAndSaveAddress7() {

    	Integer sensorAddress = 7;
		Boolean result = false;

		if (readSensor(sensorAddress)) {
	    	Float[] oxyRegistersForAddress7 = j2modLibrary.readInputRegister(sensorAddress, 0, 4);
	
	    	logger.info("Address " + sensorAddress + "[0]: " + oxyRegistersForAddress7.length + " " + oxyRegistersForAddress7[0]);
			result = saving(oxyRegistersForAddress7[0], "8");
	
			logger.info("Address " + sensorAddress + "[1]: " + oxyRegistersForAddress7.length + " " + oxyRegistersForAddress7[1]);
			result = saving(oxyRegistersForAddress7[1], "9");
		} else {
			logger.error("SENSOR " + sensorAddress + " NOT ABLE TO READ" );
		}

		return result;
	}

	// TEMP & OXY ADDRESS 10
    // Every 2 minutes on second 20
    @Scheduled(cron = "20 * * ? * *")
	private Boolean readAndSaveAddress10() {

    	Integer sensorAddress = 10;
		Boolean result = false;

		if (readSensor(sensorAddress)) {
	    	if (j2modLibrary.writeRegister(sensorAddress, 1, 31).equals("31")) {
				// Wait 1 second because this sensor must wait 400 milliseconds after input command to be able to read measures.
				try {
				    Thread.sleep(2000);
				} catch(InterruptedException ex) {
				    Thread.currentThread().interrupt();
				}
		    	Float[] tempRegistersForAddress10 = j2modLibrary.readMultipleRegister(sensorAddress, 83, 8);
	
		    	logger.info("Address " + sensorAddress + "[0]: " + tempRegistersForAddress10[0]);
				result = saving(tempRegistersForAddress10[0], "10");
		
				logger.info("Address " + sensorAddress + "[1]: " + tempRegistersForAddress10[1]);
				result = saving(tempRegistersForAddress10[1], "11");
	
				logger.info("Address " + sensorAddress + "[2]: " + tempRegistersForAddress10[2]);
				result = saving(tempRegistersForAddress10[2], "12");
	
				logger.info("Address " + sensorAddress + "[3]: " + tempRegistersForAddress10[3]);
				result = saving(tempRegistersForAddress10[3], "13");
	    	}
		} else {
			logger.error("SENSOR " + sensorAddress + " NOT ABLE TO READ" );
		}

		return result;
	}

    /**
     * Saves on local and remote(cloud) DATABASES.
     * @param reading
     * @param identifier
     * @return true if completed, false otherwise.
     */
    private Boolean saving(Float reading, String identifier) {

		if (reading != null) {
			Date now = new Date();
			try {
	            // Update the lastReading property with the value sent
	            // Update the lastReadingStamp with the TIME query parameter sent
	            int rowsSensor = getJdbcTemplate().update(
	                UPDATE_SENSOR_VALUES,
	                // Set values
	                reading, now,
	                // Where values
	                identifier
	            );
	
	            if (rowsSensor <= 0) {
	                throw new EmptyResultDataAccessException(rowsSensor);
	            }
	        } catch (EmptyResultDataAccessException e) {
	            logger.error("Sensor with identifier \"" + identifier + "\" NOT FOUND: " + e.getLocalizedMessage());
	            return false;
	        }
			
			
	        try {
	            int rowsSnapshots = getJdbcTemplate().update(
	                UPDATE_SNAPSHOT_VALUES,
	                // Set values
	                reading, now,
	                // Where values
	                identifier
	            );
	
	            if (rowsSnapshots <= 0) {
	                throw new EmptyResultDataAccessException(rowsSnapshots);
	            }
	        } catch (EmptyResultDataAccessException e) {
	            logger.error("SNAPSHOT of sensor with identifier=" + identifier + " NOT FOUND: " + e.getLocalizedMessage());
	            return false;
	        }
	        

            if (connection.loggedIn()) {
				SaveSensorAndSnapshotValueByIdentifierMutation saveSensor = 
					SaveSensorAndSnapshotValueByIdentifierMutation.builder()
					.lastReading(reading)
					.lastReadingStamp(now.toInstant())
					.identifier(identifier)
					.build();
				
				sensorMutationCall = apolloClient
					.mutate(saveSensor);
				
				sensorMutationCall.enqueue(new ApolloCall.Callback<SaveSensorAndSnapshotValueByIdentifierMutation.Data>() {
	
					@Override
					public void onResponse(Response<SaveSensorAndSnapshotValueByIdentifierMutation.Data> response) {
						logger.error(" SENSOR SAVED DATA: " + response.data());
					}
	
					@Override
					public void onFailure(ApolloException e) {
						logger.error(" SENSOR SAVED ERROR: " + e);
					}
	
				});
            } else {
                logger.error("Values not sent to cloud. Not logged in.");
                
                try {
                	LocalDateTime now2 = LocalDateTime.now();
                	logger.error("Sending values via HTTP");
					sendByRest(reading, now2, identifier);
				} catch (ClientProtocolException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
            }

		} else {
			logger.error("NULL VALUES ON READING ARRAY");
			return false;
		}
		return true;
    }

	/**
	 * Reads the setting corresponding to the identifier of the sensor.
	 * If you don't want to read the sensor, set manually to false and restart the service (jar).
	 * 
	 * If true, the sensor with this identifier is going to be read.
	 * 
	 * @param sensorAddress The identifier's value of the sensor. 
	 * @return true or false, depending on the setting record.
	 */
    private Boolean readSensor(Integer sensorAddress) {

    	String settingName = "readSensor" + sensorAddress;
  	    Setting sensorSetting = settingRepository.findByName(settingName).orElseGet(() -> {
  	  	  Setting s = new Setting();
  	  	  s.setName(settingName);
  	  	  return s;
  	    });
  	    if (sensorSetting.getValue() == null) {
  	    	sensorSetting.setValue("false");
  	    }
  	    sensorSetting.setUpdatdOn(LocalDateTime.now());
  	    settingRepository.save(sensorSetting);

    	return Boolean.parseBoolean(sensorSetting.getValue());
    }
    
    
    @Value("${httpUrl:null}")
    private String httpURL;
    
    public void sendByRest(Float reading, LocalDateTime now, String identifier) throws ClientProtocolException, IOException {
	    
    	int timeout = 30 * 1000; // 30 seconds
    	RequestConfig requestConfig = RequestConfig.custom()
    	        .setConnectTimeout(timeout)
    	        .setConnectionRequestTimeout(timeout)
    	        .setSocketTimeout(timeout).build();
    	
    	CloseableHttpClient client = HttpClients.custom()
    	        .setDefaultRequestConfig(requestConfig).build();//createDefault();
	    HttpPost httpPost = new HttpPost(httpURL + "/saveSensorByIdentifier");

//	    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss");
	    String json = "{\"lastReading\":" + reading + ",\"lastReadingStamp\":\"" + now + "\", \"identifier\": " + identifier + "}\"";
	    StringEntity entity = new StringEntity(json);
	    httpPost.setEntity(entity);
	    httpPost.setHeader("Accept", "application/json");
	    httpPost.setHeader("Content-type", "application/json");
	 
	    CloseableHttpResponse response = client.execute(httpPost);
	    System.out.println(response.getStatusLine().getStatusCode());
	    client.close();
	}
}

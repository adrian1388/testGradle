package com.test.logic;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.validation.Valid;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.test.j2mod.J2modLibrary;
import com.test.data.model.Sensor;
import com.test.data.model.VerifierAttributeSnapshot;
import com.test.data.repository.SensorRepository;
import com.test.data.repository.VerifierAttributeSnapshotRepository;

//import org.springframework.web.bind.annotation.CrossOrigin;


@Controller
public class SensorController {

	@Autowired
	private SensorRepository sensorRepository;

	@Autowired
	private VerifierAttributeSnapshotRepository snapshotRepository;

	@Autowired
	private J2modLibrary j2modLibrary;

	@GetMapping("/writeSalinity")
    public String getSensor(
    	@RequestParam(
    		name="address",
    		required=false
    	) Integer address,

    	@RequestParam(
    		name="ref",
    		required=false
    	) Integer ref,

    	@RequestParam(
    		name="value",
    		required=true
    	) Integer value,
    	
    	Model model
    ) {
		String returnValue = "";
		if (address == null) {
			address = 10;
		}
		if (ref == null) {
			ref = 97;
		}
		if (value == null) {
			value = 26;
		}
		if (j2modLibrary.writeRegister(address, ref, value).equals(value.toString())) {
			returnValue = "correctly written";
		} else {
			returnValue = "Error. Check logs";
		}

		model.addAttribute("returnValue", returnValue);
        return "WriteSalinity";
    }


    @GetMapping("/getSensor")
    public String getSensor(@RequestParam(name="sensor", required=false) String sensor, Model model) {
        model.addAttribute("sensor", sensor);
        List<String> verifiers = new ArrayList<String>();
        List<String> attributes = new ArrayList<String>();
        List<VerifierAttributeSnapshot> snaps = snapshotRepository.findAll();
        model.addAttribute("snaps", snaps);
System.out.println("snaps read");
        for (VerifierAttributeSnapshot snapshot : snaps) {

        	if (snapshot.getVerifier() != null && !verifiers.contains(snapshot.getVerifier().getName())) {
        		verifiers.add(snapshot.getVerifier().getName());
        	}
        	if (snapshot.getAttribute() != null && !attributes.contains(snapshot.getAttribute().getName())) {
        		attributes.add(snapshot.getAttribute().getName());
        	}
		}
        model.addAttribute("verifiers", verifiers);
        model.addAttribute("attributes", attributes);
        
        return "getSensors";
    }

    public void sendByRest(BigDecimal bigDecimal, LocalDateTime now, int i) throws ClientProtocolException, IOException {
    	

    	int timeout = 30 * 1000; // 30 seconds
    	RequestConfig requestConfig = RequestConfig.custom()
    	        .setConnectTimeout(timeout)
    	        .setConnectionRequestTimeout(timeout)
    	        .setSocketTimeout(timeout).build();
    	
    	CloseableHttpClient client = HttpClients.custom()
    	        .setDefaultRequestConfig(requestConfig).build();//createDefault();
	    HttpPost httpPost = new HttpPost("http://192.168.0.120:8080/saveSensorByIdentifier");
	    

	    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss'Z'");
	    String json = "{\"lastReading\":" + bigDecimal + ",\"lastReadingStamp\":\"" + now + "\", \"identifier\": " + i + "}\"";
	    StringEntity entity = new StringEntity(json);
	    httpPost.setEntity(entity);
	    httpPost.setHeader("Accept", "application/json");
	    httpPost.setHeader("Content-type", "application/json");
	 
	    CloseableHttpResponse response = client.execute(httpPost);
	    System.out.println(response.getStatusLine().getStatusCode());
	    client.close();
	}
}


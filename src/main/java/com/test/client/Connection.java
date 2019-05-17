package com.test.client;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import lombok.Data;

/**
 * Spring Component implemented to login and logout from the cloud server.
 * 
 * This is done in order to keep Cookies and CSRF Token for the subsequent Graphql queries.
 * 
 * @author hmosquera
 */
@Data
@Component
public class Connection {

    protected final Log logger = LogFactory.getLog(getClass());

    @Value("${httpUrl:null}")
    private String stringUrl;

    /**
     * CSRF Token
     */
    private String token;

    /**
     * Cookies. Contains a session id (JSESSIONID) and other parameters.
     */
    List<String> cookies;

    /**
     * Sets a HTTP POST connection to login to the cloud server.
     * 
     * This sets {@link Connection#getCookies()} and {@link Connection#getToken()}.
     */
    public void login(String username, String password) {
        String charset = "UTF-8";
        HttpURLConnection con = setUrlConnection("login");

        con.setRequestProperty("Accept-Charset", charset);
        con.setRequestProperty("Content-Type", "application/x-www-form-urlencoded;charset=" + charset);

        // TODO User hard coded
        Map<String, String> parameters = new HashMap<>();
        parameters.put("userId", username);
        parameters.put("password", password);
        parameters.put("remember-me", "false");

        con.setDoOutput(true);
        DataOutputStream out;

        // POST send
        try {
            out = new DataOutputStream(con.getOutputStream());
            out.writeBytes(getParamsString(parameters));
            out.flush();
            out.close();

            this.setCookies(con.getHeaderFields().get("Set-Cookie"));

            String json_response = "";
            InputStreamReader in = new InputStreamReader(con.getInputStream());
            BufferedReader br = new BufferedReader(in);
            String text = "";
            while ((text = br.readLine()) != null) {
              json_response += text;
            }

            // Set Token from the Response without any other string.
            this.setToken(json_response.split(":")[1].replace("\"", "").replace("}", ""));

        } catch (IOException e1) {
            e1.printStackTrace();
        }
    }

    /**
     * Sets a HTTP POST connection to logout from the cloud server.
     * 
     * This sets {@link Connection#getCookies()} and {@link Connection#getToken()} to null.
     */
    public void logout() {
        HttpURLConnection con = setUrlConnection("logout");

        con.setRequestProperty("X-CSRF-TOKEN", this.getToken());
        for (String cookie : this.getCookies()) {
            con.addRequestProperty("Cookie", cookie.split(";", 2)[0]);
        }

        int status = 0;

        con.setDoOutput(true);
        DataOutputStream out;

        // POST send
        try {
            out = new DataOutputStream(con.getOutputStream());
            out.flush();
            out.close();

            status = con.getResponseCode();

            // Nulling cookies and token if logout successful
            if (200 <= status && status <= 210) {
                this.setToken(null);
                this.setCookies(null);
                logger.info(" ****** LOGGEDOUT ****** ");
            } else {
            	logger.error(" Error logging out. Status of request: " + status);
            }

        } catch (IOException e1) {
        	logger.error(" Error logging out. Status of request: " + status);
            e1.printStackTrace();
        }
        
    }

    /**
     * @return True if Cookies and CSRF Token are not null. False otherwise. 
     */
    public Boolean loggedIn() {

        if (this.getCookies() != null && this.getToken() != null) {

            logger.debug("COOKIES   : " + this.getCookies());
            logger.debug("CSRF TOKEN: " + this.getToken());
            logger.debug(" ****** LOGGEDIN ****** ");
            return true;
        } else {
            logger.debug(" ****** NOT LOGGEDIN ****** ");
            return false;
        }

    }

    /**
     * Transforms a Map to a String of the required format:
     * <param1=value1&param2=value2>
     * 
     * @param params
     * @return united params in a String
     * @throws UnsupportedEncodingException
     */
    private String getParamsString(Map<String, String> params) throws UnsupportedEncodingException{
        StringBuilder result = new StringBuilder();

        for (Map.Entry<String, String> entry : params.entrySet()) {
          result.append(URLEncoder.encode(entry.getKey(), "UTF-8"));
          result.append("=");
          result.append(URLEncoder.encode(entry.getValue(), "UTF-8"));
          result.append("&");
        }
 
        String resultString = result.toString();
        return resultString.length() > 0
          ? resultString.substring(0, resultString.length() - 1)
          : resultString;
    }
    
    /**
     * Receives a null {@link HttpURLConnection} and sets it with the {@link URL} and the endpoint.
     * 
     * @param url
     * @param con
     * @param endpoint
     * @return con the HttpURLConnection configured.
     */
    public HttpURLConnection setUrlConnection(String endpoint) {

        HttpURLConnection con = null;
        URL url = null;
        try {
            url = new URL(stringUrl + "/" + endpoint);
            con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("POST");
        } catch (MalformedURLException e1) {
            e1.printStackTrace();
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        return con;
    }
}

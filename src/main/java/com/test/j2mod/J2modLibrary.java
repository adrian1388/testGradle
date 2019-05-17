package com.test.j2mod;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Component;

import com.fazecast.jSerialComm.SerialPort;
import com.ghgande.j2mod.modbus.Modbus;
import com.ghgande.j2mod.modbus.ModbusException;
import com.ghgande.j2mod.modbus.facade.ModbusSerialMaster;
import com.ghgande.j2mod.modbus.net.AbstractSerialConnection;
import com.ghgande.j2mod.modbus.procimg.InputRegister;
import com.ghgande.j2mod.modbus.procimg.Register;
import com.ghgande.j2mod.modbus.procimg.SimpleInputRegister;
import com.ghgande.j2mod.modbus.util.SerialParameters;
import com.test.data.model.Sensor;

@Component
public class J2modLibrary {

    protected final Log logger = LogFactory.getLog(getClass());
    
    protected static ModbusSerialMaster master;

    @PostConstruct
    public void init() {

    	logger.info("Starting Serial");
    	SerialPort[] ports = SerialPort.getCommPorts();
    	for (SerialPort port: ports) {
    	    logger.info(port.getSystemPortName());
    	}
    	
    	
    	
        // Create master
        SerialParameters parameters = new SerialParameters();
        parameters.setPortName("/dev/ttyUSB0");// For Windows: "\\COM6" // For Linux: "/dev/ttyUSB0"
        parameters.setBaudRate(9600);
        parameters.setDatabits(8);
        parameters.setParity(AbstractSerialConnection.NO_PARITY);
        parameters.setStopbits(AbstractSerialConnection.ONE_STOP_BIT);
        parameters.setEncoding(Modbus.SERIAL_ENCODING_RTU);
        parameters.setEcho(false);
        Boolean successCon = false;
        try {
    	    master = new ModbusSerialMaster(parameters, 0);

    	    master.connect();

        	logger.info("MASTER: " + master);
    	    logger.info("PARAMETERS: " + parameters);
    	    successCon = true;
    	}
    	catch (Exception e) {
    	    logger.info("PARAMETERS: " + parameters);
    	    logger.error("Cannot connect to slave", e);
    	}
        
        AbstractSerialConnection con = master.getConnection();
        try {
        	if (successCon) {
		        System.out.println(String.format(
		            "connected to %s: %s at %5d baud, %2d data bits, %2d stop bits\n available ports:%s",
		            con.getDescriptivePortName(), parameters.getPortName(), con.getBaudRate(),
		            con.getNumDataBits(), con.getNumStopBits(),
		            con.getCommPorts().toString()
	            ));
        	}
	        master.setRetries(0);
        } catch(Exception e) {
			logger.error("ERROR READING AbstractSerialConnection: " + e);
        	e.printStackTrace();
        }
        
        
    }
    
    
    /**
     * ReadRegister: Read the registers from the address specified in unitId,
     * If there is a success reading the sensor registers, the value is saved 
     * on {@link VerifierAttributerSnapshot} according to the {@link Sensor} 
     * corresponding to known unitId addresses:
     * ReadRegister: Read the registers from the address specified in unitId,
     * The {@link Sensor} corresponding known unitId addresses:
     * 
	 * <table>
	 * <tr>
	 * <td><strong>unitID</strong></td>
	 * <td>&nbsp;</td>
	 * <td><strong>Sensor</strong></td>
	 * </tr>
	 * 
	 * <tr>
	 * <td>10</td>
	 * <td>&nbsp;</td>
	 * <td>SN-PODOA-6304</td>
	 * </tr>
	 * 
	 * <tr>
	 * <td>6</td>
	 * <td>&nbsp;</td>
	 * <td>PH & Temp.</td>
	 * </tr>
	 * 
	 * <tr>
	 * <td>1</td>
	 * <td>&nbsp;</td>
	 * <td>Oxygen & Oxygen%</td>
	 * </tr>
	 * </table> 
     * 
     * @param unitId The slave unit id (Address).
     * @param ref The offset of the register to start reading from.
     * @param count The number of registers to be read.
     * @return A list of registers, if success reading. Empty list, otherwise.
     */
    public Float[] readMultipleRegister(
    	int unitId,
    	int ref,
    	int count
    ) {
    	Float[] output = new Float[2];
    	List<Float> temp = new ArrayList<Float>();
    	Register[] slaveResponse = new Register[2];
    	try {
    		slaveResponse = master.readMultipleRegisters(unitId, ref, count);
    		
    		for (int i = 0; i < slaveResponse.length - 1; i++) {
    			if (unitId == 10) {
    				temp.add(toFloat(slaveResponse[i].toBytes(), slaveResponse[i+1].toBytes()));
    			} else {
    				temp.add(toFloat(slaveResponse[i].getValue(), slaveResponse[i+1].getValue()));
    			}
    			i++;
    		}
    		output = temp.toArray(output);
            
		} catch (ModbusException e) {
			logger.error("ERROR READING REGISTER: " + e);
			e.printStackTrace();
		}

    	return output;
    }

    /**
     * ReadRegister: Read the registers from the address specified in unitId,
     * If there is a success reading the sensor registers, the value is saved 
     * on {@link VerifierAttributerSnapshot} according to the {@link Sensor} 
     * corresponding to known unitId addresses:
     * ReadRegister: Read the registers from the address specified in unitId,
     * The {@link Sensor} corresponding known unitId addresses:
     * 
	 * <table>
	 * <tr>
	 * <td><strong>unitID</strong></td>
	 * <td>&nbsp;</td>
	 * <td><strong>Sensor</strong></td>
	 * </tr>
	 * 
	 * <tr>
	 * <td>10</td>
	 * <td>&nbsp;</td>
	 * <td>SN-PODOA-6304</td>
	 * </tr>
	 * 
	 * <tr>
	 * <td>6</td>
	 * <td>&nbsp;</td>
	 * <td>PH & Temp.</td>
	 * </tr>
	 * 
	 * <tr>
	 * <td>1</td>
	 * <td>&nbsp;</td>
	 * <td>Oxygen & Oxygen%</td>
	 * </tr>
	 * </table> 
     * 
     * @param unitId The slave unit id (Address).
     * @param ref The offset of the register to start reading from.
     * @param count The number of registers to be read.
     * @return A list of registers, if success reading. Empty list, otherwise.
     */
    public Float[] readInputRegister(
    	int unitId,
    	int ref,
    	int count
    ) {
    	Float[] output = new Float[2];
    	InputRegister[] slaveResponse = new InputRegister[2];
    	try {
    		slaveResponse = master.readInputRegisters(unitId, ref, count);

//   		System.out.println("Register      : [" + i + "] " + slaveResponse[i]);
//  		System.out.println("Register      : [" + (i+1) + "] " + slaveResponse[i+1]);
			output[0] = (float) slaveResponse[1].getValue() / 100;
			output[1] = (float) slaveResponse[3].getValue() / 10;

		} catch (ModbusException e) {
			logger.error("ERROR READING REGISTER: " + e);
			e.printStackTrace();
		}

    	return output;
    }

    /**
     * WriteRegister: Write the register in the address specified in unitId,
     * If there is a success reading the sensor registers, the value is saved 
     * on {@link VerifierAttributerSnapshot} according to the {@link Sensor} 
     * corresponding to known unitId addresses:
     * 
	 * <table>
	 * <tr>
	 * <td><strong>unitID</strong></td>
	 * <td>&nbsp;</td>
	 * <td><strong>Sensor</strong></td>
	 * </tr>
	 * 
	 * <tr>
	 * <td>10</td>
	 * <td>&nbsp;</td>
	 * <td>SN-PODOA-6304</td>
	 * </tr>
	 * 
	 * <tr>
	 * <td>6</td>
	 * <td>&nbsp;</td>
	 * <td>PH & Temp.</td>
	 * </tr>
	 * 
	 * <tr>
	 * <td>1</td>
	 * <td>&nbsp;</td>
	 * <td>Oxygen & Oxygen%</td>
	 * </tr>
	 * </table> 
     * 
     * @param unitId The slave unit id (Address).
     * @param ref The offset of the register to start reading from.
     * @param count The value of the register to be written.
     * @return The value written.
     */
    public String writeRegister(
    	int unitId,
    	int ref,
    	int count
    ) {

    	int slaveResponse = 0;
    	try {
    		slaveResponse = master.writeSingleRegister(unitId, ref, new SimpleInputRegister(count));
		} catch (ModbusException e) {
			// TODO Auto-generated catch block
			logger.error("ERROR WRITING REGISTER: " + e);
			e.printStackTrace();
		}

    	return "" + slaveResponse;
    }


    /**
     * Concatenate two bytes returning the corresponding float value.
     * 
     * @param b1 First byte to concatenate.
     * @param b2 Second byte to concatenate.
     * @return The float value from b1 and b2.
     */
    public static float toFloat(byte[] b1, byte[] b2) {
    	int asInt = (b2[1] & 0xFF) 
                | ((b2[0] & 0xFF) << 8) 
                | ((b1[1] & 0xFF) << 16) 
                | ((b1[0] & 0xFF) << 24);
        return Float.intBitsToFloat(asInt);
    }

    /**
     * Moves the decimal point returning the corresponding float value.
     * 
     * @param value The int value which is going to be transformed to float.
     * @param decimal The number of places to move the decimal point.
     * @return The float value from value and dec.
     */
    public static float toFloat(int value, int decimal) {
        return ((float) value)/(float) Math.pow(10, decimal);
    }

}

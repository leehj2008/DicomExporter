package com.ge.si.dcmexport;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.dcm4che2.tool.dcmqr.DcmQR;
import org.dcm4che3.tool.storescp.StoreSCP;

public class DicomService {
	String localAE="";
	FTLString ftl = new FTLString();
	public static void main(String[] args) {
		DicomService dcmsvr=new DicomService();
		//dcmsvr.dcmMove("10007","2019-03-01","CT");
		dcmsvr.storeSCP();
	}

	public void dcmMove(Map parameters) {
		String localAE=AppConfig.getInstance().getProperty(AppConfig.MOVE_LOCAL_AE);
		String remoteAE=AppConfig.getInstance().getProperty(AppConfig.MOVE_REMOTE_AE);
		String remoteHost=AppConfig.getInstance().getProperty(AppConfig.MOVE_REMOTE_HOST);
		String remotePort=AppConfig.getInstance().getProperty(AppConfig.MOVE_REMOTE_PORT);
		String destAE=AppConfig.getInstance().getProperty(AppConfig.MOVE_DEST_AE);
		String parameterString=AppConfig.getInstance().getProperty(AppConfig.MOVE_PARAMETERS);
		String[] baseArgs = new String[] {"-L",localAE,remoteAE+"@"+remoteHost+":"+remotePort,"-cmove",destAE};
		
		String queryParameter = ftl.compileString(parameterString, parameters);
		String [] queryArgs = queryParameter.split("[|]");
		List<String> allArgs = new ArrayList<String>();
		for(String arg:baseArgs){
			allArgs.add(arg);
		}
		for(String arg:queryArgs){
			allArgs.add(arg);
		}
		String [] a = {};
		try{
			DcmQR.main(allArgs.toArray(a));
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public void storeSCP(){
		StoreSCP scp = null;
		try {
			scp = new StoreSCP();
		} catch (IOException e) {
			e.printStackTrace();
		}
		String filepath = AppConfig.getInstance().getProperty(AppConfig.SCP_FILEPATH);
		String localAE = AppConfig.getInstance().getProperty(AppConfig.SCP_LOCAL_AE);
		String localPort = AppConfig.getInstance().getProperty(AppConfig.SCP_LOCAL_PORT);
		
		String [] args = {"-b",localAE+":"+localPort,"--filepath="+filepath};
		StoreSCP.main(args);
		System.out.println("STORESCP Started...");
	}
	

}

package com.ge.si.dcmexport;

import org.dcm4che2.tool.dcmqr.DcmQR;

public class DicomService {
	String localAE="";
	public static void main(String[] args) {
		new DicomService().dcmMove("10007","2019-03-01","CT");
	}

	private void dcmMove(String pid,String studyDate,String modality) {
		String[] arg0 = new String[] {"-L",localAE,"-qPatientID="+pid};
		DcmQR.main(arg0);
	}

}

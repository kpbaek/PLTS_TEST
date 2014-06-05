package com.sbmkorea.plts.data;

public class PartHistoryData {
	
	private String partBarcode;
	private String regDate;
	private String workerName;
	private String remarks;	
	
	public PartHistoryData(String partBarcode, String regDate, String workerName, String remarks){
		this.partBarcode = partBarcode;
		this.regDate = regDate;
		this.workerName = workerName;
		this.remarks = remarks;
	}

	public void setPartBarcode(String partBarcode) {
		this.partBarcode = partBarcode;
	}

	public void setRegDate(String regDate) {
		this.regDate = regDate;
	}

	public void setWorkerName(String workerName) {
		this.workerName = workerName;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public String getPartBarcode() {
		return partBarcode;
	}

	public String getRegDate() {
		return regDate;
	}

	public String getWorkerName() {
		return workerName;
	}

	public String getRemarks() {
		return remarks;
	}

}

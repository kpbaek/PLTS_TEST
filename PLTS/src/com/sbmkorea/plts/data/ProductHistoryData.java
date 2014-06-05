package com.sbmkorea.plts.data;

public class ProductHistoryData {
	
	private String taskType;
	private String proudctBarcode;
	private String partBarcode;
	private String regDate;
	private String workerName;
	
	private String errorType;
	private String remarks;
	
	public ProductHistoryData(String taskType, String productBarcode, String partBarcode, String regDate, String workerName, String errorType, String remarks){
		this.taskType = taskType;
		this.proudctBarcode = productBarcode;
		this.partBarcode = partBarcode;
		this.regDate = regDate;
		this.workerName = workerName;
		
		this.errorType = errorType;
		this.remarks = remarks;
	}

	public String getErrorType() {
		return errorType;
	}

	public void setTaskType(String taskType) {
		this.taskType = taskType;
	}

	public void setProudctBarcode(String proudctBarcode) {
		this.proudctBarcode = proudctBarcode;
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

	public String getTaskType() {
		return taskType;
	}

	public String getProudctBarcode() {
		return proudctBarcode;
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

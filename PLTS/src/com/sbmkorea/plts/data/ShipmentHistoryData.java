package com.sbmkorea.plts.data;

public class ShipmentHistoryData {
	
	private String enterpriser ;
	private String proudctBarcode;
	private String regDate;
	private String workerName;
	
	private String errorType;
	private String remarks;
	private String shipmentBarcode;
	private String etc;
	
	public ShipmentHistoryData(String enterpriser , String productBarcode, String shipmentBarcode,String regDate, String workerName, String etc){
		this.enterpriser = enterpriser;
		this.proudctBarcode = productBarcode;
		this.shipmentBarcode = shipmentBarcode;
		this.regDate = regDate;
		this.workerName = workerName;
		this.etc = etc;
	}

	public String getErrorType() {
		return errorType;
	}

	public void setEnterpriser(String enterpriser ) {
		this.enterpriser  = enterpriser ;
	}

	public void setProudctBarcode(String proudctBarcode) {
		this.proudctBarcode = proudctBarcode;
	}

	public void setShipmentBarcode(String shipmentBarcode) {
		this.shipmentBarcode = shipmentBarcode;
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
	
	public String getEnterpriser() {
		return enterpriser ;
	}

	public String getProudctBarcode() {
		return proudctBarcode;
	}

	public String getShipmentBarcode() {
		return shipmentBarcode;
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

	public String getEtc() {
		return etc;
	}

	public void setEtc(String etc) {
		this.etc = etc;
	}

}

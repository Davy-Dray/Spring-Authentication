package com.ragnar.auth.exception;

import java.util.Date;

import org.springframework.http.HttpStatus;

import com.fasterxml.jackson.annotation.JsonFormat;

public class HttpResponse {

	private int statuscode;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "DD-MM-yyyy hh:mm:ss")
	Date timestamp;

	private HttpStatus httpStatus;

	private String reason;

	private String message;

	public HttpResponse() {

	}

	public HttpResponse(int statuscode, HttpStatus httpStatus, String reason, String message) {
		this.timestamp = new Date();
		this.statuscode = statuscode;
		this.httpStatus = httpStatus;
		this.reason = reason;
		this.message = message;
	}

	public int getStatuscode() {
		return statuscode;
	}

	public void setStatuscode(int statuscode) {
		this.statuscode = statuscode;
	}

	public HttpStatus getHttpStatus() {
		return httpStatus;
	}

	public void setHttpStatus(HttpStatus httpStatus) {
		this.httpStatus = httpStatus;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Date getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Date timestamp) {
		this.timestamp = timestamp;
	}

}

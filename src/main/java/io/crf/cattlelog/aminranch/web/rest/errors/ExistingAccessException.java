package io.crf.cattlelog.aminranch.web.rest.errors;


public class ExistingAccessException extends BadRequestAlertException {
	private static final long serialVersionUID = 1L;
	
	public ExistingAccessException() {

        super(ErrorConstants.DEFAULT_TYPE,"Access to the Ranch already Requested. Re-sending the request email." , "adminranch", "adminRanch.ranchAccess.existing");
	}
}
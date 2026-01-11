package com.churncheck.api.infra.errors;

public enum ErrorStatusResponseCodes {
	BAD_CREDENTIALS_401("Bad Credentials", 401),
	BAD_PATHVARIABLE_400("Bad Path Variable", 400),
	BAD_REQUEST_400("Bad Request", 400),
	CONFLICT_409("Conflict", 409),
	FORBIDDEN_403("Forbidden", 403),
	INVALID_QUERY_PARAMETER_400("Invalid Query Parameter", 400),
	LOCKED_423("Locked", 423),
	MALFORMED_400("Malformed", 400),
	NOT_FOUND_404("Not Found", 404),
	UNAUTHORIZED_401("Unauthorized", 401);

	private final String desc;
	private final Integer status;

	ErrorStatusResponseCodes(String desc, int status) {
        this.desc = desc;
        this.status = status;
    }

	public String getDesc() {
        return desc;
    }

    public Integer getStatus() {
        return status;
    }
}
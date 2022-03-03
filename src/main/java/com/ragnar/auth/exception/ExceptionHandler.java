package com.ragnar.auth.exception;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;


@RestControllerAdvice
public class ExceptionHandler implements ErrorController {
	private Logger LOGGER = LoggerFactory.getLogger(ExceptionHandler.class);

	public static final String METHOD_IS_NOT_ALLOWED = "this request is not allowed on this endpoint.  Please send a '%s' request";

	public static final String INTERNAL_SERVER_ERROR_MESSAGE = "Error occured while processing request";

	public static final String INCORRECT_CREDENTIALS = "Username or password incorrrect";

	public static final String ACCOUNT_DISABLED = "Your account has beeen diabled";

	public static final String NOT_AUTHORIZED = "Unauthorized";

	public static final String ERROR_PATH = "/error";

	private ResponseEntity<HttpResponse> getResponse(HttpStatus httpStatus, String message) {

		HttpResponse httpResponse = new HttpResponse(httpStatus.value(), httpStatus, httpStatus.getReasonPhrase(),
				message);

		return new ResponseEntity<>(httpResponse, httpStatus);
	}

	@org.springframework.web.bind.annotation.ExceptionHandler(Exception.class)
	public ResponseEntity<HttpResponse> internalServerError(Exception exception) {

		LOGGER.error(exception.getMessage());
		return getResponse(HttpStatus.INTERNAL_SERVER_ERROR, INTERNAL_SERVER_ERROR_MESSAGE);

	}

	@org.springframework.web.bind.annotation.ExceptionHandler(HttpRequestMethodNotSupportedException.class)
	public ResponseEntity<HttpResponse> methodNotsupportedException(HttpRequestMethodNotSupportedException exception) {
		HttpMethod supportedMethod = Objects.requireNonNull(exception.getSupportedHttpMethods()).iterator().next();

		return getResponse(HttpStatus.METHOD_NOT_ALLOWED, String.format(METHOD_IS_NOT_ALLOWED, supportedMethod));

	}

	@org.springframework.web.bind.annotation.ExceptionHandler(BadCredentialsException.class)
	public ResponseEntity<HttpResponse> badCredentialException() {

		return getResponse(HttpStatus.BAD_REQUEST, INCORRECT_CREDENTIALS);

	}

	@org.springframework.web.bind.annotation.ExceptionHandler(AccessDeniedException.class)
	public ResponseEntity<HttpResponse> accessDeniedException() {

		return getResponse(HttpStatus.FORBIDDEN, NOT_AUTHORIZED);

	}

	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@org.springframework.web.bind.annotation.ExceptionHandler(MethodArgumentNotValidException.class)
	public Map<String, String> handleValidationExceptions(MethodArgumentNotValidException ex) {
		Map<String, String> errors = new HashMap<>();
		ex.getBindingResult().getAllErrors().forEach((error) -> {
			String fieldName = ((FieldError) error).getField();
			String errorMessage = error.getDefaultMessage();
			errors.put(fieldName, errorMessage);
		});
		return errors;
	}

	public String getErrorPath() {

		return ERROR_PATH;
	}

	@RequestMapping(ERROR_PATH)
	public ResponseEntity<HttpResponse> notFound(Exception exception) {

		return getResponse(HttpStatus.NOT_FOUND, "you seem lost");

	}

}

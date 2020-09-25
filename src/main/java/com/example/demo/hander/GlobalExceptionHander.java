package com.example.demo.hander;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;

import com.example.demo.enums.StatusCode;
import com.example.demo.exception.PanException;
import com.example.demo.vo.JsonData;
import com.obs.services.exception.ObsException;

/**
 *
 * @ClassName GlobalExceptionHander
 * @Description 全局异常处理
 * @author fuling
 * @date 2020年9月21日 上午9:58:49
 */
@RestControllerAdvice
public class GlobalExceptionHander {

	/**
	 *
	 * @Title handerPanException
	 * @Description 处理自定义异常
	 * @param ex 异常
	 * @return 返回JsonData封装的错误信息
	 */
	@ExceptionHandler(PanException.class)
	public JsonData handerPanException(PanException ex) {
		return JsonData.buildError(ex.getCode(), ex.getMessage());
	}

	/**
	 *
	 * @Title handlerValidateException
	 * @Description 处理jsr参数校验不通过的方法
	 * @param ex
	 * @return 返回JsonData封装的校验不通过的相关信息
	 */
	@ExceptionHandler({MethodArgumentNotValidException.class, BindException.class, ConstraintViolationException.class})
	public JsonData handlerValidateException(Exception ex) {
		
		HashMap<String,Object> errorMap = new HashMap<>();
		
		BindingResult result = null;
		if(ex instanceof MethodArgumentNotValidException)result = ((MethodArgumentNotValidException)ex).getBindingResult();
		else if(ex instanceof BindException)result = ((BindException)ex).getBindingResult();
		else if(ex instanceof ConstraintViolationException) {
			Set<ConstraintViolation<?>> violations = ((ConstraintViolationException)ex).getConstraintViolations();
			List<Integer> index = Arrays.asList(1);
			violations.stream().forEach(vio -> {
				errorMap.put("param" + index.get(0), vio.getMessage());
				index.set(0, index.get(0) + 1);
			});
			return JsonData.buildError(StatusCode.PARAM_VALIDATE_FAILED.code(), errorMap);
		}
		
		result.getFieldErrors().forEach(error -> {
			errorMap.put(error.getField(), error.getDefaultMessage());
		});

		return JsonData.buildError(StatusCode.PARAM_VALIDATE_FAILED.code(), errorMap);

	}

	/**
	 *
	 * @Title handerObsException
	 * @Description 处理obs异常
	 * @param ex 异常
	 * @return 返回JsonData封装的错误信息
	 */
	@ExceptionHandler(ObsException.class)
	public JsonData handerObsException(ObsException ex) {
		return JsonData.buildError(StatusCode.OBS_ERROR.code(), ex.getErrorMessage());
	}


	/**
	 *
	 * @Title defaultHander
	 * @Description 默认错误处理（用来处理http异常以及其他异常）
	 * @param request 请求
	 * @param ex 异常
	 * @return 返回JsonData封装的错误信息
	 */
	@ExceptionHandler(Exception.class)
	public JsonData defaultHander(Exception ex) {
		Integer code = null;
		String message = null;

		if(ex instanceof NoHandlerFoundException
				|| ex instanceof HttpRequestMethodNotSupportedException
				|| ex instanceof IllegalStateException
				|| ex instanceof MethodArgumentTypeMismatchException
				|| ex instanceof HttpMessageNotReadableException) {
			code = StatusCode.CLIENT_ERROR.code();
			message = StatusCode.CLIENT_ERROR.message();
		}else {
			code = StatusCode.SERVER_ERROR.code();
			//message = StatusCode.SERVER_ERROR.message();
			message = ex.getMessage();
		}
		return JsonData.buildError(code, message);
	}


}

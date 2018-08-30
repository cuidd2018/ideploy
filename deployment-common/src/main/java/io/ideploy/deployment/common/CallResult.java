package io.ideploy.deployment.common;

import io.ideploy.deployment.base.ApiCode;

/**
 * API统一结果对象
 */
public class CallResult<T> {

	/** 返回代码 */
	protected int code;

	/** 错误信息 */
	protected String message;

	/** 返回对象 */
	protected T object;

	/** 是否成功，readonly */
	protected boolean success;

	@Deprecated
	public CallResult() {
	}

	public static CallResult make(int code, String message){
        return new CallResult(ApiCode.FAILURE, message, null);
    }

	public static<T> CallResult make(T object) {
		return new CallResult(ApiCode.SUCCESS, "success", object);
	}

	private CallResult(int code, String message, T object) {
		setCode(code);
		setMessage(message);
		setObject(object);
	}

	// private func

	private void setCode(int code) {
		this.code = code;
		this.success = (code == ApiCode.SUCCESS);
	}

	private void setMessage(String message) {
		this.message = message;
	}

	private void setObject(T object) {
		this.object = object;
	}

	// get func

	public int getCode() {
		return code;
	}

	public String getMessage() {
		return message;
	}

	public T getObject() {
		return object;
	}

	public boolean isSuccess() {
		return success;
	}

	public boolean isFailue(){
	    return !success;
    }

}

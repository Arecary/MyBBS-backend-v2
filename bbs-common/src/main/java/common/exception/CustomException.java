package common.exception;

import common.enums.ResultCodeEnum;

public class CustomException extends RuntimeException {
    private String code;
    private String msg;

    public CustomException(ResultCodeEnum resultCodeEnum) {
        super(resultCodeEnum.msg); // 设置父类的消息
        this.code = resultCodeEnum.code;
        this.msg = resultCodeEnum.msg;
    }

    public CustomException(String code, String msg) {
        super(msg); // 设置父类的消息
        this.code = code;
        this.msg = msg;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    @Override
    public String toString() {
        return "CustomException{" +
                "code='" + code + '\'' +
                ", msg='" + msg + '\'' +
                '}';
    }
}

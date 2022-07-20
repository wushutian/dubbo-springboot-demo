package com.xiaze.api.vo;

import lombok.Data;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data
public class HelloParamVo implements Serializable {

    @NotNull(message = "名称不能为空")
    private String name;

    private Integer count;
}

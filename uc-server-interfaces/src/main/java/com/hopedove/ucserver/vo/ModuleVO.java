package com.hopedove.ucserver.vo;

import com.hopedove.commons.vo.BasicVO;

import io.swagger.annotations.ApiModel;

import java.util.List;

@ApiModel("模块")
public class ModuleVO extends BasicVO {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Integer id;

    private String name;

    private Integer disabled;

    private List<FunctionVO> functions;

	public List<FunctionVO> getFunctions() {
		return functions;
	}

	public void setFunctions(List<FunctionVO> functions) {
		this.functions = functions;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getDisabled() {
		return disabled;
	}

	public void setDisabled(Integer disabled) {
		this.disabled = disabled;
	}

}

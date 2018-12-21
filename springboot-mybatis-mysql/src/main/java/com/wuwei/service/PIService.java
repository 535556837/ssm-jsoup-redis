package com.wuwei.service;

import com.wuwei.util.Result;

import java.util.Map;

public interface PIService {
	/**
	 * vin查信息
	 * @param licenseNum
	 * @return
	 */
	public Object queryVehicleByPrefillVIN(String vin);

	/**
	 * 车型查询
	 * @param licenseNum
	 * @return
	 */
	public Object vehicleQuery(String brandName,String modelCode) throws Exception;

	/**
	 * 车辆信息
	 * @param num
	 * @return
	 */
	public Object queryBaseItemCar(String num) throws Exception;

	/**
	 * 个人信息
	 *
	 * @return
	 */
	public Result getClientDetail(String bizType, String bizNo)throws Exception;

	/**
	 * 保单查询
	 *
	 * @return
	 */
	public Result selectPolicy(String vin,String pageSize,String pageNo,String licenseNo)throws Exception;

	/*	*//**
	 * 通用传参方法
	 * @return
	 * @throws Exception
	 *//*
	public Object total((Map<String,String> map,)throws Exception;*/

}

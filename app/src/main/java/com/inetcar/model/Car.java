package com.inetcar.model;

import java.io.Serializable;

/**
 * 汽车类
 */
public class Car implements Serializable {

    private static final long serialVersionUID = 1L;

    private String brand;					//汽车品牌
    private int logo;						//汽车标志
    private String type;					//型号
    private String plateNumber;				//车牌号
    private String engineNumber;			//发动机号
    private String bodyLevel;				//车身级别
    private int mileage;					//里程数
    private int gasoline;					//汽油量
    private char enginePerformance;			//发动机性能
    private char transmissionPerformance; 	//变速器性能
    private char lightPerformance;			//车灯好坏

    public Car() {}

    public Car(String brand, String type, String plateNumber, String engineNumber,
               String bodyLevel, int mileage, int gasoline,
               char enginePerformance, char transmissionPerformance,
               char lightPerformance) {
        this.brand = brand;
        this.logo = 0;
        this.type = type;
        this.plateNumber = plateNumber;
        this.engineNumber = engineNumber;
        this.bodyLevel = bodyLevel;
        this.mileage = mileage;
        this.gasoline = gasoline;
        this.enginePerformance = enginePerformance;
        this.transmissionPerformance = transmissionPerformance;
        this.lightPerformance = lightPerformance;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getPlateNumber() {
        return plateNumber;
    }

    public void setPlateNumber(String plateNumber) {
        this.plateNumber = plateNumber;
    }

    public String getEngineNumber() {
        return engineNumber;
    }

    public void setEngineNumber(String engineNumber) {
        this.engineNumber = engineNumber;
    }

    public String getBodyLevel() {
        return bodyLevel;
    }

    public void setBodyLevel(String bodyLevel) {
        this.bodyLevel = bodyLevel;
    }

    public int getMileage() {
        return mileage;
    }

    public void setMileage(int mileage) {
        this.mileage = mileage;
    }

    public int getGasoline() {
        return gasoline;
    }

    public void setGasoline(int gasoline) {
        this.gasoline = gasoline;
    }

    public char getEnginePerformance() {
        return enginePerformance;
    }

    public void setEnginePerformance(char enginePerformance) {
        this.enginePerformance = enginePerformance;
    }

    public char getTransmissionPerformance() {
        return transmissionPerformance;
    }

    public void setTransmissionPerformance(char transmissionPerformance) {
        this.transmissionPerformance = transmissionPerformance;
    }

    public char getLightPerformance() {
        return lightPerformance;
    }

    public void setLightPerformance(char lightPerformance) {
        this.lightPerformance = lightPerformance;
    }

    public int getLogo() {
        return logo;
    }

    public void setLogo(int logo) {
        this.logo = logo;
    }
}

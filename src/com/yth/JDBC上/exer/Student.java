package com.yth.JDBC上.exer;/**
*@ClassName Student
*@Description TODO
*@Author deleave
*@Date 2021/5/7 17:49
*@Version 1.0
**/
public class Student {
    private int FlowID;//流水号
    private int type;//考试类型
    private String IDCard;//身份证号
    private String examCard;//考号
    private String location;//生源地
    private String name;
    private int grade;//成绩

    public Student() {
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public Student(int flowID, int type, String IDCard, String examCard, String location, String name, int grade) {
        FlowID = flowID;
        this.type = type;
        this.IDCard = IDCard;
        this.examCard = examCard;
        this.location = location;
        this.name=name;
        this.grade = grade;
    }

    @Override
    public String toString() {
        System.out.println("======查询结果======");
        return info();
    }

    private String info() {
        return "流水号："+FlowID+"\n四级/六级"+type+"\n身份证号:"+IDCard+"\n准考证号："+examCard+"\n学生姓名："+name
                +"\n生源地："+location+"\n成绩："+grade;
    }

    public void setType(int type) {
        this.type = type;
    }

    public void setIDCard(String IDCard) {
        this.IDCard = IDCard;
    }

    public void setExamCard(String examCard) {
        this.examCard = examCard;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setGrade(int grade) {
        this.grade = grade;
    }

    public int getFlowID() {
        return FlowID;
    }

    public int getType() {
        return type;
    }

    public String getIDCard() {
        return IDCard;
    }

    public String getExamCard() {
        return examCard;
    }

    public String getLocation() {
        return location;
    }

    public int getGrade() {
        return grade;
    }
}

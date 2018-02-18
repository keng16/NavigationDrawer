package com.example.user.smartclassroom.Global;

import java.sql.Time;



public class Properties {
	String studremarks,roomid,courseid,sectionid,studentfname,studentmname,studentlname,statdescript,myday,pic,proffname,profmname,proflname;
	int attendanceid;

	int prof_id;
	int stud_id;

	public int getStud_id() {
		return stud_id;
	}

	public void setStud_id(int stud_id) {
		this.stud_id = stud_id;
	}

	public int getProf_id() {
		return prof_id;
	}

	public void setProf_id(int prof_id) {
		this.prof_id = prof_id;
	}

	String startingtime,endingtime,date,transaction,entity_type,holiday_date,holiday_description;
	public String getHolidayDate()
	{
		return holiday_date;
	}
	public void setHolidayDate(String holiday_date)
	{
		this.holiday_date = holiday_date;
	}
	public String getIP() {return "http://192.168.254.104/scwebservice/";}
	public String getHolidayDescript()
	{
		return holiday_description;
	}
	public void setHolidayDescript(String holiday_description) {this.holiday_description = holiday_description;}
	public String getTransact()
	{
		return transaction;
	}
	public void setTransact(String transaction)
	{
		this.transaction = transaction;
	}
	public String getEntity()
	{
		return entity_type;
	}
	public void setEntity(String entity_type)
	{
		this.entity_type = entity_type;
	}

	public String getDate() { return date; }
	public void setDate(String date)
	{
		this.date=date;
	}
	public String getStudRemark()
	{
		return studremarks;
	}
	public void setStudRemark(String studremarks)
	{
		this.studremarks = studremarks;
	}
	public int getAttendID()
	{
		return attendanceid;
	}
	public void setAttendID(int attendanceid)
	{
		this.attendanceid = attendanceid;
	}
	public String getProffname()
	{
		return proffname;
	}
	public void setProffname(String proffname) {
		this.proffname = proffname;
	}
	public String getProfmname()
	{
		return profmname;
	}
	public void setProfmname(String profmname) {
		this.profmname = profmname;
	}
	public String getProflname()
	{
		return proflname;
	}
	public void setProflname(String proflname) {
		this.proflname = proflname;
	}
	public String getRoom() {
		return roomid;
	}
	public void setRoom(String roomid) {
		this.roomid = roomid;
	}
	public String getCourseId() {
		return courseid;
	}
	public void setCourse(String courseid) {
		this.courseid = courseid;
	}
	public String getSection() {
		return sectionid;
	}
	public void setSection(String sectionid) {
		this.sectionid = sectionid;
	}

	public String getStudentfname() {
		return studentfname;
	}
	public void setStudentfname(String studentfname) {
		this.studentfname = studentfname;
	}

	public String getStudentmname() {
		return studentmname;
	}
	public void setStudentmname(String studentmname) {
		this.studentmname = studentmname;
	}

	public String getStudentlname() {
		return studentlname;
	}
	public void setStudentlname(String studentlname) {
		this.studentlname = studentlname;
	}
	public String getStatdescript() {
		return statdescript;
	}
	public void setStatdescript(String statdescript) {
		this.statdescript = statdescript;
	}
	public String getStartTime()
	{
		return startingtime;
	}
	public void setStartTime(String startingtime)
	{
		this.startingtime = startingtime;
	}
	public String getEndTime()
	{
		return endingtime;
	}
	public void setEndTime(String endingtime)
	{
		this.endingtime = endingtime;
	}
	public String getDay()
	{
		return myday;
	}
	public void setDay(String myday)
	{
		this.myday = myday;
	}
	public String getPic()
	{
		return pic;
	}
	public void setPic(String pic)
	{
		this.pic=pic;
	}


}

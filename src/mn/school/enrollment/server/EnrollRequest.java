package mn.school.enrollment.server;

import java.io.Serializable;
import java.util.Date;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import com.google.appengine.api.datastore.Email;
import com.google.appengine.api.datastore.Key;

/**
 * Сервер талд элсэх хүсэлтийг төлөөлөх класс
 * 
 * @author ub
 * 
 */
@PersistenceCapable
public class EnrollRequest implements Serializable {
    private static final long serialVersionUID = -6971348989080624257L;
    public static final String DRAFT = "draft";
    public static final String APPROVED = "approved";
    public static final String REJECTED = "rejected";
    public static final String CANCELLED = "cancelled";
    public static final String VALIDATED = "validated";

    @PrimaryKey
    @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
    private Key key;
    @Persistent
    private String name;
    @Persistent
    private String last_name;
    @Persistent
    private Email email;
    @Persistent
    private String regno;
    @Persistent
    private String pasno;
    @Persistent
    private String gender;
    @Persistent
    private String school;
    @Persistent
    private String group;
    @Persistent
    private float grad_score;
    @Persistent
    private String certificate_no;
    @Persistent
    private String nationality;
    @Persistent
    private String city;

    @Persistent
    private String enroll_prof;
    @Persistent
    private String esubject;
    @Persistent
    private float escore;
    @Persistent
    private String payment_id;
    @Persistent
    private String state;
    @Persistent
    private Date state_date;

    public EnrollRequest(String name, Email email) {
	setName(name);
	setИмэйл(email);
	state = DRAFT;
	state_date = new Date();
    }

    public String getТөлөв() {
	return state;
    }

    public void setТөлөв(String төлөв) {
	this.state = төлөв;
    }

    // сургууль зөвшөөрөх
    public void approve() {
	if (state.equals(DRAFT)) {
	    state_date = new Date();
	    state = APPROVED;
	}
    }

    // сургууль татгалзах
    public void reject() {
	if (state.equals(DRAFT)) {
	    state_date = new Date();
	    state = REJECTED;
	}
    }

    // сургууль зөвшөөрсний дараа элсэгч баталгаажуулах
    public void validate() {
	if (state.equals(APPROVED)) {
	    state_date = new Date();
	    state = VALIDATED;
	}
    }

    // сургууль зөвшөөрсний дараа элсэгч цуцлах
    public void cancel() {
	if (state.equals(APPROVED)) {
	    state_date = new Date();
	    state = CANCELLED;
	}
    }

    public void setKey(Key key) {
	this.key = key;
    }

    public Key getKey() {
	return key;
    }

    public void setName(String name) {
	this.name = name;
    }

    public String getName() {
	return name;
    }

    public void setЭцгийнНэр(String эцгийнНэр) {
	this.last_name = эцгийнНэр;
    }

    public String getЭцгийнНэр() {
	return last_name;
    }

    public void setИмэйл(Email имэйл) {
	this.email = имэйл;
    }

    public Email getИмэйл() {
	return email;
    }

    public void setРегистр(String регистр) {
	this.regno = регистр;
    }

    public String getРегистр() {
	return regno;
    }

    public void setПасспорт(String пасспорт) {
	this.pasno = пасспорт;
    }

    public String getПасспорт() {
	return pasno;
    }

    public void setСургууль(String төгссөнСургууль) {
	this.school = төгссөнСургууль;
    }

    public String getСургууль() {
	return school;
    }

    public void setҮнэлгээ(float төгссөнҮнэлгээ) {
	this.grad_score = төгссөнҮнэлгээ;
    }

    public float getҮнэлгээ() {
	return grad_score;
    }

    public void setГэрчилгээ(String гэрчилгээ) {
	this.certificate_no = гэрчилгээ;
    }

    public String getГэрчилгээ() {
	return certificate_no;
    }

    public void setХаръяалал(String иргэнийХаръяалал) {
	this.nationality = иргэнийХаръяалал;
    }

    public String getХаръяалал() {
	return nationality;
    }

    public void setЕшОноо(float ешОноо) {
	this.escore = ешОноо;
    }

    public float getЕшОноо() {
	return escore;
    }

    public void setЭлсэхМэргэжил(String элсэхМэргэжил) {
	this.enroll_prof = элсэхМэргэжил;
    }

    public String getЭлсэхМэргэжил() {
	return enroll_prof;
    }

    public String getАймаг() {
	return city;
    }

    public void setАймаг(String s) {
	city = s;
    }

    public void setОгноо(Date огноо) {
	this.state_date = огноо;
    }

    public Date getОгноо() {
	return state_date;
    }

    public void setТөлбөрId(String төлбөрId) {
	this.payment_id = төлбөрId;
    }

    public String getТөлбөрId() {
	return payment_id;
    }

    public void setХүйс(String хүйс) {
	this.gender = хүйс;
    }

    public String getХүйс() {
	return gender;
    }

    public void setБүлэг(String бүлэг) {
	this.group = бүлэг;
    }

    public String getБүлэг() {
	return group;
    }

    public void setЕшХичээл(String ешХичээл) {
	this.esubject = ешХичээл;
    }

    public String getЕшХичээл() {
	return esubject;
    }
}

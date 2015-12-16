package mn.school.enrollment.client;

import java.io.Serializable;
import java.util.Date;

public class EnrollRequestClient implements Serializable {
    private static final long serialVersionUID = -6971348989080624257L;
    public static final String DRAFT = "draft";
    public static final String VALIDATED = "validated";
    public static final String CANCELLED = "cancelled";
    public static final String APPROVED = "approved";
    public static final String REJECTED = "rejected";

    private String key;
    private String нэр;
    private String эцгийнНэр;
    private String имэйл;
    private String регистр;
    private String пасспорт;
    private String хүйс;
    private String төгссөнСургууль;
    private String бүлэг;
    private float төгссөнҮнэлгээ;
    private String гэрчилгээ;
    private String иргэнийХаръяалал;
    private String аймаг;
    

    private String элсэхМэргэжил;
    private String ешХичээл;
    private float ешОноо;
    private String төлөв;
    private Date огноо;
    private String төлбөр_id;

    public EnrollRequestClient() {

    }

    public EnrollRequestClient(String name, String email) {
	setНэр(name);
	setИмэйл(email);
	төлөв = DRAFT;
	огноо = new Date();
    }

    public String getТөлөв() {
	return төлөв;
    }

    public void setТөлөв(String төлөв) {
	this.төлөв = төлөв;
    }

    public void setKey(String key) {
	this.key = key;
    }

    public String getKey() {
	return key;
    }

    public void setНэр(String нэр) {
	this.нэр = нэр;
    }

    public String getНэр() {
	return нэр;
    }

    public void setЭцгийнНэр(String эцгийнНэр) {
	this.эцгийнНэр = эцгийнНэр;
    }

    public String getЭцгийнНэр() {
	return эцгийнНэр;
    }

    public void setИмэйл(String имэйл) {
	this.имэйл = имэйл;
    }

    public String getИмэйл() {
	return имэйл;
    }

    public void setРегистр(String регистр) {
	this.регистр = регистр;
    }

    public String getРегистр() {
	return регистр;
    }

    public void setПасспорт(String пасспорт) {
	this.пасспорт = пасспорт;
    }

    public String getПасспорт() {
	return пасспорт;
    }

    public void setСургууль(String төгссөнСургууль) {
	this.төгссөнСургууль = төгссөнСургууль;
    }

    public String getСургууль() {
	return төгссөнСургууль;
    }

    public void setҮнэлгээ(float төгссөнҮнэлгээ) {
	this.төгссөнҮнэлгээ = төгссөнҮнэлгээ;
    }

    public float getҮнэлгээ() {
	return төгссөнҮнэлгээ;
    }

    public void setГэрчилгээ(String гэрчилгээ) {
	this.гэрчилгээ = гэрчилгээ;
    }

    public String getГэрчилгээ() {
	return гэрчилгээ;
    }

    public void setХаръяалал(String иргэнийХаръяалал) {
	this.иргэнийХаръяалал = иргэнийХаръяалал;
    }

    public String getХаръяалал() {
	return иргэнийХаръяалал;
    }

    public void setЕшОноо(float ешОноо) {
	this.ешОноо = ешОноо;
    }

    public float getЕшОноо() {
	return ешОноо;
    }

    public void setЭлсэхМэргэжил(String элсэхМэргэжил) {
	this.элсэхМэргэжил = элсэхМэргэжил;
    }

    public String getЭлсэхМэргэжил() {
	return элсэхМэргэжил;
    }

    public String getАймаг() {
	return аймаг;
    }

    public void setАймаг(String s) {
	аймаг = s;
    }

    public void setОгноо(Date огноо) {
	this.огноо = огноо;
    }

    public Date getОгноо() {
	return огноо;
    }

    public String getТөлбөрId() {
	return төлбөр_id;
    }
    
    public void setТөлбөрId(String id) {
	төлбөр_id = id;
    }

    public void setХүйс(String хүйс) {
	this.хүйс = хүйс;
    }

    public String getХүйс() {
	return хүйс;
    }

    public void setБүлэг(String бүлэг) {
	this.бүлэг = бүлэг;
    }

    public String getБүлэг() {
	return бүлэг;
    }

    public void setЕшХичээл(String ешХичээл) {
	this.ешХичээл = ешХичээл;
    }

    public String getЕшХичээл() {
	return ешХичээл;
    }
}

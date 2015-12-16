package mn.school.enrollment.server;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;
import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import mn.school.enrollment.client.EnrollRequestClient;
import mn.school.enrollment.client.EnrollService;
import mn.school.enrollment.shared.FieldVerifier;

import com.google.appengine.api.datastore.Email;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

/**
 * The server side implementation of the RPC service.
 */
@SuppressWarnings("serial")
public class EnrollServiceImpl extends RemoteServiceServlet implements
	EnrollService {
    public EnrollServiceImpl() {

    }

    public String greetServer(String input) throws IllegalArgumentException {
	// Verify that the input is valid.
	if (!FieldVerifier.isValidName(input)) {
	    // If the input is not valid, throw an IllegalArgumentException back
	    // to
	    // the client.
	    throw new IllegalArgumentException(
		    "Name must be at least 4 characters long");
	}

	String serverInfo = getServletContext().getServerInfo();
	String userAgent = getThreadLocalRequest().getHeader("User-Agent");
	return "Hello, " + input + "!<br><br>I am running " + serverInfo
		+ ".<br><br>It looks like you are using:<br>" + userAgent;
    }

    @Override
    public String бүртгэх(EnrollRequestClient obj) {
	PersistenceManager pm = PMF.get().getPersistenceManager();
	try {
	    pm.makePersistent(clientObj2Server(obj));
	} finally {
	    pm.close();
	}

	return "";
    }

    /**
     * Клиент талын объектыг сервер талын объект руу хувиргах метод
     * 
     * @param o
     *            - клиент талаас ирэх ЭлсэхХүсэлт объект
     * @return
     */
    private EnrollRequest clientObj2Server(EnrollRequestClient o) {
	EnrollRequest e = new EnrollRequest(o.getНэр(), new Email(o.getИмэйл()));
	e.setKey((o.getKey() != null && o.getKey().length() > 0 ? KeyFactory
		.stringToKey(o.getKey()) : null));
	e.setЭцгийнНэр(o.getЭцгийнНэр());
	e.setРегистр(o.getРегистр());
	e.setПасспорт(o.getПасспорт());
	e.setХүйс(o.getХүйс());

	e.setСургууль(o.getСургууль());
	e.setБүлэг(o.getБүлэг());
	e.setГэрчилгээ(o.getГэрчилгээ());
	e.setҮнэлгээ(o.getҮнэлгээ());
	e.setОгноо(o.getОгноо());

	e.setЭлсэхМэргэжил(o.getЭлсэхМэргэжил());
	e.setЕшХичээл(o.getЕшХичээл());
	e.setЕшОноо(o.getЕшОноо());

	e.setТөлбөрId(o.getТөлбөрId());
	e.setХаръяалал(o.getХаръяалал());
	e.setАймаг(o.getАймаг());
	e.setТөлөв(o.getТөлөв());

	return e;
    }

    /**
     * Сервер талын объектыг клиент объект руу хувиргах метод
     * 
     * @param e
     * @return
     */
    private EnrollRequestClient serverObj2Client(EnrollRequest e) {
	EnrollRequestClient o = new EnrollRequestClient(e.getName(), e
		.getИмэйл().getEmail());
	if (e.getKey() != null)
	    o.setKey(KeyFactory.keyToString(e.getKey()));

	o.setЭцгийнНэр(e.getЭцгийнНэр());
	o.setРегистр(e.getРегистр());
	o.setПасспорт(e.getПасспорт());
	o.setХүйс(e.getХүйс());

	o.setСургууль(e.getСургууль());
	o.setБүлэг(e.getБүлэг());
	o.setГэрчилгээ(e.getГэрчилгээ());
	o.setҮнэлгээ(e.getҮнэлгээ());
	o.setОгноо(e.getОгноо());

	o.setЭлсэхМэргэжил(e.getЭлсэхМэргэжил());
	o.setЕшХичээл(e.getЕшХичээл());
	o.setЕшОноо(e.getЕшОноо());

	o.setТөлбөрId(e.getТөлбөрId());
	o.setХаръяалал(e.getХаръяалал());
	o.setАймаг(e.getАймаг());
	o.setТөлөв(e.getТөлөв());

	return o;
    }

    @Override
    public String зөвшөөрөх(String key) {
	String result = "";
	PersistenceManager pm = PMF.get().getPersistenceManager();
	EnrollRequest e = pm.getObjectById(EnrollRequest.class, KeyFactory
		.stringToKey(key));
	if (e != null) {
	    if (e.getТөлөв().equals(EnrollRequest.DRAFT)) {
		e.validate();
		try {
		    pm.makePersistent(e);

		    // зөвшөөрөл олгогдсон тухай и-мэйлээр мэдэгдэх
		    Properties props = new Properties();
		    Session session = Session.getDefaultInstance(props, null);

		    Message msg = new MimeMessage(session);
		    msg.setFrom(new InternetAddress("ubs121@gmail.com",
			    "Enrollment Service"));
		    msg.addRecipient(Message.RecipientType.TO,
			    new InternetAddress(e.getИмэйл().getEmail(),
				    "Эрхэм хүндэт " + e.getName()));
		    msg.setSubject("Элсэлт зөвшөөрөгдлөө");
		    msg
			    .setText("Таны элсэх хүсэлтийг хянаж зөвшөөрөл олгосон байна. Та өөрөө элсэх хүсэлтээ баталгаажуулна уу.");
		    Transport.send(msg);
		} catch (Exception ex) {
		    result = ex.toString();
		}

	    } else {
		result = "Ноорог төлөвтэй хүсэлтийг зөвшөөрч болно!";
	    }
	} else {
	    result = key + " кодтой элсэх хүсэлт олдсонгүй!";
	}
	pm.close();
	return result;
    }

    @Override
    public String татгалзах(String key, String comment) {
	String result = "";
	PersistenceManager pm = PMF.get().getPersistenceManager();
	EnrollRequest e = pm.getObjectById(EnrollRequest.class, KeyFactory
		.stringToKey(key));
	if (e != null) {
	    if (e.getТөлөв().equals(EnrollRequest.DRAFT)) {
		e.reject();
		try {
		    pm.makePersistent(e);

		    // татгалзсан тухай и-мэйлээр мэдэгдэх
		    Properties props = new Properties();
		    Session session = Session.getDefaultInstance(props, null);

		    Message msg = new MimeMessage(session);
		    msg.setFrom(new InternetAddress("ubs121@gmail.com",
			    "Enrollment Service"));
		    msg.addRecipient(Message.RecipientType.TO,
			    new InternetAddress(e.getИмэйл().getEmail(),
				    "Эрхэм хүндэт " + e.getName()));
		    msg
			    .setSubject("Уучлаарай, элсэлтийн хүсэлт зөвшөөрөгдсөнгүй");
		    msg
			    .setText("Уучлаарай, таны элсэх хүсэлтийг зөвшөөрсөнгүй. "
				    + comment);
		    Transport.send(msg);
		} catch (Exception ex) {
		    result = ex.toString();
		}
	    } else {
		result = "Ноорог төлөвтэй хүсэлтийг татгалзаж болно!";
	    }
	} else {
	    result = key + " кодтой элсэх хүсэлт олдсонгүй!";
	}
	pm.close();
	return result;
    }

    @Override
    public String батлах(String key) {
	String result = "";
	PersistenceManager pm = PMF.get().getPersistenceManager();
	EnrollRequest e = pm.getObjectById(EnrollRequest.class, KeyFactory
		.stringToKey(key));
	if (e != null) {
	    if (e.getТөлөв().equals(EnrollRequest.APPROVED)) {
		e.validate();
		pm.makePersistent(e);
	    } else {
		result = "Хүсэлт батлагдахын өмнө сургуулиар хянагдсан байх ёстой!";
	    }
	} else {
	    result = key + " кодтой элсэх хүсэлт олдсонгүй!";
	}
	pm.close();
	return result;
    }

    @Override
    public String цуцлах(String key) {
	String result = "";
	PersistenceManager pm = PMF.get().getPersistenceManager();
	EnrollRequest e = pm.getObjectById(EnrollRequest.class, KeyFactory
		.stringToKey(key));
	if (e != null) {
	    if (e.getТөлөв().equals(EnrollRequest.APPROVED)) {
		e.cancel();
		pm.makePersistent(e);

	    } else {
		result = "Хүсэлт цуцлахын өмнө сургуулиар хянагдсан байх ёстой!";
	    }
	} else {
	    result = key + " кодтой элсэх хүсэлт олдсонгүй!";
	}
	pm.close();
	return result;
    }

    @Override
    public EnrollRequestClient элсэхХүсэлт(String k) {
	PersistenceManager pm = PMF.get().getPersistenceManager();
	EnrollRequest e = pm.getObjectById(EnrollRequest.class, KeyFactory
		.stringToKey(k));
	return serverObj2Client(e);
    }

    @Override
    public List<EnrollRequestClient> элсэхХүсэлтүүд() {
	String query = "select from " + EnrollRequest.class.getName();
	PersistenceManager pm = PMF.get().getPersistenceManager();
	List<EnrollRequest> list = (List<EnrollRequest>) pm.newQuery(query)
		.execute();
	List<EnrollRequestClient> retList = new ArrayList<EnrollRequestClient>();
	for (EnrollRequest e : list) {
	    retList.add(serverObj2Client(e));
	}
	pm.close();
	return retList;
    }

    @Override
    public List<EnrollRequestClient> элсэхХүсэлтүүд(String where, String order,
	    String range) {
	String query = "select from " + EnrollRequest.class.getName();
	// нөхцөлт хайлт
	if (where != null && where.length() > 0)
	    query = query + " where " + where;

	if (order != null && order.length() > 0) {
	    query = query + " order by " + order;
	    // үр дүнг хязгаарлах
	    if (range != null && range.length() > 0)
		query = query + " range " + range;
	}

	PersistenceManager pm = PMF.get().getPersistenceManager();
	List<EnrollRequest> list = (List<EnrollRequest>) pm.newQuery(query)
		.execute();
	List<EnrollRequestClient> retList = new ArrayList<EnrollRequestClient>();
	for (EnrollRequest e : list) {
	    retList.add(serverObj2Client(e));
	}
	pm.close();
	return retList;
    }

    /**
     * Бүргэлийн дугаар авах
     */
    @Override
    public String дугаарАвах(String name, String email) {
	String result = "";
	try {

	    log("дугаарАвах метод дуудагдлаа...");
	    // TODO: Энэ и-мэйл хаягаар өмнө нь үүссэн
	    // эсэхийг шалгах хэрэгтэй
	    EnrollRequest e = new EnrollRequest(name, new Email(email));

	    PersistenceManager pm = PMF.get().getPersistenceManager();
	    try {
		pm.makePersistent(e);
	    } finally {
		pm.close();
	    }

	    String key = KeyFactory.keyToString(e.getKey());
	    result = "Таны бүртгэлийн кодыг үүсгэж и-мэйлээр явуулсан. И-мэйлээ шалгана уу. "
		    + key;
	    log("Шинэ хүсэлт бүртгэгдлээ: " + result);

	    // бүртгэлийн дугаарыг и-мэйлээр илгээх
	    Properties props = new Properties();
	    Session session = Session.getDefaultInstance(props, null);

	    Message msg = new MimeMessage(session);
	    // TODO: и-мэйл хаягаа солих
	    msg.setFrom(new InternetAddress("ubs121@gmail.com",
		    "Enrollment Service"));
	    msg.addRecipient(Message.RecipientType.TO, new InternetAddress(
		    email, "Эрхэм хүндэт " + name));
	    msg.setSubject("Бүртгэлийн код");
	    msg.setText("Програмд нэвтрэх код " + key);
	    Transport.send(msg);

	} catch (javax.mail.internet.AddressException ex) {
	    result = "И-мэйл хаяг буруу байна!";
	} catch (Exception ex) {
	    result = ex.toString();
	}

	return result;
    }

    /**
     * Програмд нэвтрэх
     */
    @Override
    public EnrollRequestClient нэвтрэх(String email, String key) {
	PersistenceManager pm = PMF.get().getPersistenceManager();
	EnrollRequest e = pm.getObjectById(EnrollRequest.class, KeyFactory
		.stringToKey(key));
	if (e != null && email.equals(e.getИмэйл().getEmail()))
	    return serverObj2Client(e);
	else
	    return null;
    }

    @Override
    public void хүсэлтУстгах(List<String> keys) {
	PersistenceManager pm = PMF.get().getPersistenceManager();
	Query query = pm.newQuery(EnrollRequest.class, ":p.contains(key)");
	query.deletePersistentAll(keys);
    }

}

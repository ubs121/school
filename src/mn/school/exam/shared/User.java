package mn.goody.exam.shared;

import java.util.Date;

import javax.jdo.annotations.Extension;
import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import com.google.gwt.user.client.rpc.IsSerializable;

@PersistenceCapable(identityType = IdentityType.APPLICATION)
public class User implements IsSerializable {
	@PrimaryKey
	@Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
	@Extension(vendorName = "datanucleus", key = "gae.encoded-pk", value = "true")
	private String id;
	@Persistent
	private String uid;
	@Persistent
	private String pwd;
	@Persistent
	private String firstName;
	@Persistent
	private String lastName;
	@Persistent
	@Extension(vendorName = "datanucleus", key = "gae.encoded-pk", value = "true")
	private String group_id;

	@Persistent
	private Date last_login;
	@Persistent
	private String last_ip;

	// харгалзах группын төрөл
	private int groupType;

	public User() {
		groupType = UserGroup.STUDENT_GROUP;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

	public String getUid() {
		return uid;
	}

	public void setPwd(String pwd) {
		this.pwd = pwd;
	}

	public String getPwd() {
		return pwd;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setGroup(String group) {
		this.group_id = group;
	}

	public String getGroup() {
		return group_id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getId() {
		return id;
	}

	public int getGroupType() {
		return groupType;
	}

	public void setGroupType(int gt) {
		groupType = gt;
	}

	public void setLastLogin(Date last_login) {
		this.last_login = last_login;
	}

	public Date getLastLogin() {
		return last_login;
	}

	public void setLastIp(String last_ip) {
		this.last_ip = last_ip;
	}

	public String getLastIp() {
		return last_ip;
	}
}

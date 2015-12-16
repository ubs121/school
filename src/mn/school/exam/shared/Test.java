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
public class Test implements IsSerializable {
	public static final String TYPE_EXAM = "mn/school/exam";
	public static final String TYPE_PRACTICE = "practice";

	@PrimaryKey
	@Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
	@Extension(vendorName = "datanucleus", key = "gae.encoded-pk", value = "true")
	private String id;
	@Persistent
	private String name;
	@Persistent
	private Date start;
	@Persistent
	private int duration;
	@Persistent
	@Extension(vendorName = "datanucleus", key = "gae.encoded-pk", value = "true")
	private String group_id;
	@Persistent
	private String location;
	@Persistent
	private String type;
	@Persistent
	@Extension(vendorName = "datanucleus", key = "gae.encoded-pk", value = "true")
	private String author_id;

	public Test() {

	}

	public Test(String name) {
		this.name = name;

	}

	public void setId(String id) {
		this.id = id;
	}

	public String getId() {
		return id;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setStart(Date start) {
		this.start = start;
	}

	public Date getStart() {
		return start;
	}

	public void setDuration(int duration) {
		this.duration = duration;
	}

	public int getDuration() {
		return duration;
	}

	public void setGroup(String group) {
		this.group_id = group;
	}

	public String getGroup() {
		return group_id;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getLocation() {
		return location;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getType() {
		return type;
	}

	public void setAuthor(String author) {
		this.author_id = author;
	}

	public String getAuthor() {
		return author_id;
	}
}

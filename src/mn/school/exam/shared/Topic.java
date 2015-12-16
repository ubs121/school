package mn.goody.exam.shared;

import javax.jdo.annotations.Extension;
import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * Асуултын сэдэв
 */
@PersistenceCapable
public class Topic implements IsSerializable {
	@PrimaryKey
	@Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
	@Extension(vendorName = "datanucleus", key = "gae.encoded-pk", value = "true")
	private String id;
	@Persistent
	private String name;
	@Persistent
	@Extension(vendorName = "datanucleus", key = "gae.encoded-pk", value = "true")
	private String parent_id;

	public Topic() {

	}

	public Topic(String name) {
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

	public void setParent(String p) {
		this.parent_id = p;
	}

	public String getParent() {
		return parent_id;
	}
}

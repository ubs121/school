package mn.goody.exam.shared;

import javax.jdo.annotations.Extension;
import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * Асуулт класс
 * @author ub
 *
 */
@PersistenceCapable(identityType = IdentityType.APPLICATION)
public class Quiz implements IsSerializable {
	public static final int LEVEL_SUPER_EASY = 1;
	public static final int LEVEL_EASY = 2;
	public static final int LEVEL_NORMAL = 3;
	public static final int LEVEL_HARD = 4;
	public static final int LEVEL_SUPER_HARD = 5;

	@PrimaryKey
	@Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
	@Extension(vendorName = "datanucleus", key = "gae.encoded-pk", value = "true")
	private String id;
	@Persistent
	private int level;
	@Persistent
	@Extension(vendorName = "datanucleus", key = "gae.encoded-pk", value = "true")
	private String topic_id;
	@Persistent
	private String content;
	@Persistent
	private int duration;
	@Persistent
	private int score;

	public Quiz() {
		setLevel(1);
	}

	public Quiz(int lev, int dur, String cnt) {
		setLevel(lev);
		setDuration(dur);
		setContent(cnt);
	}

	public Quiz(int lev, int dur, String cnt, String topic) {
		setLevel(lev);
		setDuration(dur);
		setContent(cnt);
		setTopic(topic);
	}

	public void setId(String key) {
		id = key;
	}

	public String getId() {
		return id;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public int getLevel() {
		return level;
	}

	public void setTopic(String tid) {
		this.topic_id = tid;
	}

	public String getTopic() {
		return topic_id;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getContent() {
		return content;
	}

	public void setDuration(int duration) {
		this.duration = duration;
	}

	public int getDuration() {
		return duration;
	}

	public void setScore(int score) {
		this.score = score;
	}

	public int getScore() {
		return score;
	}

}

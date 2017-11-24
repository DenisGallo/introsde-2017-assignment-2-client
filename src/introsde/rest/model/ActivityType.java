package introsde.rest.model;

import java.io.Serializable;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import com.fasterxml.jackson.annotation.JsonIgnore;

@XmlRootElement(name="activity_type")
@XmlAccessorType(XmlAccessType.FIELD)
@Entity
@Table(name="ActivityTypes")
@NamedQuery(name="ActivityType.findAll", query="SELECT at FROM ActivityType at")
public class ActivityType implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="activitytype_id")
	private String name;

	@OneToMany(mappedBy="type", cascade = CascadeType.ALL)
	@XmlTransient
	@JsonIgnore
	private List<Activity> activities;
	
	public ActivityType() {
		
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<Activity> getActivities() {
		return activities;
	}

	public void setActivities(List<Activity> activities) {
		this.activities = activities;
	}
	
	public void addActivity(Activity activity) {
        this.activities.add(activity);
        if (activity.getType() != this) {
            activity.setType(this);
        }
    }
}

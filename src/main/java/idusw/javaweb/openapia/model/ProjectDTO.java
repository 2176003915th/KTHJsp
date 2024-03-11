package idusw.javaweb.openapia.model;

public class ProjectDTO {
    private Long pid;
    private String projectName;
    private String projectDescription;
    private String status;
    private String clientCompany;
    private String projectLeader;
    private String estimatedBudget;
    private String totalAmountSpent;
    private String estimatedProjectDuration;
    private String projectImage;
    private String regTimeStamp;
    private String revTimeStamp;

    public Long getPid() {
        return pid;
    }

    public void setPid(Long pid) {
        this.pid = pid;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getProjectDescription() {
        return projectDescription;
    }

    public void setProjectDescription(String projectDescription) {
        this.projectDescription = projectDescription;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getClientCompany() {
        return clientCompany;
    }

    public void setClientCompany(String clientCompany) {
        this.clientCompany = clientCompany;
    }

    public String getProjectLeader() {
        return projectLeader;
    }

    public void setProjectLeader(String projectLeader) {
        this.projectLeader = projectLeader;
    }

    public String getEstimatedBudget() {
        return estimatedBudget;
    }

    public void setEstimatedBudget(String estimatedBudget) {
        this.estimatedBudget = estimatedBudget;
    }

    public String getTotalAmountSpent() {
        return totalAmountSpent;
    }

    public void setTotalAmountSpent(String totalAmountSpent) {
        this.totalAmountSpent = totalAmountSpent;
    }

    public String getEstimatedProjectDuration() {
        return estimatedProjectDuration;
    }

    public void setEstimatedProjectDuration(String estimatedProjectDuration) {this.estimatedProjectDuration = estimatedProjectDuration;}

    public String getProjectImage() {return projectImage;}

    public void setProjectImage(String projectImage) {this.projectImage = projectImage;}

    public String getRegTimeStamp() {return regTimeStamp;}

    public void setRegTimeStamp(String regTimeStamp) {this.regTimeStamp = regTimeStamp;}

    public String getRevTimeStamp() {return revTimeStamp;}

    public void setRevTimeStamp(String revTimeStamp) {this.revTimeStamp = revTimeStamp;}
}

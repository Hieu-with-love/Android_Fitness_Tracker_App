package hcmute.edu.vn.fitnesstrackerapp.model;

public class TeamMember {
    private String name;
    private String studentId;
    private String role;

    public TeamMember(String name, String studentId, String role) {
        this.name = name;
        this.studentId = studentId;
        this.role = role;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
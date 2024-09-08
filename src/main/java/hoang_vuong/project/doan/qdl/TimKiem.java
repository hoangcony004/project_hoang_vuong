package hoang_vuong.project.doan.qdl;

public class TimKiem {
    private String name;
    private String link;

    public TimKiem(String name, String link) {
        this.name = name;
        this.link = link;
    }

    // Getters và Setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }
}


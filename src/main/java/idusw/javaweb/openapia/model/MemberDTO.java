package idusw.javaweb.openapia.model;

//lombok을 사용하면 boilder-plate (상용구, 관용구) code를 손쉽게 작성할 수 있음
public class MemberDTO {
    //Data Transfer Object (데이터 전송 객체), 데이터를 주고 받을 때 사용하는 객체
    private Long mid;
    private String fullName;
    private String email;
    private String pw;
    private String zipcode;
    private String regTimeStamp;



    //getter / setter 메소드
    public Long getMid() {
        return mid;
    }

    public void setMid(Long mid) {
        this.mid = mid;
    }

    public String getFullName() { return fullName; }

    public void setFullName(String fullName) { this.fullName = fullName; }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPw() {
        return pw;
    }

    public void setPw(String pw) {
        this.pw = pw;
    }

    public String getZipcode() {return zipcode;}

    public void setZipcode(String zipcode) {this.zipcode = zipcode;}

    public String getRegTimeStamp() {return regTimeStamp;}

    public void setRegTimeStamp(String regTimeStamp) {this.regTimeStamp = regTimeStamp;}
}

package com.example.cinematicket.Model;

public class ReservationModel {
    String uid,moviename,moviedate,custname,custphone,custemail,seat,time;

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getMoviename() {
        return moviename;
    }

    public void setMoviename(String moviename) {
        this.moviename = moviename;
    }

    public String getMoviedate() {
        return moviedate;
    }

    public void setMoviedate(String moviedate) {
        this.moviedate = moviedate;
    }

    public String getCustname() {
        return custname;
    }

    public void setCustname(String custname) {
        this.custname = custname;
    }

    public String getCustphone() {
        return custphone;
    }

    public void setCustphone(String custphone) {
        this.custphone = custphone;
    }

    public String getCustemail() {
        return custemail;
    }

    public void setCustemail(String custemail) {
        this.custemail = custemail;
    }

    public String getSeat() {
        return seat;
    }

    public void setSeat(String seat) {
        this.seat = seat;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public ReservationModel(String uid, String moviename, String moviedate, String custname, String custphone, String custemail, String seat, String time) {
        this.uid = uid;
        this.moviename = moviename;
        this.moviedate = moviedate;
        this.custname = custname;
        this.custphone = custphone;
        this.custemail = custemail;
        this.seat = seat;
        this.time = time;
    }
}

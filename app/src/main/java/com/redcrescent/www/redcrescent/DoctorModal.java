package com.redcrescent.www.redcrescent;

/**
 * Created by muhammadarbab on 28/01/2016.
 */
public class DoctorModal {

    private String doctorName ="";
    private String specialities="";
    private String educations ="";
    private String imgSrc ="";

    public  DoctorModal (){


    }

    public void setdoctorName(String _doctorName)

    {

        this.doctorName=_doctorName;

    }

    public String getdoctorName()

    {

        return this.doctorName;

    }

    public void setSpecialities(String _Specialities)

    {

        this.specialities=_Specialities;

    }

    public String getSpecialities()

    {

        return this.specialities;

    }

    public void seteducationss(String _educations)

    {

        this.educations=_educations;

    }

    public String geteducations()

    {

        return this.educations;

    }

    public void setimgSrc(String _imgSrc)

    {

        this.imgSrc=_imgSrc;

    }

    public String getimgSrc()

    {

        return this.imgSrc;

    }
}

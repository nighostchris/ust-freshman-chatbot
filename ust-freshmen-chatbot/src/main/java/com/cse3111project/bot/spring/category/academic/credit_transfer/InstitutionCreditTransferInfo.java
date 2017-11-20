package com.cse3111project.bot.spring.category.academic.credit_transfer;

class InstitutionCreditTransferInfo extends CreditTransferInfo {
    private String country;
    private String institution;

    InstitutionCreditTransferInfo(String country, String institution, 
                                  String subject, String transferCourseCode, String restriction) {
        super(subject, transferCourseCode, restriction);
        // this.country = (country == null ? "Hong Kong" : country);
        this.country = country;
        this.institution = institution;
    }

    @Override
    public String toString(){
        return "For " + institution + (country == null ? "" : " in " + country) + ",\n" + super.toString();
    }
}

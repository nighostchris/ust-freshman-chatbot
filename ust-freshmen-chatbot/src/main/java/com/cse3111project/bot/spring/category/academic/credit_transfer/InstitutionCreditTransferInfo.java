package com.cse3111project.bot.spring.category.academic.credit_transfer;

/**
 * The InstitutionCreditTransferInfo class inherits the CreditTransferInfo class and wrap the details
 * of the institution course that could be used as credit transfer for user. 
 * @version 1.0
 */
class InstitutionCreditTransferInfo extends CreditTransferInfo 
{
    private String country;
    private String institution;

    /**
     * Constructor of the InstitutionCreditTransferInfo.
     * @param country Country of the institution
     * @param institution Name of the institution
     * @param subject Subject name of the course used to transfer for credits
     * @param transferCourseCode Course code of the transferred course.
     * @param restriction Restriction for the transfer to apply.
     */
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

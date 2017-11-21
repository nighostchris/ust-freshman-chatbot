package com.cse3111project.bot.spring.category.academic.credit_transfer;

/**
 * The ExamCreditTransferInfo class inherits the CreditTransferInfo class and wrap the details
 * of the exam that could be used as credit transfer for user. 
 * @version 1.0
 */
class ExamCreditTransferInfo extends CreditTransferInfo 
{
    private String exam;

    /**
     * Constructor of ExamCreditTransferInfo.
     * @param exam Name of examination
     * @param subject Subject of the examination
     * @param transferCourseCode Course code of the transferred course
     * @param restriction Restriction for the transfer to work
     */
    ExamCreditTransferInfo(String exam, String subject, String transferCourseCode, String restriction) {
        super(subject, transferCourseCode, restriction);
        this.exam = exam;
    }

    @Override
    public String toString(){
        return "For " + exam + ",\n" + super.toString();
    }
}

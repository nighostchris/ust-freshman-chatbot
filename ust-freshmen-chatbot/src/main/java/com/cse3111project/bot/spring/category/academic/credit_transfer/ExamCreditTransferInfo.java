package com.cse3111project.bot.spring.category.academic.credit_transfer;

class ExamCreditTransferInfo extends CreditTransferInfo {
    private String exam;

    ExamCreditTransferInfo(String exam, String subject, String transferCourseCode, String restriction) {
        super(subject, transferCourseCode, restriction);
        this.exam = exam;
    }

    @Override
    public String toString(){
        return "For " + exam + ",\n" + super.toString();
    }
}

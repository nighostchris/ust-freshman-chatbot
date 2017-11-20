package com.cse3111project.bot.spring.category.academic.credit_transfer;

abstract class CreditTransferInfo {
    protected String subject;
    protected String restriction;
    protected String transferCourseCode;

    CreditTransferInfo(String subject, String transferCourseCode, String restriction) {
        this.subject = subject;
        this.transferCourseCode = transferCourseCode;
        this.restriction = restriction;
    }

    @Override
    public String toString(){
        StringBuilder sb = new StringBuilder(subject).append(" is equivalent as ")
                               .append(transferCourseCode).append(" in UST, ");
        switch (restriction) {
            case "GD":
                sb.append("yet your grade point must be at least ").append(restriction.split(":")[1].trim());
                break;
            case "AD":
                sb.append("but this mapping is only applicable for student who has local Associate Degree / Higher Diploma");
                break;
            case "ONCE":
                sb.append("but this course cannot be double-counted");
                break;
            case "Expired":
                sb.append("but this mapping will be expiring soon. Be careful");
                break;
            case "ASSESS":
                sb.append("yet this mapping requires individual assessment to check whether you are eligible to do credit transfer. You should provide detailed course information if you want to apply");
                break;
            case "TRIP":
                sb.append("but this mapping is solely considered for SBM students going for a short business trip. You could not double-count this course with others");
                break;
            case "No Restriction":
                sb.append("without any restriction at all");
                break;
            default:  // for ExamCreditTransfer
                sb.append("yet your grade point should be at least ").append(restriction);
                break;
        }

        return sb.toString() + '\n';
    }
}

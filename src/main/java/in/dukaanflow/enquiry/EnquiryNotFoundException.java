package in.dukaanflow.enquiry;
public class EnquiryNotFoundException extends RuntimeException {
    public EnquiryNotFoundException(long id) { super("Enquiry " + id + " was not found"); }
}
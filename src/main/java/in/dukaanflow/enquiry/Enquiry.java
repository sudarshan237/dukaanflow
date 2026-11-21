package in.dukaanflow.enquiry;
import java.time.Instant;
public record Enquiry(long id, String customerName, String message, String detectedIntent,
                      String productName, Integer quantity, EnquiryStatus status, String draftReply,
                      String reviewReason, Instant createdAt, Instant updatedAt) {}
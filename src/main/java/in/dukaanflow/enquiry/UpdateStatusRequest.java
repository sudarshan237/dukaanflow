package in.dukaanflow.enquiry;
import jakarta.validation.constraints.NotNull;
public record UpdateStatusRequest(@NotNull EnquiryStatus status) {}
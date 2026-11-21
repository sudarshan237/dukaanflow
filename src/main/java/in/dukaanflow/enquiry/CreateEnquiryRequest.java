package in.dukaanflow.enquiry;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
public record CreateEnquiryRequest(@NotBlank @Size(max=100) String customerName,
                                   @NotBlank @Size(max=2000) String message) {}
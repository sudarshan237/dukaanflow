package in.dukaanflow.enquiry;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/enquiries")
@CrossOrigin(origins = "*")
public class EnquiryController {
    private final EnquiryService service;
    public EnquiryController(EnquiryService service) { this.service = service; }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Enquiry create(@Valid @RequestBody CreateEnquiryRequest request) { return service.create(request); }

    @GetMapping
    public List<Enquiry> findAll() { return service.findAll(); }

    @PatchMapping("/{id}/status")
    public Enquiry updateStatus(@PathVariable long id, @Valid @RequestBody UpdateStatusRequest request) {
        return service.updateStatus(id, request.status());
    }

    @ExceptionHandler(EnquiryNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Map<String, String> notFound(EnquiryNotFoundException ex) { return Map.of("error", ex.getMessage()); }
}
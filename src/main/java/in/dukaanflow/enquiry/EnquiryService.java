package in.dukaanflow.enquiry;
import org.springframework.stereotype.Service;
import java.time.Instant;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

@Service
public class EnquiryService {
    private final Map<Long, Enquiry> store = new ConcurrentHashMap<>();
    private final AtomicLong ids = new AtomicLong();

    public Enquiry create(CreateEnquiryRequest request) {
        String message = request.message().trim();
        String normalized = message.toLowerCase();
        String product = detectProduct(normalized);
        Integer quantity = detectQuantity(normalized);
        String intent = detectIntent(normalized);
        String reason = product == null
                ? "Product could not be identified confidently; seller review required."
                : "Verify stock, price and delivery before making a customer-facing commitment.";
        String draft = buildDraft(request.customerName().trim(), product, quantity);
        Instant now = Instant.now();
        Enquiry result = new Enquiry(ids.incrementAndGet(), request.customerName().trim(), message,
                intent, product, quantity, EnquiryStatus.NEEDS_HUMAN_REVIEW, draft, reason, now, now);
        store.put(result.id(), result);
        return result;
    }

    public List<Enquiry> findAll() {
        return store.values().stream().sorted(Comparator.comparing(Enquiry::createdAt).reversed()).toList();
    }

    public Enquiry updateStatus(long id, EnquiryStatus status) {
        Enquiry old = store.get(id);
        if (old == null) throw new EnquiryNotFoundException(id);
        Enquiry updated = new Enquiry(old.id(), old.customerName(), old.message(), old.detectedIntent(),
                old.productName(), old.quantity(), status, old.draftReply(), old.reviewReason(),
                old.createdAt(), Instant.now());
        store.put(id, updated);
        return updated;
    }

    private String detectIntent(String text) {
        if (text.matches(".*\\b(price|cost|rate|how much)\\b.*")) return "PRICE_CHECK";
        if (text.matches(".*\\b(order|buy|need|want|pieces|qty|quantity)\\b.*")) return "PURCHASE_INTENT";
        if (text.matches(".*\\b(stock|available|availability)\\b.*")) return "AVAILABILITY_CHECK";
        if (text.matches(".*\\b(deliver|delivery|ship)\\b.*")) return "DELIVERY_QUERY";
        return "GENERAL_ENQUIRY";
    }

    private String detectProduct(String text) {
        if (text.contains("coffee mug") || text.matches(".*\\bmug\\b.*")) return "Classic coffee mug";
        if (text.contains("tote")) return "Canvas tote bag";
        if (text.contains("planter")) return "Desk planter";
        return null;
    }

    private Integer detectQuantity(String text) {
        Matcher matcher = Pattern.compile("\\b(\\d{1,3})\\b").matcher(text);
        return matcher.find() ? Integer.valueOf(matcher.group(1)) : null;
    }

    private String buildDraft(String customer, String product, Integer quantity) {
        if (product == null) return "Hi " + customer + ", thanks for contacting us. Could you clarify which product you mean?";
        String qty = quantity == null ? "" : " for " + quantity + " unit(s)";
        return "Hi " + customer + ", we recorded your enquiry about " + product + qty
                + ". Our seller will verify stock, price and delivery before confirming.";
    }
}
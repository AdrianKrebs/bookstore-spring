package ch.bfh.eadj.bookstore.application.exception;

public class PaymentFailedException extends BookstoreException {
    private final Code code;

    public enum Code {
        CREDIT_CARD_EXPIRED, INVALID_CREDIT_CARD, PAYMENT_LIMIT_EXCEEDED
    }

    public PaymentFailedException(PaymentFailedException.Code code) {
        this.code = code;
    }

    public PaymentFailedException.Code getCode(){
        return code ;
    }
}

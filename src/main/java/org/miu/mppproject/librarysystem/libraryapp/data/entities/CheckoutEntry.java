package org.miu.mppproject.librarysystem.libraryapp.data.entities;

import java.time.LocalDate;

public final class CheckoutEntry extends BaseEntity {

    private String entryCheckoutId;
    private final BookCopy checkedOutBook;
    private final Member checkoutTo;
    private final LocalDate checkoutDate;
    private final LocalDate dueDate;
    private final LocalDate dateReturned;
    private final double fineAmount;


    public CheckoutEntry(BookCopy checkedOutBook,
                         Member checkoutTo,
                         LocalDate checkoutDate,
                         LocalDate dueDate,
                         LocalDate dateReturned, double fineAmount) {

        super("CE-");
        this.entryCheckoutId = getId();
        this.checkedOutBook = checkedOutBook;
        this.checkoutTo = checkoutTo;
        this.checkoutDate = checkoutDate;
        this.dueDate = dueDate;
        this.dateReturned = dateReturned;
        this.fineAmount = fineAmount;
    }


    public void setEntryCheckoutId(String entryCheckoutId) {
        this.entryCheckoutId = entryCheckoutId;
    }

    public String getEntryCheckoutId() {
        return entryCheckoutId;
    }

    public BookCopy getCheckedOutBook() {
        return checkedOutBook;
    }

    public Member getCheckoutTo() {
        return checkoutTo;
    }

    public LocalDate getCheckoutDate() {
        return checkoutDate;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public boolean hasBeenReturned() {
        return dateReturned == null;
    }

    public LocalDate getDateReturned() {
        return dateReturned;
    }

    public boolean isBookOverDue() {
        return LocalDate.now().isAfter(dueDate);
    }


    public double getFineAmount() {
        if (isBookOverDue()) return fineAmount;
        return 0.0;
    }
}

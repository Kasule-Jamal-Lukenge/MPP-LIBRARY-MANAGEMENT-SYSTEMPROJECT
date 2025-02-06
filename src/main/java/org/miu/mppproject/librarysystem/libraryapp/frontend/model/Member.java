package org.miu.mppproject.librarysystem.libraryapp.frontend.model;

import java.util.List;

public class Member {

    private final List<CheckoutEntry> checkoutEntries;

    private final String memberId;

    private final ContactInfo contactInfo;

    public Member(List<CheckoutEntry> checkoutEntries, String memberId, ContactInfo contactInfo) {
        this.checkoutEntries = checkoutEntries;
        this.memberId = memberId;
        this.contactInfo = contactInfo;
    }

    public List<CheckoutEntry> getCheckoutEntries() {
        return checkoutEntries;
    }

    public String getMemberId() {
        return memberId;
    }

    public ContactInfo getContactInfo() {
        return contactInfo;
    }
}

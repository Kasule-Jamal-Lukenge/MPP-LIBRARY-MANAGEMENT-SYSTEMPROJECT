package org.miu.mppproject.librarysystem.libraryapp.data.entities;

import java.util.ArrayList;
import java.util.List;

public final class Member extends Person {
    private String memberId;
    private final List<CheckoutEntry> checkoutEntries;


    public Member(ContactInfo contactInfo) {
        super(contactInfo, "LBM");
        this.checkoutEntries = new ArrayList<>();
    }


    public String getMemberId() {
        return super.getId();
    }

    public List<CheckoutEntry> getCheckoutEntries() {
        return checkoutEntries;
    }

    public void setMemberId(String memberId) {
        this.memberId = memberId;
    }
}

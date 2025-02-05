package org.miu.mppproject.librarysystem.libraryapp.domain.service.contract;

import org.miu.mppproject.librarysystem.libraryapp.data.entities.CheckoutEntry;


import java.util.List;

public interface ICheckoutEntryService {

    List<CheckoutEntry> getCheckoutEntriesByMemberId(String memberId);

    List<CheckoutEntry> getAllCheckoutEntries();

    List<CheckoutEntry> getAllOverDueCheckoutEntries();

    void checkoutBook(String memberId, String copyNumber, String isbn);


}

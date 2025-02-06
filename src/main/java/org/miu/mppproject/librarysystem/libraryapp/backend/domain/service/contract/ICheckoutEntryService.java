package org.miu.mppproject.librarysystem.libraryapp.backend.domain.service.contract;

import org.miu.mppproject.librarysystem.libraryapp.backend.data.entities.CheckoutEntry;


import java.util.List;

public interface ICheckoutEntryService {

    List<CheckoutEntry> getCheckoutEntriesByMemberId(String memberId);

    List<CheckoutEntry> getAllCheckoutEntries();

    List<CheckoutEntry> getAllOverDueCheckoutEntries();

    void checkoutBook(String memberId, String copyNumber, String isbn);


}

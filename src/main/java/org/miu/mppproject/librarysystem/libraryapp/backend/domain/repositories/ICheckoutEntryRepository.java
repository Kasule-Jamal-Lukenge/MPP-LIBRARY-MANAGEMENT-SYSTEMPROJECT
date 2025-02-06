package org.miu.mppproject.librarysystem.libraryapp.backend.domain.repositories;

import org.miu.mppproject.librarysystem.libraryapp.backend.data.entities.CheckoutEntry;

import java.sql.SQLException;
import java.util.List;

public interface ICheckoutEntryRepository {


    void checkoutBookCopy(String memberId, String copyNumber, String isbn) throws SQLException;

    List<CheckoutEntry> getAllCheckoutEntries() throws SQLException;

    List<CheckoutEntry> getMemberCheckoutEntries(String memberId) throws SQLException;


}


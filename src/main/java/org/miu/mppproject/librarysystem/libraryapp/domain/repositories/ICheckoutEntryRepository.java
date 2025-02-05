package org.miu.mppproject.librarysystem.libraryapp.domain.repositories;

import org.miu.mppproject.librarysystem.libraryapp.data.entities.CheckoutEntry;

import java.sql.SQLException;
import java.util.List;

public interface ICheckoutEntryRepository {


    void checkoutBookCopy(String memberId, String copyNumber, String isbn) throws SQLException;

    List<CheckoutEntry> getAllCheckoutEntries() throws SQLException;

    List<CheckoutEntry> getMemberCheckoutEntries(String memberId) throws SQLException;


}


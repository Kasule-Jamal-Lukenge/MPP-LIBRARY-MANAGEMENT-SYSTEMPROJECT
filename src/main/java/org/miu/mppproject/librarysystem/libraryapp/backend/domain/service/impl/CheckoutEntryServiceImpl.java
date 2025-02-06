package org.miu.mppproject.librarysystem.libraryapp.backend.domain.service.impl;

import org.miu.mppproject.librarysystem.libraryapp.backend.core.utlis.exception.CheckoutException;
import org.miu.mppproject.librarysystem.libraryapp.backend.data.entities.CheckoutEntry;
import org.miu.mppproject.librarysystem.libraryapp.backend.domain.repositories.ICheckoutEntryRepository;
import org.miu.mppproject.librarysystem.libraryapp.backend.domain.service.contract.ICheckoutEntryService;

import java.sql.SQLException;
import java.util.List;

public class CheckoutEntryServiceImpl implements ICheckoutEntryService {

    private final ICheckoutEntryRepository checkoutRepository;


    public CheckoutEntryServiceImpl(ICheckoutEntryRepository checkoutRepository) {
        this.checkoutRepository = checkoutRepository;
    }


    @Override
    public List<CheckoutEntry> getCheckoutEntriesByMemberId(String memberId) {
        try {
            return checkoutRepository.getMemberCheckoutEntries(memberId);
        } catch (SQLException e) {
            throw new CheckoutException("An error occurred fetching checkout entries");
        }
    }

    @Override
    public List<CheckoutEntry> getAllCheckoutEntries() {
        try {
            return checkoutRepository.getAllCheckoutEntries();
        } catch (SQLException e) {
            throw new CheckoutException("An error occurred fetching checkout entries");
        }
    }

    @Override
    public List<CheckoutEntry> getAllOverDueCheckoutEntries() {
        try {
            return checkoutRepository.getAllCheckoutEntries().stream()
                    .filter(CheckoutEntry::isBookOverDue)
                    .toList();

        } catch (Exception e) {
            throw new CheckoutException("An error occurred");
        }
    }

    @Override
    public void checkoutBook(String memberId, String copyNumber, String isbn) {
        try {
            checkoutRepository.checkoutBookCopy(memberId, copyNumber, isbn);
        } catch (SQLException e) {
            throw new CheckoutException("Unable to checkout Book");
        }
    }
}

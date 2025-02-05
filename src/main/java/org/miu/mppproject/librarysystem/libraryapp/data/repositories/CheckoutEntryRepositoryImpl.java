package org.miu.mppproject.librarysystem.libraryapp.data.repositories;

import org.miu.mppproject.librarysystem.libraryapp.data.dao.CheckoutDao;
import org.miu.mppproject.librarysystem.libraryapp.data.entities.CheckoutEntry;
import org.miu.mppproject.librarysystem.libraryapp.domain.repositories.ICheckoutEntryRepository;

import java.sql.SQLException;
import java.util.List;

public class CheckoutEntryRepositoryImpl implements ICheckoutEntryRepository {


    private final CheckoutDao checkoutDao;


    public CheckoutEntryRepositoryImpl(CheckoutDao checkoutDao) {
        this.checkoutDao = checkoutDao;
    }


    @Override
    public void checkoutBookCopy(String memberId, String copyNumber,String isbn) throws SQLException {
        checkoutDao.checkoutBook(memberId,copyNumber,isbn);
    }

    @Override
    public List<CheckoutEntry> getAllCheckoutEntries() throws SQLException {
        return checkoutDao.getAllCheckoutEntries();
    }

    @Override
    public List<CheckoutEntry> getMemberCheckoutEntries(String memberId) throws SQLException {
        return checkoutDao.getMemberCheckoutEntries(memberId);
    }
}

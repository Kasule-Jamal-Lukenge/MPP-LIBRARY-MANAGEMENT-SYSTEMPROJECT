package org.miu.mppproject.librarysystem.libraryapp.core.di.modules;


import dagger.Module;
import dagger.Provides;
import org.miu.mppproject.librarysystem.libraryapp.core.dataaccessfacade.DataSource;
import org.miu.mppproject.librarysystem.libraryapp.data.dao.CheckoutDao;
import org.miu.mppproject.librarysystem.libraryapp.data.dao.MemberDao;
import org.miu.mppproject.librarysystem.libraryapp.data.repositories.CheckoutEntryRepositoryImpl;
import org.miu.mppproject.librarysystem.libraryapp.data.repositories.MemberRepositoryImpl;
import org.miu.mppproject.librarysystem.libraryapp.domain.repositories.ICheckoutEntryRepository;
import org.miu.mppproject.librarysystem.libraryapp.domain.repositories.IMemberRepository;
import org.miu.mppproject.librarysystem.libraryapp.domain.service.contract.ICheckoutEntryService;
import org.miu.mppproject.librarysystem.libraryapp.domain.service.contract.IMemberService;
import org.miu.mppproject.librarysystem.libraryapp.domain.service.impl.CheckoutEntryServiceImpl;
import org.miu.mppproject.librarysystem.libraryapp.domain.service.impl.MemberServiceImpl;

import javax.inject.Named;
import javax.inject.Singleton;
import java.sql.Connection;

@Module
public class MemberModule {


    @Provides
    @Singleton
    static IMemberRepository provideMemberRepository(MemberDao memberDao) {
        return new MemberRepositoryImpl(memberDao);
    }

    @Provides
    @Singleton
    static IMemberService provideMemberService(IMemberRepository memberRepository) {

        return new MemberServiceImpl(memberRepository);
    }


    @Provides
    @Singleton
    static MemberDao provideMemberDao(@Named("Lib") DataSource dataSource) {
        return new MemberDao(dataSource);
    }
}

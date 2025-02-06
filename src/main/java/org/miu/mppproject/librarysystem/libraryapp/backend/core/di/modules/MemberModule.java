package org.miu.mppproject.librarysystem.libraryapp.backend.core.di.modules;


import dagger.Module;
import dagger.Provides;
import org.miu.mppproject.librarysystem.libraryapp.backend.core.dataaccessfacade.DataSource;
import org.miu.mppproject.librarysystem.libraryapp.backend.data.dao.MemberDao;
import org.miu.mppproject.librarysystem.libraryapp.backend.data.repositories.MemberRepositoryImpl;
import org.miu.mppproject.librarysystem.libraryapp.backend.domain.repositories.IMemberRepository;
import org.miu.mppproject.librarysystem.libraryapp.backend.domain.service.contract.IMemberService;
import org.miu.mppproject.librarysystem.libraryapp.backend.domain.service.impl.MemberServiceImpl;

import javax.inject.Named;
import javax.inject.Singleton;

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

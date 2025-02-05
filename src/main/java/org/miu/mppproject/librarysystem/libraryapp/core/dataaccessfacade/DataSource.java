package org.miu.mppproject.librarysystem.libraryapp.core.dataaccessfacade;

import java.sql.Connection;

public interface DataSource {

    Connection getConnection();
     void initializeDatabase();
}

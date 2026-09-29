package database;

import org.apache.ibatis.datasource.pooled.PooledDataSource;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import javax.sql.DataSource;
import java.io.IOException;
import java.io.InputStream;

/**
 * Builds the MyBatis SqlSessionFactory lazily (only when a DB-driven test
 * actually needs it) and closes its connection pool at the end of the suite.
 * Individual SqlSessions are short-lived: callers open one per query in a
 * try-with-resources block, so no session outlives the query that used it.
 */
public final class MyBatisSessionFactory {

    private static SqlSessionFactory factory;

    private MyBatisSessionFactory() {
    }

    private static synchronized SqlSessionFactory factory() {
        if (factory == null) {
            DatabaseInitializer.initializeIfNeeded();
            try (InputStream configStream = Resources.getResourceAsStream("mybatis/mybatis-config.xml")) {
                factory = new SqlSessionFactoryBuilder().build(configStream);
            } catch (IOException e) {
                throw new IllegalStateException("Unable to load MyBatis configuration", e);
            }
        }
        return factory;
    }

    public static SqlSession openSession() {
        return factory().openSession();
    }

    /**
     * Closes every pooled connection so the H2 database is released when the
     * suite finishes. A no-op if no test used the database in this run.
     */
    public static synchronized void shutdown() {
        if (factory == null) {
            return;
        }
        DataSource dataSource = factory.getConfiguration().getEnvironment().getDataSource();
        if (dataSource instanceof PooledDataSource pooled) {
            pooled.forceCloseAll();
        }
        factory = null;
    }
}

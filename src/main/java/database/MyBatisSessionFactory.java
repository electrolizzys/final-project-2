package database;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import java.io.IOException;
import java.io.InputStream;

public final class MyBatisSessionFactory {

    private static final SqlSessionFactory FACTORY = build();

    private MyBatisSessionFactory() {
    }

    private static SqlSessionFactory build() {
        DatabaseInitializer.initializeIfNeeded();
        try (InputStream configStream = Resources.getResourceAsStream("mybatis/mybatis-config.xml")) {
            return new SqlSessionFactoryBuilder().build(configStream);
        } catch (IOException e) {
            throw new IllegalStateException("Unable to load MyBatis configuration", e);
        }
    }

    public static SqlSession openSession() {
        return FACTORY.openSession();
    }
}

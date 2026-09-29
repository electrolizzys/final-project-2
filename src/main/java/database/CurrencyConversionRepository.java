package database;

import database.mappers.CurrencyConversionMapper;
import database.models.CurrencyConversionRecord;
import org.apache.ibatis.session.SqlSession;

import java.util.List;

/**
 * Reads currency-conversion test data through MyBatis. Each call opens its
 * own SqlSession and closes it before returning.
 */
public final class CurrencyConversionRepository {

    private CurrencyConversionRepository() {
    }

    public static List<CurrencyConversionRecord> findAll() {
        try (SqlSession session = MyBatisSessionFactory.openSession()) {
            return session.getMapper(CurrencyConversionMapper.class).selectAll();
        }
    }
}

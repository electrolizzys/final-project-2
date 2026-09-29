package database.mappers;

import database.models.CurrencyConversionRecord;

import java.util.List;

public interface CurrencyConversionMapper {

    List<CurrencyConversionRecord> selectAll();
}

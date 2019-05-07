package com.ysy.basetools.export;

import com.ysy.basetools.util.StrUtil;
import com.ysy.basetools.util.TimeUtil;

import java.util.Date;

/**
 * Created by Administrator on 2018/8/16.
 */
public class DateCellConvert implements CellConvert<Date, Object> {

    @Override
    public String convert(Date fieldValue, Object obj, Column ef) {
        if (fieldValue != null) {
            String format = ef.extParam();
            if (StrUtil.empty(format)) {
                return TimeUtil.f4YMDHMS(fieldValue);
            }
            return TimeUtil.format(fieldValue, format);
        }
        return null;
    }
}

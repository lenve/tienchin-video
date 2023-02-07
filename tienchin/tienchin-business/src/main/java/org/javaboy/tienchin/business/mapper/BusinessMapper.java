package org.javaboy.tienchin.business.mapper;

import org.javaboy.tienchin.business.domain.Business;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.javaboy.tienchin.business.domain.vo.BusinessSummary;

import java.util.List;

/**
 * <p>
 * 线索 Mapper 接口
 * </p>
 *
 * @author javaboy
 * @since 2023-02-04
 */
public interface BusinessMapper extends BaseMapper<Business> {

    List<BusinessSummary> selectBusinessList();
}

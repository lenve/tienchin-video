package org.javaboy.dd.datasource;

import io.seata.rm.datasource.DataSourceProxy;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

/**
 * @author 江南一点雨
 * @微信公众号 江南一点雨
 * @网站 http://www.itboyhub.com
 * @国际站 http://www.javaboy.org
 * @微信 a_java_boy
 * @GitHub https://github.com/lenve
 * @Gitee https://gitee.com/lenve
 */
@Component
public class DynamicDataSource extends AbstractRoutingDataSource {

    public DynamicDataSource(LoadDataSource loadDataSource) {
        //1.设置所有的数据源
        Map<String, DataSourceProxy> allDs = loadDataSource.loadAllDataSource();
        super.setTargetDataSources(new HashMap<>(allDs));
        //2.设置默认的数据源
        //将来，并不是所有的方法上都有 @DataSource 注解，对于那些没有 @DataSource 注解的方法，该使用哪个数据源？
        super.setDefaultTargetDataSource(allDs.get(DataSourceType.DEFAULT_DS_NAME));
        //3
        super.afterPropertiesSet();
    }

    /**
     * 这个方法用来返回数据源名称，当系统需要获取数据源的时候，会自动调用该方法获取数据源的名称
      * @return
     */
    @Override
    protected Object determineCurrentLookupKey() {
        return DynamicDataSourceContextHolder.getDataSourceType();
    }
}

package org.javaboy.business.feign;

import org.javaboy.common.feign.StorageServiceApi;
import org.springframework.cloud.openfeign.FeignClient;

/**
 * @author 江南一点雨
 * @微信公众号 江南一点雨
 * @网站 http://www.itboyhub.com
 * @国际站 http://www.javaboy.org
 * @微信 a_java_boy
 * @GitHub https://github.com/lenve
 * @Gitee https://gitee.com/lenve
 */
@FeignClient("storage")
public interface StorageServiceApiImpl extends StorageServiceApi {
}